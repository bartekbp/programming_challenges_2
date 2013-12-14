package football;

import helpers.IOTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FootballTest {
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
    public void test1() throws Exception {
        // given
        final String testName = "football/football-test1";
        ioTestHelper.setInput(testName);

        Football football = new Football();

        // when
        football.run();

        // then
        ioTestHelper.assertOutput(testName);
    }

    @Test
    public void test2() throws Exception {
        // given
        final String testName = "football/football-test2";
        ioTestHelper.setInput(testName);

        Football football = new Football();

        // when
        football.run();

        // then
        ioTestHelper.assertOutput(testName);
    }

    @Test
    public void test3() throws Exception {
        // given
        final String testName = "football/football-test3";
        ioTestHelper.setInput(testName);

        Football football = new Football();

        // when
        football.run();

        // then
        ioTestHelper.assertOutput(testName);
    }

}