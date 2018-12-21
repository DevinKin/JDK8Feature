package cn.devinkin.jdk8.lambda;

import cn.devinkin.pojo.Employee;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TestLambda1 {

    // 原来使用匿名内部类
    @Test
    public void test1() {
        Comparator<Integer> com = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1, o2);
            }
        };

        TreeSet<Integer> ts = new TreeSet<>(com);
    }

    // Lambda 表达式
    @Test
    public void test2() {
        Comparator<Integer> com = (x, y) -> Integer.compare(x, y);
        TreeSet<Integer> ts = new TreeSet<>(com);
    }

    private List<Employee> employees = Arrays.asList(
            new Employee("zhangsan", 18, 999.99),
            new Employee("lisi", 20, 9929.99),
            new Employee("wangwu", 38, 1999.99),
            new Employee("zhaowu", 58, 3999.99),
            new Employee("tianqi", 68, 4999.99)
    );

    @Test
    public void test3() {
        List<Employee> list = filterEmployees(employees);
        for (Employee emp :list) {
            System.out.println(emp);
        }
    }

    @Test
    public void test4() {
        List<Employee> list1 = filterEmployees(this.employees, new FilterEmployeeByAge());
        List<Employee> list2 = filterEmployees(this.employees, new FilterEmployeeBySalary());
        for (Employee emp : list1) {
            System.out.println(emp);
        }

        for (Employee emp : list2) {
            System.out.println(emp);
        }
    }

    // 优化方式2: 匿名内部类
    @Test
    public void test5() {
        List<Employee> employees = filterEmployees(this.employees, new MyPredicate<Employee>() {
            @Override
            public boolean test(Employee employee) {
                return employee.getSalary() <= 5000;
            }
        });

        for (Employee emp : employees) {
            System.out.println(emp);
        }
    }

    // 优化方式3: Lambda表达式
    @Test
    public void test6() {
        List<Employee> list = filterEmployees(employees, (e) -> e.getSalary() >= 5000);
        list.forEach(System.out::println);
    }

    // 优化方式4: Stream Api
    @Test
    public void test7() {
        employees.stream()
                .filter((e) -> e.getSalary() >= 5000)
                .forEach(System.out::println);
        System.out.println("--------------------------");
        employees.stream()
                .map(Employee::getName)
                .forEach(System.out::println);
    }

    // 需求: 获取当前公司中员工年龄大于25的员工信息
    public List<Employee> filterEmployees(List<Employee> list) {
        List<Employee> emps = new ArrayList<>();

        for (Employee emp : list) {
            if (emp.getAge() >=  35) {
                emps.add(emp);
            }
        }
        return emps;
    }

    // 新需求: 获取当前公司中员工工资大于5000的员工信息
    public List<Employee> filterEmployees2(List<Employee> list) {
        List<Employee> emps = new ArrayList<>();

        for (Employee emp : list) {
            if (emp.getSalary() >= 5000) {
                emps.add(emp);
            }
        }
        return emps;
    }

    // 优化方式1: 设计模式, 策略设计模式
    public List<Employee> filterEmployees(List<Employee> list, MyPredicate<Employee> mp) {
        List<Employee> emps = new ArrayList<>();

        for (Employee employee : list) {
            if (mp.test(employee)) {
                emps.add(employee);
            }
        }
        return emps;
    }
}
