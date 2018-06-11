package com.github.arangobee.test.changelogs;

import org.springframework.core.env.Environment;

import com.arangodb.springframework.core.template.ArangoTemplate;
import com.github.arangobee.changeset.ChangeLog;
import com.github.arangobee.changeset.ChangeSet;

@ChangeLog(order = "3")
public class EnvironmentDependentTestResource {
  @ChangeSet(author = "testuser", id = "Envtest1", order = "01")
  public void testChangeSet7WithEnvironment(ArangoTemplate template, Environment env) {
    System.out.println("invoked Envtest1 with mongotemplate=" + template.toString() + " and Environment " + env);
  }
}
