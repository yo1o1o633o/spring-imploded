package loader;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author shuai.yang
 */
public class MyCustomClassLoader extends ClassLoader {
    private String classPath;

    public MyCustomClassLoader(String classPath) {
        this.classPath = classPath;
    }

    @Override
    protected Class<?> findClass(String name) {
        byte[] bytes = new byte[0];
        try {
            bytes = loadByte(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defineClass(name, bytes, 0, bytes.length);
    }

    /**
     * 自定义类加载器, 使用此加载器来加载指定类文件, 如果加载得是指定包开头的类, 就使用自定义的类加载逻辑进行加载, 否则调用父类的类加载逻辑
     */
    @Override
    protected Class<?> loadClass(String name, boolean resolve) {
        synchronized (getClassLoadingLock(name)) {
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                long t0 = System.nanoTime();
                try {
                    if (name.startsWith("custom")) {
                        c = findClass(name);
                    } else {
                        c = super.loadClass(name);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (c == null) {
                    long t1 = System.nanoTime();
                    c = findClass(name);

                    sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
                    sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                    sun.misc.PerfCounter.getFindClasses().increment();
                }
            }
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }

    private byte[] loadByte(String name) throws IOException {
        name = name.replaceAll("\\.", "/");
        FileInputStream fileInputStream = new FileInputStream(classPath + "/" + name + ".class");
        int len = fileInputStream.available();
        byte[] bytes = new byte[len];
        int i = fileInputStream.read(bytes);
        fileInputStream.close();
        return bytes;
    }
}
