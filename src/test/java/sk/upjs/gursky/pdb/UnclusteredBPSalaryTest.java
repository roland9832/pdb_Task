package sk.upjs.gursky.pdb;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class UnclusteredBPSalaryTest {
    private UnclusteredBPSalary bptree;

    @Before
    public void setUp() throws Exception {
        bptree = UnclusteredBPSalary.createByBulkLoading();
    }

    @After
    public void tearDown() throws Exception {
        bptree.close();
        UnclusteredBPSalary.INDEX_FILE.delete();
    }
    @Test
    public void test2() throws Exception {
        long time = System.nanoTime();
        List<PersonEntry> result = bptree.unclusteredIntervalQuery(new SalaryKey(500), new SalaryKey(1000));
        time = System.nanoTime() - time;

        System.out.println("Interval unclusetered: " + time/1_000_000.0 +" ms");
        for (int i = 0; i < 20; i++) {
            System.out.println(result.get(i));
        }
        System.out.println("Pocet zaznamov v result: " + result.size());

        for (PersonEntry e : result){
            assertTrue((500<=e.salary) && (e.salary<=1000));
        }
    }
}
