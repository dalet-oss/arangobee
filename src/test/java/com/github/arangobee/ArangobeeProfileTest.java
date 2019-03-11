package com.github.arangobee;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.arangobee.changeset.ChangeEntry;
import com.github.arangobee.exception.ArangobeeConnectionException;
import com.github.arangobee.exception.ArangobeeLockException;
import com.github.arangobee.resources.EnvironmentMock;
import com.github.arangobee.test.changelogs.AnotherArangobeeTestResource;
import com.github.arangobee.test.profiles.def.UnProfiledChangeLog;
import com.github.arangobee.test.profiles.dev.ProfiledDevChangeLog;

/**
 * Tests for Spring profiles integration
 *
 * @author lstolowski
 * @since 2014-09-17
 */
@RunWith(MockitoJUnitRunner.class)
public class ArangobeeProfileTest extends AbstractArangobeeTest {
    public static final int CHANGELOG_COUNT=6;

    @Before
    @Override
    public void before() throws ArangobeeConnectionException, ArangobeeLockException {
        super.before();
        when(dao.acquireProcessLock()).thenReturn(FAKE_LOCK);
    }

    @Test
    public void shouldRunDevProfileAndNonAnnotated() throws Exception {
        // given
        setSpringEnvironment(new EnvironmentMock("dev", "test"));
        runner.setChangeLogsScanPackage(ProfiledDevChangeLog.class.getPackage().getName());
        when(dao.isNewChange(any(ChangeEntry.class))).thenReturn(true);

        // when
        runner.execute();

        // then
        assertEquals(1, count("Pdev1", "testuser"));  //  no-@Profile  should not match

        assertEquals(1, count("Pdev4", "testuser"));  //  @Profile("dev")  should not match

        assertEquals(0, count("Pdev3", "testuser"));  //  @Profile("default")  should not match
    }

    @Test
    public void shouldRunUnprofiledChangeLog() throws Exception {
        // given
        setSpringEnvironment(new EnvironmentMock("test"));
        runner.setChangeLogsScanPackage(UnProfiledChangeLog.class.getPackage().getName());
        when(dao.isNewChange(any(ChangeEntry.class))).thenReturn(true);

        // when
        runner.execute();

        // then
        assertEquals(1, count("Pdev1", "testuser"));

        assertEquals(1, count("Pdev2", "testuser"));

        assertEquals(1, count("Pdev3", "testuser"));  //  @Profile("dev")  should not match

        assertEquals(0, count("Pdev4", "testuser"));  //  @Profile("pro")  should not match

        assertEquals(1, count("Pdev5", "testuser"));  //  @Profile("!pro")  should match
    }

    @Test
    public void shouldNotRunAnyChangeSet() throws Exception {
        // given
        setSpringEnvironment(new EnvironmentMock("foobar"));
        runner.setChangeLogsScanPackage(ProfiledDevChangeLog.class.getPackage().getName());
        when(dao.isNewChange(any(ChangeEntry.class))).thenReturn(true);

        // when
        runner.execute();

        // then
        assertEquals(0, count(null, null));
    }

    @Test
    public void shouldRunChangeSetsWhenNoEnv() throws Exception {
        // given
        setSpringEnvironment(null);
        runner.setChangeLogsScanPackage(AnotherArangobeeTestResource.class.getPackage().getName());
        when(dao.isNewChange(any(ChangeEntry.class))).thenReturn(true);

        // when
        runner.execute();

        // then
        assertEquals(CHANGELOG_COUNT, count(null, null));
    }

    @Test
    public void shouldRunChangeSetsWhenEmptyEnv() throws Exception {
        // given
        setSpringEnvironment(new EnvironmentMock());
        runner.setChangeLogsScanPackage(AnotherArangobeeTestResource.class.getPackage().getName());
        when(dao.isNewChange(any(ChangeEntry.class))).thenReturn(true);

        // when
        runner.execute();

        // then
        assertEquals(CHANGELOG_COUNT, count(null, null));
    }

    @Test
    public void shouldRunAllChangeSets() throws Exception {
        // given
        setSpringEnvironment(new EnvironmentMock("dev"));
        runner.setChangeLogsScanPackage(AnotherArangobeeTestResource.class.getPackage().getName());
        when(dao.isNewChange(any(ChangeEntry.class))).thenReturn(true);

        // when
        runner.execute();

        // then
        assertEquals(CHANGELOG_COUNT, count(null, null));
    }
}
