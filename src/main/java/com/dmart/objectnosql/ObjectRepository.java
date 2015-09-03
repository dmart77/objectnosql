package com.dmart.objectnosql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ObjectRepository implements IObjectRepository {

    protected SQLiteDatabase _db = null;
    protected SQLiteHelper _dbHelper = null;
    protected ISerializer _serializer = null;
    protected boolean _useAccentInsensitiveSearch = false;

    public ObjectRepository(Context context) {
        this(context, new GsonSerializer());
    }

    public ObjectRepository(Context context, ISerializer serializer) {
        if (context == null) {
            throw new IllegalArgumentException("context is null");
        }
        if (serializer == null) {
            throw new IllegalArgumentException("serializer is null");
        }

        _dbHelper = new SQLiteHelper(context);
        _serializer = serializer;
    }

    public void open() {
        _db = _dbHelper.getWritableDatabase();
    }

    public void close() {
        _dbHelper.close();
    }

    public void setUseAccentInsensitiveSearch(boolean useAI) {
        _useAccentInsensitiveSearch = useAI;
    }

    @Override
    public <T> void insertObject(IUniqueFilterableObject object, Class<T> objectClass) {
        ContentValues cv = new ContentValues();
        cv.put(SQLiteHelper.colObjectUuid, object.getUuid().toString());
        cv.put(SQLiteHelper.colType, objectClass.getName());
        String search = object.getKeywordsString();
        if (_useAccentInsensitiveSearch)
            search = StringUtil.removeAccents(search);
        cv.put(SQLiteHelper.colKeywords, search);
        String json = _serializer.serialize(object, objectClass);
        cv.put(SQLiteHelper.colContent, json);
        _db.insert(SQLiteHelper.tblObject, null, cv);
    }

    @Override
    public <T> void bulkInsertObjects(List<? extends IUniqueFilterableObject> objects, Class<T> objectClass) {
        if (objects == null) {
            throw new IllegalArgumentException("objects is null");
        }
        String sql = "INSERT INTO " + SQLiteHelper.tblObject + " VALUES (?,?,?,?);";
        SQLiteStatement statement = _db.compileStatement(sql);
        _db.beginTransaction();
        for (IUniqueFilterableObject o : objects) {
            statement.clearBindings();
            statement.bindString(1, o.getUuid().toString());
            statement.bindString(2, objectClass.getName());
            String search = o.getKeywordsString();
            if (_useAccentInsensitiveSearch)
                search = StringUtil.removeAccents(search);
            statement.bindString(3, search);
            String json = _serializer.serialize(o, objectClass);
            statement.bindString(4, json);
            statement.execute();
        }
        _db.setTransactionSuccessful();
        _db.endTransaction();
    }

    @Override
    public <T> List<T> findObjects(String filter, int pageSize, int pageIndex, Class<T> objectClass) {
        if (filter == null) {
            throw new IllegalArgumentException("filter is null");
        }
        int skipSize = pageIndex * pageSize;
        if (_useAccentInsensitiveSearch)
            filter = StringUtil.removeAccents(filter);
        String[] tokens = filter.split("\\s+");
        String sql = "SELECT " + SQLiteHelper.colContent +
                       " FROM " + SQLiteHelper.tblObject +
                       " WHERE " + SQLiteHelper.colType +  "=? ";
        List<String> whereArgs = new ArrayList<>();
        whereArgs.add(objectClass.getName());
        String like = "";
        for (String t : tokens) {
            if (!t.isEmpty()) {
                like += (" AND " + SQLiteHelper.colKeywords + " LIKE ? ");
                whereArgs.add("%" + t + "%");
            }
        }
        if (!like.isEmpty()) {
            sql += like;
        }
        sql += " LIMIT " + pageSize + " OFFSET " + skipSize;
        String[] argsArray = new String[whereArgs.size()];
        whereArgs.toArray(argsArray);
        Cursor cursor = _db.rawQuery(sql, argsArray);
        ArrayList<T> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            T o = null;
            do {
                String json = cursor.getString(0);
                o = (T)_serializer.deserialize(json, objectClass);
                result.add(o);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    @Override
    public <T> boolean containsObjects(Class<T> objectClass) {
        List<T> found = findObjects("", 1, 0, objectClass);
        return found.size() > 0;
    }

    @Override
    public <T> T getObject(UUID objectUuid, Class<T> objectClass) {
        if (objectUuid == null) {
            throw new IllegalArgumentException("objectUuid is null");
        }
        String sql = "SELECT " + SQLiteHelper.colContent +
                " FROM " + SQLiteHelper.tblObject +
                " WHERE " + SQLiteHelper.colType +  "=? ";
        List<String> whereArgs = new ArrayList<>();
        whereArgs.add(objectClass.getName());

        String objUuid = (" AND " + SQLiteHelper.colObjectUuid + "=? ");
        whereArgs.add(objectUuid.toString());
        sql += objUuid;
        sql += " LIMIT 1 ";

        String[] argsArray = new String[whereArgs.size()];
        whereArgs.toArray(argsArray);
        Cursor cursor = _db.rawQuery(sql, argsArray);
        T result = null;
        if (cursor.moveToFirst()) {
            String json = cursor.getString(0);
            result = (T)_serializer.deserialize(json, objectClass);
        }
        cursor.close();
        return result;
    }

    @Override
    public <T> int deleteObject(UUID objectUuid, Class<T> objectClass) {
        if (objectUuid == null) {
            throw new IllegalArgumentException("objectUuid is null");
        }
        String where = SQLiteHelper.colType +  "=? ";
        List<String> whereArgs = new ArrayList<>();
        whereArgs.add(objectClass.getName());

        String objUuid = (" AND " + SQLiteHelper.colObjectUuid + "=? ");
        whereArgs.add(objectUuid.toString());
        where += objUuid;

        String[] argsArray = new String[whereArgs.size()];
        whereArgs.toArray(argsArray);
        return _db.delete(SQLiteHelper.tblObject, where, argsArray);
    }
}
