package runner;

import helpers.IOTestHelper;
import helpers.ReflectionHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileDrivenTestMapper {
    private final ReflectionHelper reflectionHelper = new ReflectionHelper();

    public List<FileDrivenTestHolder> map(List<Class<?>> classes) {
        List<FileDrivenTestHolder> testCases = new ArrayList<FileDrivenTestHolder>();

        for(Class<?> clazz : classes) {
            testCases.addAll(map(clazz));
        }

        return testCases;
    }

    public List<FileDrivenTestHolder> map(Class<?> clazz) {
        Collection<String> fileNames = reflectionHelper.listFileNamesFromPackage(clazz.getPackage().getName(), "test[^.]*");

        List<FileDrivenTestHolder> testCases = new ArrayList<FileDrivenTestHolder>();
        for(String fileName : fileNames) {
            Runnable testRunnable = ReflectionBasedRunnable.silentFromMainMethod(clazz);
            IOTestHelper ioTestHelper = new IOTestHelper();
            testCases.add(new FileDrivenTestHolder(fileName, testRunnable, ioTestHelper));
        }

        return testCases;
    }


}
