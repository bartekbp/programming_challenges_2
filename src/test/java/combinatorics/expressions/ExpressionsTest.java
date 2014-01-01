package combinatorics.expressions;
import helpers.IOTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExpressionsTest {
    private final IOTestHelper ioTestHelper = new IOTestHelper(ExpressionsTest.class.getPackage());

    @Before
    public void setUp() {
        ioTestHelper.setUp();
    }

    @After
    public void tearDown() {
        ioTestHelper.tearDown();
    }

    @Test
    public void test1() throws Exception {
        // given
        final String testName = "test1";
        ioTestHelper.setInput(testName);

        Expressions expressions = new Expressions();

        // when
        expressions.run();

        // then
        ioTestHelper.assertOutput(testName);
    }

    @Test
    public void test2() throws Exception {
        // given
        final String testName = "test2";
        ioTestHelper.setInput(testName);

        Expressions expressions = new Expressions();

        // when
        expressions.run();

        // then
        ioTestHelper.assertOutput(testName);
    }

}
