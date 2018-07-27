package com.github.arangobee;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.github.arangobee.changeset.ChangeEntry;
import com.github.arangobee.dao.ChangeEntryDao;
import com.github.arangobee.dao.ChangeEntryIndexDao;
import com.github.arangobee.resources.EnvironmentMock;
import com.github.arangobee.test.changelogs.EnvironmentDependentTestResource;

/**
 * Created by lstolowski on 13.07.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class ArangobeeEnvTest {
	private static final String DB_NAME = "arangobeetest";

	private static final String CHANGELOG_COLLECTION_NAME = "dbchangelog";

	//  @InjectMocks
	private Arangobee runner;

	@Mock
	private ChangeEntryDao dao;

	@Mock
	private ChangeEntryIndexDao indexDao;
	
	@Mock
	private ApplicationContext applicationContext;

	private ArangoDatabase db;

	//  private DB fakeDb;
	//
	//  private MongoDatabase fakeMongoDatabase;

	@Before
	public void init() throws Exception {
//		fakeDb = new Fongo("testServer").getDB("mongobeetest");
//		fakeMongoDatabase = new Fongo("testServer").getDatabase("mongobeetest");
		ArangoDB arangoDB = new ArangoDB.Builder().host("localhost", 8529).user("root").password("password").build();
		if(!arangoDB.db(DB_NAME).exists())
			arangoDB.createDatabase(DB_NAME);
		db = arangoDB.db(DB_NAME);
		runner = new Arangobee(db, applicationContext.getAutowireCapableBeanFactory());

//		when(dao.connectDb(any(MongoClientURI.class), anyString()))
//		.thenReturn(fakeMongoDatabase);
//		when(dao.getDb()).thenReturn(fakeDb);
//		when(dao.getMongoDatabase()).thenReturn(fakeMongoDatabase);
//		when(dao.acquireProcessLock()).thenReturn(true);
		doCallRealMethod().when(dao).save(any(ChangeEntry.class));
		doCallRealMethod().when(dao).setChangelogCollectionName(anyString());
		doCallRealMethod().when(dao).setIndexDao(any(ChangeEntryIndexDao.class));
		dao.setIndexDao(indexDao);
		dao.setChangelogCollectionName(CHANGELOG_COLLECTION_NAME);

//		runner.setDbName("mongobeetest");
		runner.setEnabled(true);
	}  // TODO code duplication

	@Test
	public void shouldRunChangesetWithEnvironment() throws Exception {
		// given
		runner.setSpringEnvironment(new EnvironmentMock());
		runner.setChangeLogsScanPackage(EnvironmentDependentTestResource.class.getPackage().getName());
		when(dao.isNewChange(any(ChangeEntry.class))).thenReturn(true);

		// when
		runner.execute();

		// then
		// TODO
//		long change1 = db.collection(CHANGELOG_COLLECTION_NAME).docu
//				.count(new Document()
//						.append(ChangeEntry.KEY_CHANGEID, "Envtest1")
//						.append(ChangeEntry.KEY_AUTHOR, "testuser"));
//		assertEquals(1, change1);
	}

	@Test
	public void shouldRunChangesetWithNullEnvironment() throws Exception {
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

	}

	//  @After
	//  public void cleanUp() {
	//    runner.setArangoTemplate(null);
	//    runner.setJongo(null);
	//    fakeDb.dropDatabase();
	//  }

}
