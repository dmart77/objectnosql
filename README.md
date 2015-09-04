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

