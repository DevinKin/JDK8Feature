package cn.devinkin.jdk8.lambda;

import cn.devinkin.pojo.Employee;

public class FilterEmployeeBySalary implements MyPredicate<Employee> {
    @Override
    public boolean test(Employee employee) {
        return employee.getSalary() > 5000;
    }
}
