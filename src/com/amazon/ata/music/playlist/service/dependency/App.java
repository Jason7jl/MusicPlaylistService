package com.amazon.ata.music.playlist.service.dependency;

import com.amazon.ata.aws.dynamodb.DynamoDbClientProvider;
import com.amazon.ata.music.playlist.service.activity.AddSongToPlaylistActivity;
import com.amazon.ata.music.playlist.service.activity.CreatePlaylistActivity;
import com.amazon.ata.music.playlist.service.activity.GetPlaylistActivity;
import com.amazon.ata.music.playlist.service.activity.GetPlaylistSongsActivity;
import com.amazon.ata.music.playlist.service.activity.UpdatePlaylistActivity;
import com.amazon.ata.music.playlist.service.dynamodb.AlbumTrackDao;
import com.amazon.ata.music.playlist.service.dynamodb.PlaylistDao;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

/**
 * This class manages service dependencies.
 */
public class App {
    private DynamoDBMapper dynamoDBMapper;

    /**
     * Provides a new CreatePlaylistActivity with injected dependencies.
     *
     * @return createPlaylistActivity a new CreatePlaylistActivity with injected dependencies.
     */
    public CreatePlaylistActivity provideCreatePlaylistActivity() {
        return new CreatePlaylistActivity(providePlaylistDao());
    }

    /**
     * Provides a new GetPlaylistActivity with injected dependencies.
     *
     * @return getPlaylistActivity a new GetPlaylistActivity with injected dependencies.
     */
    public GetPlaylistActivity provideGetPlaylistActivity() {
        return new GetPlaylistActivity(providePlaylistDao());
    }

    /**
     * Provides a new UpdatePlaylistActivity with injected dependencies.
     *
     * @return updatePlaylistActivity a new UpdatePlaylistActivity with injected dependencies.
     */
    public UpdatePlaylistActivity provideUpdatePlaylistActivity() {
        return new UpdatePlaylistActivity(providePlaylistDao());
    }

    /**
     * Provides a new AddSongToPlaylistActivity with injected dependencies.
     *
     * @return addSongToPlaylistActivity a new AddSongToPlaylistActivity with injected dependencies.
     */
    public AddSongToPlaylistActivity provideAddSongToPlaylistActivity() {
        return new AddSongToPlaylistActivity(providePlaylistDao(), provideAlbumTrackDao());
    }

    /**
     * Provides a new GetPlaylistSongsActivity with injected dependencies.
     *
     * @return getPlaylistSongsActivity a new GetPlaylistSongsActivity with injected dependencies.
     */
    public GetPlaylistSongsActivity provideGetPlaylistSongsActivity() {
        return new GetPlaylistSongsActivity(providePlaylistDao());
    }

    private PlaylistDao providePlaylistDao() {
        return new PlaylistDao(provideDynamoDBMapper());
    }

    private AlbumTrackDao provideAlbumTrackDao() {
        return new AlbumTrackDao(provideDynamoDBMapper());
    }

    /**
     * Lazily provides a {@link DynamoDBMapper} singleton instance.
     *
     * @return a {@link DynamoDBMapper} instance
     */
    private DynamoDBMapper provideDynamoDBMapper() {
        if (null == dynamoDBMapper) {
            dynamoDBMapper = new DynamoDBMapper(DynamoDbClientProvider.getDynamoDBClient(Regions.US_WEST_2));
        }
        return dynamoDBMapper;
    }
}
