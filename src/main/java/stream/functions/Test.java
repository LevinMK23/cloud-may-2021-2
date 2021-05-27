package stream.functions;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Test {

    static void get(Foo foo, int arg1, int arg2) {
        System.out.println(foo.getClass());
        System.out.println(foo.foo(arg1, arg2));
    }

    public static void main(String[] args) {

        Foo foo = (a, b) -> a * b;
        Foo sum = Integer::sum;
        get(sum, 3, 5);

        // peek, forEach
        Consumer<String> consumer = arg -> {
            arg += "!";
            System.out.println(arg);
        };
        consumer.accept("Hello");

        // filter
        Predicate<Integer> predicate = x -> x > 5;

        // map, flatMap
        Function<Integer, String> function = x -> "1 + " + x + " = " + ++x;

        System.out.println(function.apply(3));

        Supplier<HashMap<String, Integer>> supplier = HashMap::new;

        supplier.get();
    }
}
