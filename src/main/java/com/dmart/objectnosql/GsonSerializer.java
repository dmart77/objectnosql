package com.dmart.objectnosql;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class GsonSerializer implements ISerializer {

    protected Gson _gson;

    public GsonSerializer() {
        GsonBuilder gb = new GsonBuilder();
        gb.serializeNulls();
        gb.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        this._gson = gb.create();
    }

    @Override
    public String serialize(Object obj, Type type){
        return _gson.toJson(obj, type);
    }

    @Override
    public Object deserialize(String json, Type type) {
        return _gson.fromJson(json, type);
    }
}
