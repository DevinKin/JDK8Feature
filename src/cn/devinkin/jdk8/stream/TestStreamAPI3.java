package cn.devinkin.jdk8.stream;

import cn.devinkin.pojo.Employee;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class TestStreamAPI3 {
    private List<Employee> employees = Arrays.asList(
            new Employee("zhangsan", 18, 999.99, Employee.Status.FREE),
            new Employee("lisi", 20, 9929.99, Employee.Status.BUSY),
            new Employee("wangwu", 38, 1999.99, Employee.Status.VOCATION),
            new Employee("zhaowu", 58, 3999.99, Employee.Status.BUSY),
            new Employee("zhaowu", 58, 3999.99, Employee.Status.VOCATION),
            new Employee("zhaowu", 58, 3999.99, Employee.Status.FREE),
            new Employee("zhaowu", 58, 3999.99, Employee.Status.BUSY),
            new Employee("tianqi", 68, 4999.99, Employee.Status.VOCATION)
    );

    @Test
    public void test1() {
        // 匹配所有元素
        boolean b = employees.stream()
                .allMatch((e) -> e.getStatus().equals(Employee.Status.BUSY));
        // 至少匹配一个元素
        boolean b1 = employees.stream()
                .anyMatch((e) -> e.getStatus().equals(Employee.Status.BUSY));
        // 没有匹配的元素
        boolean b2 = employees.stream()
                .noneMatch((e) -> e.getStatus().equals(Employee.Status.BUSY));
        // 返回第一个元素
        Optional<Employee> op = employees.stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary))
                .findFirst();

        Optional<Employee> any = employees.parallelStream()
                .filter((e) -> e.getStatus().equals(Employee.Status.FREE))
                .findAny();

        // count
        long count = employees.stream()
                .count();

        // max
        Optional<Employee> max = employees.stream()
                .max(Comparator.comparingDouble(Employee::getSalary));

        // min
        Optional<Employee> min = employees.stream()
                .min(Comparator.comparingDouble(Employee::getAge));

        System.out.println(b);
        System.out.println(b1);
        System.out.println(b2);
        System.out.println(op.get());
        System.out.println(any.get());
        System.out.println(count);
        System.out.println(max.get());
        System.out.println(min.get());
    }

    // reduce 归约
    @Test
    public void test2() {
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        Integer sum = list.stream()
                .reduce(0, (x, y) -> x + y);
        System.out.println(sum);

        System.out.println("-----------------------------------------");

        Optional<Double> sum2 = employees.stream()
                .map(Employee::getSalary)
                .reduce(Double::sum);
        System.out.println(sum2.get());
    }

    // collect 收集
    @Test
    public void test3() {
        List<String> list = employees.stream()
                .map(Employee::getName)
                .collect(Collectors.toList());
        list.forEach(System.out::println);

        System.out.println("---------------------------");
        employees.stream()
                .map(Employee::getName)
                .collect(Collectors.toSet())
                .forEach(System.out::println);
        System.out.println("---------------------------");
        employees.stream()
                .map(Employee::getName)
                .collect(Collectors.toCollection(HashSet::new))
                .forEach(System.out::println);
        System.out.println("---------------------------");
        Long count = employees.stream()
                .collect(Collectors.counting());
        System.out.println(count);
        System.out.println("---------------------------");
        Double avg = employees.stream()
                .collect(Collectors.averagingDouble(Employee::getSalary));
        System.out.println(avg);
        System.out.println("---------------------------");
        Double sum = employees.stream()
                .collect(Collectors.summingDouble(Employee::getSalary));
        System.out.println(sum);
        System.out.println("---------------------------");
        Optional<Employee> max = employees.stream().max(Comparator.comparingDouble(Employee::getSalary));
        System.out.println(max.get());
        System.out.println("---------------------------");
        Optional<Double> min = employees.stream()
                .map(Employee::getSalary)
                .collect(Collectors.minBy(Double::compare));
        System.out.println(min.get());
        System.out.println("---------------------------");
        Map<Employee.Status, List<Employee>> map = employees.stream()
                .collect(Collectors.groupingBy(Employee::getStatus));
        map.keySet()
                .forEach(k -> {
                    System.out.println(k + " : " + map.get(k));
                });
        System.out.println("---------------------------");
        Map<Employee.Status, Map<String, List<Employee>>> map2 = employees.stream()
                .collect(Collectors.groupingBy(Employee::getStatus, Collectors.groupingBy((e) -> {
                    if (((Employee) e).getAge() < 30) {
                        return "青年";
                    } else if (((Employee) e).getAge() < 50) {
                        return "中年";
                    } else {
                        return "老年";
                    }
                })));
        System.out.println(map2);
        System.out.println("---------------------------");
        Map<Boolean, List<Employee>> map3 = employees.stream()
                .collect(Collectors.partitioningBy((e) -> e.getSalary() > 8000));
        System.out.println(map3);
        System.out.println("---------------------------");
        DoubleSummaryStatistics dss = employees.stream()
                .collect(Collectors.summarizingDouble(Employee::getSalary));
        System.out.println(dss.getCount());
        System.out.println(dss.getSum());
        System.out.println(dss.getAverage());
        System.out.println(dss.getMax());
        System.out.println(dss.getMin());
        System.out.println("---------------------------");
        String emps = employees.stream()
                .map(Employee::getName)
                .collect(Collectors.joining(",", "<", ">"));
        System.out.println(emps);
    }
}
