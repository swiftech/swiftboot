package org.swiftboot.collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author swiftech
 * @since 2.0.3ø
 */
class MatrixTest {
    static Matrix<Integer> m;

    @BeforeEach
    public void setup() {
        m = new Matrix<>(3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m.set(i, j, i * 10 + j);
            }
        }

    }

    @Test
    void clip() {
        System.out.println("Clip");
        m.clip(2, 2);
        m.print();
        Assertions.assertEquals(2, m.rowCount());
        Assertions.assertEquals(2, m.colCount());
    }

    @Test
    void traverse() {
        System.out.println("Traverse");
        m.traverse((i, j, integer) -> System.out.printf("(%d,%d) %d%n", i, j, integer));
        m.print();
    }
}