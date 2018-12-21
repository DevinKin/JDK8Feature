package cn.devinkin.jdk8.sdinterface;

public interface MyFun {
    default String getName() {
        return "哈哈哈";
    }
}
