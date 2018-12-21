package cn.devinkin.jdk8.sdinterface;

public class SubClass implements MyFun, MyInterface{

    @Override
    public String getName() {
        return MyInterface.super.getName();
    }
}
