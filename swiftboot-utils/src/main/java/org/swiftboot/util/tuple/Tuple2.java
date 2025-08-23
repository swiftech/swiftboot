package org.swiftboot.util.tuple;

/**
 *
 * @param <A>
 * @param <B>
 * @since 2.4.11
 */
public class Tuple2<A, B> {

    private A a;
    private B b;

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
