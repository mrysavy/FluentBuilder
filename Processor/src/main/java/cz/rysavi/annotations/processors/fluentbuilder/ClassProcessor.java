package cz.rysavi.annotations.processors.fluentbuilder;

import cz.rysavi.annotations.FluentBuilder;
import cz.rysavi.annotations.FluentBuilder.FBCombination;
import cz.rysavi.annotations.processors.FluentBuilderProcessor;
import japa.parser.ASTHelper;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.*;
import japa.parser.ast.expr.*;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.ReturnStmt;
import japa.parser.ast.type.ClassOrInterfaceType;

import javax.annotation.Generated;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.*;

public class ClassProcessor {
    private final TypeElement classElement;

    private final FluentBuilder classFluentBuilder;

    private final String packageName;
    private final String className;
    private final String builderClassName;

    public ClassProcessor(TypeElement classElement) {
        FluentBuilder classFluentBuilder = classElement.getAnnotation(FluentBuilder.class);
        String packageName = classElement.getEnclosingElement().getSimpleName().toString();
        String className = classElement.getSimpleName().toString();
        String builderClassName = className + classFluentBuilder.builderSuffix();

        this.classElement = classElement;
        this.classFluentBuilder = classFluentBuilder;
        this.packageName = packageName;
        this.className = className;
        this.builderClassName = builderClassName;
    }

    public TypeElement getClassElement() {
        return classElement;
    }

    public FluentBuilder getClassFluentBuilder() {
        return classFluentBuilder;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public String getBuilderClassName() {
        return builderClassName;
    }

    public boolean isEnabled() {
        return !classFluentBuilder.exclude();
    }

    public void process(CompilationUnit compilationUnit) {
        if (!packageName.isEmpty()) {
            compilationUnit.setPackage(new PackageDeclaration(ASTHelper.createNameExpr(packageName)));
        }

        compilationUnit.setImports(new ArrayList<ImportDeclaration>(Arrays.asList(
                new ImportDeclaration(ASTHelper.createNameExpr(Generated.class.getName()), false, false),
                new ImportDeclaration(ASTHelper.createNameExpr("java.lang"), false, true)
        )));

        Set<ImportDeclaration> importDeclarations = new HashSet<ImportDeclaration>();
        List<FieldDeclaration> fieldDeclarations = new ArrayList<FieldDeclaration>();
        List<MethodDeclaration> withMethodDeclarations = new ArrayList<MethodDeclaration>();
        List<Expression> buildMethodBodyExpressions = new ArrayList<Expression>();

        for (VariableElement field : FBHelper.getFields(classElement)) {
            FieldProcessor fieldProcessor = new FieldProcessor(field, this);
            if (fieldProcessor.isEnabled()) {
                fieldProcessor.process(
                        importDeclarations,
                        fieldDeclarations,
                        withMethodDeclarations,
                        buildMethodBodyExpressions
                );
            }
        }

        for (FBCombination combination : classFluentBuilder.combinations()) {
            new CombinationProcessor(this, combination).process(withMethodDeclarations);
        }

        ClassOrInterfaceDeclaration builderClassDeclaration = new ClassOrInterfaceDeclaration(ModifierSet.PUBLIC, false, builderClassName);
        ASTHelper.addTypeDeclaration(compilationUnit, builderClassDeclaration);
        builderClassDeclaration.setAnnotations(
                Arrays.asList(
                        (AnnotationExpr) new SingleMemberAnnotationExpr(
                                ASTHelper.createNameExpr(Generated.class.getSimpleName()),
                                new StringLiteralExpr(FluentBuilderProcessor.class.getName())
                        )
                )
        );

        for (ImportDeclaration importDeclaration : importDeclarations) {
            compilationUnit.getImports().add(importDeclaration);
        }

        for (FieldDeclaration fieldDeclaration : fieldDeclarations) {
            ASTHelper.addMember(builderClassDeclaration, fieldDeclaration);
        }

        for (MethodDeclaration methodDeclaration : withMethodDeclarations) {
            ASTHelper.addMember(builderClassDeclaration, methodDeclaration);
        }

        MethodDeclaration buildMethodDeclaration = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.createReferenceType(className, 0), classFluentBuilder.buildMethod());
        BlockStmt buildMethodBlock = new BlockStmt();
        buildMethodDeclaration.setBody(buildMethodBlock);
        ASTHelper.addMember(builderClassDeclaration, buildMethodDeclaration);

        ASTHelper.addStmt(buildMethodBlock,
                new AssignExpr(
                        new VariableDeclarationExpr(new ClassOrInterfaceType(className), Arrays.asList(new VariableDeclarator(new VariableDeclaratorId("result")))),
                        new ObjectCreationExpr(null, new ClassOrInterfaceType(className), new ArrayList<Expression>(0)),
                        AssignExpr.Operator.assign)
        );

        for (Expression buildMethodBodyExpression : buildMethodBodyExpressions) {
            ASTHelper.addStmt(buildMethodBlock, buildMethodBodyExpression);
        }

        ASTHelper.addStmt(buildMethodBlock,
                new ReturnStmt(new NameExpr("result"))
        );
    }
}
