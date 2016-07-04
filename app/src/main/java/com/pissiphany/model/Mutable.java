package com.pissiphany.model;

import com.pissiphany.annotation.ModelPersistence;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by kierse on 2016-07-01.
 */
@ModelPersistence
public class Mutable {
    private Integer someInt;
    private Long someLong;
    private BigDecimal someBigDecimal;
    private Date someDate;
    private String someString;

    public Mutable() { }

    public Date getSomeDate() {
        return someDate;
    }

    public BigDecimal getSomeBigDecimal() {
        return someBigDecimal;
    }

    public Long getSomeLong() {
        return someLong;
    }

    public Integer getSomeInt() {
        return someInt;
    }

    public String getSomeString() {
        return someString;
    }

    @ModelPersistence.Column("SOME_INT")
    public void setSomeInt(Integer someInt) {
        this.someInt = someInt;
    }

    @ModelPersistence.Column("SOME_LONG")
    public void setSomeLong(Long someLong) {
        this.someLong = someLong;
    }

    @ModelPersistence.Column("SOME_BIG_DECIMAL")
    public void setSomeBigDecimal(BigDecimal someBigDecimal) {
        this.someBigDecimal = someBigDecimal;
    }

    @ModelPersistence.Column("SOME_DATE")
    public void setSomeDate(Date someDate) {
        this.someDate = someDate;
    }

    @ModelPersistence.Column("SOME_STRING")
    public void setSomeString(String someString) {
        this.someString = someString;
    }
}
