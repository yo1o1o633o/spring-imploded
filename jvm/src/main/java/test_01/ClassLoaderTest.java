package test_01;

/**
 * 三种类加载器，层级关系
 */
public class ClassLoaderTest {

    public static void main(String[] args) {
        // 系统类加载器jdk.internal.loader.ClassLoaders$AppClassLoader@58644d46
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        // 扩展类加载器jdk.internal.loader.ClassLoaders$PlatformClassLoader@161cd475
        ClassLoader extClassLoader = systemClassLoader.getParent();
        // 引导类加载器null
        ClassLoader bootstrapClassLoader = extClassLoader.getParent();
    }
}
