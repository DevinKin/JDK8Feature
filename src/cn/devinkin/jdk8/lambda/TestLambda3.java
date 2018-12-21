package cn.devinkin.jdk8.lambda;

import cn.devinkin.pojo.Employee;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestLambda3 {

    private List<Employee> employees = Arrays.asList(
            new Employee("zhangsan", 18, 999.99),
            new Employee("lisi", 20, 9929.99),
            new Employee("wangwu", 38, 1999.99),
            new Employee("zhaowu", 58, 3999.99),
            new Employee("tianqi", 68, 4999.99)
    );

    @Test
    public void test1() {
        Collections.sort(employees, (e1, e2) -> {
            if (e1.getAge() == e2.getAge()) {
                return e1.getName().compareTo(e2.getName());
            } else {
                return Integer.compare(e1.getAge(), e2.getAge());
            }
        });

        for (Employee emp : employees) {
            System.out.println(emp);
        }
    }

    // 新需求: 用于处理字符串的方法
    public String strHandler(String str, MyFunction mf) {
        return mf.getValue(str);
    }
    @Test
    public void test2() {
        String test = " hello world ";
        System.out.println(strHandler(test, (str) -> str.toUpperCase()));
        System.out.println(strHandler(test, (str) -> str.substring(2, 5)));
    }

    @Test
    public void test3() {
        op(100L, 200L, (x, y) -> x + y);
        op(200L, 300L, (x, y) -> x * y);
    }

    // 需求: 对于两个Long型数据进行处理
    public void op(Long l1, Long l2, MyFun2<Long, Long> mf) {
        System.out.println(mf.getValue(l1, l2));
    }
}
