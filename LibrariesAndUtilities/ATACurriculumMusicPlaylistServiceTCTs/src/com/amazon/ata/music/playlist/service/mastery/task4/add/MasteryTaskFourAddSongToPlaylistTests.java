package com.amazon.ata.music.playlist.service.mastery.task4.add;

import com.amazon.ata.music.playlist.service.helpers.MusicPlaylistIntegrationTestBase;
import com.amazon.ata.music.playlist.service.helpers.PlaylistTestHelper;

import com.amazonaws.services.atacurriculummusicplaylistservice.model.AddSongToPlaylistRequest;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.AddSongToPlaylistResult;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.MusicPlaylistClientException;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.SongModel;
import com.google.common.collect.ImmutableList;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.UUID;

import static com.amazon.ata.music.playlist.service.helpers.PlaylistAssertions.validateAddSongToPlaylistSongs;
import static com.amazon.ata.music.playlist.service.helpers.PlaylistTestHelper.createEmptyPlaylist;
import static org.testng.Assert.assertThrows;

public class MasteryTaskFourAddSongToPlaylistTests extends MusicPlaylistIntegrationTestBase {
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
        playlistName = "MT04M1_" + UUID.randomUUID().toString();
    }

    @Test
    public void addSongToPlaylist_withEmptyPlaylist_addsSong() {
        // GIVEN
        // An existing but empty playlist
        String playlistId = createEmptyPlaylist(playlistName, musicPlaylistServiceClient);
        // A song known to the album DAO
        SongModel expectedSong = PlaylistTestHelper.AVAILABLE_TRACKS.get(0);

        // WHEN
        // We add that song to the playlist
        AddSongToPlaylistRequest request = new AddSongToPlaylistRequest()
            .withId(playlistId)
            .withAsin(expectedSong.getAsin())
            .withTrackNumber(expectedSong.getTrackNumber());
        AddSongToPlaylistResult result = musicPlaylistServiceClient.addSongToPlaylist(request);

        // THEN
        // The resulting playlist has one song
        validateAddSongToPlaylistSongs(musicPlaylistServiceClient, request, result, ImmutableList.of(expectedSong));
    }

    @Test
    public void addSongToPlaylist_withPopulatedPlaylist_addsSong() {
        // GIVEN
        // An existing playlist with a song
        String playlistId = createEmptyPlaylist(playlistName, musicPlaylistServiceClient);
        SongModel existingSong = PlaylistTestHelper.AVAILABLE_TRACKS.get(5);
        SongModel expectedSong = PlaylistTestHelper.AVAILABLE_TRACKS.get(6);
        List<SongModel> expectedSongs = ImmutableList.of(existingSong, expectedSong);
        // Add the song (known to the album DAO)
        AddSongToPlaylistRequest setupRequest = new AddSongToPlaylistRequest()
            .withId(playlistId)
            .withAsin(existingSong.getAsin())
            .withTrackNumber(existingSong.getTrackNumber());
        musicPlaylistServiceClient.addSongToPlaylist(setupRequest);

        // WHEN
        // We add another song
        AddSongToPlaylistRequest request = new AddSongToPlaylistRequest()
            .withId(playlistId)
            .withAsin(expectedSong.getAsin())
            .withTrackNumber(expectedSong.getTrackNumber());
        AddSongToPlaylistResult result = musicPlaylistServiceClient.addSongToPlaylist(request);

        // THEN
        // The resulting playlist has our existing and expected songs
        validateAddSongToPlaylistSongs(musicPlaylistServiceClient, request, result, expectedSongs);
    }

    @Test
    public void addSongToPlaylist_withDuplicateSong_addsSong() {
        // GIVEN
        // An existing playlist with a single song
        String playlistId = createEmptyPlaylist(playlistName, musicPlaylistServiceClient);
        // A song known to the album DAO
        SongModel expectedSong = PlaylistTestHelper.AVAILABLE_TRACKS.get(0);
        AddSongToPlaylistRequest setupRequest = new AddSongToPlaylistRequest()
            .withId(playlistId)
            .withAsin(expectedSong.getAsin())
            .withTrackNumber(expectedSong.getTrackNumber());
        musicPlaylistServiceClient.addSongToPlaylist(setupRequest);

        // WHEN
        // We add that song to the playlist
        AddSongToPlaylistRequest request = new AddSongToPlaylistRequest()
            .withId(playlistId)
            .withAsin(expectedSong.getAsin())
            .withTrackNumber(expectedSong.getTrackNumber());
        AddSongToPlaylistResult result = musicPlaylistServiceClient.addSongToPlaylist(request);

        // THEN
        // The resulting playlist has two identical songs
        validateAddSongToPlaylistSongs(musicPlaylistServiceClient, request, result,
            ImmutableList.of(expectedSong, expectedSong));
    }

    @Test
    public void addSongToPlaylist_withNonexistentSong_throwsClientException() {
        // GIVEN
        // An existing but empty playlist
        String playlistId = createEmptyPlaylist(playlistName, musicPlaylistServiceClient);
        // A nonexistent asin (very probably)
        String asin = UUID.randomUUID().toString();

        // WHEN
        // We add that song to the playlist
        // THEN
        // the service throws a MusicPlaylistClientException
        AddSongToPlaylistRequest request = new AddSongToPlaylistRequest()
            .withId(playlistId)
            .withAsin(asin)
            .withTrackNumber(0);

        assertThrows(MusicPlaylistClientException.class, () -> {
            musicPlaylistServiceClient.addSongToPlaylist(request);
        });
    }

    @Test
    public void addSongToPlaylist_withNonexistentPlaylist_throwsClientException() {
        // GIVEN
        // A nonexistent playlist (very probably)
        String playlistId = UUID.randomUUID().toString();
        // And a valid song
        SongModel expectedSong = PlaylistTestHelper.AVAILABLE_TRACKS.get(0);

        // WHEN
        // We add a valid song to the playlist
        // THEN
        // The
        AddSongToPlaylistRequest request = new AddSongToPlaylistRequest()
            .withId(playlistId)
            .withAsin(expectedSong.getAsin())
            .withTrackNumber(expectedSong.getTrackNumber());
        assertThrows(MusicPlaylistClientException.class, () -> {
            musicPlaylistServiceClient.addSongToPlaylist(request);
        });
    }

}
