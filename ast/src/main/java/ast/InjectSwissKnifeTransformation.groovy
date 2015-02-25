package ast

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotatedNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.StaticMethodCallExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import swissknife.SwissKnife

import static org.codehaus.groovy.ast.ClassHelper.make
import static org.codehaus.groovy.ast.tools.GeneralUtils.hasDeclaredMethod
import static org.codehaus.groovy.ast.tools.GeneralUtils.param
import static org.codehaus.groovy.ast.tools.GeneralUtils.params

@CompileStatic
@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
class InjectSwissKnifeTransformation extends AbstractASTTransformation{

    static final Class MY_CLASS = InjectSwissKnife.class;
    static final ClassNode MY_TYPE = make(MY_CLASS);

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        init(nodes, source);

        AnnotationNode anno = (AnnotationNode) nodes[0];
        AnnotatedNode parent = (AnnotatedNode) nodes[1];
        if (!MY_TYPE.equals(anno.getClassNode())) return;

        if (parent instanceof ClassNode) {
            ClassNode cNode = (ClassNode) parent;
            injectSwissKnife(cNode)
        }
    }

    private void injectSwissKnife(ClassNode classNode) {
        if (hasDeclaredMethod(classNode, 'setContentView', 1)) {
            extendExistingMethod(classNode)
        } else {
            addSetContentViewMethod(classNode)
        }
    }

    @CompileDynamic
    private static void extendExistingMethod(ClassNode classNode) {
        classNode.getDeclaredMethods('setContentView').each { MethodNode methodNode ->
            Statement injectSwissKnife = statementInjectSwissKnife()
            methodNode.getCode().getStatements().add(1, injectSwissKnife)
        }
    }

    @CompileDynamic
    private static void addSetContentViewMethod(ClassNode classNode) {
        final BlockStatement body = new BlockStatement();
        body.addStatement(superSetContentView())
        body.addStatement(statementInjectSwissKnife())
        classNode.addMethod(new MethodNode('setContentView', ACC_PUBLIC, ClassHelper.VOID_TYPE,
                params(param(make(int), 'contentView')),
                ClassNode.EMPTY_ARRAY,
                body))
    }


    private static Statement superSetContentView() {
        return new ExpressionStatement(
                new MethodCallExpression(
                        new VariableExpression("super"),
                        new ConstantExpression("setContentView"),
                        new ArgumentListExpression(
                                new VariableExpression('contentView')
                        )
                )
        )
    }

    private static Statement statementInjectSwissKnife() {
        return new ExpressionStatement(
                new StaticMethodCallExpression(
                        new ClassNode(SwissKnife.class),
                        "inject",
                        new ArgumentListExpression(
                                new VariableExpression("this")
                        )
                )
        )
    }
}
