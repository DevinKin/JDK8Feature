# Java8新特性

- Java8新特性列表
  - Lambda表达式
  - 函数式接口
  - 方法引用于构造器引用
  - Stream API
  - 接口中的默认方法与静态方法
  - 新时间日期API
  - 其他特性
- Java8新特性的优点
  - 速度更快
  - **代码更少(增加了新的语法Lambda表达式)**
  - **强大的Stream API**
  - 便于并行
  - 最大化减少空指针异常Optional



## Lambda表达式

- Lambda是一个匿名函数.

- Lambda表达式引入了`->`操作符, 将Lambda分为两个部分

  - 左侧: 指定了Lambda表达式需要的所有参数
  - 右侧: 指定了Lambda体, 即Lambda表达式要执行的功能.

- 引入Lambda表达式简化代码

  ```java
  package cn.devinkin.jdk8.lambda;
  
  import cn.devinkin.pojo.Employee;
  import org.junit.jupiter.api.Test;
  
  import java.util.*;
  
  public class TestLambda1 {
  
      // 原来使用匿名内部类
      @Test
      public void test1() {
          Comparator<Integer> com = new Comparator<Integer>() {
              @Override
              public int compare(Integer o1, Integer o2) {
                  return Integer.compare(o1, o2);
              }
          };
  
          TreeSet<Integer> ts = new TreeSet<>(com);
      }
  
      // Lambda 表达式
      @Test
      public void test2() {
          Comparator<Integer> com = (x, y) -> Integer.compare(x, y);
          TreeSet<Integer> ts = new TreeSet<>(com);
      }
  
      private List<Employee> employees = Arrays.asList(
              new Employee("zhangsan", 18, 999.99),
              new Employee("lisi", 20, 9929.99),
              new Employee("wangwu", 38, 1999.99),
              new Employee("zhaowu", 58, 3999.99),
              new Employee("tianqi", 68, 4999.99)
      );
  
      @Test
      public void test3() {
          List<Employee> list = filterEmployees(employees);
          for (Employee emp :list) {
              System.out.println(emp);
          }
      }
  
      @Test
      public void test4() {
          List<Employee> list1 = filterEmployees(this.employees, new FilterEmployeeByAge());
          List<Employee> list2 = filterEmployees(this.employees, new FilterEmployeeBySalary());
          for (Employee emp : list1) {
              System.out.println(emp);
          }
  
          for (Employee emp : list2) {
              System.out.println(emp);
          }
      }
  
      // 优化方式2: 匿名内部类
      @Test
      public void test5() {
          List<Employee> employees = filterEmployees(this.employees, new MyPredicate<Employee>() {
              @Override
              public boolean test(Employee employee) {
                  return employee.getSalary() <= 5000;
              }
          });
  
          for (Employee emp : employees) {
              System.out.println(emp);
          }
      }
  
      // 优化方式3: Lambda表达式
      @Test
      public void test6() {
          List<Employee> list = filterEmployees(employees, (e) -> e.getSalary() >= 5000);
          list.forEach(System.out::println);
      }
  
      // 优化方式4: Stream Api
      @Test
      public void test7() {
          employees.stream()
                  .filter((e) -> e.getSalary() >= 5000)
                  .forEach(System.out::println);
          System.out.println("--------------------------");
          employees.stream()
                  .map(Employee::getName)
                  .forEach(System.out::println);
      }
  
      // 需求: 获取当前公司中员工年龄大于25的员工信息
      public List<Employee> filterEmployees(List<Employee> list) {
          List<Employee> emps = new ArrayList<>();
  
          for (Employee emp : list) {
              if (emp.getAge() >=  35) {
                  emps.add(emp);
              }
          }
          return emps;
      }
  
      // 新需求: 获取当前公司中员工工资大于5000的员工信息
      public List<Employee> filterEmployees2(List<Employee> list) {
          List<Employee> emps = new ArrayList<>();
  
          for (Employee emp : list) {
              if (emp.getSalary() >= 5000) {
                  emps.add(emp);
              }
          }
          return emps;
      }
  
      // 优化方式1: 设计模式, 策略设计模式
      public List<Employee> filterEmployees(List<Employee> list, MyPredicate<Employee> mp) {
          List<Employee> emps = new ArrayList<>();
  
          for (Employee employee : list) {
              if (mp.test(employee)) {
                  emps.add(employee);
              }
          }
          return emps;
      }
  }
  ```

### Lambda表达式语法

- 无参, 无返回值, Lambda体只需一条语句

  ```java
  Runnable r1 = () -> System.out.println("Hello Lambda!");
  ```

- Lambda需要一个参数

  ```java
  Consumer<String> fun = (args) -> System.out.println(args);
  ```

- Lambda只需要一个参数时, 参数的小括号可以省略

  ```java
  Consumer<String> fun = args -> System.out.println(args);
  ```

- Lambda需要两个参数, 并且有返回值, Lambda体有多条语句

  ```java
  BinaryOperator<Long> bo = (x, y) -> {
      System.out.println("实现函数接口方法!");
      return x + y;
  }
  ```

- 当Lambda体只有一条语句时, return与大括号可以省略.

  ```java
  BinaryOperator<Long> bo = (x, y) -> x + y;
  ```

- Lambda表达式中的参数类型都是由编译器推断得出的. Lambda表达式中无需指定类型.

## 函数式接口

- 接口中只有一个抽象方法的接口, 称为函数式接口.
- 可以通过Lambda表达式来创建该接口的对象.
  - 若Lambda表达式抛出一个受检异常, 那么该异常需要在目标接口的抽象方法上进行声明.
- 可以使用注解`@FunctionalInterface`修饰来检查是否是函数式接口.

### Java内置四大核心函数式接口

|        函数式接口        | 参数类型 | 返回类型 |                             用途                             |
| :----------------------: | :------: | :------: | :----------------------------------------------------------: |
|  Consumer<T> 消费型接口  |    T     |   void   |     对类型为T的对象应用操作, 包含方法: void accept(T t)      |
|  Supplier<T>供给型接口   |    无    |    T     |             返回类型为T的对象, 包含方法: T get()             |
| Function<T, R>函数型接口 |    T     |    R     | 对类型为T的对象应用操作, 并返回结果. 结果时R类型的对象. 包含方法: R apply(T t) |
|  Predicate<T>断定型接口  |    T     | boolean  | 确定类型为T的对象是否满足某约束, 并返回boolean值. 包含方法boolean test(T t) |

- 示例代码

  ```java
  public class testLambda4 {
  
      public void happy(double money, Consumer<Double> con) {
          con.accept(money);
      }
      @Test
      public void testConsumer() {
          happy(100.12, (d) -> System.out.println("consume: " + d));
      }
  
      // 产生指定个整数, 并放入集合中
      public List<Integer> getNumList(int num, Supplier<Integer> sup) {
          List<Integer> list = new ArrayList<>();
          for (int i = 0; i < num; i++) {
              Integer n = sup.get();
              list.add(n);
          }
  
          return list;
      }
  
      @Test
      public void testSupplier() {
          System.out.println(getNumList(10, () -> (int)(Math.random() * 100)));
          System.out.println(getNumList(5, () -> (int)(Math.random()* 1000)));
          getNumList(5, () -> (int)(Math.random() * 100)).forEach(System.out::println);
      }
  
      public String strHandler(String str, Function<String, String> fun) {
          return fun.apply(str);
      }
  
      @Test
      public void testFunction() {
          System.out.println(strHandler("\t\t\t test??? test \t\t", String::trim));
          System.out.println(strHandler("\t\t\t\t testlkjalsdfua. a.sd,fmaklsj", (str) -> str.substring(3, 9)));
      }
  
      // 需求: 将满足条件的字符串放入集合中
      public List<String> filterStr(List<String> strList, Predicate<String> pre) {
          List<String> list = new ArrayList<>();
          for (String str : strList) {
              if (pre.test(str)) {
                  list.add(str);
              }
          }
          return list;
      }
  
      @Test
      public void testPredicate() {
          List<String> list = Arrays.asList("Hello", "devinkin", "Lambda", "Centos","Devinkin", "ok", "bi", "lala");
          System.out.println(filterStr(list, (str) -> str.length() > 3));
          filterStr(list, (str) -> str.length() < 3).forEach(x -> System.out.println(x));
      }
  }
  ```


### 其他接口

|                        函数式接口                         |      参数类型      |     返回类型      |                             用途                             |
| :-------------------------------------------------------: | :----------------: | :---------------: | :----------------------------------------------------------: |
|                    BiFunction<T, U, R>                    |        T, U        |         R         | 对类型为T, U 参数应用操作, 返回R类型的结果. 包含方法为R apply(T t, U u) |
|             UnaryOperator<T>(Function子接口)              |         T          |         T         | 对类型为T的对象进行一元运算, 并返回T类型的结果. 包含方法为T apply(T t) |
|           BinaryOperator<T>(BiFunction 子接口)            |        T, T        |         T         | 对类型为T的对象进行二元运算, 并返回T类型的结果. 包含方法为T apply(T t1, T t2) |
|                     BiConsumer<T, U>                      |        T, U        |       void        |  对类型为T, U参数应用操作, 包含方法为void accept(T t, U u)   |
| ToIntFunction<T>  ToLongFunction<T>   ToDoubleFunction<T> |         T          | int, long, double |              分别计算int, long, double 值的函数              |
| IntFunction<R>    LongFunction<R>      DoubleFunction<R>  | int   long  double |         R         |            参数分别为int, long, double类型的函数             |

## 方法引用

- 当要传递给Lambda体的操作, 已经有已经实现了的方法, 可以使用方法引用.
- 实现Lambda的参数列表与返回值, 必须与函数式接口抽象方法的参数列表与返回值保持一致.
- 方法引用: 使用操作符`::`将方法名和对象或类的名字分隔开来. 主要有3种情况:
  - 对象::实例方法
  - 类::静态方法
  - 类::实例方法
    - 参数列表中第一个参数是实例方法的调用者, 而第二个参数时示例方法的参数时.

### 构造器引用

- 格式: `ClassName::new`
- 说明: 与函数式接口相结合, 自动与函数式接口中方法兼容. 可以把构造器引用赋值给定义的方法, 与构造器参数列表与接口抽象方法的参数列表一致.

### 数组引用

- 格式: `type[]::new`

### 使用案例代码

```java
public class TestMethodRef {

    // 对象::实例方法名
    @Test
    public void test1() {
        Consumer<String> con1 = x -> System.out.println(x);
        PrintStream ps = System.out;
        Consumer<String> cons2 = ps::println;
        cons2.accept("test");
    }

    @Test
    public void test2() {
        Employee emp = new Employee("king", 38, 12311.24);
        Supplier<String> sup1 = () -> emp.getName();
        Supplier<String> sup2 = emp::getName;
        System.out.println(sup1.get());
        System.out.println(sup2.get());
    }

    // 类::静态方法名
    @Test
    public void test3() {
        Comparator<Integer> com1 = (x, y) -> Integer.compare(x, y);
        //Integer.compare是静态方法
        Comparator<Integer> com2 = Integer::compare;
    }

    // 类::实例方法
    @Test
    public void test4() {
        BiPredicate<String, String> bp1 = (x, y) -> x.equals(y);
        BiPredicate<String, String> bp2 = String::equals;
    }

    // 构造器引用
    @Test
    public void test5() {
        Supplier<Employee> sup1 = () -> new Employee("king", 23, 1234.34);
        Supplier<Employee> sup2 = Employee::new;
        Function<String, Employee> fun1 = (x) -> new Employee(x);
        Function<String, Employee> fun2 = Employee::new;
        BiFunction<String, Integer, Employee> bf1 = Employee::new;

        System.out.println(sup1.get());
        System.out.println(sup2.get());

        System.out.println(fun1.apply("king1"));
        System.out.println(fun2.apply("king2"));

        System.out.println(bf1.apply("two", 13));
    }

    // 数组引用
    @Test
    public void test6() {
        Function<Integer, String[]> fun1 = (x) -> new String[x];
        Function<Integer, String[]> fun2 = String[]::new;
        System.out.println(fun2.apply(10).length);

        Function<Integer, String[]> fun3 = String[]::new;
        String[] strs2 = fun2.apply(20);
        System.out.println(strs2.length);
    }
}
```

## Stream API

### 流的简介

- Stream不会存储元素
- Stream不会改变源对象, 会返回一个持有结果的新Stream.
- Stream操作时延迟执行的. 等到需要结果的时候才执行.

### Stream操作的三个步骤

- 创建
- 中间操作
- 终止操作(终端操作)

### 创建Stream

- 四种方法
  - 通过Collection接口获取流
  - 通过数组创建流
  - 由值创建流
  - 由函数创建流(创建无限流)

- Java8中Collection接口被扩展了, 提供了两个获取流的方法
  - `default Stream<E> stream()`: 返回一个串行流.
  - `default Stream<E> parallelStream()`: 返回一个并行流. 

- Arrays的静态方法`stream()`可以获取数组流

  - `static <T> Stream<T> stream(T[] array)`: 返回一个流.
- 由值创建流: 使用静态方法`Stream.of()`

  - `public static<T> Stream<T> of(T... values)`: 返回一个流
- 由函数创建流: 创建无限流, 使用静态方法`Stream.iterate()`和`Stream.generate()`
  - 迭代: `public static<T> Stream<T> iterate(final T seed, final UnaryOperator<T> f)`, UnaryOperator是一元运算的函数式接口.
  - 生成: `public static<T> Stream<T> generate(Supplier<T> s)`

- 示例代码

  ```java
  package cn.devinkin.jdk8.stream;
  
  import cn.devinkin.pojo.Employee;
  import org.junit.jupiter.api.Test;
  
  import java.util.ArrayList;
  import java.util.Arrays;
  import java.util.List;
  import java.util.stream.Stream;
  
  public class TestStreamAPI1 {
      // 创建Stream
      @Test
      public void test1() {
          // 1. 通过Collection接口扩展的方法获取流
          List<String> list = new ArrayList<>();
          Stream<String> strStream1 = list.stream();
          Stream<String> strStream2 = list.parallelStream();
  
          // 2. 通过Arrays中的静态方法stream()获取数组流
          Employee[] emps = new Employee[10];
          Stream<Employee> employeeStream = Arrays.stream(emps);
  
          // 3. 通过Stream类的静态方法of()
          Stream<String> stream3 = Stream.of("aa", "bb", "cc");
  
          // 4. 创建无限流
          // 迭代
          Stream<Integer> stream4 = Stream.iterate(0, (x) -> x + 2);
          stream4.limit(10).forEach(System.out::println);
          // 生成
          Stream<Double> stream5 = Stream.generate(Math::random);
          stream5.limit(10).forEach(System.out::println);
      }
  }
  ```

### Stream的中间操作

- 惰性求值: 除非出发终止操作, 否则中间操作不会执行任何的处理, 而在终止操作时一次性全部处理.

- 筛选与切片

  |        方法         |                             描述                             |
  | :-----------------: | :----------------------------------------------------------: |
  | filter(Predicate p) |               接收Lambda, 从流中排除某些元素.                |
  |     distinct()      |   筛选, 通过流生成元素的hashCode()和equals()去除重复元素.    |
  | limit(long maxSize) |               截断流, 使其元素不超过给定数量.                |
  |    skip(long n)     | 跳过元素, 返回一个扔掉了前n个元素的流, 若流中的元素不足n个, 则返回一个空流, 与limit(n)互补. |

- 映射

  |              方法               |                             描述                             |
  | :-----------------------------: | :----------------------------------------------------------: |
  |         map(Function f)         | 接收一个函数作为参数, 该函数会被应用到每个元素上, 并将其映射到一个新的元素. |
  | mapToDouble(ToDoubleFunction f) | 接收一个函数作为参数, 该函数会被应用带每个元素上, 并产生一个新的DoubleStream. |
  |    mapToInt(ToIntFunction f)    | 接收一个函数作为参数, 该函数会被应用带每个元素上, 并产生一个新的IntStream. |
  |   mapToLong(ToLongFunction f)   | 接收一个函数作为参数, 该函数会被应用带每个元素上, 并产生一个新的LongStream. |
  |       flatMap(Function f)       | 接收一个函数作为参数, 将流中的每个值都换成另一个流, 然后把所有流连接成一个流. |

- 排序

  |          方法           |                描述                 |
  | :---------------------: | :---------------------------------: |
  |        sorted()         |  产生一个新流, 其中按自然顺序排序.  |
  | sorted(Comparator comp) | 产生一个新流, 其中按比较器顺序排序. |

### Stream的终止操作

- 终止操作会从流中生成结果, 其结果可以是任何不适流的值.

- 查找和匹配

  |          方法          |                            描述                             |
  | :--------------------: | :---------------------------------------------------------: |
  | allMatch(Predicate p)  |                    检查是否匹配所有元素                     |
  | anyMatch(Predicate p)  |                  检查是否至少匹配一个元素                   |
  | noneMatch(Predicate p) |                  检查是否没有匹配所有元素                   |
  |      findFirst()       |                       返回第一个元素                        |
  |       findAny()        |                   返回当前流中的任意元素                    |
  |        count()         |                      返回流中元素总数                       |
  |   max(Comparator c)    |                       返回流中最大值                        |
  |   min(Comparator c)    |                       返回流中最小值                        |
  |  forEach(Consumer c)   | 内部迭代(使用Collection接口需要用户去做迭代, 称为外部迭代.) |

- 归约, BinaryOperator是二元运算符.

  |               方法               |                          描述                           |
  | :------------------------------: | :-----------------------------------------------------: |
  | reduce(T iden, BinaryOperator b) |     可以将流中元素反复结合起来, 得到一个值. 返回T.      |
  |     reduce(BinaryOperator b)     | 可以将流中元素反复结合起来, 得到一个值. 返回Optional<T> |

- 收集

  |         方法         |                             描述                             |
  | :------------------: | :----------------------------------------------------------: |
  | collect(Collector c) | 将流转换为其他形式. 接收一个Collector接口的实现, 用于给Stream中元素做汇总的方法. |

### 测试代码

```java
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
```

### 并行流和顺序流

- 并行流就是把一个内容分成多个数据块, 并用不同线程分别处理每个数据块的流.

- Java8中将并行进行了优化, 我们可以很容易的对数据进行并行操作. StreamAPI可以声明性地通过`parallel()`与`sequential()`在并行流和顺序流之间进行切换.

- 获取一定范围的数字流序列, 闭区间: `LongStream.rangeClosed()`

- Fork-Join测试代码

  - ForkJoinCaculation

    ```java
    package cn.devinkin.jdk8.fj;
    
    import java.util.concurrent.RecursiveTask;
    
    public class ForkJoinCaculation extends RecursiveTask<Long> {
        private long start;
        private long end;
    
        private static final long THRESHOLD = 10000;
    
        public ForkJoinCaculation(long start, long end) {
            this.start = start;
            this.end = end;
        }
    
        @Override
        protected Long compute() {
            long length = end - start;
            if (length <= THRESHOLD) {
                long sum = 0;
    
                for (long i = start; i <= end; i++) {
                    sum += i;
                }
                return sum;
            } else {
                long middle = (start + end) / 2;
                ForkJoinCaculation left = new ForkJoinCaculation(start, middle);
                // 拆分子任务, 压入线程队列
                left.fork();
                ForkJoinCaculation right = new ForkJoinCaculation(middle + 1, end);
                // 拆分子任务, 压入线程队列
                right.fork();
    
                return left.join() + right.join();
            }
        }
    }
    ```

  - TestForkJoin

    ```java
    package cn.devinkin.jdk8.fj;
    
    
    import org.junit.Test;
    
    import java.time.Duration;
    import java.time.Instant;
    import java.util.concurrent.ForkJoinPool;
    import java.util.concurrent.ForkJoinTask;
    import java.util.stream.LongStream;
    
    public class TestForkJoin {
    
        @Test
        public void test1() {
            Instant start = Instant.now();
            ForkJoinPool pool = new ForkJoinPool();
            ForkJoinTask<Long> task = new ForkJoinCaculation(0, 50000000L);
            Long sum = pool.invoke(task);
            System.out.println(sum);
            Instant end = Instant.now();
            System.out.println(Duration.between(start, end).toMillis() + " ms");
        }
    
        @Test
        public void test2() {
            Instant start = Instant.now();
            long sum = 0;
            for (long i = 0; i <= 500000000L; i++) {
                sum += i;
            }
            System.out.println(sum);
            Instant end = Instant.now();
            System.out.println(Duration.between(start,end).toMillis() + " ms");
        }
    
    
        /**
         * java8并行流
         */
        @Test
        public void test3() {
            Instant start = Instant.now();
            long sum = LongStream.rangeClosed(0, 1000000000L)
                    .parallel()
                    .reduce(0, Long::sum);
            System.out.println(sum);
            Instant end = Instant.now();
            System.out.println(Duration.between(start,end).toMillis() + " ms");
        }
    }
    ```


## Optional类

- `Optional<T>`类(java.util.Optional)是一个容器类, 代表一值或不存在, 原来用null表示一个值不存在, 现在用Optional可以更好的表达这个概念. 并且可以避免空指针异常.
- 常用方法
  - `Optional.of(T t)`: 创建一个Optional实例
  - `Optional.empty()`: 创建一个空的Optional实例
  - `Optional.ofNullable(T t)`: 若t不为null, 创建Optional实例, 否则创建空实例
  - `isPresent()`: 判断是否包含值
  - `orElse(T t)`: 如果调用对象包含值, 返回该值, 否则返回t
  - orElseGet(Supplier s): 如果调用对象包含值, 返回该值, 否则返回`s`获取的值
  - `map(Function f)`: 如果有值对其处理, 并返回处理后的`Optional`, 否则返回`Optional.empty()`
  - `flatMap(Function mapper)`: 与`map`类似, 要求返回值必须是`Optional`

## 接口中的默认方法和静态方法

### 默认方法

- 类优先原则: 若一个接口定义了一个默认方法, 而另一个父类或接口中又定义了一个同名的方法时
  - 选择父类中的方法. 如果一个父类提供了具体的实现, 那么接口中具有相同名称和参数的默认方法会被忽略.
  - 接口冲突. 如果一个父接口提供一个默认方法, 而另一个接口也提供了一个具有相同名称和参数列表的方法(不管方法是否是默认方法), 那么必须覆盖该方法来解决冲突.

- 选择父类中的方法: 测试代码

  - MyFun

    ```java
    package cn.devinkin.jdk8.sdinterface;
    
    public interface MyFun {
        default String getName() {
            return "哈哈哈";
        }
    }
    ```

  - MyClass

    ```java
    package cn.devinkin.jdk8.sdinterface;
    
    public class MyClass {
        public String getName() {
            return "呵呵呵";
        }
    }
    ```

  - SubClass

    ```java
    package cn.devinkin.jdk8.sdinterface;
    
    public class SubClass extends MyClass implements MyFun {
    }
    ```

  - MyTest

    ```java
    package cn.devinkin.jdk8.sdinterface;
    
    public class TestDefaultInterface {
        public static void main(String[] args) {
            SubClass sc = new SubClass();
            System.out.println(sc.getName());
        }
    }											
    ```

- 接口冲突: 测试代码

  - MyInterface

    ```java
    package cn.devinkin.jdk8.sdinterface;
    
    public interface MyInterface {
        default String getName() {
            return "嘿嘿嘿";
        }
    }
    ```

  - SubClass

    ```java
    package cn.devinkin.jdk8.sdinterface;
    
    public class SubClass implements MyFun, MyInterface{
    
        @Override
        public String getName() {
            return MyInterface.super.getName();
        }
    }
    ```

### 接口中的静态方法

- 与类的静态方法基本一致

## 新时间日期API

|                        方法                        |                             描述                             | 示例                                                         |
| :------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------- |
|                       now()                        |                静态方法, 根据当前时间创建对象                | LocalDate localDate = LocalDate.now();                                        LocalTime localTime = LocalTime.now(); |
|                        of()                        |             静态方法, 根据指定日期/时间创建对象              | LocalDate localDate = LocalDate.of(2016, 10, 26);               LocalTime localTime = LocalTime.of(02, 22, 56);              LocalDateTime localDateTime = LocalDateTime.of(2016,  12, 10, 55); |
|     plusDays, plusWeeks, plusMonths, plusYears     |       向当前LocalDate对象添加几天, 几周, 几个月, 几年        |                                                              |
| minusDays,  withDayOfYear,   withMonth,   withYear | 将月份天数, 年份天数, 月份, 年份修改为指定的值并返回新的LocalDate对象 |                                                              |
|                    plus, minus                     |                添加或减少一个Duration或Period                |                                                              |
| withDayOfMonth, withDayOfYear, withMonth, withYear | 将月份天数，年份天数，月份，年份修改为指定的值并返回新的LocalDate对象 |                                                              |
|                   getDayOfMonth                    |                      获得月份天数(1-31)                      |                                                              |
|                    getDayOfYear                    |                     获得年份天数(1-366)                      |                                                              |
|                    getDayOfWeek                    |            获得星期几（返回一个DayOfWeek枚举值）             |                                                              |
|                      getMonth                      |                获得月份，返回一个Month枚举值                 |                                                              |
|                   getMonthValue                    |                       获得月份（1-12）                       |                                                              |
|                      getYear                       |                           获得年份                           |                                                              |
|                       until                        |    获得两个日期之间的Period对象或者指定ChronoUnits的数字     |                                                              |
|                 isBefore，isAfter                  |                      比较连个LocalDate                       |                                                              |
|                     isLeapYear                     |                        判断是否是闰年                        |                                                              |

- Instant：时间戳，用于时间戳的运算，以Unix元年（UTC时区1970年1月1日午夜时分）开始所经历的描述进行运算。
- Duration：用于计算两个“时间”间隔。
- Period：用于计算两个”日期“间隔。

### 日期的操纵

- TemporalAdjuster：时间校正器。
- TemporalAdjusters：该类通过静态方法提供了大量的常用TemporalAdjuster的实现。

### 解析与格式化

- `java.time.format.DateTimeFormatter`类：提供了三种格式化方法
  - 预定义的标准格式。
  - 语言环境相关的格式。
  - 自定义的格式。

### 时区的处理

- 带时区的时间分别为
  - ZonedDate
  - ZonedTime
  - ZonedDateTime
- 每个时区都对应着ID，地区ID都为`{区域}/{城市}`的格式，如`Asia/Shanghai`
- ZoneId：该类包含了所有的时区信息
  - `getAvailableZoneIds()`：可以获取所有时区的时区信息。
  - of(id)：用于指定的时区信息获取ZoneId对象。

### 与传统日期处理的转换

| 类                                                          | To遗留类                              | From遗留类                  |
| ----------------------------------------------------------- | ------------------------------------- | --------------------------- |
| java.time.Instant <-> java.util.Date                        | Date.from(instant)                    | date.toInstant()            |
| java.time.Instant <-> java.sql.TimeStamp                    | Timestamp.from(instant)               | timestamp.toInstant()       |
| java.time.ZonedDateTime <-> java.util.GregorianCalendar     | GregorianCalendar.from(zonedDateTime) | cal.toZonedDateTime()       |
| java.time.LocalDate <-> java.sql.Time                       | Date.valueOf(localDate)               | date.toLocalDate()          |
| java.time.LocalTime <-> java.sql.Time                       | Date.valueOf(localDate)               | date.toLocalTime()          |
| java.time.LocalDateTime <-> java.sql.Timestamp              | Timestamp.valueOf(localDateTime)      | timestamp.toLocalDateTime() |
| java.time.ZoneId <-> java.util.TimeZone                     | Timezone.getTimeZone(id)              | timeZone.toZoneId()         |
| java.time.format.DateTimeFormatter <-> java.text.DateFormat | formatter.toFormat()                  | 无                          |

## 可重复注解

- Java8中：可以使用可重复的注解以及可用于类型的注解。

- 编写注解，使用`@Repeatable(MyAnnotations.class)`注解的容器。

  ```java
  package cn.devinkin.jdk8.annotation;
  
  import java.lang.annotation.Repeatable;
  import java.lang.annotation.Retention;
  import java.lang.annotation.RetentionPolicy;
  import java.lang.annotation.Target;
  
  import static java.lang.annotation.ElementType.*;
  
  @Repeatable(MyAnnotations.class)
  @Target({TYPE,FIELD,METHOD,PARAMETER,CONSTRUCTOR,LOCAL_VARIABLE})
  @Retention(RetentionPolicy.RUNTIME)
  public @interface MyAnnotation {
  
      String value() default  "devinkin";
  }
  ```

- 编写注解容器

  ```java
  package cn.devinkin.jdk8.annotation;
  
  import java.lang.annotation.Retention;
  import java.lang.annotation.RetentionPolicy;
  import java.lang.annotation.Target;
  
  import static java.lang.annotation.ElementType.*;
  
  @Target({TYPE,FIELD,METHOD,PARAMETER,CONSTRUCTOR,LOCAL_VARIABLE})
  @Retention(RetentionPolicy.RUNTIME)
  public @interface MyAnnotations {
  
      MyAnnotation[] value();
  }
  ```

- 编写测试类

  ```java
      @Test
      public void test1() throws NoSuchMethodException {
          Class<TestAnnotation> clazz = TestAnnotation.class;
          Method m1 = clazz.getMethod("show", String.class);
  
          MyAnnotation[] mas = m1.getAnnotationsByType(MyAnnotation.class);
          for (MyAnnotation ma : mas) {
              System.out.println(ma.value());
          }
      }
  ```


