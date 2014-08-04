package cz.rysavi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface FluentBuilder {
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.SOURCE)
	public static @interface Configuration {
		public boolean enabled() default true;

		public String builderSuffix() default "Builder";

		public String buildMethod() default "build";

		public String copyMethod() default "copy";

		public String getterPrefix() default "get";

		public String setterPrefix() default "set";

		public String withPrefix() default "with";

		public Combination[] combinations() default {};

		public Addition[] additions() default {};
	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.SOURCE)
	public static @interface Definition {
		public boolean excluded() default false;

		public String getterName() default "";

		public String setterName() default "";

		public String withMethodName() default "";
	}

	@Target(ElementType.ANNOTATION_TYPE)
	@Retention(RetentionPolicy.SOURCE)
	public static @interface Combination {
		public String name();

		public String[] fields();
	}

	@Target(ElementType.ANNOTATION_TYPE)
	@Retention(RetentionPolicy.SOURCE)
	public static @interface Addition {
		public String name();

		public String withMethodName() default "";

		public String method();

		public Parameter[] parameters() default {};

		@Target(ElementType.ANNOTATION_TYPE)
		@Retention(RetentionPolicy.SOURCE)
		public static @interface Parameter {
			public String name();

			public String type();
		}
	}
}
