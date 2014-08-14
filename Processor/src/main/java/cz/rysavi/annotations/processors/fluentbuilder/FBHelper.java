package cz.rysavi.annotations.processors.fluentbuilder;

import java.util.Iterator;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.validation.constraints.NotNull;

import cz.rysavi.annotations.FluentBuilder;

public final class FBHelper {
	/**
	 * Private singleton constructor
	 */
	private FBHelper() {
	}

	public static String firstUpper(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}
		if (str.length() == 1) {
			return str.toUpperCase();
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public static String simpleClassName(String str) {
		return str;
		// TODO poresit generika a importy
//		int index = str.lastIndexOf(".");
//		if (index < 0) {
//			return str;
//		}
//		return str.substring(index + 1);
	}

	// TODO vcetne superclasses
	// TODO Configuration z superclasses k odpovidajicim fieldum
	public static List<ExecutableElement> getMethods(TypeElement classElement) {
		return ElementFilter.methodsIn(classElement.getEnclosedElements());
	}

	public static List<VariableElement> getFields(TypeElement classElement) {
		List<VariableElement> result = ElementFilter.fieldsIn(classElement.getEnclosedElements());

		for (Iterator<VariableElement> iterator = result.iterator(); iterator.hasNext();) {
			VariableElement item = iterator.next();
			if (item.getModifiers().contains(Modifier.STATIC)) {
				iterator.remove();
			}
		}

		TypeMirror superClass = classElement.getSuperclass();
		if (superClass.getKind() != TypeKind.NONE) {
			result.addAll(getFields((TypeElement) ((DeclaredType) superClass).asElement()));
		}

		return result;
	}

	public static ExecutableElement getSetter(TypeElement classElement, String setterName, String setterType) {
		for (ExecutableElement methodElement : getMethods(classElement)) {
			String methodName = methodElement.getSimpleName().toString();
			String methodType = methodElement.getParameters().size() > 0 ? methodElement.getParameters().get(0).asType().toString() : null;
			if (setterName.equals(methodName) && setterType.equals(methodType)) {
				return methodElement;
			}
		}
		return null;
	}

	public static ExecutableElement getGetter(TypeElement classElement, String getterName, String getterType) {
		for (ExecutableElement methodElement : getMethods(classElement)) {
			String methodName = methodElement.getSimpleName().toString();
			String methodType = methodElement.getReturnType().toString();
			if (getterName.equals(methodName) && getterType.equals(methodType)) {
				return methodElement;
			}
		}
		return null;
	}

	public static VariableElement getField(TypeElement classElement, String expectedFieldName) {
		for (VariableElement fieldElement : getFields(classElement)) {
			String fieldName = fieldElement.getSimpleName().toString();
			if (expectedFieldName.equals(fieldName)) {
				return fieldElement;
			}
		}
		return null;
	}

	public static String constructGetterName(@NotNull FluentBuilder.Configuration configuration, FluentBuilder.Definition definition, @NotNull VariableElement fieldElement) {
		if (definition != null && definition.getterName().length() > 0) {
			return definition.getterName();
		} else {
			String prefix = fieldElement.asType().getKind() == TypeKind.BOOLEAN ? configuration.isPrefix() : configuration.getterPrefix();
			return prefix + FBHelper.firstUpper(fieldElement.getSimpleName().toString());
		}
	}

	public static String constructSetterName(@NotNull FluentBuilder.Configuration configuration, FluentBuilder.Definition definition, @NotNull VariableElement fieldElement) {
		if (definition != null && definition.setterName().length() > 0) {
			return definition.setterName();
		} else {
			return configuration.setterPrefix() + FBHelper.firstUpper(fieldElement.getSimpleName().toString());
		}
	}

	public static String constructWithMethodName(@NotNull FluentBuilder.Configuration configuration, FluentBuilder.Definition definition, @NotNull VariableElement fieldElement) {
		if (definition != null && !definition.withMethodName().isEmpty()) {
			return definition.withMethodName();
		} else {
			return configuration.withPrefix() + FBHelper.firstUpper(fieldElement.getSimpleName().toString());
		}
	}

	public static String constructWithMethodName(@NotNull FluentBuilder.Configuration configuration, @NotNull FluentBuilder.Addition addition) {
		if (!addition.withMethodName().isEmpty()) {
			return addition.withMethodName();
		} else {
			return configuration.withPrefix() + FBHelper.firstUpper(addition.name());
		}
	}
}
