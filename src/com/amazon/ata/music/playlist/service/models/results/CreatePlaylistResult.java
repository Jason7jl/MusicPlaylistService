package com.amazon.ata.music.playlist.service.models.results;

import com.amazon.ata.music.playlist.service.models.PlaylistModel;

public class CreatePlaylistResult {
    private PlaylistModel playlist;

    public CreatePlaylistResult(Builder builder) {
        this.playlist = builder.playlist;
    }

    public PlaylistModel getPlaylist() {
        return playlist;
    }

    public void setPlaylist(PlaylistModel playlist) {
        this.playlist = playlist;
    }

    public static Builder builder() {return new Builder();}

    public static final class Builder {
        private PlaylistModel playlist;

        public Builder withPlaylist(PlaylistModel playlist) {
            this.playlist = playlist;
            return this;
        }

        public CreatePlaylistResult build() {return new CreatePlaylistResult(this);}
    }
}
