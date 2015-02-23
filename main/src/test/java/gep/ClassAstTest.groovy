package gep

import org.junit.Test

import static org.fest.assertions.api.Assertions.assertThat

class ClassAstTest {
    @Test
    public void testShouldClassBeTransformed() throws Exception {
        def object = new ClassAstUsage()

        object.onCreate('bundle')

        assertThat(object.contentView).isEqualTo(6)
    }
    @Test
    public void testShouldAddMethod() throws Exception {
        def object = new ClassAstUsageCase2()

        object.onCreate('bundle case 2')

        assertThat(object.contentView).isEqualTo(7)
    }
}
