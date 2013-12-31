package combinatorics.counting;
import helpers.IOTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CountingTest {
    private final IOTestHelper ioTestHelper = new IOTestHelper(CountingTest.class.getPackage());

    @Before
    public void setUp() {
        ioTestHelper.setUp();
    }

    @After
    public void tearDown() {
        ioTestHelper.tearDown();
    }

    @Test
    public void testRun() throws Exception {
        // given
        final String testName = "test1";
        ioTestHelper.setInput(testName);

        Counting counting = new Counting();

        // when
        counting.run();

        // then
        ioTestHelper.assertOutput(testName);
    }

}
