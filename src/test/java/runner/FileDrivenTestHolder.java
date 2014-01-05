package runner;

import helpers.IOTestHelper;
import junit.framework.TestCase;

public class FileDrivenTestHolder {
    public static final String DEFAULT_OUTPUT_SUFFIX = ".result";
    private final String inputTestFile;
    private final String outputTestFile;
    private final Runnable methodUnderTest;
    private final IOTestHelper helper;

    public FileDrivenTestHolder(String testFile, Runnable methodUnderTest, IOTestHelper helper) {
        this(testFile, testFile + DEFAULT_OUTPUT_SUFFIX, methodUnderTest, helper);
    }

    private FileDrivenTestHolder(String inputTestFile, String outputTestFile, Runnable methodUnderTest, IOTestHelper helper) {
        this.inputTestFile = inputTestFile;
        this.outputTestFile = outputTestFile;
        this.methodUnderTest = methodUnderTest;
        this.helper = helper;
    }

    public void runTest() {

        try {
            helper.setUp();

            // given
            helper.setInput(inputTestFile);

            // when
            methodUnderTest.run();

            // then
            helper.assertOutput(outputTestFile);
        } finally {
            helper.tearDown();
        }
    }

    @Override
    public String toString() {
        return inputTestFile;
    }
}
