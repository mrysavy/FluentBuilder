package cz.rysavi.annotations.processors;

import cz.rysavi.annotations.FluentBuilder;
import cz.rysavi.annotations.processors.fluentbuilder.ClassProcessor;
import japa.parser.ast.CompilationUnit;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@SupportedAnnotationTypes("cz.rysavi.annotations.FluentBuilder")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class FluentBuilderProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement element : ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(FluentBuilder.class))) {
            ClassProcessor classProcessor = new ClassProcessor(element);

            if (classProcessor.isEnabled()) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, String.format("Processing class %s.", classProcessor.getClassName()));
                CompilationUnit compilationUnit = new CompilationUnit();
                classProcessor.process(compilationUnit);

                PrintWriter writer = null;
                try {
                    FileObject sourceFile = processingEnv.getFiler().createSourceFile(classProcessor.getBuilderClassName());
                    writer = new PrintWriter(sourceFile.openWriter());
                    writer.print(compilationUnit.toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (writer != null) {
                        writer.close();
                    }
                }
//                System.out.println(compilationUnit.toString());
            } else {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, String.format("Ignoring class %s (exclude() == true).", classProcessor.getClassName()));
            }
        }

        return true;
    }
}
