package shellsort;

import helpers.IOTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ShellSortTest {
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
        final String testName = "shellsort/shellsort-test1";
        ioTestHelper.setInput(testName);

        ShellSort shellSort = new ShellSort();

        // when
        shellSort.run();

        // then
        ioTestHelper.assertOutput(testName);
    }

}
