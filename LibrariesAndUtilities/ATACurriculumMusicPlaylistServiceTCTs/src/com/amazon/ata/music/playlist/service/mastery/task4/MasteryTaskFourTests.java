package com.amazon.ata.music.playlist.service.mastery.task4;

import com.amazon.ata.music.playlist.service.helpers.MusicPlaylistIntegrationTestBase;
import com.amazon.ata.music.playlist.service.helpers.PlaylistTestHelper;

import com.amazonaws.services.atacurriculummusicplaylistservice.model.AddSongToPlaylistRequest;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.AddSongToPlaylistResult;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.GetPlaylistSongsRequest;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.GetPlaylistSongsResult;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.SongModel;
import com.google.common.collect.ImmutableList;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.UUID;

import static com.amazon.ata.music.playlist.service.helpers.PlaylistAssertions.validateAddSongToPlaylistSongs;
import static com.amazon.ata.music.playlist.service.helpers.PlaylistAssertions.validateGetPlaylistSongs;
import static com.amazon.ata.music.playlist.service.helpers.PlaylistTestHelper.createEmptyPlaylist;

public class MasteryTaskFourTests extends MusicPlaylistIntegrationTestBase {

    @BeforeClass
    public void setup() throws Exception {
        super.setup();
    }

    @Test
    public void masteryTaskFour_addAndGetSong_returnsSong() throws Exception {
        // GIVEN
        // An existing but empty playlist
        String playlistId = createEmptyPlaylist("MT4" + UUID.randomUUID().toString(), musicPlaylistServiceClient);
        // A song known to the album DAO
        SongModel expectedSong = PlaylistTestHelper.AVAILABLE_TRACKS.get(0);
        List<SongModel> expectedSongs = ImmutableList.of(expectedSong);

        // WHEN
        // We add that song to the playlist
        AddSongToPlaylistRequest addSongRequest = new AddSongToPlaylistRequest()
            .withId(playlistId)
            .withAsin(expectedSong.getAsin())
            .withTrackNumber(expectedSong.getTrackNumber());
        AddSongToPlaylistResult addSongResult = musicPlaylistServiceClient.addSongToPlaylist(addSongRequest);
        // and retrieve the song list
        GetPlaylistSongsRequest getSongsRequest = new GetPlaylistSongsRequest()
            .withId(playlistId);
        GetPlaylistSongsResult getSongsResult = musicPlaylistServiceClient.getPlaylistSongs(getSongsRequest);

        // THEN
        // The add returned the single song in the playlist
        validateAddSongToPlaylistSongs(musicPlaylistServiceClient, addSongRequest, addSongResult, expectedSongs);
        validateGetPlaylistSongs(getSongsRequest, getSongsResult, expectedSongs);
    }


    @Test
    public void masteryTaskFour_addAndGetSongsToExistingPlaylist_returnsSongs() throws Exception {
        // GIVEN
        // An existing but empty playlist
        String playlistId = createEmptyPlaylist("MT4" + UUID.randomUUID().toString(), musicPlaylistServiceClient);
        // 2 songs known to the album DAO
        SongModel expectedSong1 = PlaylistTestHelper.AVAILABLE_TRACKS.get(0);
        SongModel expectedSong2 = PlaylistTestHelper.AVAILABLE_TRACKS.get(1);
        List<SongModel> expectedSongs = ImmutableList.of(expectedSong1, expectedSong2);

        // WHEN
        // We add two songs to the playlist
        AddSongToPlaylistRequest addSongRequest1 = new AddSongToPlaylistRequest()
            .withId(playlistId)
            .withAsin(expectedSong1.getAsin())
            .withTrackNumber(expectedSong1.getTrackNumber());
        musicPlaylistServiceClient.addSongToPlaylist(addSongRequest1);

        AddSongToPlaylistRequest addSongRequest2 = new AddSongToPlaylistRequest()
            .withId(playlistId)
            .withAsin(expectedSong2.getAsin())
            .withTrackNumber(expectedSong2.getTrackNumber());
        AddSongToPlaylistResult addSongResult2 = musicPlaylistServiceClient.addSongToPlaylist(addSongRequest2);


        // and retrieve the song list
        GetPlaylistSongsRequest getSongsRequest = new GetPlaylistSongsRequest()
            .withId(playlistId);
        GetPlaylistSongsResult getSongsResult = musicPlaylistServiceClient.getPlaylistSongs(getSongsRequest);

        // THEN
        // The second add returned the second song to the playlist
        validateAddSongToPlaylistSongs(musicPlaylistServiceClient, addSongRequest2, addSongResult2, expectedSongs);
        validateGetPlaylistSongs(getSongsRequest, getSongsResult, expectedSongs);
    }
}
