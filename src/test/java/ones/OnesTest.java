package ones;

import helpers.IOTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OnesTest {
    private final IOTestHelper ioTestHelper = new IOTestHelper();

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
        final String testName = "ones/ones-test1";
        ioTestHelper.setInput(testName);

        Ones ones = new Ones();

        // when
        ones.run();

        // then
        ioTestHelper.assertOutput(testName);
    }

}
