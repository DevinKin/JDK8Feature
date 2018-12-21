package cn.devinkin.jdk8.sdinterface;

public interface MyInterface {
    default String getName() {
        return "嘿嘿嘿";
    }
}
