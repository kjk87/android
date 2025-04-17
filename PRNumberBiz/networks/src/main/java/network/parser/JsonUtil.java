package network.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by J2N on 16. 6. 20..
 */
public class JsonUtil {

    private static Gson gson;

    static {
        gson = new GsonBuilder().create();
    }

    /**
     * @return Object -> Json
     * */
    public static String getObjToJson(Object obj, Type type) {

        return gson.toJson(obj, type);
    }

    /**
     * @return String -> Map
     * */
    public static Map<String, String> getJStringToMap(String jString) {

        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        return gson.fromJson(jString, type);
    }
}
