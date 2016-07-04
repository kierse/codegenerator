package com.pissiphany.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by kierse on 2016-07-01.
 */
//@ModelPersistence
public class Immutable {
    private Integer someInt;
    private Long someLong;
    private BigDecimal someBigDecimal;
    private Date someDate;
    private String someString;

    private Immutable(Builder b) {
        this.someInt = b.someInt;
        this.someLong = b.someLong;
        this.someBigDecimal = b.someBigDecimal;
        this.someDate = b.someDate;
        this.someString = b.someString;
    }

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

    public void setSomeInt(Integer someInt) {
        this.someInt = someInt;
    }

    public void setSomeLong(Long someLong) {
        this.someLong = someLong;
    }

    public void setSomeBigDecimal(BigDecimal someBigDecimal) {
        this.someBigDecimal = someBigDecimal;
    }

    public void setSomeDate(Date someDate) {
        this.someDate = someDate;
    }

    public void setSomeString(String someString) {
        this.someString = someString;
    }

    public static class Builder {
        private Integer someInt;
        private Long someLong;
        private BigDecimal someBigDecimal;
        private Date someDate;
        private String someString;

        public Builder() { }

//        @ModelPersistence.Column("SOME_INT")
        public Builder setSomeInt(Integer someInt) {
            this.someInt = someInt;
            return this;
        }

//        @ModelPersistence.Column("SOME_LONG")
        public Builder setSomeLong(Long someLong) {
            this.someLong = someLong;
            return this;
        }

//        @ModelPersistence.Column("SOME_BIG_DECIMAL")
        public Builder setSomeBigDecimal(BigDecimal someBigDecimal) {
            this.someBigDecimal = someBigDecimal;
            return this;
        }

//        @ModelPersistence.Column("SOME_DATE")
        public Builder setSomeDate(Date someDate) {
            this.someDate = someDate;
            return this;
        }

//        @ModelPersistence.Column("SOME_STRING")
        public Builder setSomeString(String someString) {
            this.someString = someString;
            return this;
        }

        public Immutable build() {
            return new Immutable(this);
        }
    }
}
