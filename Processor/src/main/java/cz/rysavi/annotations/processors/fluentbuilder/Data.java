package cz.rysavi.annotations.processors.fluentbuilder;

import java.util.ArrayList;
import java.util.List;

public class Data {
	private String packageName;
	private String builderClassName;
	private String instanceClassName;
	private List<Field> fields = new ArrayList<Field>();
	private List<Field> constructorFields = new ArrayList<Field>();
	private List<Combination> combinations = new ArrayList<Combination>();
	private List<Addition> additions = new ArrayList<Addition>();
	private List<Constructor> constructors = new ArrayList<Constructor>();

	public String getPackageName() {
		return packageName;
	}

	public Data setPackageName(String setPackageName) {
		this.packageName = setPackageName;
		return this;
	}

	public String getBuilderClassName() {
		return builderClassName;
	}

	public Data setBuilderClassName(String setBuilderClassName) {
		this.builderClassName = setBuilderClassName;
		return this;
	}

	public String getInstanceClassName() {
		return instanceClassName;
	}

	public Data setInstanceClassName(String setInstanceClassName) {
		this.instanceClassName = setInstanceClassName;
		return this;
	}

	public List<Field> getFields() {
		return fields;
	}

	public Data setFields(List<Field> setFields) {
		this.fields = setFields;
		return this;
	}

	public Data addField(Field field) {
		this.fields.add(field);
		return this;
	}

	public List<Field> getConstructorFields() {
		return constructorFields;
	}

	public Data setConstructorFields(List<Field> setConstructorFields) {
		this.constructorFields = setConstructorFields;
		return this;
	}

	public Data addConstructorField(Field constructorField) {
		this.constructorFields.add(constructorField);
		return this;
	}

	public List<Combination> getCombinations() {
		return combinations;
	}

	public Data setCombinations(List<Combination> setCombinations) {
		this.combinations = setCombinations;
		return this;
	}

	public Data addCombination(Combination combination) {
		this.combinations.add(combination);
		return this;
	}

	public List<Addition> getAdditions() {
		return additions;
	}

	public Data setAdditions(List<Addition> setAdditions) {
		this.additions = setAdditions;
		return this;
	}

	public Data addAddition(Addition addition) {
		this.additions.add(addition);
		return this;
	}

	public List<Constructor> getConstructors() {
		return constructors;
	}

	public Data setConstructors(List<Constructor> setConstructors) {
		this.constructors = setConstructors;
		return this;
	}

	public Data addConstructor(Constructor constructor) {
		this.constructors.add(constructor);
		return this;
	}

	public static class Parameter {
		private String type;
		private String name;

		public Parameter(String type, String name) {
			this.type = type;
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public Parameter setType(String setType) {
			this.type = setType;
			return this;
		}

		public String getName() {
			return name;
		}

		public Parameter setName(String setName) {
			this.name = setName;
			return this;
		}
	}

	public static class Field extends Parameter {
		private String withMethodName;
		private String setterMethodName;
		private String getterMethodName;
		private boolean constructorField;
		private boolean ignoreNulls;

		public Field(String type, String name) {
			super(type, name);
		}

		@Override
		public Field setType(String setType) {
			super.setType(setType);
			return this;
		}

		@Override
		public Field setName(String setName) {
			super.setName(setName);
			return this;
		}

		public String getWithMethodName() {
			return withMethodName;
		}

		public Field setWithMethodName(String setWithMethodName) {
			this.withMethodName = setWithMethodName;
			return this;
		}

		public String getSetterMethodName() {
			return setterMethodName;
		}

		public Field setSetterMethodName(String setSetterMethodName) {
			this.setterMethodName = setSetterMethodName;
			return this;
		}

		public String getGetterMethodName() {
			return getterMethodName;
		}

		public Field setGetterMethodName(String setGetterMethodName) {
			this.getterMethodName = setGetterMethodName;
			return this;
		}

		public boolean isConstructorField() {
			return constructorField;
		}

		public Field setConstructorField(boolean constructorField) {
			this.constructorField = constructorField;
			return this;
		}

		public boolean isIgnoreNulls() {
			return ignoreNulls;
		}

		public Field setIgnoreNulls(boolean ignoreNulls) {
			this.ignoreNulls = ignoreNulls;
			return this;
		}
	}

	public static class Combination {
		private String name;
		private String withMethodName;
		private List<Field> fields = new ArrayList<Field>();

		public Combination(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public Combination setName(String setName) {
			this.name = setName;
			return this;
		}

		public String getWithMethodName() {
			return withMethodName;
		}

		public Combination setWithMethodName(String setWithMethodName) {
			this.withMethodName = setWithMethodName;
			return this;
		}

		public List<Field> getFields() {
			return fields;
		}

		public Combination setFields(List<Field> setFields) {
			this.fields = setFields;
			return this;
		}

		public Combination addField(Field field) {
			this.fields.add(field);
			return this;
		}
	}

	public static class Addition {
		private String name;
		private String methodName;
		private String withMethodName;
		private List<Parameter> parameters = new ArrayList<Parameter>();

		public Addition(String name, String methodName) {
			this.name = name;
			this.methodName = methodName;
		}

		public String getName() {
			return name;
		}

		public Addition setName(String setName) {
			this.name = setName;
			return this;
		}

		public String getMethodName() {
			return methodName;
		}

		public Addition setMethodName(String setMethodName) {
			this.methodName = setMethodName;
			return this;
		}

		public String getWithMethodName() {
			return withMethodName;
		}

		public Addition setWithMethodName(String setWithMethodName) {
			this.withMethodName = setWithMethodName;
			return this;
		}

		public List<Parameter> getParameters() {
			return parameters;
		}

		public Addition setParameters(List<Parameter> setParameters) {
			this.parameters = setParameters;
			return this;
		}

		public Addition addParameter(Parameter parameter) {
			this.parameters.add(parameter);
			return this;
		}
	}

	public static class Constructor {
		private List<Parameter> parameters = new ArrayList<Parameter>();

		public List<Parameter> getParameters() {
			return parameters;
		}

		public Constructor setParameters(List<Parameter> setParameters) {
			this.parameters = setParameters;
			return this;
		}

		public Constructor addParameter(Parameter parameter) {
			this.parameters.add(parameter);
			return this;
		}
	}
}
