package combinatorics.howmanyfibs;
import helpers.IOTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HowManyFibsTest {
    private final IOTestHelper ioTestHelper = new IOTestHelper(HowManyFibsTest.class.getPackage());

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

        HowManyFibs howManyFibs = new HowManyFibs();

        // when
        howManyFibs.run();

        // then
        ioTestHelper.assertOutput(testName);
    }

}
