package runner;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;


@RunWith(JUnitParamsRunner.class)
public class TestRunner {
    private final TestClassesProvider provider;
    private final FileDrivenTestMapper mapper;

    public TestRunner() {
        provider = new TestClassesProvider();
        mapper = new FileDrivenTestMapper();
    }

    @Test
    @Parameters
    public void runAllTests(FileDrivenTestHolder testHolder) {
        testHolder.runTest();
    }

    private List<FileDrivenTestHolder> parametersForRunAllTests() {
        return mapper.map(provider.allTestClasses());
    }

    @Test
    @Parameters
    public void runSingleTest(FileDrivenTestHolder testHolder) {
        testHolder.runTest();
    }

    private List<FileDrivenTestHolder> parametersForRunSingleTest() {
        return mapper.map(provider.singleTestClass());
    }

}
