package cn.devinkin.jdk8.lambda;

import cn.devinkin.pojo.Employee;

public class FilterEmployeeByAge implements MyPredicate<Employee> {

    @Override
    public boolean test(Employee employee) {
        return employee.getAge() >= 35;
    }
}
