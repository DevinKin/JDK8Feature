package cn.devinkin.jdk8.optional;


import cn.devinkin.pojo.Employee;
import org.junit.Test;

import java.util.Optional;

import static java.util.Optional.of;

public class TestOptional {
    // Optional.of()
    @Test
    public void test1() {
        Optional<Employee> op = of(new Employee());
        Optional<Employee> op2 = of(null);
        System.out.println(op.get());
        System.out.println(op2.get());
    }

    // Optional.ofNullable()
    @Test
    public void test2() {
        Optional<Employee> op = Optional.ofNullable(null);
        Optional<Employee> op2 = Optional.ofNullable(new Employee());
        System.out.println(op2.get());
        System.out.println(op.get());
    }

    // Optional.isPresent()
    @Test
    public void test3() {
        Optional<Employee> op = Optional.ofNullable(null);
        Optional<Employee> op2 = Optional.ofNullable(new Employee());
        if (op.isPresent()) {
            System.out.println(op.get());
        }
        if (op2.isPresent()) {
            System.out.println(op2.get());
        }
    }

    // Optional.orElse()
    @Test
    public void test4() {
        Optional<Employee> op = Optional.ofNullable(null);

        Employee emp = op.orElse(new Employee("张三", 18, 88.98, Employee.Status.FREE));
        System.out.println(emp);
    }

    // Optional.orElseGet()
    @Test
    public void test5() {
        Optional<Employee> op = Optional.ofNullable(null);

        Employee emp = op.orElseGet(
                () -> new Employee("张三", 18, 88.98, Employee.Status.FREE)
        );
        System.out.println(emp);
    }

    // Optional.map()
    @Test
    public void test6() {
        Optional<Employee> op = Optional.ofNullable(new Employee("张三", 18, 888.88, Employee.Status.FREE));
        Optional<String> str = op.map(Employee::getName);
        System.out.println(str.get());
    }

    // Optional.flatMap()
    @Test
    public void test7() {
        Optional<Employee> op = Optional.ofNullable(new Employee("张三", 18, 888.88, Employee.Status.FREE));
        Optional<String> s = op.flatMap((e) -> of(e.getName()));
        System.out.println(s.get());
    }

    @Test
    public void test8() {
        Man man = new Man();
        String n = getGodnessName(man);
        System.out.println(n);
        Man newman = new Man();
    }
    @Test
    public void test9() {
        Optional<NewMan> newman = Optional.of(new NewMan());
        Optional<NewMan> newman2 = Optional.ofNullable(null);
        String n = getGodnessName2(newman2);
        String n2 = getGodnessName2(newman);
        System.out.println(n);
        System.out.println(n2);
    }

    // 需求: 获取一个man的godness的name属性
    public String getGodnessName(Man man) {
        if (man != null) {
            if (man.getGodness() != null) {
                return man.getGodness().getName();
            }
        }
        return "";
    }

    public String getGodnessName2(Optional<NewMan> man) {
        return man.orElseGet(() -> new NewMan()).getGodness()
                .orElseGet(() -> new Godness("test"))
                .getName();
    }
}
