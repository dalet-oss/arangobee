package com.github.arangobee;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.arangobee.dao.ChangeEntryDao;
import com.github.arangobee.dao.ChangeEntryIndexDao;

/**
 * Created by lstolowski on 13.07.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class AnrangobeeEnvTest {
  private static final String CHANGELOG_COLLECTION_NAME = "dbchangelog";

//  @InjectMocks
//  private Arangobee runner = new Arangobee();

  @Mock
  private ChangeEntryDao dao;

  @Mock
  private ChangeEntryIndexDao indexDao;

//  private DB fakeDb;
//
//  private MongoDatabase fakeMongoDatabase;
//
//  @Before
//  public void init() throws Exception {
//    fakeDb = new Fongo("testServer").getDB("mongobeetest");
//    fakeMongoDatabase = new Fongo("testServer").getDatabase("mongobeetest");
//
//    when(dao.connectDb(any(MongoClientURI.class), anyString()))
//        .thenReturn(fakeMongoDatabase);
//    when(dao.getDb()).thenReturn(fakeDb);
//    when(dao.getMongoDatabase()).thenReturn(fakeMongoDatabase);
//    when(dao.acquireProcessLock()).thenReturn(true);
//    doCallRealMethod().when(dao).save(any(ChangeEntry.class));
//    doCallRealMethod().when(dao).setChangelogCollectionName(anyString());
//    doCallRealMethod().when(dao).setIndexDao(any(ChangeEntryIndexDao.class));
//    dao.setIndexDao(indexDao);
//    dao.setChangelogCollectionName(CHANGELOG_COLLECTION_NAME);
//
//    runner.setDbName("mongobeetest");
//    runner.setEnabled(true);
//  }  // TODO code duplication
//
//  @Test
//  public void shouldRunChangesetWithEnvironment() throws Exception {
//    // given
//    runner.setSpringEnvironment(new EnvironmentMock());
//    runner.setChangeLogsScanPackage(EnvironmentDependentTestResource.class.getPackage().getName());
//    when(dao.isNewChange(any(ChangeEntry.class))).thenReturn(true);
//
//    // when
//    runner.execute();
//
//    // then
//    long change1 = fakeMongoDatabase.getCollection(CHANGELOG_COLLECTION_NAME)
//        .count(new Document()
//            .append(ChangeEntry.KEY_CHANGEID, "Envtest1")
//            .append(ChangeEntry.KEY_AUTHOR, "testuser"));
//    assertEquals(1, change1);
//
//  }
//
//  @Test
//  public void shouldRunChangesetWithNullEnvironment() throws Exception {
//    // given
//    runner.setSpringEnvironment(null);
//    runner.setChangeLogsScanPackage(EnvironmentDependentTestResource.class.getPackage().getName());
//    when(dao.isNewChange(any(ChangeEntry.class))).thenReturn(true);
//
//    // when
//    runner.execute();
//
//    // then
//    long change1 = fakeMongoDatabase.getCollection(CHANGELOG_COLLECTION_NAME)
//        .count(new Document()
//            .append(ChangeEntry.KEY_CHANGEID, "Envtest1")
//            .append(ChangeEntry.KEY_AUTHOR, "testuser"));
//    assertEquals(1, change1);
//
//  }
//
//  @After
//  public void cleanUp() {
//    runner.setArangoTemplate(null);
//    runner.setJongo(null);
//    fakeDb.dropDatabase();
//  }

}
