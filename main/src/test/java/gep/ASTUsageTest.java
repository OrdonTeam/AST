package gep;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.fest.assertions.api.Assertions.assertThat;

public class ASTUsageTest {

    @Test
    public void testGreet() throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(stream));

        new ASTUsage().greet();

        String output = stream.toString();
        assertThat(output).contains("Starting greet");
        assertThat(output).contains("Hello World");
        assertThat(output).contains("Ending greet");
    }
}