package org.swiftboot.util.tuple;

/**
 *
 * @param <A>
 * @param <B>
 * @param <C>
 * @param <D>
 * @since 2.4.11
 */
public class Tuple4<A, B, C, D> {

    private A a;
    private B b;
    private C c;
    private D d;

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
