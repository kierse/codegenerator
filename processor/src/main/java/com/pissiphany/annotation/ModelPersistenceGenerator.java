package com.pissiphany.annotation;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

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
@SupportedAnnotationTypes({
        "com.pissiphany.annotation.ModelPersistence",
        "com.pissiphany.annotation.ModelPersistence.Column"
})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ModelPersistenceGenerator extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return true;
    }
}
