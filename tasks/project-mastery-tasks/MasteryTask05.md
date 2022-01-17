## Mastery Task 5: Zoom, Enhance

### Milestone 1: Enhance AddSongToPlaylist

We've finished the base functionality of all our APIs! We can successfully create, retrieve, and update playlists, as
well as add songs to them and retrieve their song list.
Before we call our Lambdas complete, our last task is to implement the remaining features to make sure we present a truly lovable product.
One such feature is to queue a song to be played in the front of the playlist.

Update the `AddSongToPlaylist` endpoint to read the `AddSongToPlaylistRequest#queueNext` 
flag, if it is true, insert the song in front of list in O(1) time.
`DynamoDBMapper` will convert lists to an `ArrayList` when loading data, 
perfect for most use cases. However,
we've gone ahead and configured the `DynamoDBMapper` to use the `LinkedList` 
implementation that we've covered in class.
Because we know that it is using a `LinkedList` as the `List` interface's
implementation, it's safe to *cast* it in this case. Casting to `LinkedList` allows us to use the
methods in `LinkedList` that are not in the `List` interface. The process of
casting an interface to an implementation class is known as *downcasting*.

In general, be very careful when downcasting. It's dependent on the
underlying implementation class, which isn't always known ahead of time. 
Furthermore, it's challenging to catch casting mistakes during compilation, 
so it's possible to push unstable code without realizing it!

### Milestone 2: Enhance GetPlaylistSongs

And the final minimum lovable feature is to update the `GetPlaylistSongs`
endpoint to return songs in shuffled or reversed order based on the
value of `GetPlaylistSongsRequest#order`. Notice that `order` is a
`String` type, but we can use the `SongOrder` class, which declares
*constant* `String` values to compare. `SongOrder` is generated
by Coral, our service framework, so you won't find its code in your
project package. You can view the source by hitting Command-click
with your mouse over the class name in IntelliJ.

We've seen and used `Collections#sort` already. The `Collections`
class also contains other *static* methods that operate on data
structures. The two methods you might find useful are
`Collections#shuffle` and `Collections#reverse`. Use these based
on the value of the `order` field in `GetPlaylistSongsRequest`
to modify the list before returning it in the API.

### Doneness Checklist

* You've updated AddSongToPlaylist's functionality to allow adding songs to the front of the playlist
* You've updated GetPlaylistSongs functionality to allow retrieving the playlist songs in default, shuffled, or reversed order
