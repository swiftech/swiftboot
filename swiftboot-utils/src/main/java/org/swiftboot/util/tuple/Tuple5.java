package org.swiftboot.util.tuple;

/**
 *
 * @param <A>
 * @param <B>
 * @param <C>
 * @param <D>
 * @param <E>
 * @since 2.4.11
 */
public class Tuple5<A, B, C, D, E> {

    private A a;
    private B b;
    private C c;
    private D d;
    private E e;

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
