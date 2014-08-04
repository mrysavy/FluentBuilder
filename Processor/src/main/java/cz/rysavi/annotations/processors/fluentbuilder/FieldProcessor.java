package cz.rysavi.annotations.processors.fluentbuilder;

import cz.rysavi.annotations.FluentBuilder;
import japa.parser.ASTHelper;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.expr.*;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.ReturnStmt;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.PrimitiveType;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

// TODO dodelat kombinace (se jmeny fieldu - budou provolavat odpovidajici withXXX)
// TODO dodelat pouziti specifickeho konstruktoru
// TODO dodelat unit testy (namockovat writer a prohnat AST parserem a zkontrolovat)
// TODO dodelat klonovani builderu, klonovani puvodniho objektu, sestaveni builderu z puvodniho objektu
// TODO prenest komentare fieldu z orig tridy do builderu
public class FieldProcessor {
    private final VariableElement fieldElement;
    private final ClassProcessor classProcessor;

    private final FluentBuilder fieldFluentBuilder;

    private final String fieldName;
    private final String fieldType;

    public FieldProcessor(VariableElement fieldElement, ClassProcessor classProcessor) {
        FluentBuilder fieldFluentBuilder = fieldElement.getAnnotation(FluentBuilder.class);
        String fieldName = fieldElement.getSimpleName().toString();
        String fieldType = fieldElement.asType().toString();

        this.fieldElement = fieldElement;
        this.classProcessor = classProcessor;
        this.fieldFluentBuilder = fieldFluentBuilder;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    public boolean isEnabled() {
        if (fieldFluentBuilder != null) {
            if (fieldFluentBuilder.exclude()) {
                return false;
            }
            if (fieldFluentBuilder.includeForce()) {
                return true;
            }
        }

        return FBHelper.getSetter(classProcessor.getClassElement(), constructSetterName(), fieldType) != null;
    }

    public void process(
            Set<ImportDeclaration> importDeclarations,
            List<FieldDeclaration> fieldDeclarations,
            List<MethodDeclaration> withMethodDeclarations,
            List<Expression> buildMethodBodyExpressions
    ) {
        processImports(importDeclarations);
        processField(fieldDeclarations);
        processWithMethodField(withMethodDeclarations);
        processBuildMethodBody(buildMethodBodyExpressions);
    }

    private void processImports(Set<ImportDeclaration> importDeclarations) {
        if (!(fieldElement.asType() instanceof PrimitiveType) && !fieldType.startsWith("java.lang.")) {
            ImportDeclaration importDeclaration = new ImportDeclaration(ASTHelper.createNameExpr(fieldType), false, false);
            importDeclarations.add(importDeclaration);
        }
    }

    private void processField(List<FieldDeclaration> fieldDeclarations) {
        String fieldTypeSimple = FBHelper.simpleClassName(fieldType);
        FieldDeclaration fieldDeclaration = ASTHelper.createFieldDeclaration(ModifierSet.PRIVATE, ASTHelper.createReferenceType(fieldTypeSimple, 0), fieldName);
        fieldDeclarations.add(fieldDeclaration);
    }

    private void processWithMethodField(List<MethodDeclaration> withMethodDeclarations) {
        String fieldTypeSimple = FBHelper.simpleClassName(fieldType);
        String withMethodName = constructWithMethodName();

        MethodDeclaration withMethodDeclaration = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.createReferenceType(classProcessor.getBuilderClassName(), 0), withMethodName);
        Parameter methodDeclarationParameter = ASTHelper.createParameter(ASTHelper.createReferenceType(fieldTypeSimple, 0), fieldName);
        ASTHelper.addParameter(withMethodDeclaration, methodDeclarationParameter);
        withMethodDeclarations.add(withMethodDeclaration);

        BlockStmt block = new BlockStmt();
        withMethodDeclaration.setBody(block);

        AssignExpr expr = new AssignExpr(new FieldAccessExpr(new ThisExpr(), fieldName), new NameExpr(fieldName), AssignExpr.Operator.assign);

        ASTHelper.addStmt(block, expr);
        ASTHelper.addStmt(block, new ReturnStmt(new ThisExpr()));
    }

    private void processBuildMethodBody(List<Expression> buildMethodBodyExpressions) {
        buildMethodBodyExpressions.add(
                new MethodCallExpr(new NameExpr("result"), constructSetterName(), Arrays.asList((Expression) new FieldAccessExpr(new ThisExpr(), fieldName)))
        );
    }

    private String constructSetterName() {
        return FBHelper.constructSetterName(classProcessor.getClassFluentBuilder(), fieldFluentBuilder, fieldName);
    }

    private String constructWithMethodName() {
        return FBHelper.constructWithMethodName(classProcessor.getClassFluentBuilder(), fieldFluentBuilder, fieldName);
    }
}
