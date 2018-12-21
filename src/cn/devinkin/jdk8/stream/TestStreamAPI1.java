package cn.devinkin.jdk8.stream;

import cn.devinkin.pojo.Employee;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TestStreamAPI1 {
    // 创建Stream
    @Test
    public void test1() {
        // 1. 通过Collection接口扩展的方法获取流
        List<String> list = new ArrayList<>();
        Stream<String> strStream1 = list.stream();
        Stream<String> strStream2 = list.parallelStream();

        // 2. 通过Arrays中的静态方法stream()获取数组流
        Employee[] emps = new Employee[10];
        Stream<Employee> employeeStream = Arrays.stream(emps);

        // 3. 通过Stream类的静态方法of()
        Stream<String> stream3 = Stream.of("aa", "bb", "cc");

        // 4. 创建无限流
        // 迭代
        Stream<Integer> stream4 = Stream.iterate(0, (x) -> x + 2);
        stream4.limit(10).forEach(System.out::println);
        // 生成
        Stream<Double> stream5 = Stream.generate(Math::random);
        stream5.limit(10).forEach(System.out::println);
    }
}
