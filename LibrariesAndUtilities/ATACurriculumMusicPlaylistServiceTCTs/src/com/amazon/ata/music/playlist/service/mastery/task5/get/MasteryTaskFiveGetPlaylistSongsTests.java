package com.amazon.ata.music.playlist.service.mastery.task5.get;

import com.amazon.ata.music.playlist.service.helpers.MusicPlaylistIntegrationTestBase;

import com.amazonaws.services.atacurriculummusicplaylistservice.model.AddSongToPlaylistRequest;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.GetPlaylistSongsRequest;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.GetPlaylistSongsResult;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.SongModel;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.SongOrder;
import com.google.common.collect.ImmutableList;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.amazon.ata.music.playlist.service.helpers.PlaylistAssertions.validateGetPlaylistSongs;
import static com.amazon.ata.music.playlist.service.helpers.PlaylistAssertions.validateGetPlaylistSongsInOrder;
import static com.amazon.ata.music.playlist.service.helpers.PlaylistTestHelper.AVAILABLE_TRACKS;
import static com.amazon.ata.music.playlist.service.helpers.PlaylistTestHelper.createEmptyPlaylist;

public class MasteryTaskFiveGetPlaylistSongsTests extends MusicPlaylistIntegrationTestBase {
    private static final SongModel SINGLE_SONG = AVAILABLE_TRACKS.get(9);
    private static final List<SongModel> EXPECTED_SONGS = AVAILABLE_TRACKS.subList(1, 11);
    private String emptyPlaylistId;
    private String singleSongPlaylistId;
    private String manySongsPlaylistId;

    @BeforeClass
    public void setup() throws Exception {
        super.setup();

        createTestPlaylists();
    }

    private void createTestPlaylists() {
        // EMPTY PLAYLIST
        emptyPlaylistId =
            createEmptyPlaylist("MT05M2_" + UUID.randomUUID().toString(), musicPlaylistServiceClient);

        // ONE SONG PLAYLIST
        singleSongPlaylistId =
            createEmptyPlaylist("MT05M2_" + UUID.randomUUID().toString(), musicPlaylistServiceClient);
        AddSongToPlaylistRequest addRequest = new AddSongToPlaylistRequest()
            .withId(singleSongPlaylistId)
            .withAsin(SINGLE_SONG.getAsin())
            .withTrackNumber(SINGLE_SONG.getTrackNumber());
        musicPlaylistServiceClient.addSongToPlaylist(addRequest);

        // MANY SONGS PLAYLIST
        manySongsPlaylistId =
            createEmptyPlaylist("MT05M2_" + UUID.randomUUID().toString(), musicPlaylistServiceClient);
        for (SongModel song : EXPECTED_SONGS) {
            addRequest = new AddSongToPlaylistRequest()
                .withId(manySongsPlaylistId)
                .withAsin(song.getAsin())
                .withTrackNumber(song.getTrackNumber());
            musicPlaylistServiceClient.addSongToPlaylist(addRequest);
        }
    }

    @Test
    public void getPlaylistSongs_withEmptyPlaylistDefault_getsZeroSongs() {
        // GIVEN
        // an existent but empty playlist

        // WHEN
        // We retrieve the playlist
        GetPlaylistSongsRequest request = new GetPlaylistSongsRequest()
            .withId(emptyPlaylistId)
            .withOrder(SongOrder.DEFAULT);
        GetPlaylistSongsResult result = musicPlaylistServiceClient.getPlaylistSongs(request);

        // THEN
        // The playlist should include zero songs
        validateGetPlaylistSongs(request, result, Collections.emptyList());
    }

    @Test
    public void getPlaylistSongs_withEmptyPlaylistReversed_getsZeroSongs() {
        // GIVEN
        // an existent but empty playlist

        // WHEN
        // We retrieve the playlist reversed
        GetPlaylistSongsRequest request = new GetPlaylistSongsRequest()
            .withId(emptyPlaylistId)
            .withOrder(SongOrder.REVERSED);
        GetPlaylistSongsResult result = musicPlaylistServiceClient.getPlaylistSongs(request);

        // THEN
        // The playlist should include zero songs
        validateGetPlaylistSongs(request, result, Collections.emptyList());
    }

    @Test
    public void getPlaylistSongs_withEmptyPlaylistShuffled_getsZeroSongs() {
        // GIVEN
        // an existent but empty playlist

        // WHEN
        // We retrieve the playlist shuffled
        GetPlaylistSongsRequest request = new GetPlaylistSongsRequest()
            .withId(emptyPlaylistId)
            .withOrder(SongOrder.SHUFFLED);
        GetPlaylistSongsResult result = musicPlaylistServiceClient.getPlaylistSongs(request);

        // THEN
        // The playlist should include zero songs
        validateGetPlaylistSongs(request, result, Collections.emptyList());
    }

    @Test
    public void getPlaylistSongs_withSingleSongPlaylistDefault_getsOneSong() {
        // GIVEN
        // An existent playlist with a single song

        // WHEN
        // We retrieve the playlist
        GetPlaylistSongsRequest getRequest = new GetPlaylistSongsRequest()
            .withId(singleSongPlaylistId)
            .withOrder(SongOrder.DEFAULT);
        GetPlaylistSongsResult result = musicPlaylistServiceClient.getPlaylistSongs(getRequest);

        // THEN
        // The song list has one song
        validateGetPlaylistSongs(getRequest, result, ImmutableList.of(SINGLE_SONG));
    }

    @Test
    public void getPlaylistSongs_withSingleSongPlaylistReversed_getsOneSong() {
        // GIVEN
        // An existent playlist with a single song

        // WHEN
        // We retrieve the playlist reversed
        GetPlaylistSongsRequest getRequest = new GetPlaylistSongsRequest()
            .withId(singleSongPlaylistId)
            .withOrder(SongOrder.REVERSED);
        GetPlaylistSongsResult result = musicPlaylistServiceClient.getPlaylistSongs(getRequest);

        // THEN
        // The song list has one song
        validateGetPlaylistSongs(getRequest, result, ImmutableList.of(SINGLE_SONG));
    }

    @Test
    public void getPlaylistSongs_withSingleSongPlaylistShuffled_getsOneSong() {
        // GIVEN
        // An existent playlist with a single song

        // WHEN
        // We retrieve the playlist shuffled
        GetPlaylistSongsRequest getRequest = new GetPlaylistSongsRequest()
            .withId(singleSongPlaylistId)
            .withOrder(SongOrder.SHUFFLED);
        GetPlaylistSongsResult result = musicPlaylistServiceClient.getPlaylistSongs(getRequest);

        // THEN
        // The song list has one song
        validateGetPlaylistSongs(getRequest, result, ImmutableList.of(SINGLE_SONG));
    }

    @Test
    public void getPlaylistSongs_withMultipleSongPlaylistDefault_getsAllSongsInOrder() {
        // GIVEN
        // An existent playlist with several songs

        // WHEN
        // We retrieve the playlist
        GetPlaylistSongsRequest getRequest = new GetPlaylistSongsRequest()
            .withId(manySongsPlaylistId)
            .withOrder(SongOrder.DEFAULT);
        GetPlaylistSongsResult getResult = musicPlaylistServiceClient.getPlaylistSongs(getRequest);

        // THEN
        // The song list has the right number of songs in the default order
        validateGetPlaylistSongsInOrder(getRequest, getResult, EXPECTED_SONGS);
    }

    @Test
    public void getPlaylistSongs_withMultipleSongPlaylistNoSpecifiedOrder_getsAllSongsInDefaultOrder() {
        // GIVEN
        // An existent playlist with several songs

        // WHEN
        // We retrieve the playlist
        GetPlaylistSongsRequest getRequest = new GetPlaylistSongsRequest()
            .withId(manySongsPlaylistId);
        GetPlaylistSongsResult getResult = musicPlaylistServiceClient.getPlaylistSongs(getRequest);

        // THEN
        // The song list has the right songs in the default order
        validateGetPlaylistSongsInOrder(getRequest, getResult, EXPECTED_SONGS);
    }

    @Test
    public void getPlaylistSongs_withMultipleSongPlaylistReversed_getsAllSongsInReverseOrder() {
        // GIVEN
        // An existent playlist with several songs

        // WHEN
        // We retrieve the playlist reversed
        GetPlaylistSongsRequest getRequest = new GetPlaylistSongsRequest()
            .withId(manySongsPlaylistId)
            .withOrder(SongOrder.REVERSED);
        GetPlaylistSongsResult getResult = musicPlaylistServiceClient.getPlaylistSongs(getRequest);

        // THEN
        // The song list has the right songs in reversed order
        List<SongModel> reversedSongs = new ArrayList(EXPECTED_SONGS);
        Collections.reverse(reversedSongs);

        validateGetPlaylistSongsInOrder(getRequest, getResult, reversedSongs);
    }

    @Test
    public void getPlaylistSongs_withMultipleSongPlaylistShuffled_getsAllSongsInAnyNotDefaultOrder() {
        // GIVEN
        // An existent playlist with a several songs

        // WHEN
        // We retrieve the playlist shuffled
        GetPlaylistSongsRequest getRequest = new GetPlaylistSongsRequest()
            .withId(manySongsPlaylistId)
            .withOrder(SongOrder.SHUFFLED);
        GetPlaylistSongsResult getResult = musicPlaylistServiceClient.getPlaylistSongs(getRequest);

        // THEN
        // The song list has the right number of songs in any order (might still end up in default order by chance)
        validateGetPlaylistSongs(getRequest, getResult, EXPECTED_SONGS);
    }
}
