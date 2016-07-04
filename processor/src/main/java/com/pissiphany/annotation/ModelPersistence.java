package com.pissiphany.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by kierse on 2016-07-02.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface ModelPersistence {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.SOURCE)
    @interface Column {
        String value();
    }
}
