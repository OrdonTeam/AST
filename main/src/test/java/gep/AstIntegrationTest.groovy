package gep

import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import swissknife.SwissKnife

import static org.mockito.Matchers.anyObject

@PrepareForTest(SwissKnife.class)
@RunWith(PowerMockRunner.class)
class AstIntegrationTest {

    @Ignore
    @Test
    public void testIntegration() throws Exception {
        PowerMockito.mockStatic(SwissKnife.class)
        PowerMockito.doNothing().when(SwissKnife.class,"inject",anyObject())

        def object = new IntegrationAstUsage()

        object.onCreate('bundle case 2')
        PowerMockito.verifyStatic(Mockito.times(3))
        SwissKnife.inject(anyObject()); // 2
    }

    @Test
    public void testSTH() throws Exception {//TODO edit file path!
        def file = new File('..SwissInjectUsageClass.groovy')
        assert file.exists()

        GroovyClassLoader invoker = new GroovyClassLoader()
        def clazz = invoker.parseClass(file)
        def out = clazz.newInstance()
    }
}
