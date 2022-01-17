package com.amazon.ata.music.playlist.service.helpers;

import com.amazonaws.services.atacurriculummusicplaylistservice.ATACurriculumMusicPlaylistService;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.AddSongToPlaylistRequest;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.AddSongToPlaylistResult;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.CreatePlaylistRequest;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.CreatePlaylistResult;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.GetPlaylistRequest;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.GetPlaylistResult;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.GetPlaylistSongsRequest;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.GetPlaylistSongsResult;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.PlaylistModel;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.SongModel;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Contains PlaylistModel related static assertion methods.
 */
public class PlaylistAssertions {
    private static final String ADD_SONG_TO_PLAYLIST_MSG = "Expected addSongToPlaylistRequest [%s] result";
    private static final String GET_PLAYLIST_SONGS_MSG = "Expected getPlaylistSongsRequest [%s] result";

    private PlaylistAssertions() { }

    /**
     * Convenience method for validating that the result of the CreatePlaylist API matches
     * the provided request.
     *
     * @param request the CreatePlaylist request object used in the CreatePlaylist API call
     * @param result  the CreatePlaylist result object from the CreatePlaylist API call
     * @return the created PlaylistModel
     */
    public static PlaylistModel assertCreatePlaylistResultMatchesRequest(CreatePlaylistRequest request,
                                                                         CreatePlaylistResult result) {
        PlaylistModel resultPlaylist = validateNonNullPlaylist(result.getPlaylist());

        assertEquals(resultPlaylist.getName(), request.getName(),
            "Expected the CreatePlaylist API to create a playlist " +
                "with a playlist name matching the request's playlist name.");
        assertEquals(resultPlaylist.getCustomerId(), request.getCustomerId(),
            "Expected the CreatePlaylist API to create a playlist " +
                "with a customer ID matching the request's customer ID.");

        if (request.getTags() != null) {
            assertNotNull(resultPlaylist.getTags(),
                "Expected the CreatePlaylist API to create a playlist with tags," +
                    "but the new playlist had null tags.");

            for (String tag : request.getTags()) {
                assertTrue(resultPlaylist.getTags().contains(tag),
                    String.format("Expected the CreatePlaylist API to create a playlist with a tag '%s' but got %s.",
                        tag,
                        Arrays.toString(resultPlaylist.getTags().toArray())));
            }
        }

        assertEquals(resultPlaylist.getSongCount(), Integer.valueOf(0),
            "Expected the CreatePlaylist API to create a playlist with a song count of 0.");
        return resultPlaylist;
    }

    /**
     * Asserts the Playlist is not null and has a non null ID
     * with generic error messages.
     *
     * @param playlist the result of the CreatePlaylist API call
     * @return the PlaylistModel in the CreatePlaylistResult
     */
    public static PlaylistModel validateNonNullPlaylist(PlaylistModel playlist) {
        assertNotNull(playlist,
            "Expected a created playlist in the API response.");
        assertNotNull(playlist.getId(),
            "Expected a created playlist with a generated ID in the API response.");

        return playlist;
    }

    /**
     * Asserts addSongToPlaylistResult contains the expected songs
     * and that the playlist songCount has been properly updated.
     *
     * @param client the ATACurriculumMusicPlaylistService used to get the playlist
     * @param addSongToPlaylistRequest the request to add the song to the playlist
     * @param addSongToPlaylistResult the result of addSongToPlaylistRequest
     * @param expectedSongs the songs we expect to find in addSongToPlaylistResult
     */
    public static void validateAddSongToPlaylistSongs(ATACurriculumMusicPlaylistService client,
                                                  AddSongToPlaylistRequest addSongToPlaylistRequest,
                                                  AddSongToPlaylistResult addSongToPlaylistResult,
                                                  List<SongModel> expectedSongs) {
        List<SongModel> actualSongs = addSongToPlaylistResult.getSongList();
        int expectedSongCount = expectedSongs.size();

        validateAddSongToPlaylistLength(client, addSongToPlaylistRequest, addSongToPlaylistResult, expectedSongCount);

        assertTrue(actualSongs.containsAll(expectedSongs),
            String.format(ADD_SONG_TO_PLAYLIST_MSG + "to contain songs: %s! Instead returned: %s",
                addSongToPlaylistRequest, expectedSongs, actualSongs));
    }

    /**
     * Asserts addSongToPlaylistResult contains the expected songs in a specified order
     * and that the playlist songCount has been properly updated.
     *
     * @param client the ATACurriculumMusicPlaylistService used to get the playlist
     * @param addSongToPlaylistRequest the request to add the song to the playlist
     * @param addSongToPlaylistResult the result of addSongToPlaylistRequest
     * @param expectedSongs the songs we expect to find in addSongToPlaylistResult
     */
    public static void validateAddSongToPlaylistSongsInOrder(ATACurriculumMusicPlaylistService client,
                                                      AddSongToPlaylistRequest addSongToPlaylistRequest,
                                                      AddSongToPlaylistResult addSongToPlaylistResult,
                                                      List<SongModel> expectedSongs) {
        List<SongModel> actualSongs = addSongToPlaylistResult.getSongList();
        int expectedSongCount = expectedSongs.size();

        validateAddSongToPlaylistLength(client, addSongToPlaylistRequest, addSongToPlaylistResult, expectedSongCount);

        assertEquals(actualSongs, expectedSongs,
            String.format(ADD_SONG_TO_PLAYLIST_MSG + "to contain songs: %s in the same order! Instead returned: %s!",
                addSongToPlaylistRequest, expectedSongs, actualSongs));
    }

    /**
     * Asserts getPlaylistSongsResult contains the expected songs.
     *
     * @param getPlaylistSongsRequest the request to get the songs for a playlist
     * @param getPlaylistSongsResult the result of getPlaylistSongsRequest
     * @param expectedSongs the songs we expect to find in getPlaylistSongsResult
     */
    public static void validateGetPlaylistSongs(GetPlaylistSongsRequest getPlaylistSongsRequest,
                                                GetPlaylistSongsResult getPlaylistSongsResult,
                                                List<SongModel> expectedSongs) {
        List<SongModel> actualSongs = getPlaylistSongsResult.getSongList();
        int expectedSongCount = expectedSongs.size();

        assertTrue(actualSongs.containsAll(expectedSongs),
            String.format(GET_PLAYLIST_SONGS_MSG + "to contain songs: %s! Instead returned: %s!",
                getPlaylistSongsRequest, expectedSongs, actualSongs));

        assertEquals(actualSongs.size(), expectedSongCount,
            String.format(GET_PLAYLIST_SONGS_MSG + "to return %d songs! Instead returned %d songs.",
                getPlaylistSongsRequest, expectedSongCount, actualSongs.size()));
    }

    /**
     * Asserts getPlaylistSongsResult contains the expected songs in an expected order.
     *
     * @param getPlaylistSongsRequest the request to get the songs for a playlist
     * @param getPlaylistSongsResult the result of getPlaylistSongsRequest
     * @param expectedSongs the songs we expect to find in getPlaylistSongsResult
     */
    public static void validateGetPlaylistSongsInOrder(GetPlaylistSongsRequest getPlaylistSongsRequest,
                                                GetPlaylistSongsResult getPlaylistSongsResult,
                                                List<SongModel> expectedSongs) {
        List<SongModel> actualSongs = getPlaylistSongsResult.getSongList();
        int expectedSongCount = expectedSongs.size();

        assertEquals(actualSongs, expectedSongs,
            String.format(GET_PLAYLIST_SONGS_MSG + "to contain songs: %s in the same order! Instead returned: %s!",
                getPlaylistSongsRequest, expectedSongs, actualSongs));

        assertEquals(actualSongs.size(), expectedSongCount,
            String.format(GET_PLAYLIST_SONGS_MSG + "to return %d songs! Instead returned %d songs.",
                getPlaylistSongsRequest, expectedSongCount, actualSongs.size()));
    }

    /**
     * Asserts addSongToPlaylistResult contains the expected number songs
     * and that the playlist SongCount has been properly updated.
     *
     * @param client the ATACurriculumMusicPlaylistService used to get the playlist
     * @param addSongToPlaylistRequest the request to add the song to the playlist
     * @param addSongToPlaylistResult the result of addSongToPlaylistRequest
     * @param expectedSongCount the number of songs we expect to find in addSongToPlaylistResult
     *                          and as the Playlist's songCount
     */
    private static void validateAddSongToPlaylistLength(ATACurriculumMusicPlaylistService client,
                                                     AddSongToPlaylistRequest addSongToPlaylistRequest,
                                                     AddSongToPlaylistResult addSongToPlaylistResult,
                                                     int expectedSongCount) {
        String playlistId = addSongToPlaylistRequest.getId();
        List<SongModel> actualSongs = addSongToPlaylistResult.getSongList();

        GetPlaylistRequest getPlaylistRequest = new GetPlaylistRequest().withId(playlistId);

        GetPlaylistResult getPlaylistResult = client.getPlaylist(getPlaylistRequest);

        PlaylistModel playlist = getPlaylistResult.getPlaylist();
        int actualSongCount = playlist.getSongCount();

        assertEquals(actualSongs.size(), expectedSongCount,
            String.format(ADD_SONG_TO_PLAYLIST_MSG + "to return %d songs! Instead returned %d songs.",
                addSongToPlaylistRequest, expectedSongCount, actualSongs.size()));

        assertEquals(actualSongCount, expectedSongCount,
            String.format(ADD_SONG_TO_PLAYLIST_MSG +
                    "to update playlist's songCount to %d! Instead, updated playlist has songCount of %d.",
                addSongToPlaylistRequest, expectedSongCount, actualSongCount));

    }

}
