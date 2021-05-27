package org.swiftboot.collections;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Matrix for any object (Beta)
 *
 * @param <E>
 * @author swiftech
 * @since 2.0.3
 */
public class Matrix<E> {
    private List<List<E>> m;

    public Matrix(int row, int col) {
        m = new ArrayList<>(row);
        for (int i = 0; i < row; i++) {
            List<E> sub = new ArrayList<>();
            for (int j = 0; j < col; j++) {
                sub.add(null);
            }
            m.add(sub);
        }
    }

    public E get(int i, int j) {
        List<E> ts = m.get(i);
        if (ts != null) {
            return ts.get(j);
        }
        return null;
    }

    public void set(int i, int j, E e) {
        List<E> sub = m.get(i);
        if (sub != null) {
            sub.set(j, e);
        }
    }

    public void clip(int row, int col) {
        List<List<E>> m2 = m.subList(0, row);
        List<List<E>> ret = new ArrayList<>();
        for (int i = 0; i < m2.size(); i++) {
            List<E> sub = m2.get(i);
            ret.add(i, sub.subList(0, col));
        }
        m = ret;
    }

    public void traverse(MatrixVisitor<E> visitor) {
        for (int i = 0; i < m.size(); i++) {
            List<E> sub = m.get(i);
            for (int j = 0; j < sub.size(); j++) {
                E e = sub.get(j);
                visitor.onElement(i, j, e);
            }
        }
    }

    public int rowCount() {
        return m.size();
    }

    public int colCount() {
        return m.size() > 0 ? m.get(0).size() : 0;
    }

    public void print() {
        for (List<E> sub : m) {
            System.out.println("|" + StringUtils.join(sub, '|') + "|");
        }
    }

    @FunctionalInterface
    public interface MatrixVisitor<E> {
        void onElement(int i, int j, E e);
    }
}
