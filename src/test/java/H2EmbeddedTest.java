import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;
import com.people.PeopleDAO;
import com.people.PeopleInfo;

@ContextConfiguration(locations = {"classpath:test-db-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class H2EmbeddedTest {

	private static final Log LOGGER = LogFactory.getLog(H2EmbeddedTest.class);

	@Autowired
	private PeopleDAO peopleDao;

	@Test
	public void testContext() {
		assertNotNull(peopleDao);
	}

	public interface PeopleMaker {
		PeopleInfo makePerson(int num);
	}

	public interface UpsertOperation<T> {
		void doUpsert(List<T> data);
	}

	@Test
	public void testBatchUpsertNoTx() {
		doBatchUpsertTest("noTx", new UpsertOperation<PeopleInfo>() {
			public void doUpsert(List<PeopleInfo> data) {
				peopleDao.batchUpsert(data);
			}
		}, 100000, 5);
	}

	//    @Test
	//    public void testBatchUpsertWithTx() {
	//        doBatchUpsertTest("withTx", new UpsertOperation<PeopleInfo>() {
	//            @Override
	//            public void doUpsert(List<PeopleInfo> data) {
	//                peopleDAO.batchUpsertInTx(data);
	//            }
	//        }, 100000, 5);
	//    }

	private void doBatchUpsertTest(String tag, UpsertOperation<PeopleInfo> upsertOperation, final int numItems,
		final int repeatCnt) {

		for (int i = 0; i < repeatCnt; i++) {
			final String uniqTag = String.valueOf(System.nanoTime());

			List<PeopleInfo> ppl = getPeopleList(numItems, new PeopleMaker() {
				public PeopleInfo makePerson(int num) {
					PeopleInfo info = new PeopleInfo();
					info.setFirstName("firstname" + num + uniqTag);
					//info.setLastLogin(new Timestamp());
					info.setLastName("lastname" + num + uniqTag);
					info.setSsn(UUID.randomUUID().toString().substring(0, 11));
					return info;
				}
			});
			StopWatch sw = new StopWatch();
			sw.reset();
			sw.start();
			upsertOperation.doUpsert(ppl);
			sw.stop();
			LOGGER.info(String.format("UpsertTest[%s], iteration: [%d] -- Inserted [%d] people in: [%d] ms",
				tag, i + 1, numItems, sw.getTime()));
		}

	}

	private void printPppl(List<PeopleInfo> peopleWithLoginInfo) {
		for (PeopleInfo info : peopleWithLoginInfo) {
			LOGGER.info(info);
		}
	}

	private List<PeopleInfo> getPeopleList(int numPeople, PeopleMaker maker) {
		final List<PeopleInfo> ppl = Lists.newArrayList();
		for (int i = 0; i < numPeople; i++) {
			ppl.add(maker.makePerson(i));
		}
		return ppl;
	}
}