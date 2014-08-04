package cz.rysavi.annotations.processors.fluentbuilder;

import cz.rysavi.annotations.FluentBuilder;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import java.util.List;

public class FBHelper {
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
        int index = str.lastIndexOf(".");
        if (index < 0) {
            return str;
        }
        return str.substring(index + 1);
    }

    public static List<ExecutableElement> getMethods(TypeElement classElement) {
        return ElementFilter.methodsIn(classElement.getEnclosedElements());
    }

    public static List<VariableElement> getFields(TypeElement classElement) {
        return ElementFilter.fieldsIn(classElement.getEnclosedElements());
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

    public static VariableElement getField(TypeElement classElement, String expectedFieldName) {
        for (VariableElement fieldElement : getFields(classElement)) {
            String fieldName = fieldElement.getSimpleName().toString();
            if (expectedFieldName.equals(fieldName)) {
                return fieldElement;
            }
        }
        return null;
    }

    public static String constructSetterName(FluentBuilder classFluentBuilder, FluentBuilder fieldFluentBuilder, String fieldName) {
        if (fieldFluentBuilder != null && fieldFluentBuilder.setterName().length() > 0) {
            return fieldFluentBuilder.setterName();
        } else {
            return classFluentBuilder.setterPrefix() + FBHelper.firstUpper(fieldName);
        }
    }

    public static String constructWithMethodName(FluentBuilder classFluentBuilder, FluentBuilder fieldFluentBuilder, String fieldName) {
        if (fieldFluentBuilder != null && !fieldFluentBuilder.withMethodName().isEmpty()) {
            return fieldFluentBuilder.withMethodName();
        } else {
            return classFluentBuilder.withPrefix() + FBHelper.firstUpper(fieldName);
        }
    }
}
