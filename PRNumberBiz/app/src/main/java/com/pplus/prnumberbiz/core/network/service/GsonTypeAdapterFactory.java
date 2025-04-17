package com.pplus.prnumberbiz.core.network.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.pple.pplus.utils.part.logs.LogUtil;
import com.pplus.prnumberbiz.core.util.DebugConfig;

import java.io.IOException;

/**
 * Created by J2N on 16. 6. 20..
 */
public class GsonTypeAdapterFactory implements TypeAdapterFactory{

    private final String LOG_TAG;

    public GsonTypeAdapterFactory(Class serviceClz){

        this.LOG_TAG = serviceClz.getSimpleName();
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type){

        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);

        return new TypeAdapter<T>(){

            public void write(JsonWriter out, T value) throws IOException{

                delegate.write(out, value);
            }

            public T read(JsonReader in) throws IOException{
                JsonElement jsonElement = elementAdapter.read(in);

                if(DebugConfig.isDebugMode()) {
                    if(jsonElement.isJsonObject()) {
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        if(jsonObject.isJsonObject()) {

                            if(jsonObject.has("row")) {
                                JsonObject object = jsonObject.getAsJsonObject();

                                if(!object.isJsonNull()) {
                                    // job.. logic
                                    LogUtil.e(LOG_TAG, "response result = {}", object);
                                }
                            } else if(jsonObject.has("rows") ) {
                                JsonObject object = jsonObject.getAsJsonObject();

                                if(!object.isJsonNull()) {
                                    // job.. logic
                                    LogUtil.e(LOG_TAG, "response result = {}", object);
                                }
                            } else if(jsonObject.has("resultCode") ) {
//                            } else {
                                LogUtil.e(LOG_TAG, "response error result = {}", jsonObject.getAsJsonObject());
                            }
                        }
                    }

                }

                return delegate.fromJsonTree(jsonElement);
            }


        }.nullSafe();
    }
}
