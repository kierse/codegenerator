package com.pissiphany.model.persistence;

import java.util.Map;

/**
 * Created by kierse on 2016-07-02.
 */
public interface ModelPersistenceService<T> {
    // TODO add NonNull annotation
    T fromCursor(Map<String, String> cursor);
}
