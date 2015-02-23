package ast

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

import static org.codehaus.groovy.ast.ClassHelper.make
import static org.codehaus.groovy.ast.tools.GeneralUtils.hasDeclaredMethod
import static org.codehaus.groovy.ast.tools.GeneralUtils.param
import static org.codehaus.groovy.ast.tools.GeneralUtils.params

@CompileStatic
@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
public class InjectContentViewTransformation extends AbstractASTTransformation {

    static final Class MY_CLASS = InjectContentView.class;
    static final ClassNode MY_TYPE = make(MY_CLASS);

    public void visit(ASTNode[] nodes, SourceUnit source) {
        init(nodes, source);
        AnnotatedNode parent = (AnnotatedNode) nodes[1];
        AnnotationNode anno = (AnnotationNode) nodes[0];
        if (!MY_TYPE.equals(anno.getClassNode())) return;

        if (parent instanceof ClassNode) {
            ClassNode cNode = (ClassNode) parent;
            int layout = getMemberIntValue(anno, 'value');
            applyLayout(cNode, layout)
        }
    }

    private static void applyLayout(ClassNode cNode, int layout) {
        if (hasDeclaredMethod(cNode, 'onCreate', 1)) {
            extendExistingMethod(cNode, layout)
        } else {
            addOnCreateMethod(cNode, layout)
        }
    }

    @CompileDynamic
    private static void extendExistingMethod(ClassNode cNode, int layout) {
        cNode.getMethods('onCreate').each { MethodNode methodNode ->
            Statement setContentView = statementSetContentView(layout)
            methodNode.getCode().getStatements().add(1, setContentView)
        }
    }

    @CompileDynamic
    private static void addOnCreateMethod(ClassNode classNode, int layout) {
        final BlockStatement body = new BlockStatement();
        body.addStatement(superOnCreate())
        body.addStatement(statementSetContentView(layout))
        classNode.addMethod(new MethodNode('onCreate', ACC_PUBLIC, ClassHelper.VOID_TYPE,
                params(param(make(Object), 'savedInstanceState')),
                ClassNode.EMPTY_ARRAY,
                body))
    }

    private static Statement superOnCreate() {
        return new ExpressionStatement(
                new MethodCallExpression(
                        new VariableExpression("super"),
                        new ConstantExpression("onCreate"),
                        new ArgumentListExpression(
                                new ConstantExpression('savedInstanceState')
                        )
                )
        )
    }

    private static Statement statementSetContentView(Integer layout) {
        return new ExpressionStatement(
                new MethodCallExpression(
                        new VariableExpression("this"),
                        new ConstantExpression("setContentView"),
                        new ArgumentListExpression(
                                new ConstantExpression(layout)
                        )
                )
        )
    }

}