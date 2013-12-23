package arithmeticandalgebra.reverseandadd;

import helpers.IOTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReverseAndAddTest {
    private final IOTestHelper ioTestHelper = new IOTestHelper(ReverseAndAddTest.class.getPackage());

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

        ReverseAndAdd reverseAndAdd = new ReverseAndAdd();

        // when
        reverseAndAdd.run();

        // then
        ioTestHelper.assertOutput(testName);
    }

}
