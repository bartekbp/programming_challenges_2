package multiplicationgame;

import helpers.IOTestHelper;
import multiplicationgame.MultiplicationGame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class MultiplicationGameTest {
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
        final String testName = "multiplicationgame/multiplicationGame-test1";
        ioTestHelper.setInput(testName);

        MultiplicationGame multiplicationGame = new MultiplicationGame();

        // when
        multiplicationGame.run();

        // then
        ioTestHelper.assertOutput(testName);
    }

}
