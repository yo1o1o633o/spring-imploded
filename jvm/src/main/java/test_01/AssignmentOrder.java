package test_01;

public class AssignmentOrder {
    private static int a = 1;

    static {
        a = 10;
        b = 20;
    }

    private static int b = 2;

    /**
     * a输出10
     * b输出2
     * 原因分析
     *
     * 类加载过程的第二部准备阶段， 会将类变量加载到方法区中， 同时赋值初始值。  此时 a = 0  b = 0;
     * 类加载过程的第三阶段初始化阶段， 会基于类变量的显示赋值和静态代码块的赋值，按照赋值顺序对类变量进行显示赋值 a -> 1 -> 10, b -> 20 -> 2
     * */
    public static void main(String[] args) {
        System.out.println(a);
        System.out.println(b);
    }
}
