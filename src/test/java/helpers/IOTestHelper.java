package helpers;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Paths;

import static org.junit.Assert.assertArrayEquals;

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
        String expected = readFile(fileName).trim();
        String actual = getOutput().trim();
        String[] expectedLines = expected.split("\n") ;
        String[] actualLines = actual.split("\n");
        assertArrayEquals(expectedLines, actualLines);
    }

    public void setInput(String fileName) {
        oldInput = System.in;
        try {
            System.setIn(new ByteArrayInputStream(readFile(fileName).getBytes("utf-8")));
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String readFile(String file) {
        try {
            return IOUtils.toString(readResource(file)).replaceAll("\r", "").replaceAll("(?m)^#.*$[\n]?", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private InputStream readResource(String file) {
        return ClassLoader.getSystemClassLoader().getResourceAsStream(Paths.get(file).toString());
    }

}