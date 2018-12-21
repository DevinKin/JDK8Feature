package cn.devinkin.jdk8.lambda;

@FunctionalInterface
public interface MyFun<T> {
    Integer getValue(T num);
}
