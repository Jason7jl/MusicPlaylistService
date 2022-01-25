package com.amazon.ata.music.playlist.service.converters;

import com.amazon.ata.music.playlist.service.dynamodb.models.AlbumTrack;
import com.amazon.ata.music.playlist.service.models.PlaylistModel;
import com.amazon.ata.music.playlist.service.dynamodb.models.Playlist;
import com.amazon.ata.music.playlist.service.models.SongModel;

import java.util.ArrayList;
import java.util.List;

public class ModelConverter {
    /**
     * Converts a provided {@link Playlist} into a {@link PlaylistModel} representation.
     * @param playlist the playlist to convert
     * @return the converted playlist
     */
    public PlaylistModel toPlaylistModel(Playlist playlist) {
        return PlaylistModel.builder()
                .withId(playlist.getId())
                .withName(playlist.getName())
                .withCustomerId(playlist.getCustomerId())
                .withSongCount(playlist.getSongCount())
                .withTags(playlist.getTags() != null ? new ArrayList<>(playlist.getTags()) : null)
            .build();
    }


    public SongModel toSongModel(AlbumTrack albumTrack) {

        return SongModel.builder()
                .withAlbum(albumTrack.getAlbumName())
                .withAsin(albumTrack.getAsin())
                .withTitle(albumTrack.getSongTitle())
                .withTrackNumber(albumTrack.getTrackNumber())
                .build();
    }

    public List<SongModel> toSongModelList(List<AlbumTrack> songList ) {
        List<SongModel> songModelList = new ArrayList<>();


        for (AlbumTrack albumTrack : songList) {

            songModelList.add(toSongModel(albumTrack));
    }


        return songModelList;
    }


}
