package cz.rysavi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface FluentBuilder {
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.SOURCE)
	public static @interface Configuration {
		boolean enabled() default true;

		String builderSuffix() default "Builder";

		String buildMethod() default "build";

		String copyMethod() default "copy";

		String getterPrefix() default "get";

		String isPrefix() default "is";

		String setterPrefix() default "set";

		String withPrefix() default "with";

		String[] constructorFields() default { };

		Combination[] combinations() default { };

		Addition[] additions() default { };

		Constructor[] constructors() default { };
	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.SOURCE)
	public static @interface Definition {
		boolean excluded() default false;

		boolean ignoreNulls() default false;

		String getterName() default "";

		String setterName() default "";

		String withMethodName() default "";
	}

	@Target(ElementType.ANNOTATION_TYPE)
	@Retention(RetentionPolicy.SOURCE)
	public static @interface Combination {
		String name();

		String[] fields();
	}

	@Target(ElementType.ANNOTATION_TYPE)
	@Retention(RetentionPolicy.SOURCE)
	public static @interface Addition {
		String name();

		String withMethodName() default "";

		String method();

		Parameter[] parameters() default { };

		@Target(ElementType.ANNOTATION_TYPE)
		@Retention(RetentionPolicy.SOURCE)
		public static @interface Parameter {
			String name();

			String type();
		}
	}

	@Target(ElementType.ANNOTATION_TYPE)
	@Retention(RetentionPolicy.SOURCE)
	public static @interface Constructor {
		String[] fields();
	}
}
