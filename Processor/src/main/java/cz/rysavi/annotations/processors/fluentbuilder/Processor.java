package cz.rysavi.annotations.processors.fluentbuilder;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import cz.rysavi.annotations.FluentBuilder;

public class Processor {
	private final TypeElement classElement;
	private final FluentBuilder.Configuration configuration;
	private final String packageName;
	private final String fullClassName;
	private final String simpleClassName;
	private final String builderFullClassName;
	private final String builderSimpleClassName;

	public Processor(TypeElement classElement) {
		FluentBuilder.Configuration configuration = classElement.getAnnotation(FluentBuilder.Configuration.class);
		String packageName = ((PackageElement) classElement.getEnclosingElement()).getQualifiedName().toString();
		String fullClassName = classElement.getQualifiedName().toString();
		String simpleClassName = classElement.getSimpleName().toString();
		String builderFullClassPath = fullClassName + configuration.builderSuffix();
		String builderSimpleClassName = simpleClassName + configuration.builderSuffix();

		this.classElement = classElement;
		this.configuration = configuration;
		this.packageName = packageName;
		this.fullClassName = fullClassName;
		this.simpleClassName = simpleClassName;
		this.builderFullClassName = builderFullClassPath;
		this.builderSimpleClassName = builderSimpleClassName;
	}

	public boolean isEnabled() {
		return configuration.enabled();
	}

	public String getFullClassName() {
		return fullClassName;
	}

	public String getBuilderFullClassName() {
		return builderFullClassName;
	}

	public void process(Data data) {
		processClass(data);
	}

	private void processClass(Data data) {
		if (!packageName.isEmpty()) {
			data.setPackageName(packageName);
		}

		data.setBuilderClassName(builderSimpleClassName);
		data.setInstanceClassName(simpleClassName);

		// Set<ImportDeclaration> importDeclarations = new TreeSet<ImportDeclaration>(new Comparator<ImportDeclaration>() {
		// @Override
		// public int compare(ImportDeclaration o1, ImportDeclaration o2) {
		// return o1.toString().compareTo(o2.toString());
		// }
		// });
		for (VariableElement field : FBHelper.getFields(classElement)) {
			processField(field, data);
		}

		for (FluentBuilder.Combination combination : configuration.combinations()) {
			processCombination(combination, data);
		}

		for (FluentBuilder.Addition addition : configuration.additions()) {
			processAddition(addition, data);
		}
	}

	private void processField(VariableElement fieldElement, Data data) {
		FluentBuilder.Definition definition = fieldElement.getAnnotation(FluentBuilder.Definition.class);

		if (definition != null && definition.excluded()) {
			return;
		}

		String fieldName = fieldElement.getSimpleName().toString();
		String fieldType = fieldElement.asType().toString();

		// if (fieldElement.asType().getKind() == TypeKind.DECLARED && !fieldType.startsWith("java.lang.")) {
		// ImportDeclaration importDeclaration = new ImportDeclaration(ASTHelper.createNameExpr(fieldType), false, false);
		// importDeclarations.add(importDeclaration);
		// }
		String fieldTypeSimple = FBHelper.simpleClassName(fieldType);
		data.addField(
			new Data.Field(fieldTypeSimple, fieldName)
				.setWithMethodName(constructWithMethodName(definition, fieldElement))
				.setSetterMethodName(constructSetterName(definition, fieldElement))
				.setGetterMethodName(constructGetterName(definition, fieldElement))
			);
	}

	private void processCombination(FluentBuilder.Combination combination, Data data) {
		String combinationName = combination.name();
		String[] combinationFields = combination.fields();

		String withMethodName = configuration.withPrefix() + FBHelper.firstUpper(combinationName);

		Data.Combination dataCombination = new Data.Combination(combinationName)
			.setWithMethodName(withMethodName);
		for (String combinationField : combinationFields) {
			VariableElement fieldElement = FBHelper.getField(classElement, combinationField);
			if (fieldElement == null) {
				// TODO
				continue;
			}
			String fieldName = fieldElement.getSimpleName().toString();
			String fieldType = fieldElement.asType().toString();
			String fieldTypeSimple = FBHelper.simpleClassName(fieldType);

			FluentBuilder.Definition definition = fieldElement.getAnnotation(FluentBuilder.Definition.class);
			String fieldWithMethodName = FBHelper.constructWithMethodName(configuration, definition, fieldElement);

			dataCombination.addField(
				new Data.Field(fieldTypeSimple, fieldName)
					.setWithMethodName(fieldWithMethodName)
				);
		}

		data.addCombination(dataCombination);
	}

	private void processAddition(FluentBuilder.Addition addition, Data data) {
		String withMethodName = FBHelper.constructWithMethodName(configuration, addition);
		FluentBuilder.Addition.Parameter[] parameters = addition.parameters();

		Data.Addition dataAddition = new Data.Addition(addition.name(), addition.method())
			.setWithMethodName(withMethodName);

		for (FluentBuilder.Addition.Parameter parameter : parameters) {
			String parameterName = parameter.name();
			String parameterType = parameter.type();
			dataAddition.addParameter(new Data.Parameter(parameterType, parameterName));
		}

		data.addAddition(dataAddition);
	}

	private String constructGetterName(FluentBuilder.Definition definition, VariableElement fieldElement) {
		return FBHelper.constructGetterName(configuration, definition, fieldElement);
	}

	private String constructSetterName(FluentBuilder.Definition definition, VariableElement fieldElement) {
		return FBHelper.constructSetterName(configuration, definition, fieldElement);
	}

	private String constructWithMethodName(FluentBuilder.Definition definition, VariableElement fieldElement) {
		return FBHelper.constructWithMethodName(configuration, definition, fieldElement);
	}
}
