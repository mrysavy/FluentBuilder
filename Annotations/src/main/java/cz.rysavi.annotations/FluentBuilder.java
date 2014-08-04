package cz.rysavi.annotations;

import java.lang.annotation.*;

@Inherited
@Documented
@Target({
        ElementType.TYPE,
        ElementType.FIELD
})
@Retention(RetentionPolicy.SOURCE)
public @interface FluentBuilder {
    public boolean exclude() default false; // TYPE | FIELD

    public boolean includeForce() default false; // FIELD

    public String setterName() default ""; // FIELD

    public String withMethodName() default ""; // FIELD

    public String builderSuffix() default "Builder"; // TYPE

    public String buildMethod() default "build"; // TYPE

    public String setterPrefix() default "set"; // TYPE

    public String withPrefix() default "with"; // TYPE

    public FBCombination[] combinations() default {};

    @Inherited
    @Documented
    @Target(ElementType.ANNOTATION_TYPE)
    @Retention(RetentionPolicy.SOURCE)
    public static @interface FBCombination {
        public String name();

        public String[] fields();
    }
}
