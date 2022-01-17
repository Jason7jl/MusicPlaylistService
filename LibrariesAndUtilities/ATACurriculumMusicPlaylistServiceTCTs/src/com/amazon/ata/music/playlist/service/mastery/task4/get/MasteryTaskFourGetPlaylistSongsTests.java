package com.amazon.ata.music.playlist.service.mastery.task4.get;

import com.amazon.ata.music.playlist.service.helpers.MusicPlaylistIntegrationTestBase;

import com.amazonaws.services.atacurriculummusicplaylistservice.model.AddSongToPlaylistRequest;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.AddSongToPlaylistResult;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.GetPlaylistSongsRequest;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.GetPlaylistSongsResult;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.MusicPlaylistClientException;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.SongModel;
import com.google.common.collect.ImmutableList;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.amazon.ata.music.playlist.service.helpers.PlaylistAssertions.validateAddSongToPlaylistSongs;
import static com.amazon.ata.music.playlist.service.helpers.PlaylistAssertions.validateGetPlaylistSongs;
import static com.amazon.ata.music.playlist.service.helpers.PlaylistTestHelper.AVAILABLE_TRACKS;
import static com.amazon.ata.music.playlist.service.helpers.PlaylistTestHelper.createEmptyPlaylist;
import static org.testng.Assert.assertThrows;

public class MasteryTaskFourGetPlaylistSongsTests extends MusicPlaylistIntegrationTestBase {
    private String playlistName;

    @BeforeClass
    public void setup() throws Exception {
        super.setup();
    }

    /**
     * Each method gets its own playlist, so everything is clean.
     * Use a UUID in case "Instant" doesn't have sufficient resolution for concurrent tests.
     * @throws Exception when unusual circumstances that should fail the test occur.
     */
    @BeforeMethod
    public void setupEachTest() throws Exception {
        playlistName = "MT04M2_" + UUID.randomUUID().toString();
    }

    @Test
    public void getPlaylistSongs_withEmptyPlaylist_getsZeroSongs() throws Exception {
        // GIVEN
        // an existent but empty playlist
        String playlistId = createEmptyPlaylist(playlistName, musicPlaylistServiceClient);

        // WHEN
        // We retrieve the playlist
        GetPlaylistSongsRequest request = new GetPlaylistSongsRequest()
            .withId(playlistId);
        GetPlaylistSongsResult result = musicPlaylistServiceClient.getPlaylistSongs(request);

        // THEN
        // The playlist should include zero songs
        validateGetPlaylistSongs(request, result, Collections.emptyList());
    }

    @Test
    public void getPlaylistSongs_withSingleSongPlaylist_getsOneSong() throws Exception {
        // GIVEN
        // An existent playlist with a single song
        String playlistId = createEmptyPlaylist(playlistName, musicPlaylistServiceClient);
        SongModel initialSong = AVAILABLE_TRACKS.get(9);
        List<SongModel> expectedSongs = ImmutableList.of(initialSong);
        AddSongToPlaylistRequest addRequest = new AddSongToPlaylistRequest()
            .withId(playlistId)
            .withAsin(initialSong.getAsin())
            .withTrackNumber(initialSong.getTrackNumber());
        AddSongToPlaylistResult addResult = musicPlaylistServiceClient.addSongToPlaylist(addRequest);

        // WHEN
        // We retrieve the playlist
        GetPlaylistSongsRequest getRequest = new GetPlaylistSongsRequest()
            .withId(playlistId);
        GetPlaylistSongsResult result = musicPlaylistServiceClient.getPlaylistSongs(getRequest);

        // THEN
        // The song list has one song
        validateAddSongToPlaylistSongs(musicPlaylistServiceClient, addRequest, addResult, expectedSongs);
        validateGetPlaylistSongs(getRequest, result, expectedSongs);
    }

    @Test
    public void getPlaylistSongs_withMultipleSongPlaylist_getsAllSongs() throws Exception {
        // GIVEN
        // An existent playlist with a several songs
        String playlistId = createEmptyPlaylist(playlistName, musicPlaylistServiceClient);
        List<SongModel> expectedSongs = AVAILABLE_TRACKS.subList(7, 11);
        for (SongModel song : expectedSongs) {
            AddSongToPlaylistRequest addRequest = new AddSongToPlaylistRequest()
                .withId(playlistId)
                .withAsin(song.getAsin())
                .withTrackNumber(song.getTrackNumber());
            musicPlaylistServiceClient.addSongToPlaylist(addRequest);
        }

        // WHEN
        // We retrieve the playlist
        GetPlaylistSongsRequest getRequest = new GetPlaylistSongsRequest()
            .withId(playlistId);
        GetPlaylistSongsResult getResult = musicPlaylistServiceClient.getPlaylistSongs(getRequest);

        // THEN
        // The song list has the right songs
        validateGetPlaylistSongs(getRequest, getResult, expectedSongs);
    }

    @Test
    public void getPlaylistSongs_forUnknownPlaylist_throwsClientException() throws Exception {
        // GIVEN
        // A nonexistent playlist id (almost certainly)
        String unknownPlaylistId = UUID.randomUUID().toString() + "X";

        // WHEN
        // We attempt to get songs for the playlist
        // THEN
        // It throws a client exception
        GetPlaylistSongsRequest unknownPlaylistRequest = new GetPlaylistSongsRequest()
            .withId(unknownPlaylistId);
        assertThrows(MusicPlaylistClientException.class, () -> {
            musicPlaylistServiceClient.getPlaylistSongs(unknownPlaylistRequest);
        });
    }

}
