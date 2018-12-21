package cn.devinkin.jdk8.lambda;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.function.Consumer;

/**
 * 1. Lambda 表达式的基础语法: Java8中引入了一个新的操作符"->", 该操作符为箭头操作符或Lambda操作符
 *
 * 左侧: Lambda表达式的参数列表
 * 左侧: Lambda 表达式中所需执行的功能, 即Lambda体
 */
public class TestLambda2 {

    /**
     * 语法格式1: 无参数, 无返回值
     *  () -> System.out.println("Hello Lambda!");
     */
    @Test
    public void test1() {
        // jdk1.7以前, 必须是final
        int num = 0;

        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello World!" + num);
            }
        };
        r.run();

        System.out.println("---------------------------");

        Runnable r1 = () -> System.out.println("Hello Lambda!");
        r1.run();
    }

    /**
     * 语法格式2: 有一个参数, 并且无返回值
     */
    @Test
    public void test2() {
        Consumer<String> con = (x) -> System.out.println(x);
        con.accept("devinkin");
    }

    /**
     * 语法格式3: 有一个参数, 小括号可以不写
     */
    @Test
    public void test3() {
        Consumer<String> con = x -> System.out.println(x);
        con.accept("devinkin");
    }

    /**
     * 语法格式4: 有两个以上的参数, 有返回值, Lambda体重有多个参数
     */
    @Test
    public void test4() {
        Comparator<Integer> com = (x, y) -> {
            System.out.println("函数式接口");
            return Integer.compare(x, y);
        };
    }

    /**
     * 语法格式5: 当Lambda体只有一条语句时, return与大括号可以省略.
     */
    @Test
    public void test5() {
        Comparator<Integer> com = (x, y) -> Integer.compare(x,y);
    }

    // 需求: 对一个数进行运算
    @Test
    public void test6() {
        System.out.println(operation(100, x -> x + 100));
    }

    public Integer operation(Integer num, MyFun<Integer> mf) {
        return mf.getValue(num);
    }
}
