package cn.devinkin.jdk8.lambda;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Java8 内置的四大核心函数式接口
 */
public class testLambda4 {

    public void happy(double money, Consumer<Double> con) {
        con.accept(money);
    }
    @Test
    public void testConsumer() {
        happy(100.12, (d) -> System.out.println("consume: " + d));
    }

    // 产生指定个整数, 并放入集合中
    public List<Integer> getNumList(int num, Supplier<Integer> sup) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Integer n = sup.get();
            list.add(n);
        }

        return list;
    }

    @Test
    public void testSupplier() {
        System.out.println(getNumList(10, () -> (int)(Math.random() * 100)));
        System.out.println(getNumList(5, () -> (int)(Math.random()* 1000)));
        getNumList(5, () -> (int)(Math.random() * 100)).forEach(System.out::println);
    }

    public String strHandler(String str, Function<String, String> fun) {
        return fun.apply(str);
    }

    @Test
    public void testFunction() {
        System.out.println(strHandler("\t\t\t test??? test \t\t", String::trim));
        System.out.println(strHandler("\t\t\t\t testlkjalsdfua. a.sd,fmaklsj", (str) -> str.substring(3, 9)));
    }

    // 需求: 将满足条件的字符串放入集合中
    public List<String> filterStr(List<String> strList, Predicate<String> pre) {
        List<String> list = new ArrayList<>();
        for (String str : strList) {
            if (pre.test(str)) {
                list.add(str);
            }
        }
        return list;
    }

    @Test
    public void testPredicate() {
        List<String> list = Arrays.asList("Hello", "devinkin", "Lambda", "Centos","Devinkin", "ok", "bi", "lala");
        System.out.println(filterStr(list, (str) -> str.length() > 3));
        filterStr(list, (str) -> str.length() < 3).forEach(x -> System.out.println(x));
    }
}
