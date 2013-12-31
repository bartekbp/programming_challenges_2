package combinatorics.howmanypiecesofland;
import helpers.IOTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HowManyPiecesOfLandTest {
    private final IOTestHelper ioTestHelper = new IOTestHelper(HowManyPiecesOfLandTest.class.getPackage());

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

        HowManyPiecesOfLand howManyPiecesOfLand = new HowManyPiecesOfLand();

        // when
        howManyPiecesOfLand.run();

        // then
        ioTestHelper.assertOutput(testName);
    }

}
