package helpers;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class IOTestHelper {
    private PrintStream oldOutput;
    private ByteArrayOutputStream newOutput;
    private InputStream oldInput;

    public void setUp() {
        System.out.flush();
        oldOutput = System.out;

        newOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(newOutput));
    }

    public void tearDown() {
        System.out.flush();
        System.setOut(oldOutput);
        System.setIn(oldInput);
    }

    private String getOutput() {
        System.out.flush();
        String out = newOutput.toString();
        out = out.replaceAll("\r", "");
        return out;
    }

    public void assertOutput(String testName) throws IOException {
        assertEquals(readResult(testName), getOutput());
    }

    public void setInput(String testName) throws IOException {
        oldInput = System.in;
        System.setIn(readResource(testName));
    }

    private String readResult(String testName) throws IOException {
        return readFile(testName + ".result");
    }

    private String readFile(String file) throws IOException {
        return IOUtils.toString(readResource(file));
    }

    private InputStream readResource(String file) {
        return this.getClass().getResourceAsStream("../" + file);
    }


}