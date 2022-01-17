package com.amazon.ata.music.playlist.service.mastery.task5.add;

import com.amazon.ata.music.playlist.service.helpers.MusicPlaylistIntegrationTestBase;
import com.amazon.ata.music.playlist.service.helpers.PlaylistTestHelper;

import com.amazonaws.services.atacurriculummusicplaylistservice.model.AddSongToPlaylistRequest;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.AddSongToPlaylistResult;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.SongModel;
import com.google.common.collect.ImmutableList;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.UUID;

import static com.amazon.ata.music.playlist.service.helpers.PlaylistAssertions.validateAddSongToPlaylistSongs;
import static com.amazon.ata.music.playlist.service.helpers.PlaylistAssertions.validateAddSongToPlaylistSongsInOrder;
import static com.amazon.ata.music.playlist.service.helpers.PlaylistTestHelper.createEmptyPlaylist;

public class MasteryTaskFiveAddSongToPlaylistTests extends MusicPlaylistIntegrationTestBase {
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
        playlistName = "MT05M1_" + UUID.randomUUID().toString();
    }

    @Test
    public void addSongToPlaylist_withEmptyPlaylistAndQueueNextFlag_addsSongPlaylist() {
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
            .withTrackNumber(expectedSong.getTrackNumber())
            .withQueueNext(true);
        AddSongToPlaylistResult result = musicPlaylistServiceClient.addSongToPlaylist(request);

        // THEN
        // The resulting playlist has one song
        validateAddSongToPlaylistSongs(musicPlaylistServiceClient, request, result, ImmutableList.of(expectedSong));
    }

    @Test
    public void addSongToPlaylist_withPopulatedPlaylistAndQueueNextFlag_addsSongToBeginningOfPlaylist() {
        // GIVEN
        // An existing playlist with a song
        String playlistId = createEmptyPlaylist(playlistName, musicPlaylistServiceClient);
        SongModel firstAddedSong = PlaylistTestHelper.AVAILABLE_TRACKS.get(5);
        SongModel secondAddedSong = PlaylistTestHelper.AVAILABLE_TRACKS.get(6);
        List<SongModel> expectedSongs = ImmutableList.of(secondAddedSong, firstAddedSong);

        // Add the song (known to the album DAO)
        AddSongToPlaylistRequest setupRequest = new AddSongToPlaylistRequest()
            .withId(playlistId)
            .withAsin(firstAddedSong.getAsin())
            .withTrackNumber(firstAddedSong.getTrackNumber());
        musicPlaylistServiceClient.addSongToPlaylist(setupRequest);

        // WHEN
        // We add another song queued next
        AddSongToPlaylistRequest request = new AddSongToPlaylistRequest()
            .withId(playlistId)
            .withAsin(secondAddedSong.getAsin())
            .withTrackNumber(secondAddedSong.getTrackNumber())
            .withQueueNext(true);
        AddSongToPlaylistResult result = musicPlaylistServiceClient.addSongToPlaylist(request);

        // THEN
        // The resulting playlist has two songs in the expected order
        validateAddSongToPlaylistSongsInOrder(musicPlaylistServiceClient, request, result, expectedSongs);
    }

    @Test
    public void addSongToPlaylist_withPopulatedPlaylistWithoutQueueNextFlag_addsSongToEndOfPlaylist() {
        // GIVEN
        // An existing playlist with a song
        String playlistId = createEmptyPlaylist(playlistName, musicPlaylistServiceClient);
        SongModel firstAddedSong = PlaylistTestHelper.AVAILABLE_TRACKS.get(5);
        SongModel secondAddedSong = PlaylistTestHelper.AVAILABLE_TRACKS.get(6);
        List<SongModel> expectedSongs = ImmutableList.of(firstAddedSong, secondAddedSong);
        // Add the song (known to the album DAO)
        AddSongToPlaylistRequest setupRequest = new AddSongToPlaylistRequest()
            .withId(playlistId)
            .withAsin(firstAddedSong.getAsin())
            .withTrackNumber(firstAddedSong.getTrackNumber());
        musicPlaylistServiceClient.addSongToPlaylist(setupRequest);

        // WHEN
        // We add another song queued next
        AddSongToPlaylistRequest request = new AddSongToPlaylistRequest()
            .withId(playlistId)
            .withAsin(secondAddedSong.getAsin())
            .withTrackNumber(secondAddedSong.getTrackNumber());
        AddSongToPlaylistResult result = musicPlaylistServiceClient.addSongToPlaylist(request);

        // THEN
        // The resulting playlist has two songs in the expected order
        validateAddSongToPlaylistSongsInOrder(musicPlaylistServiceClient, request, result, expectedSongs);
    }
}
