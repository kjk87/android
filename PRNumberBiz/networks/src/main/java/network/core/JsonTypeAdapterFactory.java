package network.core;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by J2N on 16. 6. 20..
 */
public class JsonTypeAdapterFactory implements TypeAdapterFactory{
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);

        return new TypeAdapter<T>() {

            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            public T read(JsonReader in) throws IOException {

                JsonElement jsonElement = elementAdapter.read(in);

//                if (jsonElement.isJsonObject()) {
//                    JsonObject jsonObject = jsonElement.getAsJsonObject();
//                    if(jsonObject.isJsonObject()){
//                        if (jsonObject.has("data") && jsonObject.get("data").isJsonObject())
//                        {
//                            JsonObject object = jsonObject.getAsJsonObject();
//
//                            //                            if(!object.isJsonNull()){
//                            //                                // job.. logic
//                            //                            }
//                        }
//                    }
//
//                }

                return delegate.fromJsonTree(jsonElement);
            }
        }.nullSafe();
    }
}
