package tai.orm.query;

import tai.orm.entity.Entity;
import tai.orm.query.impl.JoinData;

public interface CreateJoinData {
    JoinData create(
        String parentEntityAlias, Entity parentEntity, String childEntityField, boolean isLast
    );
}
