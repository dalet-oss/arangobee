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
import com.github.arangobee.test.env.EnvironmentDependentTestResource;

/**
 * Created by lstolowski on 13.07.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class ArangobeeEnvTest extends AbstractArangobeeTest {

    @Before
    @Override
    public void before() throws ArangobeeConnectionException, ArangobeeLockException {
        super.before();
        when(dao.acquireProcessLock()).thenReturn(FAKE_LOCK);
    }

    @Test
    public void shouldRunChangesetWithEnvironment() throws Exception {
        // given
        setSpringEnvironment(new EnvironmentMock());
        runner.setChangeLogsScanPackage(EnvironmentDependentTestResource.class.getPackage().getName());
        when(dao.isNewChange(any(ChangeEntry.class))).thenReturn(true);

        // when
        runner.execute();

        // then
        assertEquals(1, count("Envtest1", "testuser"));
    }
}
