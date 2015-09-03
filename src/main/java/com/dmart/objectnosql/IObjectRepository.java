package com.dmart.objectnosql;

import java.util.List;
import java.util.UUID;

public interface IObjectRepository {
    <T> void insertObject(IUniqueFilterableObject object, Class<T> objectClass);
    <T> void bulkInsertObjects(List<? extends IUniqueFilterableObject> objects, Class<T> objectClass);
    <T> List<T> findObjects(String filter, int pageSize, int pageIndex, Class<T> objectClass);
    <T> boolean containsObjects(Class<T> objectClass);
    <T> T getObject(UUID objectUuid, Class<T> objectClass);
    <T> int deleteObject(UUID objectUuid, Class<T> objectClass);
}
