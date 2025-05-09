package org.swiftboot.collections;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Matrix for any object (Beta)
 *
 * @param <E>
 * @author swiftech
 * @since 2.1
 */
public class Matrix<E> {
    private List<List<E>> m;

    /**
     * Create a matrix from a list with all elements.
     *
     * @param list
     * @param col
     */
    public Matrix(List<E> list, int col) {
        m = new ArrayList<>();
        int rowIdx = 0;
        int colIdx = 0;
        for (int i = 0; i < list.size(); i++) {
            E glyphNode = list.get(i);
            colIdx = i % col;
            if (colIdx == 0) {
                rowIdx = i / col;
                m.add(rowIdx, new ArrayList<>(col));
            }
            List<E> row = m.get(rowIdx);
            row.add(glyphNode);
        }
    }

    /**
     * Create a fixed size matrix with all null values.
     *
     * @param row
     * @param col
     */
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

    public List<E> getRow(int rowIdx) {
        return m.get(rowIdx);
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
        return !m.isEmpty() ? m.get(0).size() : 0;
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
