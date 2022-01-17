## Mastery Task 1: Finish What We Started

### Milestone 1: Create UML Class Diagrams

For this task, you will be creating class diagrams referenced in the
[DESIGN_DOCUMENT.md](../../DESIGN_DOCUMENT.md) in your project for the known classes in 
the service.

Create class diagrams of the following classes and how they
relate to each other:

* Activity classes
* DAO classes
* Exception classes
* DynamoDB model classes

#### Requirements
* Indicate inheritance relationships among classes, including
  inheriting/implementing superclasses/supertypes that are from
  outside of your project package (but for those superclasses/types,
  you don't need to include any of their methods or variables)
* The diagrams do **not** need to include the `App` class or any
  classes in the following packages:
  * `com.amazon.ata.music.playlist.service.lambda`
  * `com.amazon.ata.music.playlist.service.dependency`
  * `com.amazon.ata.music.playlist.service.converters`
* Your DynamoDB model classes must represent the **end-state**
  of your models (they start off incomplete). See the
  "Data Model" section of the
  [DESIGN_DOCUMENT.md](../../DESIGN_DOCUMENT.md) to find the
  relevant fields.
* The DynamoDB model classes in the diagram must indicate which
  fields will represent the partition and (if any) sort key.
  * Use the `@DynamoDBHashKey` and `@DynamoDBRangeKey` annotations 
    to mark the relevant fields
  * You do not need to provide any other annotations, but be sure to
    indicate the Java type for each member variable. Use
    this same syntax for all of your DynamoDB model classes in your
    class diagrams.
* With the DynamoDB model classes, show the relationship between
  the Plain Old Java Objects (POJOs) **both** as a member variable
  and as a line between the classes. (Yes, be redundant here, so it's
  obvious there is a `List` of contained objects...)

#### Recommendations/tips
* Here is the
  [PlantUML reference for class diagrams](http://wiki.plantuml.net/site/class-diagram)
* If your diagram is too large, you may split it into sub-diagrams
  using the
  [`newpage` keyword](http://wiki.plantuml.net/site/sequence-diagram#splitting_diagrams)
  in your PlantUML. This example is a sequence diagram, but you can do the
  same thing to split up class diagrams. Also, note that you can
  provide a title with the keyword to label each diagram if you like.
* IntelliJ can display PlantUML diagrams as you type if you install the
  [PlantUML integration plugin](https://plugins.jetbrains.com/plugin/7017-plantuml-integration).
  * **For Mac users:** If you get an error in the plug-in complaining about GraphViz, then run these on
    the command line, then close and reopen IntelliJ:
    * `brew install libtool`
    * `brew install graphviz`
* The Class Diagram analyzing code is a little finicky, so try to keep your diagrams
  simple. In particular:
  * Do not worry about aggregation vs composition (open diamonds vs closed diamonds)
    for "has-a"/"contains" relationships. You can use either `o--` or `*--` for those.
  * You should be able to represent relationships with either left- or right-facing
    arrows (`o--` or `--o', `..|>` or `<|..` ) to suit your diagram layout.
    You *will* need the correct *orientation* of the relationship (from one class/interface
    to the other).
  * You should be able to include any number of the appropriate dots (`.`) or
    dashes (`-`) in your lines (`o-` or `o--` or `o---`) to suit your diagram layout
  * Avoid adding labels to your relationships between types:

    |      |    |    |
    |-----:|:---|:---|
    | YES: | `ClassA o-- ClassB`          |   |
    |  NO: | `ClassA o-- "1:many" ClassB` | "1:many" label may cause tests to fail |

  * Include spaces between your relationship lines and the types in the relationship:

    |      |    |    |
    |-----:|:---|:---|
    | YES: | `ClassA o-- ClassB` |   |
    |  NO: | `ClassA o--ClassB`  | line is touching ClassB                 |
    |  NO: | `ClassAo--ClassB`   | even worse! line is touching both types |

  * Make your **implements** relationships (if any) use the dotted line notation,
    and the "closed arrowhead":

    |      |    |    |
    |-----:|:---|:---|
    | YES: | <code>ClassA ..&#124;> TypeB</code> |   |
    |  NO: | `ClassA ..> TypeB`                  | use closed arrowhead instead |
    |  NO: | <code>ClassA --&#124;> TypeB</code> | use dotted line instead      |
  * Make your **extends**/**inherits** relationships (if any) use the solid line
    notation, and make the "closed arrowhead":

    |      |    |    |
    |-----:|:---|:---|
    | YES: | <code>SubClass --&#124;> SuperClass</code> |   |
    |  NO: | `SubClass --> SuperClass`                  | use closed arrowhead instead |
    |  NO: | <code>SubClass ..&#124;> SuperClass</code> | use solid line instead      |
  * If your TCTs are failing on this milestone, be sure to read the build output
    carefully. In particular:
    * pay attention to the **name of the test that failed**. This will tell you which
      diagram has a problem.
    * pay attention to the full error message (just above the stack trace), which
      should indicate what the test thinks is wrong with your diagram.

Add your class diagram to `src/resources/mastery-task1-music-playlist-CD.puml`.

Next, take a look at the sequence diagrams of two of the API operations we'll be working 
on in milestone 2: GetPlaylist and CreatePlaylist. You will learn to create sequence diagrams
like these in a later unit. For now, look over the diagrams and, if you wish,
take a look at the UML syntax for creating such a diagram.

### Milestone 2: Complete GetPlaylistActivity's implementation, implement CreatePlaylistActivity

#### Complete GetPlaylist

We were able to successfully retrieve the saved playlist by making an HTTP request to our service's GetPlaylist API,
but we were missing some fields in the response due to our incomplete data model.

Update the `dynamodb.models.Playlist` class with the missing fields indicated in your design from milestone 1.
Be sure to also add the correct `DynamoDB` annotations for the key indicated in your design of the data models.
Refer to the "Data Model" and "Music Playlist Service API Implementation Notes"
sections of the [DESIGN_DOCUMENT.md](../../DESIGN_DOCUMENT.md) in your project for requirements.

You may want to update your `GetPlaylistActivityTest` tests accordingly. Decide if
any new tests are required, but there might not be any required.

Note: While it's not necessary to always include the `@DynamoDBAttribute` annotation with an `attributeName` parameter,
some teams always prefer to have it for readability purposes.
It explicitly states the relationship with the DynamoDB table and removes the implicit dependency on the getter/setter
method names, allowing the method name to change without affecting the `DynamoDB` interaction.

With the updated `Playlist` class, update the `ModelConverter` 's `toPlaylistModel` method to map the new fields from the
`Playlist` object to the `PlaylistModel` object. Then add or update unit tests as appropriate.
We've created the `ModelConverter` class to centralize this conversion logic; otherwise, each API that returns an output will have to duplicate it.

Note that your `Playlist` model stores tags as a `Set<String>` while the `PlaylistModel` class
seeks to store tags as an `ArrayList`. You can initialize an `ArrayList` with the Set of tags
as a parameter to perform this conversion. Additionally, because DynamoDB cannot store empty Lists, we must instead pass `null` for the value of tags if the List would be empty.

Why a separate `PlaylistModel` if we already have the `Playlist`
class corresponding to our DynamoDB items? `PlaylistModel` represents
our API Gateway's API definition and is what the clients will interact with,
which is why we must convert the information from our DynamoDB model to
the API defined class. We wouldn't want to directly expose our DynamoDB data model to our clients, especially with our implementation of storing the song list within the playlist itself. It also allows us to update our
internal classes independently without risking our customers being
affected! You can consider this a service-level abstraction instead of
class-level abstractions like interfaces and composition that we've seen
before.

Once you have updated the `Playlist` class and the `ModelConverter`, uncomment and run the tests inside `GetPlaylistActivityTest` to verify your code works correctly.

#### Implement CreatePlaylist

With the updated `Playlist` class, implement `CreatePlaylistActivity`'s `handleRequest` method based on the design document's implementation notes and add unit tests to cover your new code.
You will have to add a new `savePlaylist` method in the `PlaylistDao` class.
Refer to the "Data Model" and "Music Playlist Service API Implementation Notes"
sections of the [DESIGN_DOCUMENT.md](../../DESIGN_DOCUMENT.md) in your project for requirements.

Write unit test(s) for `PlaylistDao` and `CreatePlaylistActivity` as appropriate.

You can use the `com.amazon.ata.music.playlist.service.util.MusicPlaylistServiceUtils`
methods to both validate the requested name and generate a random playlist ID before
saving the new playlist. Note that the methods are `static`, so the call syntax is based 
on the class name, for example, `MusicPlaylistServiceUtils.generatePlaylistId()`.

Remember the callout in the [DESIGN_DOCUMENT.md](../../DESIGN_DOCUMENT.md) in your project to initialize the list of songs here with an empty list before storing it in DynamoDB.
If we do not, we lose the ability to add songs to it in the other APIs!

The playlist tags are also passed in as a `List<String>`, but we want to store it as a `Set<String>` to meet the criteria of not storing duplicate tags. Remember that we enforce that the client
either passes in a list of size at least 1, or `null`; it will not pass in an empty list in this case!

Upon completion, upload your code to your `CreatePlaylistActivty` Lambda. Try out some requests with different names, 
with and without our prohibited characters and with and without tags. Ensure the creation of your playlists in your dynamoDB table.
You should also upload and test your `GetPlaylistActivity` Lambda now that you have playlists.

### Completion Checklist

* You've finished GetPlaylist's and implemented CreatePlaylist's functionality
* You've completed the requested UML class diagrams
* Mastery Task 1 TCTs are passing in your pipeline
* Your Lambda functions successfully get and create playlists.
