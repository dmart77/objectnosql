# ObjectNoSQL
A very simple, lightweight and easy-to-use Android NoSQL database. It is basically a wrapper library over Android's SQLite database hiding all SQL related stuff. Library was born from a need to conveniently store data for offline usage and not to depend on model changes.

Main features:
* Insert
* Bulk insert
* Delete
* Query by keyword
* Query for existance of type

Bonus features:
* Pluggable serializer
* Accent insensitive search

## 1. How to include *objectnosql*

Add repository to *build.gradle*
```
repositories {
    maven {
        url  "http://dl.bintray.com/dmart77/maven"
    }
}
```
Add dependency to the same *build.gradle*
```
dependencies {
    compile 'com.dmart.objectnosql:objectnosql:1.2'
}
```

## 2. How to use *objectnosql* 

The only requirement is that your data objects that you want to store in a database must implement *IUniqueFilterableObject* interface. 
```
UUID getUuid();
String getKeywordsString();
```
The next step is instantiating *ObjectRepository* and calling any of its methods.
```
public interface IObjectRepository {
    <T> void insertObject(IUniqueFilterableObject object, Class<T> objectClass);
    <T> void bulkInsertObjects(List<? extends IUniqueFilterableObject> objects, Class<T> objectClass);
    <T> List<T> findObjects(String filter, int pageSize, int pageIndex, Class<T> objectClass);
    <T> boolean containsObjects(Class<T> objectClass);
    <T> T getObject(UUID objectUuid, Class<T> objectClass);
    <T> int deleteObject(UUID objectUuid, Class<T> objectClass);
}
```
Thats about it.
