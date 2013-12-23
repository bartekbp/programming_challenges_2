package pairsumoniousnumbers;

import helpers.IOTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PairsumoniousNumbersTest {
    private final IOTestHelper ioTestHelper = new IOTestHelper(PairsumoniousNumbersTest.class.getPackage());

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

        PairsumoniousNumbers pairsumoniousNumbers = new PairsumoniousNumbers();

        // when
        pairsumoniousNumbers.run();

        // then
        ioTestHelper.assertOutput(testName);
    }

}
