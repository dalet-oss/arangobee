![arangobee](https://raw.githubusercontent.com/cmoine/arangobee/master/misc/arangobee_min.png)

[![Build Status](https://travis-ci.org/cmoine/arangobee.svg?branch=master)](https://travis-ci.org/cmoine/arangobee) [![Coverity Scan Build Status](https://scan.coverity.com/projects/2721/badge.svg)](https://scan.coverity.com/projects/2721) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.cmoine/arangobee/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.cmoine/arangobee) [![Licence](https://img.shields.io/hexpm/l/plug.svg)](https://github.com/cmoine/arangobee/blob/master/LICENSE)
---


**arangobee** is a Java tool which helps you to *manage changes* in your MongoDB and *synchronize* them with your application.
The concept is very similar to other db migration tools such as [Liquibase](http://www.liquibase.org) or [Flyway](http://flywaydb.org) but *without using XML/JSON/YML files*.

The goal is to keep this tool simple and comfortable to use.


**arangobee** provides new approach for adding changes (change sets) based on Java classes and methods with appropriate annotations.

## Getting started

### Add a dependency

With Maven
```xml
<dependency>
  <groupId>org.cmoine</groupId>
  <artifactId>arangobee</artifactId>
  <version>0.14</version>
</dependency>
```
With Gradle
```groovy
compile 'org.javassist:javassist:3.18.2-GA' // workaround for ${javassist.version} placeholder issue*
compile 'com.github.arangobee:arangobee:0.14'
```

### Usage with Spring

You need to instantiate arangobee object and provide some configuration.
If you use Spring can be instantiated as a singleton bean in the Spring context. 
In this case the migration process will be executed automatically on startup.

```java
@Bean
public Arangobee arangobee(){
  Arangobee arangobee = new Arangobee(arango().build().db(database())); // arango() and database() methods are defined in your AbstractArangoConfiguration implementation
  arangobee.setChangeLogsScanPackage("com.example.yourapp.changelogs"); // the package to be scanned for changesets
  arangobee.setEnabled(true);
  return arangobee;
}
```

### Creating change logs

`ChangeLog` contains bunch of `ChangeSet`s. `ChangeSet` is a single task (set of instructions made on a database). In other words `ChangeLog` is a class annotated with `@ChangeLog` and containing methods annotated with `@ChangeSet`.

```java 
package com.example.yourapp.changelogs;

@ChangeLog
public class DatabaseChangelog {
  
  @ChangeSet(order = "001", id = "someChangeId", author = "testAuthor")
  public void importantWorkToDo(ArangoDatabase db){
     // task implementation
  }


}
```
#### @ChangeLog

Class with change sets must be annotated by `@ChangeLog`. There can be more than one change log class but in that case `order` argument should be provided:

```java
@ChangeLog(order = "001")
public class DatabaseChangelog {
  //...
}
```
ChangeLogs are sorted alphabetically by `order` argument and changesets are applied due to this order.

#### @ChangeSet

Method annotated by @ChangeSet is taken and applied to the database. History of applied change sets is stored in a collection called `dbchangelog` (by default) in your MongoDB

##### Annotation parameters:

`order` - string for sorting change sets in one changelog. Sorting in alphabetical order, ascending. It can be a number, a date etc.

`id` - name of a change set, **must be unique** for all change logs in a database

`author` - author of a change set

`runAlways` - _[optional, default: false]_ changeset will always be executed but only first execution event will be stored in dbchangelog collection

##### Defining ChangeSet methods
Method annotated by `@ChangeSet` can have one of the following definition:

```java
@ChangeSet(order = "001", id = "someChangeWithoutArgs", author = "testAuthor")
public void someChange1() {
   // method without arguments can do some non-db changes
}

@ChangeSet(order = "002", id = "someChangeWithMongoDatabase", author = "testAuthor")
public void someChange2(ArangoDatabase db) {
  // type: com.arangodb.ArangoDatabase, operations allowed by driver are possible
  // example: 
  ArangoCollection mycollection = db.collection("mycollection");
  BaseDocument doc = new BaseDocument();
  doc.addAttribute("testName", "example");
  doc.addAttribute("test", "1");
  mycollection.insertDocument(doc);
}

@ChangeSet(order = "006", id = "someChangeWithEnv", author = "testAuthor")
public void someChange3(ArangoDatabase db, Environment environment) {
  // type: com.arangodb.ArangoDatabase
  // type: org.springframework.core.env.Environment
  // Spring Data integration allows using MongoTemplate and Environment in the ChangeSet
}
```

### Using Spring profiles
     
**arangobee** accepts Spring's `org.springframework.context.annotation.Profile` annotation. If a change log or change set class is annotated  with `@Profile`, 
then it is activated for current application profiles.

_Example 1_: annotated change set will be invoked for a `dev` profile
```java
@Profile("dev")
@ChangeSet(author = "testuser", id = "myDevChangest", order = "01")
public void devEnvOnly(ArangoDatabase db){
  // ...
}
```
_Example 2_: all change sets in a changelog will be invoked for a `test` profile
```java
@ChangeLog(order = "1")
@Profile("test")
public class ChangelogForTestEnv{
  @ChangeSet(author = "testuser", id = "myTestChangest", order = "01")
  public void testingEnvOnly(ArangoDatabase db){
    // ...
  } 
}
```
