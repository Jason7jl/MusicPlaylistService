package com.amazon.ata.music.playlist.service.mastery.task5;

import com.amazon.ata.music.playlist.service.helpers.MusicPlaylistIntegrationTestBase;

import com.amazonaws.services.atacurriculummusicplaylistservice.model.AddSongToPlaylistRequest;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.GetPlaylistSongsRequest;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.GetPlaylistSongsResult;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.SongModel;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.SongOrder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.amazon.ata.music.playlist.service.helpers.PlaylistAssertions.validateGetPlaylistSongsInOrder;
import static com.amazon.ata.music.playlist.service.helpers.PlaylistTestHelper.AVAILABLE_TRACKS;
import static com.amazon.ata.music.playlist.service.helpers.PlaylistTestHelper.createEmptyPlaylist;

public class MasteryTaskFiveTests extends MusicPlaylistIntegrationTestBase {

    @BeforeClass
    public void setup() throws Exception {
        super.setup();
    }

    @Test
    public void masteryTaskFive_addSongsQueuedNextAndGetInReverseOrder_returnsSongsInOrderAdded() throws Exception {
        // GIVEN
        // An existing but empty playlist
        String playlistId = createEmptyPlaylist("MT5" + UUID.randomUUID().toString(), musicPlaylistServiceClient);
        List<SongModel> expectedSongs = AVAILABLE_TRACKS.subList(7, 11);

        // WHEN
        // We add several songs to the playlist queued next
        for (SongModel song : expectedSongs) {
            AddSongToPlaylistRequest addNextRequest = new AddSongToPlaylistRequest()
                .withId(playlistId)
                .withAsin(song.getAsin())
                .withTrackNumber(song.getTrackNumber())
                .withQueueNext(true);
            musicPlaylistServiceClient.addSongToPlaylist(addNextRequest);
        }
        // and retrieve the song list default
        GetPlaylistSongsRequest getSongsDefaultRequest = new GetPlaylistSongsRequest()
            .withId(playlistId)
            .withOrder(SongOrder.DEFAULT);
        GetPlaylistSongsResult getSongsDefaultResult =
            musicPlaylistServiceClient.getPlaylistSongs(getSongsDefaultRequest);
        // and retrieve the song list reversed
        GetPlaylistSongsRequest getSongsReversedRequest = new GetPlaylistSongsRequest()
            .withId(playlistId)
            .withOrder(SongOrder.REVERSED);
        GetPlaylistSongsResult getSongsReversedResult =
            musicPlaylistServiceClient.getPlaylistSongs(getSongsReversedRequest);

        // THEN
        // The adds put the songs in the list at the front
        List<SongModel> reversedExpectedSongs = new ArrayList<>(expectedSongs);
        Collections.reverse(reversedExpectedSongs);
        validateGetPlaylistSongsInOrder(getSongsDefaultRequest, getSongsDefaultResult, reversedExpectedSongs);

        // And get reversed returns songs in opposite order
        validateGetPlaylistSongsInOrder(getSongsReversedRequest, getSongsReversedResult, expectedSongs);
    }
}
