package test;

import javax.annotation.Generated;
import java.util.*;

@Generated("cz.rysavi.annotations.processors.FluentBuilderProcessor")
public class Test02Builder {
	private final List<Execution> executions = new ArrayList<Execution>();

	private FieldValue<java.lang.String> field01 = new FieldValue(null);
	private FieldValue<java.lang.String> field02 = null;

	public Test02Builder() {
	}

	public Test02Builder(Test02Builder builder) {
		this.field01 = builder.field01;
		this.field02 = builder.field02;
	}

	public Test02Builder(Test02 instance) {
		this.field01 = new FieldValue(instance.getField01());
		this.field02 = new FieldValue(instance.getField02());
	}

	public Test02Builder withField01(java.lang.String field01) {
		this.field01 = new FieldValue(field01);
		return this;
	}

	public Test02Builder withField02(java.lang.String field02) {
		this.field02 = new FieldValue(field02);
		return this;
	}

	public Test02 build() {
		Test02 result = new Test02();
		if(this.field01 != null) result.setField01(this.field01.value);
		if(this.field02 != null) result.setField02(this.field02.value);
		for (Execution execution : executions) {
			execution.execute(result);
		}
		return result;
	}

	public static Test02 copy(Test02 instance) {
		Test02 result = new Test02();
		result.setField01(instance.getField01());
		result.setField02(instance.getField02());
		return result;
	}

	private static interface Execution {
		public void execute(Test02 instance);
	}

	private static class FieldValue<T> {
		private T value;

		public FieldValue(T value) {
			this.value = value;
		}
	}
}
