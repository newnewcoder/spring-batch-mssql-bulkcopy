package com.github.newnewcoder.batch;

import java.math.BigDecimal;
import java.util.Date;
import com.github.newnewcoder.batch.writer.Column;
import com.github.newnewcoder.batch.writer.ItemEntity;


import static com.github.newnewcoder.batch.writer.Column.Types.*;

public class TestModel implements ItemEntity {

    @Column(name = "DATE_COL", type = DATETIME)
    private Date dateCol;

    @Column(name = "NVARCHAR_COL", type = NVARCHAR, precision = 20)
    private String nvarcharCol;

    @Column(name = "INTEGER_COL", type = INT)
    private Integer integerCol;

    @Column(name = "VARCHAR_COL", type = VARCHAR, precision = 20)
    private String varcharCol;

    @Column(name = "DECIMAL_COL", type = DECIMAL, precision = 5, scale = 3)
    private BigDecimal decimalCol;

    public Date getDateCol() {
        return dateCol;
    }

    public void setDateCol(Date dateCol) {
        this.dateCol = dateCol;
    }

    public String getNvarcharCol() {
        return nvarcharCol;
    }

    public void setNvarcharCol(String nvarcharCol) {
        this.nvarcharCol = nvarcharCol;
    }

    public Integer getIntegerCol() {
        return integerCol;
    }

    public void setIntegerCol(Integer integerCol) {
        this.integerCol = integerCol;
    }

    public String getVarcharCol() {
        return varcharCol;
    }

    public void setVarcharCol(String varcharCol) {
        this.varcharCol = varcharCol;
    }

    public BigDecimal getDecimalCol() {
        return decimalCol;
    }

    public void setDecimalCol(BigDecimal decimalCol) {
        this.decimalCol = decimalCol;
    }
}
