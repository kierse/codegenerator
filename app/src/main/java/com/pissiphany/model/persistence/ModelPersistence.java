package com.pissiphany.model.persistence;

/**
 * Created by kierse on 2016-07-02.
 */
public interface ModelPersistence<T> {
    T fromCursor(Object cursor);
}
