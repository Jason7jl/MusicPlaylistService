## Mastery Task 4: Without Music, Life Would B-flat

### Milestone 1: Implement AddSongToPlaylistActivity

We're able to Create, Get, and Update playlists, but they're not
very useful without being able to add songs! Let's fix
that by implementing the AddSongToPlaylist API. Review the
"Music Playlist Service API Implementation Notes" section of the
[design document](../../DESIGN_DOCUMENT.md) for requirements.

NOTE: For this task, you don't need to support the `queueNext`
option. We will do that in a future task. We will implement your base API first for this task and add more features later. In the next milestone, we'll do the same thing with the `GetPlaylistSongs` endpoint.

Our `AlbumTrack` java model is empty. We'll need to add
the fields matching the data model and stored data in the
`album_tracks` table in your account. Make sure to include
annotations to mark the partition key **and** sort key.

Notice some attributes in the table are not camelCase, such 
as `track_number`. However, we can use the `attributeName` 
property of the `@DynamoDBAttribute` annotation to explicitly 
set the attribute name.

When the Java model is ready, implement a `getAlbumTrack` method
in `AlbumTrackDao` that uses the `DynamoDBMapper` to load an
item from the `album_tracks` table. Add tests for `AlbumTrackDao`
as appropriate.

As with `PlaylistModel` for previous APIs, we must convert our
`AlbumTrack` data model to the API-defined `SongModel`. With
the updated `AlbumTrack` class, create a `toSongModel` method
in `ModelConverter` to map the new fields from the `AlbumTrack`
object to the `SongModel` object. Update `ModelConverterTest` as
appropriate.

Once done, we can implement `AddSongToPlaylistActivity`'s 
`handleRequest` method to fetch the album information and add
it to the `Playlist`'s songList, save the updated `Playlist`
and return the updated song list from the API. Update the
Activity to use the `ModelConverter` to convert the
`AlbumTrack`'s to `SongModel`'s as needed. Make the change,
and then uncomment and run the `AddSongToPlaylistActivityTest`
unit tests.

Once your tests pass, upload your code to Lambda and ensure it works.

### Milestone 2: Implement GetPlaylistSongsActivity

Next, let's implement
`GetPlaylistSongsActivity`'s `handleRequest` method to return the
song list, along with unit tests as needed. Review the
"Music Playlist Service API Implementation Notes" section of the
[design document](../../DESIGN_DOCUMENT.md) for requirements.

No need to support the `order` field in the `GetPlaylistSongsRequest` yet,
as you will implement that in a later task. For now, we'll only be able to return
songs in the default playlist order. This API only needs to load the
`Playlist` from the playlists table and return the `Playlist` 's song
list.

To return the result of this activity, 
we'll have to convert the `AlbumTrack`'s to our API
defined `SongModel` class and add them in a list in the `GetPlaylistSongsResult`.

### Doneness Checklist

* You've implemented AddSongToPlaylist's functionality
* You've implemented GetPlaylistSongs functionality
* `AddSongsToPlaylistActivtyTest` tests pass
* `GetPlaylistSongsActivtyTest` tests pass
* Both of your Lambda functions are working on AWS
