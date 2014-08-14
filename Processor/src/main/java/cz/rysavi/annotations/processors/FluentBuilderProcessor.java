package cz.rysavi.annotations.processors;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.tools.FileObject;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import cz.rysavi.annotations.FluentBuilder;
import cz.rysavi.annotations.processors.fluentbuilder.Data;
import cz.rysavi.annotations.processors.fluentbuilder.Processor;

@SupportedAnnotationTypes("cz.rysavi.annotations.FluentBuilder.Configuration")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class FluentBuilderProcessor extends AbstractProcessor {
	private final STGroup builderClassTemplateGroup;

	public FluentBuilderProcessor() {
		this.builderClassTemplateGroup = new STGroupFile("templates/builder.stg");
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for (TypeElement element : ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(FluentBuilder.Configuration.class))) {
			Processor processor = new Processor(element);

			if (processor.isEnabled()) {
				Data data = new Data();

				processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, String.format("Processing class %s.", processor.getFullClassName()));
				processor.process(data);

				ST builderClassTemplate = builderClassTemplateGroup.getInstanceOf("builderClass");
				builderClassTemplate.add("data", data);

				PrintWriter writer = null;
				try {
					FileObject sourceFile = processingEnv.getFiler().createSourceFile(processor.getBuilderFullClassName());
					writer = new PrintWriter(sourceFile.openWriter());
					writer.print(builderClassTemplate.render());
				} catch (IOException e) {
					throw new RuntimeException(e);
				} finally {
					if (writer != null) {
						writer.close();
					}
				}
				// System.out.println(builderClassTemplate.render());
			} else {
				processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, String.format("Ignoring class %s (exclude() == true).", processor.getFullClassName()));
			}
		}

		return false;
	}
}
