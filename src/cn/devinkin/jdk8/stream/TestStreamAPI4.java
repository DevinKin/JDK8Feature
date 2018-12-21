package cn.devinkin.jdk8.stream;

import cn.devinkin.pojo.Employee;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Stream;

public class TestStreamAPI4 {

    private List<Employee> employees = Arrays.asList(
            new Employee("zhangsan", 18, 999.99),
            new Employee("lisi", 20, 9929.99),
            new Employee("wangwu", 38, 1999.99),
            new Employee("zhaowu", 58, 3999.99),
            new Employee("zhaowu", 58, 3999.99),
            new Employee("zhaowu", 58, 3999.99),
            new Employee("zhaowu", 58, 3999.99),
            new Employee("tianqi", 68, 4999.99)
    );
    // 给定一个数字列表, 如何返回一个由每个数的平方构成的列表呢?
    @Test
    public void test1() {
        Integer[] nums = new Integer[] {1,2,3,4,5,6,7,8,9};
        Arrays.stream(nums)
                .map((x) -> x * x)
                .forEach(System.out::println);
        System.out.println("------------------------------");
    }

    // 使用map和reduce计算流中有多少个employee
    @Test
    public void test2() {
        Optional<Integer> sum = employees.stream()
                .map((e) -> 1)
                .reduce(Integer::sum);
        System.out.println(sum.get());
    }

    List<Transaction> transactions = null;

    @Before
    public void before() {
        Trader raou1 = new Trader("Raou1", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");

        transactions = Arrays.asList(
                new Transaction(brian,2011, 3000),
                new Transaction(raou1, 2012, 1000),
                new Transaction(raou1, 2011, 4000),
                new Transaction(mario, 2012, 7100),
                new Transaction(mario, 2012, 7000),
                new Transaction(alan, 2012, 9500)
        );
    }

    // 1. 找出2011年发生的所有交易, 并按交易额排序(从低到高)
    @Test
    public void test3() {
        transactions.stream()
                .filter(x -> x.getYear() == 2011)
                .sorted(Comparator.comparingInt(Transaction::getValue))
                .forEach(System.out::println);
    }

    // 2. 交易员都在哪些不同的城市工作过
    @Test
    public void test4() {
        transactions.stream()
                .map(Transaction::getTrader)
                .map(Trader::getCity)
                .distinct()
                .forEach(System.out::println);
    }

    // 3. 查找所有来自剑桥的交易员, 并按名字排序
    @Test
    public void test5() {
        transactions.stream()
                .map(Transaction::getTrader)
                .filter(x -> "Cambridge".equals(x.getCity()))
                .sorted(Comparator.comparing(Trader::getName))
                .distinct()
                .forEach(System.out::println);
    }

    // 4. 返回所有交易员名字字符串, 按字母排序
    @Test
    public void test6() {
        transactions.stream()
                .map(Transaction::getTrader)
                .map(Trader::getName)
                .sorted()
                .forEach(System.out::println);
        System.out.println("----------------------");
        String str = transactions.stream()
                .map((t) -> t.getTrader().getName())
                .sorted()
                .reduce("", String::concat);
        System.out.println(str);
        System.out.println("----------------------");
        transactions.stream()
                .map(Transaction::getTrader)
                .map(Trader::getName)
                .flatMap(TestStreamAPI4::filterCharacter)
                .forEach(System.out::print);
        System.out.println();
        System.out.println("----------------------");
        transactions.stream()
                .map(Transaction::getTrader)
                .map(Trader::getName)
                .flatMap(TestStreamAPI4::filterString)
                .sorted(String::compareToIgnoreCase)
                .forEach(System.out::print);
    }

    // 5. 有没有交易员是在米兰工作的
    @Test
    public void test7() {
        boolean bl = transactions.stream()
                .anyMatch((t) -> t.getTrader().getCity().equals("Milan"));
        System.out.println(bl);
    }

    // 6. 打印生活在剑桥的交易员的所有交易额
    @Test
    public void test8() {
        Optional<Integer> sum = transactions.stream()
                .filter((t) -> t.getTrader().getCity().equals("Cambridge"))
                .map(Transaction::getValue)
                .reduce(Integer::sum);
        System.out.println(sum.get());
    }

    // 7. 所有交易中, 最高的交易额是多少
    @Test
    public void test9() {
        Optional<Integer> max = transactions.stream()
                .map(Transaction::getValue)
                .max(Integer::compareTo);
        System.out.println(max.get());
    }

    // 8. 所有交易中, 最低的交易额是多少
    @Test
    public void test10() {
        Optional<Integer> min = transactions.stream()
                .map(Transaction::getValue)
                .min(Integer::compare);
        System.out.println(min.get());
    }

    public static Stream<Character> filterCharacter(String str) {
        List<Character> list = new ArrayList<>();

        for (Character ch :str.toCharArray()) {
            list.add(ch);
        }
        return list.stream();
    }

    public static Stream<String> filterString(String str) {
        List<String> list = new ArrayList<>();

        for (Character ch : str.toCharArray()) {
            list.add(ch.toString());
        }
        return list.stream();
    }
}
