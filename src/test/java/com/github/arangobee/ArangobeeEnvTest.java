package com.github.arangobee;

import com.github.arangobee.changeset.ChangeEntry;
import com.github.arangobee.exception.ArangobeeConnectionException;
import com.github.arangobee.exception.ArangobeeLockException;
import com.github.arangobee.resources.EnvironmentMock;
import com.github.arangobee.test.env.EnvironmentDependentTestResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


/**
 * Created by lstolowski on 13.07.2017.
 */
@ExtendWith(MockitoExtension.class)
public class ArangobeeEnvTest extends AbstractArangobeeTest {

    @BeforeEach
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
