package cn.devinkin.jdk8.optional;

public class Man {
    private Godness godness;

    public Man(Godness godness) {
        this.godness = godness;
    }

    public Man() {
    }

    @Override
    public String toString() {
        return "Man{" +
                "godness=" + godness +
                '}';
    }

    public Godness getGodness() {
        return godness;
    }

    public void setGodness(Godness godness) {
        this.godness = godness;
    }
}
