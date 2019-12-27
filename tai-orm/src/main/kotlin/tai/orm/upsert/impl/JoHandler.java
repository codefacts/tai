package tai.orm.upsert.impl;

import java.util.Map;

/**
 * Created by Jango on 17/02/07.
 */
public interface JoHandler {
    void handle(Map<String, Object> jsonObject);
}
