package gep

import groovy.mock.interceptor.MockFor
import org.junit.Test
import swissknife.SwissKnife

import static org.fest.assertions.api.Assertions.assertThat

class AstIntegrationTest {

    @Test
    public void testIntegration() throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream()
        System.setOut(new PrintStream(stream))


        def object = new IntegrationAstUsage()

        object.onCreate('bundle case 2')


        String output = stream.toString()
        assertThat(object.contentView).isEqualTo(7)
        assertThat(output).contains("SwissKnifeInjected")

    }
}
