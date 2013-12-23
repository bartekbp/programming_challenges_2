package arithmeticandalgebra.archaeologistsdilemma;

import helpers.IOTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ArchaeologistsDilemmaTest {
    private final IOTestHelper ioTestHelper = new IOTestHelper(ArchaeologistsDilemmaTest.class.getPackage());

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

        ArchaeologistsDilemma archaeologistsDilemma = new ArchaeologistsDilemma();

        // when
        archaeologistsDilemma.run();

        // then
        ioTestHelper.assertOutput(testName);
    }

}
