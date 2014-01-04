package runner;

import helpers.IOTestHelper;
import junit.framework.TestSuite;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;


@RunWith(JUnitParamsRunner.class)
public class TestRunner {
    private static final String MAIN_METHOD_NAME = "main";
    private final TestClassesProvider provider;

    public TestRunner() {
        provider = new TestClassesProvider();
    }

    @Test
    @Parameters
    public void runTest(String testName) throws Throwable {
        final IOTestHelper helper = new IOTestHelper();

        try {
            helper.setUp();

            // given
            helper.setInput(testName);
            Method method = classUnderTest(testName).getMethod(MAIN_METHOD_NAME, String[].class);

            // when
            method.invoke(null, new Object[] {null} );

            // then
            helper.assertOutput(testName);
        } finally {
            helper.tearDown();
        }
    }

    private List<String> parametersForRunTest() {
        List<Class<?>> classes = provider.provideClasses();

        List<String> testCases = new ArrayList<String>();
        for(Class<?> clazz : classes) {
            testCases.addAll(getAllTestCases(clazz));
        }

        return testCases;
    }

    private Collection<String> getAllTestCases(Class<?> clazz) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(clazz.getPackage().getName()))
                .setScanners(new ResourcesScanner())
                .filterInputsBy(new FilterBuilder().includePackage(clazz)));

        return reflections.getResources(Pattern.compile("test[^.]*"));
    }

    private Class<?> classUnderTest(String testName) throws ClassNotFoundException {
        String path = Paths.get(testName).getParent().toString();
        String packageName = path.replaceAll(File.separator, ".");
        return Class.forName(packageName + ".Main");
    }


}
