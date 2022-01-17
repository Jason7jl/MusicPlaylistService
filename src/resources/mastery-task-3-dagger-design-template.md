## Mastery Task 3 Dagger Design Template

### Analyze `App.java`

Looking through `App.java` and your class diagram, identify:

1. Which classes should be returned from the Dagger component
1. Which classes have constructors that should be annotated with `@Inject`
1. Which objects will we have to provide in a Provider method in a Module class.
   **Recall**: *Java annotations are ways to provide additional information
   to code that can be used during compilation or runtime, such as `@Override`,
   `@Test`, or `@Mock`. The action of adding an annotation is called "annotating".*

When identifying the classes, think about:
1. Which classes only **have** dependencies on other classes, i.e. the
   "root" classes that we interact with at the top of our service.
1. Which classes only **are** dependencies of other classes, but have
   no dependencies of their own
1. Which classes both **are** dependencies of other classes and **have**
   dependencies on other classes

Remember that:
1. "Root" classes should be provided using Dagger's `Component` interface,
   and should have their constructors annotated with `@Inject`.
1. All of the root classes' dependencies should either:
   1. have constructors annotated with `@Inject`
   1. **or** be provided in Provider methods of Module classes that can
      be registered to Dagger's `Component` interface.
1. If there are relevant Singleton classes, Dagger's `Component` interface
   should also be annotated `@Singleton`

### Capture Your Analysis

List the class(es) that `App.java` provides that are **not** dependencies of other classes, that is, no other classes
 in the project depend on these classes

* 

List the class(es) that `App.java` provides that **are** dependencies of other classes

* 

List the class(es) that `App.java` creates that have constructors we must annotate with `@Inject`

* 

List the class(es) that `App.java` creates that we must provide in a Dagger module

* 

List the class(es) that `App.java` creates as Singletons.

* 

### Pseudocode Dagger classes

Fill in the below annotations and method declarations.
(We require that you name your component, `ServiceComponent`, and
your module, `DaoModule`, as indicated below. Use these names in
your implementation as well)

```
@______
@______(______ = {______.class})
public interface ServiceComponent {
    ______ provide______();

    ______ provide______();

    ______ provide______();

    ______ provide______();

    ______ provide______();
}
```

```
@______
public class DaoModule {

    @______
    @______
    public ______ provide______() {
        // Implementation in milestone 2
    }
}
```
