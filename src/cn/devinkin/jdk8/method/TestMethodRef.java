package cn.devinkin.jdk8.method;

import cn.devinkin.pojo.Employee;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.function.*;


public class TestMethodRef {

    // 对象::实例方法名
    @Test
    public void test1() {
        Consumer<String> con1 = x -> System.out.println(x);
        PrintStream ps = System.out;
        Consumer<String> cons2 = ps::println;
        cons2.accept("test");
    }

    @Test
    public void test2() {
        Employee emp = new Employee("king", 38, 12311.24);
        Supplier<String> sup1 = () -> emp.getName();
        Supplier<String> sup2 = emp::getName;
        System.out.println(sup1.get());
        System.out.println(sup2.get());
    }

    // 类::静态方法名
    @Test
    public void test3() {
        Comparator<Integer> com1 = (x, y) -> Integer.compare(x, y);
        //Integer.compare是静态方法
        Comparator<Integer> com2 = Integer::compare;
    }

    // 类::实例方法
    @Test
    public void test4() {
        BiPredicate<String, String> bp1 = (x, y) -> x.equals(y);
        BiPredicate<String, String> bp2 = String::equals;
    }

    // 构造器引用
    @Test
    public void test5() {
        Supplier<Employee> sup1 = () -> new Employee("king", 23, 1234.34);
        Supplier<Employee> sup2 = Employee::new;
        Function<String, Employee> fun1 = (x) -> new Employee(x);
        Function<String, Employee> fun2 = Employee::new;
        BiFunction<String, Integer, Employee> bf1 = Employee::new;

        System.out.println(sup1.get());
        System.out.println(sup2.get());

        System.out.println(fun1.apply("king1"));
        System.out.println(fun2.apply("king2"));

        System.out.println(bf1.apply("two", 13));
    }

    // 数组引用
    @Test
    public void test6() {
        Function<Integer, String[]> fun1 = (x) -> new String[x];
        Function<Integer, String[]> fun2 = String[]::new;
        System.out.println(fun2.apply(10).length);

        Function<Integer, String[]> fun3 = String[]::new;
        String[] strs2 = fun2.apply(20);
        System.out.println(strs2.length);
    }
}
