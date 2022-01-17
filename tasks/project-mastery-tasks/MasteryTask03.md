## Mastery Task 3: Implement the Dagger framework

### Milestone 1: Plan our Dagger

One of the action items in the design was to replace our initial
dependency injection implementation using the `App` class with the
Dagger framework.

Use the provided
[Dagger design template](../../src/resources/mastery-task-3-dagger-design-template.md)
to create your design. There are more specific instructions and hints to
get you started in the template itself.

Hint: You can find a class in IntelliJ by hitting Command + O
and typing the class name (e.g. `App`).

Update the template file so you can commit it along with the implementation in milestone 2.

### Milestone 2: Implementing our Plan

#### Implement `DaoModule` & `ServiceComponent`

Implement your design from milestone 1 by creating a Module class named
`DaoModule` and a Dagger Component class named `ServiceComponent`.
Remember that `ServiceComponent` must be annotated with `@Singleton`
and `@Component`, and we must pass in our module to the component
annotation for Dagger to discover it.

All Dagger classes should live in the
`com.amazon.ata.music.playlist.service.dependency` package.

#### Build

Note that the Dagger framework will *generate* code to implement dependency injection for us when we compile our project,
so we must annotate our classes, create our `ServiceComponent` interface, and create the `DaoModule` class first, then
click 'build' to compile our code and let Dagger generate new classes based on our
existing `@Component`, `@Module`, and `@Inject` annotated classes.

(Note we are using an IntelliJ plugin to make this work. The Dagger annotation processor is included in `build.gradle`
as the plugin `net.ltgt.apt-idea' version "0.15"` and the dependency `annotationProcessor"com.google.dagger:dagger-compiler:2.15"`.

After building, you can navigate to the `build` directory and find the generated Dagger classes under the 
`classes.java.main.com.amazon.ata.music.playlist.service.dependency` package.

When we build our code, and because we annotated the interface
with `@Component`, the Dagger framework has enough information to
generate a class that implements our `ServiceComponent` interface.
It can also fulfill all of the dependencies based on what classes
we declare as return types in our component interface.
The class that Dagger creates has `Dagger` in front of the
name to indicate it is created by the framework. Our code can
request an instance of this generated class from Dagger to
instantiate our dependency objects. This replaces our hand-written
`App` class!

#### Replace use of `App` (in each of the Activity `provider` classes)

After verifying that our Dagger classes are generated, let's take
a look at the `GetPlaylistActivtyProvider` class. `GetPlaylistActivtyProvider` is the
entry point to our Lambda and is what handles the request from API
Gateway.

For example, when we want to use the `GetPlaylistActivty` handler, we first
go through the `GetPlaylistActivityProvider` class which instantiates a 
`GetPlaylistAcivity` along with a `playlistDAO` and `DynamoDBMapper`

```java
    @Override
    public GetPlaylistResult handleRequest(final GetPlaylistRequest getPlaylistRequest, Context context) {
        return getApp().provideGetPlaylistActivity().handleRequest(getPlaylistRequest, context);
    }
```

Notice that `getApp()` implements the
singleton pattern to return an `App` instance once where we can get
our Activity objects.

```
    private App getApp() {
        if (app == null) {
            app = new App();
        }

        return app;
    }
```

Rename and update the `getApp` method to instead return the
generated `DaggerServiceComponent`, following this same
singleton pattern. You'll use the
`DaggerServiceComponent.create()` method instead of a
constructor.

Next, update the `GetPlaylistResult` methods to use your updated
method so that we remove the interaction with the `App`
class and replace it with the `DaggerServiceComponent`.

You will need to do this for each `Provider` class in the `lambda`
package!

#### Delete `App`!

After verifying that your service works and previous TCTs
pass, you can go ahead and delete the `App` class! We've
completely replaced our old hand-managed dependency class
with a much simpler and extendable implementation with Dagger!

You can verify your work by running the `MT3IntrospectionTests`
and by uploading your code to your Lambda functions and making sure
they work properly.

### Doneness Checklist

* You've implemented dependency injection with Dagger and removed the `App` class.
* `MT3IntrospectionTests` tests pass.
* Your Lambda functions still behave properly with the new code.
