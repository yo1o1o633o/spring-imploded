package context;

public interface BeanPostProcessor {

    public Object postProcessBeforeInitialization(Object bean, String beanName);

    public Object postProcessAfterInitialization(Object bean, String beanName);
}
