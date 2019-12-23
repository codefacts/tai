package orm.entity_config;

import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by sohan on 7/7/2017.
 */
final public class ArrayBuilderImpl<T> implements ArrayBuilder<T> {
    final ImmutableList.Builder<T> fieldBuilder = ImmutableList.builder();

    public ArrayBuilderImpl<T> add(T field) {
        fieldBuilder.add(field);
        return this;
    }

    public ArrayBuilderImpl<T> addAll(Collection<T> fields) {
        fieldBuilder.addAll(fields);
        return this;
    }

    public List<T> build(CreateArrayFunc<T> createArrayFunc) {
        return Arrays.asList(createArrayFunc.apply(fieldBuilder.build()));
    }
}
