import config.AppConfig;
import context.ApplicationContext;
import service.UserService;

public class Test {
    public static void main(String[] args) throws Exception {

        ApplicationContext applicationContext = new ApplicationContext(AppConfig.class);
        UserService userService = (UserService) applicationContext.getBean("userService");

        userService.getOrder();
    }
}
