package com.amazon.ata.music.playlist.service.activity;

import com.amazon.ata.music.playlist.service.converters.ModelConverter;
import com.amazon.ata.music.playlist.service.dynamodb.models.AlbumTrack;
import com.amazon.ata.music.playlist.service.dynamodb.models.Playlist;
import com.amazon.ata.music.playlist.service.exceptions.AlbumTrackNotFoundException;
import com.amazon.ata.music.playlist.service.exceptions.InvalidAttributeChangeException;
import com.amazon.ata.music.playlist.service.exceptions.PlaylistNotFoundException;
import com.amazon.ata.music.playlist.service.models.requests.AddSongToPlaylistRequest;
import com.amazon.ata.music.playlist.service.models.results.AddSongToPlaylistResult;
import com.amazon.ata.music.playlist.service.models.SongModel;
import com.amazon.ata.music.playlist.service.dynamodb.AlbumTrackDao;
import com.amazon.ata.music.playlist.service.dynamodb.PlaylistDao;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of the AddSongToPlaylistActivity for the MusicPlaylistService's AddSongToPlaylist API.
 * <p>
 * This API allows the customer to add a song to their existing playlist.
 */
public class AddSongToPlaylistActivity implements RequestHandler<AddSongToPlaylistRequest, AddSongToPlaylistResult> {
    private final Logger log = LogManager.getLogger();
    private final PlaylistDao playlistDao;
    private final AlbumTrackDao albumTrackDao;
    private final ModelConverter modelConverter = new ModelConverter();


    /**
     * Instantiates a new AddSongToPlaylistActivity object.
     *
     * @param playlistDao   PlaylistDao to access the playlist table.
     * @param albumTrackDao AlbumTrackDao to access the album_track table.
     */
    public AddSongToPlaylistActivity(PlaylistDao playlistDao, AlbumTrackDao albumTrackDao) {
        this.playlistDao = playlistDao;
        this.albumTrackDao = albumTrackDao;
    }

    /**
     * This method handles the incoming request by adding an additional song
     * to a playlist and persisting the updated playlist.
     * <p>
     * It then returns the updated song list of the playlist.
     * <p>
     * If the playlist does not exist, this should throw a PlaylistNotFoundException.
     * <p>
     * If the album track does not exist, this should throw an AlbumTrackNotFoundException.
     *
     * @param addSongToPlaylistRequest request object containing the playlist ID and an asin and track number
     *                                 to retrieve the song data
     * @return addSongToPlaylistResult result object containing the playlist's updated list of
     * API defined {@link SongModel}s
     */
    @Override
    public AddSongToPlaylistResult handleRequest(final AddSongToPlaylistRequest addSongToPlaylistRequest, Context context) {
        log.info("Received AddSongToPlaylistRequest {} ", addSongToPlaylistRequest);

        if (addSongToPlaylistRequest.getId() == null || addSongToPlaylistRequest.getAsin() == null) {
            throw new PlaylistNotFoundException();
        }

        Playlist playlist = playlistDao.getPlaylist(addSongToPlaylistRequest.getId());

        if (!addSongToPlaylistRequest.getId().equals(playlist.getId())) {
            throw new InvalidAttributeChangeException("Your playlist doesn't exist! Please, checks a playlistID.");
        }

        AlbumTrack albumTrack = albumTrackDao.getAlbumTrack(addSongToPlaylistRequest.getAsin()
                , addSongToPlaylistRequest.getTrackNumber());

        List<AlbumTrack> songList = playlist.getSongList();

        if (addSongToPlaylistRequest.isQueueNext()) {
            songList.add(0, albumTrack);
            playlist.setSongCount(playlist.getSongCount() + 1);
        } else {
            songList.add(albumTrack);
            playlist.setSongCount(playlist.getSongCount() + 1);
        }

        playlist.setSongList(songList);

        playlistDao.savePlaylist(playlist);

        return AddSongToPlaylistResult.builder()
                .withSongList(modelConverter.toSongModelList(songList))
                .build();
    }
}
