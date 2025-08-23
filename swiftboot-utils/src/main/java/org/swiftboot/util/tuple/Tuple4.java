package org.swiftboot.util.tuple;

/**
 * Tuple object with 4 objects a, b, c, d
 *
 * @param <A>
 * @param <B>
 * @param <C>
 * @param <D>
 * @since 2.4.11
 */
public class Tuple4<A, B, C, D> {

    private final A a;
    private final B b;
    private final C c;
    private final D d;

    public Tuple4(A a, B b, C c, D d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
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

    public D d() {
        return d;
    }
}
