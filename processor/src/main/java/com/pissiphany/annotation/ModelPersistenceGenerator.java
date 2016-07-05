package com.pissiphany.annotation;

import com.pissiphany.model.persistence.ModelPersistenceService;
import com.squareup.javapoet.*;

import javax.annotation.Generated;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by kierse on 2016-07-02.
 *
 * How to debug annotation processor:
 * https://blog.xmartlabs.com/2016/03/28/Debugging-an-Annotator-Processor-in-your-project/
 *
 * TL;DR
 *
 * 1. run the following on command line
 *
 *   ./gradlew --no-daemon -Dorg.gradle.debug=true :app:clean :app:javaCompile
 *
 * 2. run debug "Debug annotation processor" configuration
 */
@SupportedAnnotationTypes("com.pissiphany.annotation.ModelPersistence")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ModelPersistenceGenerator extends AbstractProcessor {
    private static final String CANONICAL_INT = "java.lang.Integer";
    private static final String CANONICAL_LONG = "java.lang.Long";
    private static final String CANONICAL_BIG_DECIMAL = "java.math.BigDecimal";
    private static final String CANONICAL_DATE = "java.util.Date";
    private static final String CANONICAL_STRING = "java.lang.String";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(ModelPersistence.class)) {
            if (element.getKind() != ElementKind.CLASS && element.getKind() != ElementKind.INTERFACE) {
                throw new AssertionError("annotation can only be used with a class or interface");
            }

            List<ExecutableElement> methodElements = new ArrayList<>();
            for (Element enclosedElement : element.getEnclosedElements()) {
                if (enclosedElement.getKind() != ElementKind.METHOD) continue;

                Annotation annotation = enclosedElement.getAnnotation(ModelPersistence.Column.class);
                if (annotation == null) continue;
                ModelPersistence.Column columnAnnotation = (ModelPersistence.Column) annotation;

                if (columnAnnotation.value().isEmpty()) {
                    throw new IllegalArgumentException("must specify a non-empty column value");
                }

                ExecutableElement methodElement = (ExecutableElement) enclosedElement;
                if (methodElement.getParameters().size() != 1) {
                    throw new IllegalArgumentException("Column annotation only applicable for setter methods");
                }

                methodElements.add(methodElement);
            }

            TypeElement classElement = (TypeElement) element;

            MethodSpec fromCursor = buildFromCursorMethod(classElement, methodElements);
            TypeSpec modelPersistenceClass = buildModelPersistenceClass(classElement, fromCursor);
            JavaFile javaFile = buildFile(classElement, modelPersistenceClass);

            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
//            try {
//                javaFile.writeTo(System.out);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

        return true;
    }

    private MethodSpec buildFromCursorMethod(TypeElement classElement, List<ExecutableElement> methodElements) {
        TypeName classType = TypeName.get(classElement.asType());

        MethodSpec.Builder builder = MethodSpec.methodBuilder("fromCursor")
                         .addAnnotation(Override.class)
//                         .addAnnotation(NotNull.class)
                         .addModifiers(Modifier.PUBLIC)
                         .addParameter(ParameterizedTypeName.get(Map.class, String.class, String.class), "cursor")
                         .returns(classType);

        builder.addStatement("$T model = new $T()", classElement, classElement);
        for (ExecutableElement method : methodElements) {
            ModelPersistence.Column annotation = method.getAnnotation(ModelPersistence.Column.class);

            String value = annotation.value();
            Name methodName = method.getSimpleName();

            String statement = null;
            Object[] args = new Object[] {
                    methodName,
                    value
            };
            switch (method.getParameters().get(0).asType().toString()) {
                case CANONICAL_INT:
                    statement = "model.$L(Integer.valueOf(cursor.get($S)))";
                    break;
                case CANONICAL_LONG:
                    statement = "model.$L(Long.valueOf(cursor.get($S)))";
                    break;
                case CANONICAL_BIG_DECIMAL:
                    statement = "model.$L(new $T(cursor.get($S)))";
                    args = new Object[] {
                            methodName,
                            BigDecimal.class,
                            value
                    };
                    break;
                case CANONICAL_DATE:
                    builder.addStatement("$T date = null", Date.class)
                           .beginControlFlow("try")
                           .addStatement("date = new $T($S, $T.US).parse(cursor.get($S))", SimpleDateFormat.class, "SOME_FORMAT", Locale.class, value)
                           .nextControlFlow("catch ($T e)", ParseException.class)
                           .addStatement("// log error")
                           .endControlFlow();
                    statement = "model.$L(date)";
                    args = new Object[] { methodName };
                    break;
                case CANONICAL_STRING:
                    statement = "model.$L(cursor.get($S))";
                    break;

            }

            if (statement != null) builder.addStatement(statement, args);
        }
        builder.addStatement("return model");

        return builder.build();
    }

    private TypeSpec buildModelPersistenceClass(TypeElement classElement, MethodSpec fromCursor) {
        return TypeSpec.classBuilder(classElement.getSimpleName() + "ModelPersistenceService")
                       .addAnnotation(
                               AnnotationSpec.builder(Generated.class)
                                             .addMember("value", "$S", this.getClass().getCanonicalName())
                                             .build()
                       )
                       .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                       .addSuperinterface(ParameterizedTypeName.get(
                               ClassName.get(ModelPersistenceService.class),
                               TypeName.get(classElement.asType())
                       ))
                       .addMethod(fromCursor)
                       .build();
    }

    private JavaFile buildFile(TypeElement classElement, TypeSpec klass) {
        PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();
        return JavaFile.builder(packageElement.getQualifiedName().toString() + ".persistence", klass)
                       .build();
    }
}
