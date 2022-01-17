package com.amazon.ata.music.playlist.service.preparedness.task3;

import com.amazon.ata.music.playlist.service.helpers.MusicPlaylistIntegrationTestBase;

import com.amazonaws.services.atacurriculummusicplaylistservice.model.GetPlaylistRequest;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.GetPlaylistResult;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.PlaylistModel;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class PreparednessTaskThreeTests extends MusicPlaylistIntegrationTestBase {
    private static final String PLAYLIST_ID = "PPT03";

    @BeforeClass
    public void setup() throws Exception {
        super.setup();
    }

    @Test
    public void preparednessTask3_getPlaylistWithIdPPT03_returnsPlaylistWithId() {
        // GIVEN
        GetPlaylistRequest request = new GetPlaylistRequest()
                .withId(PLAYLIST_ID);
        GetPlaylistResult result = musicPlaylistServiceClient.getPlaylist(request);

        // WHEN
        PlaylistModel playlist = result.getPlaylist();

        // THEN
        // We don't expect participants to have added other fields to the DynamoDB model at this point
        assertEquals(playlist.getId(), PLAYLIST_ID,
                "Expected DynamoDB playlists table to contain a playlist with id: " + PLAYLIST_ID);
    }
}
