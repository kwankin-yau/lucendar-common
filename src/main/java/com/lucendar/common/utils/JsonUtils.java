package com.lucendar.common.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonUtils {

    /**
     * Merge "source" into "target". If fields have equal name, merge them recursively.
     */
    public static void deepMerge(JsonObject target, JsonObject source) {
        source.entrySet().forEach(e -> {
            String key = e.getKey();
            JsonElement value = e.getValue();

            if (!target.has(key))
                target.add(key, value);
            else {
                if (value instanceof JsonObject)
                    deepMerge((JsonObject) value, target.getAsJsonObject(key));
                else
                    target.add(key, value);
            }

        });
    }
}
