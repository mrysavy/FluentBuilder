package test;

import javax.annotation.Generated;
import java.util.*;

@Generated("cz.rysavi.annotations.processors.FluentBuilderProcessor")
public class Test01Builder {
	private final List<Execution> executions = new ArrayList<Execution>();

	public Test01Builder() {
	}

	public Test01Builder(Test01Builder builder) {
	}

	public Test01Builder(Test01 instance) {
	}

	public Test01 build() {
		Test01 result = new Test01();
		for (Execution execution : executions) {
			execution.execute(result);
		}
		return result;
	}

	public static Test01 copy(Test01 instance) {
		Test01 result = new Test01();
		return result;
	}

	private static interface Execution {
		public void execute(Test01 instance);
	}

	private static class FieldValue<T> {
		private T value;

		public FieldValue(T value) {
			this.value = value;
		}
	}
}
