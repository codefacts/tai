package tai.orm.upsert.builder;

import tai.orm.core.BuilderContext;
import tai.orm.upsert.UpsertFunction;

/**
 * Created by Jango on 2017-01-21.
 */
@FunctionalInterface
public interface UpsertFunctionBuilder {
    UpsertFunction build(String entity, BuilderContext<UpsertFunction> functionMap);
}
