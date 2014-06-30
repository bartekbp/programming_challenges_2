package helpers;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class IOTestHelper {
    private PrintStream oldOutput;
    private ByteArrayOutputStream newOutput;
    private InputStream oldInput;
    private boolean isClosed = false;


    public void setUp() {
        System.out.flush();
        oldOutput = System.out;

        newOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(newOutput));
    }

    public void tearDown() {
        if(!isClosed) {
            System.out.flush();
            System.setOut(oldOutput);
            System.setIn(oldInput);
        }

        isClosed = true;
    }

    private String getOutput() {
        System.out.flush();
        String out = newOutput.toString();
        out = out.replaceAll("\r", "");
        return out;
    }

    public void assertOutput(String fileName)  {
        assertEquals(readFile(fileName), getOutput());
    }

    public void setInput(String fileName) {
        oldInput = System.in;
        System.setIn(readResource(fileName));
    }

    private String readFile(String file) {
        try {
            return IOUtils.toString(readResource(file)).replaceAll("\r", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private InputStream readResource(String file) {
        return ClassLoader.getSystemClassLoader().getResourceAsStream(Paths.get(file).toString());
    }

}