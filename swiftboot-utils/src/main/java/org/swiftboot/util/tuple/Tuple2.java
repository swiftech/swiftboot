package org.swiftboot.util.tuple;

/**
 * Tuple object with 2 objects a, b
 *
 * @param <A>
 * @param <B>
 * @since 2.4.11
 */
public class Tuple2<A, B> {

    private final A a;
    private final B b;

    public Tuple2(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A a() {
        return a;
    }

    public B b() {
        return b;
    }
}
