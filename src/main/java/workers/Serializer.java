package workers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Serializer {
    private final Gson gson = new Gson();

    public <T> String toJson(T object) {
        return gson.toJson(object);
    }

    public <T> T fromJson(String json, Class<T> type) {
        return gson.fromJson(json, type);
    }

    public String convertMapToJson(Map<Object, Object> map) {
        Type itemsListType = new TypeToken<HashMap<Object, Object>>() {
        }.getType();
        return gson.toJson(map, itemsListType);
    }
}
