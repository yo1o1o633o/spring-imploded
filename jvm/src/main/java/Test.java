import loader.MyCustomClassLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Test {
    public static void main(String[] args) {
        MyCustomClassLoader myCustomClassLoader = new MyCustomClassLoader("D://");
        Class<?> clazz = null;
        try {
            clazz = myCustomClassLoader.loadClass("com");
            Object o = clazz.newInstance();
            Method method = clazz.getDeclaredMethod("", String.class);
            Object invoke = method.invoke(o, "");
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
