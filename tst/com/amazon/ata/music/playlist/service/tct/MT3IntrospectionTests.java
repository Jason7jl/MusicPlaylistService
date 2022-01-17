package com.amazon.ata.music.playlist.service.tct;

import com.amazon.ata.test.reflect.ClassQuery;
import com.amazon.ata.test.reflect.ConstructorQuery;
import com.amazon.ata.test.reflect.MethodQuery;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Tag("MT03")
public class MT3IntrospectionTests {
    private static final String BASE_PACKAGE = "com.amazon.ata.music.playlist.service.";
    private static final Logger log = LogManager.getLogger();
    private Class<?> component;
    private Class<?> module;

    // Would be more efficient to use @BeforeClass to find the classes once
    // Blocked on TODO: https://sim.amazon.com/issues/ATAENG-1910
    @BeforeEach
    public void beforeAll() {
        log.info("Searching project for a single Component interface...");
        component = findSingleDaggerClass("Component");

        // search for the Module class, filtering out classes like *Module_*Factory
        log.info("Searching project for a single Module class...");
        module = findSingleDaggerClass("Module");
    }

    @Test
    public void mt03_daggerClasses_correctlyAnnotated() {
        // GIVEN our dagger classes WHEN we inspect them
        // THEN they should be annotated with correct Dagger annotations
        assertNotNull(component.getAnnotation(Component.class),
            String.format("Expected Component interface [%s] to be annotated with @Component", component));
        assertNotNull(component.getAnnotation(Singleton.class),
            String.format("Expected Component interface [%s] to be annotated with @Singleton", component));
        assertNotNull(module.getAnnotation(Module.class),
            String.format("Expected Module class [%s] to be annotated with @Module", module));

        assertTrue(ArrayUtils.contains(component.getAnnotation(Component.class).modules(), module),
            String.format("Expected Component interface [%s]'s to register the [%s] module.",
                component.getSimpleName(),
                module.getSimpleName()));

        log.info("Searching for 2 DAO classes to test their annotations...");
        List<Class<?>> daoClasses = findNonFrameworkClasses("dynamodb", "Dao", Object.class)
            .collect(Collectors.toList());
        assertClassesAnnotatedWithInject(daoClasses, 2);

        log.info("Searching for 5 Activity classes to test their annotations...");
        List<Class<?>> activityClasses = findNonFrameworkClasses("activity", "Activity", RequestHandler.class)
            .collect(Collectors.toList());
        assertClassesAnnotatedWithInject(activityClasses, 5);
    }

    @Test
    public void mt03_componentClass_providesActivityClasses() {
        // GIVEN all the (non-TCT) Activity classes
        // WHEN we search the declared methods of the Component interface that return Activity types
        // THEN there should be one for each Activity class
        findNonFrameworkClasses("activity", "Activity", RequestHandler.class)
            .forEach(clazz -> MethodQuery.inType(component).withReturnType(clazz).findMethodOrFail());
    }

    @Test
    public void mt03_module_providesSingletonDynamoDbMapper() {
        // GIVEN a method in the  DynamoDBMapper return type
        // we could add a dependency in this package to get the class, but it scans pretty fast
        Class<?> dynamoDbMapper = ClassQuery.inExactPackage("com.amazonaws.services.dynamodbv2.datamodeling")
            .withExactSimpleName("DynamoDBMapper")
            .findClassOrFail();

        // WHEN we search the Module class for a method that returns DynamoDBMapper
        Method providerMethod =  MethodQuery.inType(module).withReturnType(dynamoDbMapper)
            .findMethodOrFail();

        // THEN it should exist and be annotated with @Provides and @Singleton
        assertNotNull(providerMethod.getAnnotation(Provides.class),
            String.format("Expected method [%s] in Module [%s] to be annotated with @Provides",
                providerMethod.getName(), module.getSimpleName()));
        assertNotNull(providerMethod.getAnnotation(Singleton.class),
            String.format("Expected method [%s] in Module [%s] to be annotated with @Singleton",
                providerMethod.getName(), module.getSimpleName()));
    }

    @Test
    public void mt03_appClass_deleted() {
        // GIVEN our project WHEN we search for an App class
        Set<Class<?>> classes = ClassQuery.inExactPackage(BASE_PACKAGE + "dependency")
            .withExactSimpleName("App")
            .findClasses();

        // THEN it should not be present
        assertTrue(classes.isEmpty(),
            String.format("Expected to not find an App class, but found these class(es) %s", classes));
    }

    private Class<?> findSingleDaggerClass(String nameContaining) {
        return findNonFrameworkClasses("dependency", nameContaining, Object.class)
            // reduce won't be called on a stream containing a single item
            .reduce((first, second) -> fail(String.format("Found multiple classes containing '%s' in the class name, " +
                "such as [%s, %s]", nameContaining, first.getSimpleName(), second.getSimpleName())))
            .orElseThrow(() -> new AssertionError(
                String.format("Expected a single class with class name containing '%s', but didn't find" +
                    " any matching classes.", nameContaining)));
    }

    /**
     * Constructs a ClassQuery with simpleNameContaining, while filtering out extra framework classes like
     * *Test, dagger generated classes, and the ExecuteTCTActivity class
     *
     * @param packageQualifier the ending package qualifier to add to BASE_PACKAGE
     * @param simpleNameContaining the string the class names must contain
     * @param subTypeOf the parent type class to search for within the package
     * @return Stream&lt;Class&lt;?&gt;&gt; of classes after filtering
     */
    private Stream<Class<?>> findNonFrameworkClasses(String packageQualifier,
                                                     String simpleNameContaining,
                                                     Class<?> subTypeOf) {
        log.info("Searching in package {}", BASE_PACKAGE + packageQualifier);
        return ClassQuery.inExactPackage(BASE_PACKAGE + packageQualifier)
            .withSimpleNameContaining(simpleNameContaining)
            .withSubTypeOf(subTypeOf)
            .findClasses().stream()
            // filter out unit test classes
            .filter(clazz -> !clazz.getSimpleName().contains("Test"))
            // filter out dagger generated classes
            .filter(clazz -> !clazz.getSimpleName().contains("Factory"))
            .filter(clazz -> !clazz.getSimpleName().contains("Dagger"))
            .filter(clazz -> !clazz.getSimpleName().contains("_"))
            // filter out TCT activity class
            .filter(clazz -> !clazz.getSimpleName().contains("ExecuteTctActivity"));
    }

    private void assertClassesAnnotatedWithInject(List<Class<?>> classes,
                                                  long expectedClassCount) {
        assertEquals(expectedClassCount, classes.size(), String.format("Expected [%s] class(es) to be annotated with " +
            "@Inject but found [%s] total. %n Found: %s", expectedClassCount, classes.size(), classes));

        classes.stream()
            .peek(clazz -> log.info("Validating class {} has a constructor annotated with @Inject", clazz))
            .map(ConstructorQuery::inClass)
            .map(ConstructorQuery::findConstructorOrFail)
            .forEach(constructor -> assertNotNull(constructor.getAnnotation(Inject.class),
                String.format("Expected constructor for class [%s] to be annotated with @Inject",
                    constructor.getDeclaringClass())));
    }
}
