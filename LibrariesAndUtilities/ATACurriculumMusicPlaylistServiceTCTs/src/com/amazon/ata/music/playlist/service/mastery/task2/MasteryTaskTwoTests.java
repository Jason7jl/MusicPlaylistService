package com.amazon.ata.music.playlist.service.mastery.task2;

import com.amazon.ata.music.playlist.service.helpers.MusicPlaylistIntegrationTestBase;

import com.amazonaws.services.atacurriculummusicplaylistservice.model.CreatePlaylistRequest;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.CreatePlaylistResult;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.GetPlaylistRequest;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.GetPlaylistResult;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.MusicPlaylistClientException;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.MusicPlaylistServiceException;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.PlaylistModel;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.UpdatePlaylistRequest;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.UpdatePlaylistResult;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Instant;

import static com.amazon.ata.music.playlist.service.helpers.PlaylistAssertions.assertCreatePlaylistResultMatchesRequest;
import static com.amazon.ata.music.playlist.service.helpers.PlaylistAssertions.validateNonNullPlaylist;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class MasteryTaskTwoTests extends MusicPlaylistIntegrationTestBase {
    private PlaylistModel originalPlaylist;

    @BeforeClass
    public void setup() throws Exception {
        super.setup();
    }

    @BeforeMethod
    public void setupTest() {
        String playlistName = "MT02_UpdatePlaylistTest_" + Instant.now();
        CreatePlaylistRequest createPlaylistRequest = new CreatePlaylistRequest()
            .withName(playlistName)
            .withCustomerId("TCT Customer ID")
            .withTags("TCT tag", "TCT tag 2");

        CreatePlaylistResult createPlaylistResult = musicPlaylistServiceClient.createPlaylist(createPlaylistRequest);
        originalPlaylist = assertCreatePlaylistResultMatchesRequest(createPlaylistRequest, createPlaylistResult);

        System.out.println("Executing UpdatePlaylist test with a new playlist with ID: " + originalPlaylist.getId());
    }

    @Test
    public void masteryTaskTwo_updatePlaylist_updatesPlaylistName() {
        // GIVEN
        String updatedName = "MT02_UpdatePlaylistTest_AfterUpdate_" + Instant.now();
        UpdatePlaylistRequest updatePlaylistRequest = new UpdatePlaylistRequest()
            .withId(originalPlaylist.getId())
            .withName(updatedName)
            .withCustomerId(originalPlaylist.getCustomerId());

        // WHEN we update it
        UpdatePlaylistResult updatePlaylistResult = musicPlaylistServiceClient.updatePlaylist(updatePlaylistRequest);
        PlaylistModel updatedPlaylist = validateNonNullPlaylist(updatePlaylistResult.getPlaylist());

        // THEN the UpdatePlaylist API result and DynamoDB should match the updated playlist
        assertUpdatedPlaylist(updatedPlaylist);

        PlaylistModel updatedPlaylistFromGet = getPlaylist();
        assertUpdatedPlaylist(updatedPlaylistFromGet);
    }

    @Test
    public void masteryTaskTwo_updatePlaylistWithDifferentCustomerId_doesNotUpdatePlaylist() {
        // GIVEN an UpdatePlaylist request with a different customer ID than the created one
        String updatedName = "MT02_UpdatePlaylistTest_AfterUpdate_" + Instant.now();
        UpdatePlaylistRequest updatePlaylistRequest = new UpdatePlaylistRequest()
            .withId(originalPlaylist.getId())
            .withName(updatedName)
            .withCustomerId("Malicious Customer ID");

        // WHEN we try to update it
        try {
            musicPlaylistServiceClient.updatePlaylist(updatePlaylistRequest);

            fail("Expected attempt to update playlist with a different customer ID to throw an exception.");
        } catch (MusicPlaylistClientException e) {
            // Got the client exception, looks good
            // double check the customer ID has not updated

            PlaylistModel updatedPlaylist = getPlaylist();
            assertEquals(updatedPlaylist.getCustomerId(), originalPlaylist.getCustomerId(),
                "Invalid update unexpectedly changed playlist's customer ID!");
        } catch (MusicPlaylistServiceException e) {
            fail(String.format("Received unexpected exception with a [%s] status code when trying to update a " +
                "playlist with a different customer ID. Does your exception hierarchy match your design " +
                    "and does the parent exception extend Exception or RuntimeException?",
                e.getStatusCode()), e);
        }
    }

    @Test
    public void masteryTaskTwo_updatePlaylistWithInvalidName_doesNotUpdatePlaylist() {
        // GIVEN a UpdatePlaylist request with a name with invalid characters [',",\]
        UpdatePlaylistRequest updatePlaylistRequest = new UpdatePlaylistRequest()
            .withId(originalPlaylist.getId())
            .withName("invalid'playlist\"name\\")
            .withCustomerId("TCT Customer ID");

        // WHEN we attempt to update it it
        try {
            UpdatePlaylistResult updatePlaylistResult =
                musicPlaylistServiceClient.updatePlaylist(updatePlaylistRequest);
            PlaylistModel updatedPlaylist = validateNonNullPlaylist(updatePlaylistResult.getPlaylist());

            fail(String.format("Expected attempt to update a playlist with an " +
                    "invalid playlist name: '%s' to throw an exception.",
                updatedPlaylist.getName()));
            // THEN we expect to get a 400 response from the service
        } catch (MusicPlaylistClientException e) {
            // Got the client exception, test passes
            // double check the playlist name has not updated

            PlaylistModel updatedPlaylist = getPlaylist();
            assertEquals(updatedPlaylist.getName(), originalPlaylist.getName(),
                "Invalid update unexpectedly changed playlist's name to contain invalid characters!");
        } catch (MusicPlaylistServiceException e) {
            fail(String.format("Received unexpected exception with a [%s] status code when trying to update a " +
                "playlist with an invalid name. Does your exception hierarchy match your design and " +
                    "does the parent exception extend Exception or RuntimeException?",
                e.getStatusCode()), e);
        }
    }

    private PlaylistModel getPlaylist() {
        GetPlaylistRequest getPlaylistRequest = new GetPlaylistRequest()
            .withId(originalPlaylist.getId());

        GetPlaylistResult getPlaylistResult = musicPlaylistServiceClient.getPlaylist(getPlaylistRequest);
        return validateNonNullPlaylist(getPlaylistResult.getPlaylist());
    }

    private void assertUpdatedPlaylist(PlaylistModel updatedPlaylist) {
        // we should allow changing the name
        assertNotEquals(originalPlaylist.getName(), updatedPlaylist.getName(),
            "Expected the UpdatePlaylist API to update the playlist's name.");

        // but we should not update any other fields
        assertEquals(updatedPlaylist.getId(), originalPlaylist.getId(),
            "Expected the UpdatePlaylist API to return the same ID as the created playlist.");

        assertEquals(updatedPlaylist.getCustomerId(), originalPlaylist.getCustomerId(),
            "Expected the UpdatePlaylist API to return the same customerID that was in the create request");

        assertEquals(updatedPlaylist.getTags().size(), originalPlaylist.getTags().size(),
            String.format("Expected the UpdatePlaylist API to return the same count of tags that was in the create " +
                "request. %n Expected %s %n got %s", originalPlaylist.getTags(), updatedPlaylist.getTags()));

        assertTrue(updatedPlaylist.getTags().containsAll(originalPlaylist.getTags()),
            String.format("Expected the UpdatePlaylist API to return the same tags that were in the create request. " +
                "%n Expected %s %n got %s", originalPlaylist.getTags(), updatedPlaylist.getTags()));

        assertEquals(updatedPlaylist.getSongCount(), originalPlaylist.getSongCount(),
            "Expected the UpdatePlaylist API to return the same song count that was in the create request");
    }
}
