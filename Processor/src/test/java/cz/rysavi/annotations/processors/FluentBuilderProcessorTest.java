package cz.rysavi.annotations.processors;

import static com.google.testing.compile.JavaFileObjects.forResource;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

import javax.tools.JavaFileObject;

import org.testng.annotations.Test;

import com.google.common.truth.Truth;

public class FluentBuilderProcessorTest {

	private static void fbTest(String className) {
		JavaFileObject base = forResource(className + ".java");
		JavaFileObject generated = forResource(className + "Builder.java");
		fbTest(base, generated);
	}
	
	private static void fbTest(JavaFileObject base, JavaFileObject generated) {
		Truth.ASSERT
			.about(javaSource())
			.that(base)
			.processedWith(new FluentBuilderProcessor())
			.compilesWithoutError()
			.and()
			.generatesSources(generated);
	}
	
	@Test
	public void test01() {
		fbTest("Test01");
	}
	
	@Test
	public void test02() {
		fbTest("Test02");
	}
}
