package com.github.newnewcoder.batch.jdbc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import com.github.newnewcoder.batch.writer.ItemEntity;
import com.microsoft.sqlserver.jdbc.ISQLServerBulkRecord;
import com.microsoft.sqlserver.jdbc.SQLServerException;

public class SQLServerListBulkRecord implements ISQLServerBulkRecord {

    private final List<? extends ItemEntity> data;

    private int currentIndex = 0;

    private Object[] currentData;

    private final Map<Integer, ColumnMetadata> columnMetadata;

    public SQLServerListBulkRecord(List<? extends ItemEntity> data, Map<Integer, ColumnMetadata> columnMetadata) {
        this.columnMetadata = columnMetadata.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (o, n) -> o, LinkedHashMap::new));
        this.data = data;
    }

    @Override
    public Set<Integer> getColumnOrdinals() {
        return columnMetadata.keySet();
    }

    @Override
    public String getColumnName(int column) {
        return columnMetadata.get(column).columnName;
    }

    @Override
    public int getColumnType(int column) {
        return columnMetadata.get(column).columnType;
    }

    @Override
    public int getPrecision(int column) {
        return columnMetadata.get(column).precision;
    }

    @Override
    public int getScale(int column) {
        return columnMetadata.get(column).scale;
    }

    @Override
    public boolean isAutoIncrement(int column) {
        return false;
    }

    @Override
    public Object[] getRowData() throws SQLServerException {
        return currentData;
    }

    @Override
    public boolean next() throws SQLServerException {
        currentData = null;
        if (currentIndex < data.size()) {
            ItemEntity rowModel = data.get(currentIndex);
            currentData = columnMetadata.entrySet().stream().map(entry -> {
                BeanWrapper rowModelBeanWrapper = new BeanWrapperImpl(rowModel);
                return rowModelBeanWrapper.getPropertyValue(entry.getValue().fieldName);
            }).toArray();
            currentIndex++;
        }
        return currentData != null;
    }
}
