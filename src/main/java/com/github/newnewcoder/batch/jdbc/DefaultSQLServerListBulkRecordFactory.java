package com.github.newnewcoder.batch.jdbc;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import com.github.newnewcoder.batch.writer.Column;
import com.github.newnewcoder.batch.writer.ItemEntity;
import com.microsoft.sqlserver.jdbc.ISQLServerBulkRecord;

public class DefaultSQLServerListBulkRecordFactory implements SQLServerBulkRecordFactory {

    @Override
    public ISQLServerBulkRecord create(List<? extends ItemEntity> entity) {
        Assert.isTrue(!CollectionUtils.isEmpty(entity) && entity.get(0) != null, "TableEntity can not be null.");

        List<Field> columns = FieldUtils.getAllFieldsList(entity.get(0).getClass()).stream()
                .filter(p -> p.isAnnotationPresent(Column.class)).collect(Collectors.toList());

        Map<Integer, ColumnMetadata> columnMetadata = new HashMap();
        IntStream.range(1, columns.size()).boxed().forEach(i -> {
            Field column = columns.get(i);
            Column metadata = column.getAnnotation(Column.class);
            columnMetadata.put(i, new ColumnMetadata(column.getName(), metadata.name(), metadata.type().getValue(), metadata.precision(), metadata.scale()));
        });
        return new SQLServerListBulkRecord(entity, columnMetadata);
    }
}
