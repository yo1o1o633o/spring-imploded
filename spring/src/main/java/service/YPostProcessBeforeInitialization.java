package service;

import context.BeanPostProcessor;
import context.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Component
public class YPostProcessBeforeInitialization implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println("初始化前");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("初始化后");
        // AOP切面， 在初始化后开始执行， 采用动态代理，生成代理对象
        if ("userService".equals(beanName)) {
            // 生产代理对象
            return Proxy.newProxyInstance(YPostProcessBeforeInitialization.class.getClassLoader(), YPostProcessBeforeInitialization.class.getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    // 例如找切点
                    System.out.println("代理逻辑");
                    return method.invoke(bean, args);
                }
            });
        }
        return bean;
    }
}
