package arithmeticandalgebra.sternbrocotnumbersystem;

import helpers.IOTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SternBrocotNumberSystemTest {
    private final IOTestHelper ioTestHelper = new IOTestHelper(SternBrocotNumberSystemTest.class.getPackage());

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

        SternBrocotNumberSystem sternBrocotNumberSystem = new SternBrocotNumberSystem();

        // when
        sternBrocotNumberSystem.run();

        // then
        ioTestHelper.assertOutput(testName);
    }

}
