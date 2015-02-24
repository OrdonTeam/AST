package gep

import org.junit.Test

import java.io.ByteArrayOutputStream
import java.io.PrintStream

import static org.fest.assertions.api.Assertions.assertThat

public class SwissInjectTest {


    @Test
    public void testInjectIntoExistingMethod() throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream()
        System.setOut(new PrintStream(stream))

        def object = new SwissInjectUsageClass()
        object.setContentView(1)

        String output = stream.toString()
        assertThat(output).contains("SwissKnifeInjected")
    }

    @Test
    public void testAddMethodAndInject() throws Exception {
            ByteArrayOutputStream stream = new ByteArrayOutputStream()
            System.setOut(new PrintStream(stream))

            def object = new SwissInjectUsageClassWithOutMethod()
            object.setContentView(1)

            String output = stream.toString()
            assertThat(output).contains("SwissKnifeInjected")
    }
}
