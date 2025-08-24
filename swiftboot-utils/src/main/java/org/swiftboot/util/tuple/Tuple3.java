package org.swiftboot.util.tuple;

/**
 * Tuple object with 3 objects a, b, c
 *
 * @param <A>
 * @param <B>
 * @param <C>
 * @since 2.4.11
 */
public class Tuple3<A, B, C> {

    private final A a;
    private final B b;
    private final C c;

    public Tuple3(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public A a() {
        return a;
    }

    public B b() {
        return b;
    }

    public C c() {
        return c;
    }
}
