package com.stratio.meta.common.data;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CResultSetIterator implements Iterator<com.stratio.meta.common.data.Row> {

    private final CassandraResultSet cResultSet;
    private int current;

    public CResultSetIterator(CassandraResultSet cResultSet) {
        this.cResultSet = cResultSet;
        this.current = 0;
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return current < cResultSet.getRows().size();
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public com.stratio.meta.common.data.Row next() {
        if (!hasNext()){
            throw new NoSuchElementException();
        }
        return cResultSet.getRows().get(current++);
    }

    /**
     * Removes from the underlying collection the last element returned
     * by this iterator (optional operation).  This method can be called
     * only once per call to {@link #next}.  The behavior of an iterator
     * is unspecified if the underlying collection is modified while the
     * iteration is in progress in any way other than by calling this
     * method.
     *
     * @throws UnsupportedOperationException if the {@code remove}
     *                                       operation is not supported by this iterator
     * @throws IllegalStateException         if the {@code next} method has not
     *                                       yet been called, or the {@code remove} method has already
     *                                       been called after the last call to the {@code next}
     *                                       method
     */
    @Override
    public void remove() {
        cResultSet.remove(current);
    }
}
