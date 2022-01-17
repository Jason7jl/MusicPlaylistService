package com.amazon.ata.music.playlist.service.helpers;

import com.amazon.ata.integration.test.LambdaIntegrationTestBase;

import com.amazonaws.services.atacurriculummusicplaylistservice.ATACurriculumMusicPlaylistService;
import com.amazonaws.services.atacurriculummusicplaylistservice.ATACurriculumMusicPlaylistServiceClientBuilder;

/**
 * Configures the service client using the builder and the cloudformation stack name that should be used by all tests
 * for this project.
 */
public class MusicPlaylistIntegrationTestBase extends LambdaIntegrationTestBase {
    protected ATACurriculumMusicPlaylistService musicPlaylistServiceClient;

    /**
     * Configures the musicPlaylistServiceClient to be used in the test class.
     * @throws Exception when the client cannot be created
     */
    public void setup() throws Exception {
        this.musicPlaylistServiceClient = getServiceClient(ATACurriculumMusicPlaylistServiceClientBuilder.standard(),
                "ATACurriculumMusicPlaylistServiceLambda");
    }

}
