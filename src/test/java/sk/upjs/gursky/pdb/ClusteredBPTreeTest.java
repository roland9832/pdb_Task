package sk.upjs.gursky.pdb;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sk.upjs.gursky.bplustree.entries.BPObjectIntDouble;

public class ClusteredBPTreeTest {

	//private static final File INDEX_FILE = new File("person.kl");
	private ClusteredBPTree bptree;
	
	@Before
	public void setUp() throws Exception {
		//bptree = ClusteredBPTree.createOneByOne();
		bptree = ClusteredBPTree.createByBulkLoading();
	}

	@After
	public void tearDown() throws Exception {
		bptree.close();
		ClusteredBPTree.INDEX_FILE.delete();
	}

	//mame 850 000 v b+ strome
	@Test
	public void testInsertAndRead() throws Exception {
		long time = System.currentTimeMillis();
		PersonEntry prev = null;
		int toPrint = 50;
		for (PersonEntry entry : bptree) {
			System.out.println(entry);
			if (prev != null) {
				assertTrue(prev.compareTo(entry)<=0);
				assertTrue(prev.getKey().compareTo(entry.getKey())<=0);
			}
			prev = entry;
			if(toPrint==0){
				break;
			}
			toPrint--;
		}
		System.out.println("time: "+time);
	}

	@Test
	public void test() throws Exception {
		//intervalovy dopyt
		long time = System.nanoTime();
		List<PersonEntry> result = bptree.intervalQuery(new PersonStringKey("a"), new PersonStringKey("b999999999"));
		time = System.nanoTime() - time;
		
		System.out.println("Search time: "+time/1_000_000.0+" ms");

		assertTrue(result.size() > 0);
		int toPrint = 20;
		for (PersonEntry entry:result){
			System.out.println(entry);
			if(toPrint==0) break;
			toPrint--;
		}
		/*TOTO VYPISALO
		* index created in: 6393.294599ms
			Search time: 69.4221 ms
			yfax0hkdnu a004c9innj 32 619*/
	}
}
