package helpers;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.Collection;
import java.util.regex.Pattern;

public class ReflectionHelper {

    public Collection<String> listFileNamesFromPackage(String prefix, String regex) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(prefix))
                .setScanners(new ResourcesScanner())
                .filterInputsBy(new FilterBuilder().includePackage(prefix)));

        return reflections.getResources(Pattern.compile(regex));
    }
}
