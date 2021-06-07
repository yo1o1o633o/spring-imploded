package context;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {

    private Class configClass;
    /**
     * 单例池， 保存单例Bean
     * */
    private ConcurrentHashMap<String, Object> singleMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public ApplicationContext(Class configClass) {
        this.configClass = configClass;
        // 扫描Bean， 生产Bean定义
        scan();

        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            BeanDefinition beanDefinition = entry.getValue();
            // 如果是单例Bean，创建并放入单例池
            if (beanDefinition.getScope().equals("singleton")) {
                singleMap.put(entry.getKey(), createBean(beanDefinition));
            }
        }
    }

    public Object createBean(BeanDefinition definition) {
        Class clazz = definition.getClazz();

        try {
            Object o = clazz.getDeclaredConstructor().newInstance();
            // 依赖注入, 获取当前Bean的所有属性
            Field[] fields = clazz.getDeclaredFields();
            // 循环属性
            for (Field field : fields) {
                // 是否有Autowired注解
                if (field.isAnnotationPresent(Autowired.class)) {
                    // 获取Autowired注解的字段值, 去获取Bean
                    Object bean = getBean(field.getName());
                    field.setAccessible(true);
                    // 给当前对象o的field字段赋值
                    field.set(o, bean);
                }
            }
            // Aware回调
            if (o instanceof BeanNameAware) {
                BeanNameAware beanNameAware = (BeanNameAware) o;
                beanNameAware.setBeanName(definition.getBeanName());
            }
            // 初始化前
            for (BeanPostProcessor processor : beanPostProcessorList) {
                processor.postProcessBeforeInitialization(o, definition.getBeanName());
            }

            // 初始化
            if (o instanceof InitializingBean) {
                InitializingBean beanNameAware = (InitializingBean) o;
                beanNameAware.afterPropertiesSet();
            }

            // 初始化后
            for (BeanPostProcessor processor : beanPostProcessorList) {
                processor.postProcessAfterInitialization(o, definition.getBeanName());
            }
            return o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void scan() {
        // 解析传入配置类上的配置的扫描路径
        // 获取配置类上的ComponentScan注解信息
        ComponentScan componentScanAnnotation = (ComponentScan) configClass.getDeclaredAnnotation(ComponentScan.class);
        // 注解值
        String value = componentScanAnnotation.value();
        // 获取当前类加载器
        ClassLoader classLoader = ApplicationContext.class.getClassLoader();
        // 获取路径地址
        URL resource = classLoader.getResource(value);
        if (resource != null) {
            // 转成文件对象
            File file = new File(resource.getFile());
            // 如果是一个目录
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    // 获取路径地址 E:\Github\spring-imploded\spring\target\classes\service\UserService.class
                    String absolutePath = f.getAbsolutePath();
                    // 不是.class结尾的文件， 不是类文件
                    if (!absolutePath.endsWith(".class")) {
                        continue;
                    }
                    // 截取全限定名 service.UserService.class
                    String className = absolutePath.substring(absolutePath.indexOf(value), absolutePath.indexOf(".class"));
                    className = className.replaceAll("\\\\", ".");

                    Class<?> aClass = null;
                    try {
                        aClass = classLoader.loadClass(className);
                        // 判断加载的类是否有Component注解
                        if (aClass.isAnnotationPresent(Component.class)) {
                            // 判断aClass是否实现了BeanPostProcessor接口
                            if (BeanPostProcessor.class.isAssignableFrom(aClass)) {
                                BeanPostProcessor processor = (BeanPostProcessor) aClass.getDeclaredConstructor().newInstance();
                                beanPostProcessorList.add(processor);
                            }
                            // 表示当前类是一个Bean
                            Component componentAnnotation = aClass.getDeclaredAnnotation(Component.class);
                            String beanName = componentAnnotation.value();

                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setClazz(aClass);
                            beanDefinition.setBeanName(beanName);
                            // 如果配置了作用域注解
                            if (aClass.isAnnotationPresent(Scope.class)) {
                                Scope scopeAnnotation = aClass.getDeclaredAnnotation(Scope.class);
                                String scope = scopeAnnotation.value();
                                beanDefinition.setScope(scope);
                            } else {
                                // 没有默认为单例
                                beanDefinition.setScope("singleton");
                            }
                            // 将Bean定义放入Map中
                            beanDefinitionMap.put(beanName, beanDefinition);
                        }
                    } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Object getBean(String beanName) throws Exception {
        if (beanDefinitionMap.containsKey(beanName)) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.getScope().equals("singleton")) {
                return singleMap.get(beanName);
            } else {
                return createBean(beanDefinition);
            }
        } else {
            throw new Exception("未找到Bean");
        }
    }
}
