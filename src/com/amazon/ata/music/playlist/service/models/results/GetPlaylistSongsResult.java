package com.amazon.ata.music.playlist.service.models.results;

import com.amazon.ata.music.playlist.service.models.SongModel;

import java.util.List;

public class GetPlaylistSongsResult {
    private List<SongModel> songList;

    public GetPlaylistSongsResult(Builder builder) {
        this.songList = builder.songList;
    }

    public List<SongModel> getSongList() {
        return songList;
    }

    public void setSongList(List<SongModel> songList) {
        this.songList = songList;
    }

    public static Builder builder() {return new Builder();}

    public static final class Builder {
        private List<SongModel> songList;

        public Builder withSongList(List<SongModel> songListToUse) {
            this.songList = songListToUse;
            return this;
        }

        public GetPlaylistSongsResult build() {return new GetPlaylistSongsResult(this);}
    }
}
