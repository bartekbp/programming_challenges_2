package polynomialcoefficients;

import helpers.IOTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import polynomialcoefficients.PolynomialCoefficients;


public class PolynomialCoefficientsTest {
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
        final String testName = "polynomialcoefficients/polynomialCoefficients-test1";
        ioTestHelper.setInput(testName);

        PolynomialCoefficients polynomialCoefficients = new PolynomialCoefficients();

        // when
        polynomialCoefficients.run();

        // then
        ioTestHelper.assertOutput(testName);
    }

}
