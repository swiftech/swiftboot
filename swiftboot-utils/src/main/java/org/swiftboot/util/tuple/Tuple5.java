package org.swiftboot.util.tuple;

/**
 * Tuple object with 5 objects a, b, c, d, e
 *
 * @param <A>
 * @param <B>
 * @param <C>
 * @param <D>
 * @param <E>
 * @since 2.4.11
 */
public class Tuple5<A, B, C, D, E> {

    private final A a;
    private final B b;
    private final C c;
    private final D d;
    private final E e;

    public Tuple5(A a, B b, C c, D d, E e) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
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

    public E e() {
        return e;
    }
}
