package runner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectionBasedRunnable implements Runnable {
    private final Class<?> clazz;
    private final Method method;
    private final Object[] methodArgs;

    public static ReflectionBasedRunnable silentFromMainMethod(Class<?> clazz) {
        return silentFromMainMethod(clazz, new String[0]);
    }

    private ReflectionBasedRunnable(Class<?> clazz, Method method, Object... methodArgs) {
        this.clazz = clazz;
        this.method = method;
        this.methodArgs = methodArgs;
    }

    private ReflectionBasedRunnable(Method method, Object... methodArgs) {
        this(null, method, methodArgs);
    }

    private static ReflectionBasedRunnable silentFromMainMethod(Class<?> clazz, String... args) {
        return silentFromMethodName(clazz, "main", new Object[] {args});
    }

    private static ReflectionBasedRunnable silentFromMethodName(Class<?> clazz, String methodName, Object... methodArgs) {
        try {
            return fromMethodName(clazz, methodName, methodArgs);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static ReflectionBasedRunnable fromMethodName(Class<?> clazz, String methodName, Object... methodArgs) throws NoSuchMethodException {
        List<Class<?>> classesOfArgs = new ArrayList<Class<?>>(methodArgs.length);
        for(Object obj : methodArgs) {
            classesOfArgs.add(obj.getClass());
        }

        Method method = clazz.getMethod(methodName, classesOfArgs.toArray(new Class<?>[classesOfArgs.size()]));
        method.setAccessible(true);

        return new ReflectionBasedRunnable(clazz, method, methodArgs);
    }

    @Override
    public void run() {
        try {
            method.invoke(clazz, methodArgs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
