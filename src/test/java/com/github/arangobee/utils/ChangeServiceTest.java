package com.github.arangobee.utils;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;

import com.github.arangobee.changeset.ChangeEntry;
import com.github.arangobee.exception.ArangobeeChangeSetException;
import com.github.arangobee.test.changelogs.AnotherArangobeeTestResource;
import com.github.arangobee.test.changelogs.AnrangobeeTestResource;

import junit.framework.Assert;

/**
 * @author lstolowski
 * @since 27/07/2014
 */
public class ChangeServiceTest {

    @Test
    public void shouldFindChangeLogClasses() {
        // given
        String scanPackage=AnrangobeeTestResource.class.getPackage().getName();
        ChangeService service=new ChangeService(scanPackage);
        // when
        List<Class<?>> foundClasses=service.fetchChangeLogs();
        // then
        assertTrue(foundClasses != null && foundClasses.size() > 0);
    }

    @Test
    public void shouldFindChangeSetMethods() throws ArangobeeChangeSetException {
        // given
        String scanPackage=AnrangobeeTestResource.class.getPackage().getName();
        ChangeService service=new ChangeService(scanPackage);

        // when
        List<Method> foundMethods=service.fetchChangeSets(AnrangobeeTestResource.class);

        // then
        assertTrue(foundMethods != null);
        assertEquals(3, foundMethods.size());
    }

    @Test
    public void shouldFindAnotherChangeSetMethods() throws ArangobeeChangeSetException {
        // given
        String scanPackage=AnotherArangobeeTestResource.class.getPackage().getName();
        ChangeService service=new ChangeService(scanPackage);

        // when
        List<Method> foundMethods=service.fetchChangeSets(AnotherArangobeeTestResource.class);

        // then
        assertTrue(foundMethods != null);
        assertEquals(3, foundMethods.size());
    }

    @Test
    public void shouldFindIsRunAlwaysMethod() throws ArangobeeChangeSetException {
        // given
        String scanPackage=AnrangobeeTestResource.class.getPackage().getName();
        ChangeService service=new ChangeService(scanPackage);

        // when
        List<Method> foundMethods=service.fetchChangeSets(AnotherArangobeeTestResource.class);
        // then
        for (Method foundMethod : foundMethods) {
            if (foundMethod.getName().equals("testChangeSetWithAlways")) {
                assertTrue(service.isRunAlwaysChangeSet(foundMethod));
            } else {
                assertFalse(service.isRunAlwaysChangeSet(foundMethod));
            }
        }
    }

    @Test
    public void shouldCreateEntry() throws ArangobeeChangeSetException {

        // given
        String scanPackage=AnrangobeeTestResource.class.getPackage().getName();
        ChangeService service=new ChangeService(scanPackage);
        List<Method> foundMethods=service.fetchChangeSets(AnrangobeeTestResource.class);

        for (Method foundMethod : foundMethods) {

            // when
            ChangeEntry entry=service.createChangeEntry(foundMethod);

            // then
            Assert.assertEquals("testuser", entry.getAuthor());
            Assert.assertEquals(AnrangobeeTestResource.class.getName(), entry.getChangeLogClass());
            Assert.assertNotNull(entry.getTimestamp());
            Assert.assertNotNull(entry.getChangeId());
            Assert.assertNotNull(entry.getChangeSetMethodName());
        }
    }

    @Test(expected=ArangobeeChangeSetException.class)
    public void shouldFailOnDuplicatedChangeSets() throws ArangobeeChangeSetException {
        String scanPackage=ChangeLogWithDuplicate.class.getPackage().getName();
        ChangeService service=new ChangeService(scanPackage);
        service.fetchChangeSets(ChangeLogWithDuplicate.class);
    }

}
