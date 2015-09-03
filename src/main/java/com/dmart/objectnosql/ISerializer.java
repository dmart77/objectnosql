package com.dmart.objectnosql;

import java.lang.reflect.Type;

public interface ISerializer {
    String serialize(Object obj, Type type);
    Object deserialize(String json, Type type);
}
