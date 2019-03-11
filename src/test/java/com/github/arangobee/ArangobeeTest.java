package com.github.arangobee;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.arangobee.changeset.ChangeEntry;
import com.github.arangobee.exception.ArangobeeConfigurationException;
import com.github.arangobee.exception.ArangobeeConnectionException;
import com.github.arangobee.exception.ArangobeeException;
import com.github.arangobee.exception.ArangobeeLockException;
import com.github.arangobee.test.changelogs.AnrangobeeTestResource;

@RunWith(MockitoJUnitRunner.class)
public class ArangobeeTest extends AbstractArangobeeTest {
    @Before
    @Override
    public void before() throws ArangobeeConnectionException, ArangobeeLockException {
        super.before();
        runner.setChangeLogsScanPackage(AnrangobeeTestResource.class.getPackage().getName());
        when(dao.acquireProcessLock()).thenReturn(FAKE_LOCK);
    }

    @Test(expected=ArangobeeConfigurationException.class)
    public void shouldThrowAnExceptionIfNoDbSet() throws Exception {
        new Arangobee(null, null).execute();
    }

    @Test
    public void shouldExecuteAllChangeSets() throws Exception {
        // given
        when(dao.isNewChange(any(ChangeEntry.class))).thenReturn(true);

        // when
        runner.execute();

        // then
        verify(dao, times(6)).save(any(ChangeEntry.class));
        
        // dbchangelog collection checking
        assertEquals(1, count("test1", "testuser"));
        assertEquals(1, count("test2", "testuser"));
        assertEquals(1, count("test3", "testuser"));
        assertEquals(6, count(null, "testuser"));
    }

    @Test
    public void shouldPassOverChangeSets() throws Exception {
        // given
        when(dao.isNewChange(any(ChangeEntry.class))).thenReturn(false);

        // when
        runner.execute();

        // then
        verify(dao, times(0)).save(any(ChangeEntry.class)); // no changesets saved to dbchangelog
    }

    @Test
    public void shouldExecuteProcessWhenLockAcquired() throws Exception {
        // given
        when(dao.acquireProcessLock()).thenReturn(FAKE_LOCK);

        // when
        runner.execute();

        // then
        verify(dao, atLeastOnce()).isNewChange(any(ChangeEntry.class));
    }

    @Test
    public void shouldReleaseLockAfterWhenLockAcquired() throws Exception {
        // given
        when(dao.acquireProcessLock()).thenReturn(FAKE_LOCK);

        // when
        runner.execute();

        // then
        verify(dao).releaseProcessLock(FAKE_LOCK);
    }

    @Test(expected=ArangobeeException.class)
    public void shouldNotExecuteProcessWhenLockNotAcquired() throws Exception {
        // given
        when(dao.acquireProcessLock()).thenReturn(null);

        // when
        runner.execute();

        // then
        verify(dao, never()).isNewChange(any(ChangeEntry.class));
    }

    @Test
    public void shouldReturnExecutionStatusBasedOnDao() throws Exception {
        // given
        when(dao.isProccessLockHeld()).thenReturn(true);

        boolean inProgress = runner.isExecutionInProgress();

        // then
        assertTrue(inProgress);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldReleaseLockWhenExceptionInMigration() throws Exception {
        // given
        // would be nicer with a mock for the whole execution, but this would mean breaking out to separate class..
        // this should be "good enough"
        when(dao.acquireProcessLock()).thenReturn(FAKE_LOCK);
        when(dao.isNewChange(any(ChangeEntry.class))).thenThrow(RuntimeException.class);

        // when
        // have to catch the exception to be able to verify after
        try {
            runner.execute();
        } catch (Exception e) {
            // do nothing
        }
        // then
        verify(dao).releaseProcessLock(FAKE_LOCK);

    }
}
