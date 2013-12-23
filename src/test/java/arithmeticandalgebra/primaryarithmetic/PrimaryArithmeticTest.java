package arithmeticandalgebra.primaryarithmetic;

import helpers.IOTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PrimaryArithmeticTest {
    private final IOTestHelper ioTestHelper = new IOTestHelper(PrimaryArithmeticTest.class.getPackage());

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

        PrimaryArithmetic primaryArithmetic = new PrimaryArithmetic();

        // when
        primaryArithmetic.run();

        // then
        ioTestHelper.assertOutput(testName);
    }

}
