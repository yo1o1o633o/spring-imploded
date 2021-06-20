package service;

import context.*;

@Scope(value = "singleton")
@Component("userService")
public class UserServiceImpl implements UserService, BeanNameAware, InitializingBean {

    @Autowired
    private OrderService orderService;

    private String beanName;

    @Override
    public void getOrder() {
        System.out.println("订单");
    }

    /**
     * 实现BeanNameAware接口， 在Bean对象创建时， 会调用该方法进行回调
     * */
    @Override
    public void setBeanName(String beanName) {
        System.out.println("Bean创建时调用了此方法BeanName为： " + beanName);
        this.beanName = beanName;
    }

    /**
     * 实现InitializingBean接口， 在Bean对象创建时， 会调用该初始化
     * */
    @Override
    public void afterPropertiesSet() {
        System.out.println("Bean创建时调用了此方法afterPropertiesSet");
    }
}
