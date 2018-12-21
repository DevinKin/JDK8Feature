package cn.devinkin.jdk8.lambda;

@FunctionalInterface
public interface MyFun2<T, R> {
    R getValue(T t1, T t2);
}
