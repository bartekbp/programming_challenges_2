package arithmeticandalgebra.polynomialcoefficients;

import helpers.IOTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class PolynomialCoefficientsTest {
    private final IOTestHelper ioTestHelper = new IOTestHelper(PolynomialCoefficientsTest.class.getPackage());

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

        PolynomialCoefficients polynomialCoefficients = new PolynomialCoefficients();

        // when
        polynomialCoefficients.run();

        // then
        ioTestHelper.assertOutput(testName);
    }

}
