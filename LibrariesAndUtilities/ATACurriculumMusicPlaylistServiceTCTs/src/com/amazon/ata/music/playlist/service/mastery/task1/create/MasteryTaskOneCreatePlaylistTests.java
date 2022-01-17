package com.amazon.ata.music.playlist.service.mastery.task1.create;

import com.amazon.ata.music.playlist.service.helpers.MusicPlaylistIntegrationTestBase;

import com.amazonaws.services.atacurriculummusicplaylistservice.model.CreatePlaylistRequest;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.CreatePlaylistResult;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.MusicPlaylistClientException;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.PlaylistModel;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Instant;
import java.util.Arrays;

import static com.amazon.ata.music.playlist.service.helpers.PlaylistAssertions.assertCreatePlaylistResultMatchesRequest;
import static com.amazon.ata.music.playlist.service.helpers.PlaylistAssertions.validateNonNullPlaylist;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class MasteryTaskOneCreatePlaylistTests extends MusicPlaylistIntegrationTestBase {
    private String playlistName;

    @BeforeClass
    public void setup() throws Exception {
        super.setup();
    }

    @BeforeMethod
    public void setupTest() {
        playlistName = "MT01_CreatePlaylistTest_" + Instant.now();
        // sysout because Hydra doesn't support log4j -> CloudFormation out of the box
        // https://tiny.amazon.com/1hfswiy1t/wamazbinviewHydrFAQ
        System.out.println("Executing CreatePlaylist test with playlistName " + playlistName);
    }

    @Test
    public void masteryTaskOne_createPlaylistWithValidInputAndTagsSupplied_succeeds() {
        // GIVEN a CreatePlaylist request
        CreatePlaylistRequest createPlaylistRequest = new CreatePlaylistRequest()
            .withName(playlistName)
            .withCustomerId("TCT Customer ID")
            .withTags("TCT tag", "TCT tag 2");

        // WHEN we call the CreatePlaylist API
        CreatePlaylistResult result = musicPlaylistServiceClient.createPlaylist(createPlaylistRequest);

        // THEN the CreatePlaylistResult should contain a playlist matching the fields in the request
        assertCreatePlaylistResultMatchesRequest(createPlaylistRequest, result);
    }

    @Test
    public void masteryTaskOne_createPlaylistWithNoTagsSupplied_succeeds() {
        // GIVEN
        CreatePlaylistRequest createPlaylistRequest = new CreatePlaylistRequest()
            .withName(playlistName)
            .withCustomerId("TCT Customer ID");

        // WHEN
        CreatePlaylistResult result = musicPlaylistServiceClient.createPlaylist(createPlaylistRequest);

        // THEN
        PlaylistModel playlist = assertCreatePlaylistResultMatchesRequest(createPlaylistRequest, result);

        // In case participant returns an empty set if the tags in the request were null
        if (playlist.getTags() != null) {
            assertTrue(playlist.getTags().isEmpty(),
                "Created a Playlist with empty tags, but received a non empty set of tags" +
                    Arrays.toString(playlist.getTags().toArray()));
        }
    }

    @Test
    public void masteryTaskOne_createPlaylistWithDuplicateTags_doesNotStoreDuplicateTags() {
        // GIVEN
        CreatePlaylistRequest createPlaylistRequest = new CreatePlaylistRequest()
            .withName(playlistName)
            .withCustomerId("TCT Customer ID")
            .withTags("TCT tag", "TCT tag", "TCT tag 2");

        // WHEN
        CreatePlaylistResult result = musicPlaylistServiceClient.createPlaylist(createPlaylistRequest);

        // THEN
        PlaylistModel playlist = validateNonNullPlaylist(result.getPlaylist());
        assertNotNull(playlist.getTags(), "Expected creating a playlist with tags to return a set of tags.");
        assertEquals(playlist.getTags().size(), 2,
            "Expected a playlist with duplicate tags to only save unique tags, but got " + playlist.getTags());
    }

    @Test
    public void masteryTaskOne_createPlaylistMultipleTimes_createsDistinctPlaylistIds() {
        // GIVEN
        CreatePlaylistRequest createPlaylistRequest = new CreatePlaylistRequest()
            .withName(playlistName)
            .withCustomerId("TCT Customer ID")
            .withTags("TCT tag", "TCT tag 2");

        // WHEN
        CreatePlaylistResult firstRequest = musicPlaylistServiceClient.createPlaylist(createPlaylistRequest);
        CreatePlaylistResult secondRequest = musicPlaylistServiceClient.createPlaylist(createPlaylistRequest);

        // THEN
        PlaylistModel firstPlaylist = validateNonNullPlaylist(firstRequest.getPlaylist());
        PlaylistModel secondPlaylist = validateNonNullPlaylist(secondRequest.getPlaylist());

        assertNotEquals(firstPlaylist.getId(), secondPlaylist.getId(),
            String.format("Created two playlists and received the same ID. First ID '%s', Second ID '%s'.",
                firstPlaylist.getId(), secondPlaylist.getId()));
    }

    @Test
    public void masteryTaskOne_createPlaylistWithInvalidName_failsToCreatePlaylist() {
        // GIVEN a CreatePlaylist request with a name with invalid characters [',",\]
        CreatePlaylistRequest createPlaylistRequest = new CreatePlaylistRequest()
            .withName("invalid'playlist\"name\\")
            .withCustomerId("TCT Customer ID");

        // WHEN we attempt to create it
        try {
            CreatePlaylistResult result = musicPlaylistServiceClient.createPlaylist(createPlaylistRequest);

            // in case they haven't implemented the error checking yet
            // we'll still check for completeness of the request
            PlaylistModel invalidPlaylist = assertCreatePlaylistResultMatchesRequest(createPlaylistRequest, result);

            // print a nicer error message, testng's expected exception prints the exception class which we don't
            // want participants to get sidetracked on
            fail(String.format("Created a playlist with an invalid playlist name: '%s'.",
                invalidPlaylist.getName()));
        // THEN we expect to get a 400 response from the service
        } catch (MusicPlaylistClientException e) {
            // Got the client exception, test passes
            // return because checkstyle doesn't allow empty blocks
            return;
        }
    }

    @Test
    public void masteryTaskOne_createPlaylistWithInvalidCustomerId_failsToCreatePlaylist() {
        // GIVEN a CreatePlaylist request with a customerId with invalid characters [',",\]
        CreatePlaylistRequest createPlaylistRequest = new CreatePlaylistRequest()
                                                          .withName("HappyList")
                                                          .withCustomerId("TCT\\'Customer' \"ID\"");

        // WHEN we attempt to create it
        try {
            CreatePlaylistResult result = musicPlaylistServiceClient.createPlaylist(createPlaylistRequest);

            // in case they haven't implemented the error checking yet
            // we'll still check for completeness of the request
            PlaylistModel invalidPlaylist = assertCreatePlaylistResultMatchesRequest(createPlaylistRequest, result);

            // print a nicer error message, testng's expected exception prints the exception class which we don't
            // want participants to get sidetracked on
            fail(String.format("Created a playlist with an invalid playlist customer ID: '%s'.",
                               invalidPlaylist.getCustomerId()));
            // THEN we expect to get a 400 response from the service
        } catch (MusicPlaylistClientException e) {
            // Got the client exception, test passes
            // return because checkstyle doesn't allow empty blocks
            return;
        }
    }
}
