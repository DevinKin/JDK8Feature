package cn.devinkin.jdk8.stream;

import cn.devinkin.pojo.Employee;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TestStreamAPI2 {
    private List<Employee> employees = Arrays.asList(
            new Employee("zhangsan", 18, 999.99),
            new Employee("lisi", 20, 9929.99),
            new Employee("wangwu", 38, 1999.99),
            new Employee("zhaowu", 58, 3999.99),
            new Employee("zhaowu", 58, 3999.99),
            new Employee("zhaowu", 58, 3999.99),
            new Employee("zhaowu", 58, 3999.99),
            new Employee("tianqi", 68, 4999.99)
    );
    /**
     * Stream中间操作
     */
    // filter
    @Test
    public void test1() {
        Stream<Employee> employeeStream = employees.stream()
                .filter(e -> {
                    System.out.println("Stream API 的中间操作");
                    return e.getAge() < 35;
                });
        employeeStream
                .forEach(System.out::println);
    }

    // limit
    @Test
    public void test2() {
        employees.stream()
                .filter(e -> {
                    System.out.println("短路");
                    return e.getSalary() < 4000;
                })
                .limit(2)
                .forEach(System.out::println);
    }

    // skip
    @Test
    public void test3() {
        employees.stream()
                .filter(e -> e.getSalary() < 4000)
                .skip(2)
                .forEach(System.out::println);
    }

    // distinct
    @Test
    public void test4() {
        employees.stream()
                .distinct()
                .forEach(System.out::println);
    }

    // map
    @Test
    public void test5() {
        List<String> list = Arrays.asList("aaa", "bbb", "Ccc", "ddd", "eee");

        list.stream()
                .map((str) -> str.toUpperCase())
                .forEach(System.out::println);
        System.out.println("-----------------------------");

        employees.stream()
                .map(Employee::getName)
                .forEach(System.out::println);

        System.out.println("-----------------------------");
        Stream<Stream<Character>> streamStream = list.stream()
                .map(TestStreamAPI2::filterCharacter);
        streamStream.forEach((sm) -> {
            sm.forEach(System.out::println);
        });

        System.out.println("-----------------------------");
        Stream<Character> characterStream = list.stream()
                .flatMap(TestStreamAPI2::filterCharacter);
        characterStream
                .forEach(System.out::println);
    }

    @Test
    public void test6() {
        List<String> list = Arrays.asList("aaa", "bbb", "ccc", "ddd", "eee");
        List list2 = new ArrayList();
        list2.add(11);
        list2.add(22);
        list2.addAll(list);
        System.out.println(list2);
    }

    // sort
    @Test
    public void test7() {
        List<String> list = Arrays.asList("ccc", "aaa", "bbb","ddd", "eee");
        list.stream()
            .sorted()
            .forEach(System.out::println);
        System.out.println("-----------------------------------------------------");

        employees.stream()
                .sorted((e1, e2) -> {
                   if (e1.getAge() == e2.getAge()) {
                       return e1.getName().compareTo(e2.getName());
                   } else {
                       return Integer.compare(e1.getAge(), e2.getAge());
                   }
                })
                .forEach(System.out::println);
    }

    public static Stream<Character> filterCharacter(String str) {
        List<Character> list = new ArrayList<>();
        for (Character ch : str.toCharArray()) {
            list.add(ch);
        }
        return list.stream();
    }
}
