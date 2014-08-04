package cz.rysavi.annotations.processors.fluentbuilder;

import cz.rysavi.annotations.FluentBuilder;
import cz.rysavi.annotations.FluentBuilder.FBCombination;
import japa.parser.ASTHelper;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.expr.*;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.ReturnStmt;

import javax.lang.model.element.VariableElement;
import java.util.Arrays;
import java.util.List;

public class CombinationProcessor {
    private final ClassProcessor classProcessor;

    private final FBCombination combination;

    private final String combinationName;
    private final String[] combinationFields;

    public CombinationProcessor(ClassProcessor classProcessor, FBCombination combination) {
        String combinationName = combination.name();
        String[] combinationFields = combination.fields();

        this.classProcessor = classProcessor;
        this.combination = combination;
        this.combinationName = combinationName;
        this.combinationFields = combinationFields;
    }

    public void process(List<MethodDeclaration> withMethodDeclarations) {
        String withMethodName = classProcessor.getClassFluentBuilder().withPrefix() + FBHelper.firstUpper(combinationName);

        MethodDeclaration withMethodDeclaration = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.createReferenceType(classProcessor.getBuilderClassName(), 0), withMethodName);
        withMethodDeclarations.add(withMethodDeclaration);

        BlockStmt withMethodbBlock = new BlockStmt();
        withMethodDeclaration.setBody(withMethodbBlock);

        Expression expression = new ThisExpr();
        for (String combinationField : combinationFields) {
            VariableElement fieldElement = FBHelper.getField(classProcessor.getClassElement(), combinationField);
            if (fieldElement == null) {
                // TODO
                continue;
            }
            String fieldName = fieldElement.getSimpleName().toString();
            String fieldType = fieldElement.asType().toString();
            String fieldTypeSimple = FBHelper.simpleClassName(fieldType);

            FluentBuilder fieldFluentBuilder = fieldElement.getAnnotation(FluentBuilder.class);
            String fieldWithMethodName = FBHelper.constructWithMethodName(classProcessor.getClassFluentBuilder(), fieldFluentBuilder, fieldName);

            Parameter methodDeclarationParameter = ASTHelper.createParameter(ASTHelper.createReferenceType(fieldTypeSimple, 0), fieldName);
            ASTHelper.addParameter(withMethodDeclaration, methodDeclarationParameter);

            expression = new MethodCallExpr(expression, fieldWithMethodName, Arrays.asList((Expression) new NameExpr(fieldName)));
        }

        ASTHelper.addStmt(withMethodbBlock, new ReturnStmt(expression));
    }
}
