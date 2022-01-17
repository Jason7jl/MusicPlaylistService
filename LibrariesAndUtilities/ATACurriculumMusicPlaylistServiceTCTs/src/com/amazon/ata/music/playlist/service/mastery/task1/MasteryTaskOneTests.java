package com.amazon.ata.music.playlist.service.mastery.task1;

import com.amazon.ata.music.playlist.service.helpers.MusicPlaylistIntegrationTestBase;

import com.amazonaws.services.atacurriculummusicplaylistservice.model.CreatePlaylistRequest;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.CreatePlaylistResult;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.GetPlaylistRequest;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.GetPlaylistResult;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.PlaylistModel;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Instant;

import static com.amazon.ata.music.playlist.service.helpers.PlaylistAssertions.assertCreatePlaylistResultMatchesRequest;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class MasteryTaskOneTests extends MusicPlaylistIntegrationTestBase {

    @BeforeClass
    public void setup() throws Exception {
        super.setup();
    }

    @Test
    public void masteryTaskOne_createAndGetNewPlaylist_returnsPlaylist() {
        // GIVEN a newly created playlist
        CreatePlaylistRequest createPlaylistRequest = new CreatePlaylistRequest()
            .withName("MT01_GetPlaylistTest_" + Instant.now())
            .withCustomerId("TCT Customer")
            .withTags("TCT tag", "TCT tag 2");

        CreatePlaylistResult result = musicPlaylistServiceClient.createPlaylist(createPlaylistRequest);
        PlaylistModel expectedPlaylist = assertCreatePlaylistResultMatchesRequest(createPlaylistRequest, result);

        // WHEN we call GetPlaylist to retrieve it
        GetPlaylistRequest getPlaylistRequest = new GetPlaylistRequest()
            .withId(expectedPlaylist.getId());

        GetPlaylistResult getPlaylistResult = musicPlaylistServiceClient.getPlaylist(getPlaylistRequest);
        PlaylistModel actualPlaylist = getPlaylistResult.getPlaylist();

        // THEN the result of GetPlaylist matches our created playlist
        assertEquals(actualPlaylist.getId(), expectedPlaylist.getId(),
            String.format("Created a playlist with ID [%s], but result of GetPlaylist returned ID [%s]",
                expectedPlaylist.getId(), actualPlaylist.getId()));

        assertEquals(actualPlaylist.getName(), expectedPlaylist.getName(),
            String.format("Created a playlist with name [%s], but result of GetPlaylist returned name [%s]",
                expectedPlaylist.getName(), actualPlaylist.getName()));

        assertEquals(actualPlaylist.getCustomerId(), expectedPlaylist.getCustomerId(),
            String.format("Created a playlist with customer ID [%s], but result of GetPlaylist returned" +
                    " customer ID [%s]",
                expectedPlaylist.getCustomerId(), actualPlaylist.getCustomerId()));

        assertEquals(actualPlaylist.getSongCount(), expectedPlaylist.getSongCount(),
            String.format("Created a playlist with songCount [%s], but result of GetPlaylist returned songCount [%s]",
                expectedPlaylist.getId(), actualPlaylist.getId()));

        assertEquals(actualPlaylist.getTags().size(), expectedPlaylist.getTags().size(),
            String.format("Created a playlist with tags [%s], but result of GetPlaylist " +
                    "returns a different amount of tags [%s]",
                expectedPlaylist.getTags(), actualPlaylist.getTags()));

        assertTrue(actualPlaylist.getTags().containsAll(expectedPlaylist.getTags()),
            String.format("Created a playlist with tags [%s], but result of GetPlaylist returns different tags [%s]",
                expectedPlaylist.getTags(), actualPlaylist.getTags()));
    }
}
