## Mastery Task 2: I'll Give You an Exception This Time

### Milestone 1: Designing an exception hierarchy

After a project check-in, your senior engineer pointed out that
before starting on the UpdatePlaylist API, we should first validate
if the request is somehow providing a different `customerId` value
that is different than the stored Playlist's value.

The customer ID field is important to verify who is making the
request, and a different one could mean a bug in the music client,
or a larger security issue. It would be a very negative experience
if someone could accidentally (or maliciously) affect another
user's data, even if it's as relatively benign as a music playlist.
Letting bugs like that slip could cause us to lose our customers'
trust, not only in our playlist service but Amazon Music as a
whole! Thus, the "Music Playlist Service API Implementation Notes"
section of the [design document](../../DESIGN_DOCUMENT.md) documents
that you will add this validation to the activity.

Let's propose a new exception class,
`InvalidAttributeChangeException`. However, your senior engineer
points out the similarity between the new exception and
`InvalidAttributeValueException`. He suggests creating a hierarchy
with a third, more generic exception class. He suggests that
this can prove useful when we eventually centralize our validation
logic in its own class (which we will do after finishing the API).
Classes that use this validation logic would have the
flexibility to catch either exception subclasses,
or our new generic exception, depending on the needs.

Update the plantUML class diagram at `src/resources/mastery-task1-music-playlist-CD.puml` with the two new exceptions and updated
hierarchy.

When you are done, verify that the `MT2DesignIntrospectionTests` pass before implementing your design.

### Milestone 2: Implement UpdatePlaylistActivity

Let's implement the exception hierarchy and the `UpdatePlaylistActivity`
class based on our [design document](../../DESIGN_DOCUMENT.md)

Implement the validation logic discussed above and documented in the
"Music Playlist Service API Implementation Notes" section of the
[design document](../../DESIGN_DOCUMENT.md)

Also, review the design to understand the exact requirements (e.g., which field(s) are actually updated by this activity).

Verify that `MT2IntrospectionTests` passes before moving on.

Implement `UpdatePlaylistActivity`'s `handleRequest` method and add unit tests to
cover your new code.

Remember that DynamoDBMapper's save operation is idempotent, similar
to a PUT call. If your `PlaylistDao`'s save method that you create for
the CreatePlaylist use case is general enough, you should be able to
use it for the UpdatePlaylist case as well!

Uncomment and run the UpdatePlaylistActivity unit tests.
Upload your code to your `UpdatePlaylistActivity` Lambda and verify it works by
updating a playlist.

### Doneness Checklist

* Your exception hierarchy design passes `MT2DesignIntrospectionTests`
* Your exception hierarchy implementation passes `MT2IntrospectionTests`
* You've implemented UpdatePlaylist's functionality
* The UpdatePlaylistActivty unit tests are passing.
