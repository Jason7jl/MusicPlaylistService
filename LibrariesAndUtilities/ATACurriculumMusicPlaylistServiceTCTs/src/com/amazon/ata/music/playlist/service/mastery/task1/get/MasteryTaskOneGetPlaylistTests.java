package com.amazon.ata.music.playlist.service.mastery.task1.get;

import com.amazon.ata.music.playlist.service.helpers.MusicPlaylistIntegrationTestBase;

import com.amazonaws.services.atacurriculummusicplaylistservice.model.GetPlaylistRequest;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.GetPlaylistResult;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.PlaylistModel;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class MasteryTaskOneGetPlaylistTests extends MusicPlaylistIntegrationTestBase {
    private static final String PPT03_PLAYLIST_ID = "PPT03";
    private static final String PPT03_CUSTOMER_ID = "1";

    @BeforeClass
    public void setup() throws Exception {
        super.setup();
    }

    @Test
    public void masteryTaskOne_getPlaylistWithIdPPT03_playlistHasAllFields() {
        // GIVEN the ID of the playlist created in PPT03
        GetPlaylistRequest request = new GetPlaylistRequest()
            .withId(PPT03_PLAYLIST_ID);

        // WHEN we retrieve the playlist
        GetPlaylistResult result = musicPlaylistServiceClient.getPlaylist(request);
        PlaylistModel playlist = result.getPlaylist();

        // THEN the playlist should contain the fields provided in the preparedness task
        // We expect participants to have added the other fields to their DynamoDB models at this point
        assertWithMessage(playlist.getId(), PPT03_PLAYLIST_ID,
            "ID: '%s', was it deleted?", PPT03_PLAYLIST_ID);
        assertNotNull(playlist.getName(),
            "Expected the GetPlaylist API to return the playlist created in PPT03, but it had a null name. " +
                "Has the item been modified in DynamoDB?");
        assertWithMessage(playlist.getCustomerId(), PPT03_CUSTOMER_ID,
            "customer ID: '%s', has the item been modified in DynamoDB?", PPT03_CUSTOMER_ID);
        assertNotNull(playlist.getSongCount(),
            "Expected the GetPlaylist API to return the playlist created in PPT03, but it had a null songCount. " +
                "Has the item been modified in DynamoDB?");

        // in case participants map null tags to empty set
        if (playlist.getTags() != null) {
            assertTrue(playlist.getTags().isEmpty(),
                "Expected the GetPlaylist API to return the playlist " +
                    "created in PPT03 with no tags, but got" + Arrays.toString(playlist.getTags().toArray()));
        }
    }

    private void assertWithMessage(Object actual, Object expected, String msgSuffix, Object... formatArgs) {
        assertEquals(actual, expected,
            String.format(
                "Expected the GetPlaylist API to return the playlist created in PPT03 with " + msgSuffix,
                formatArgs));
    }
}
