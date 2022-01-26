package com.amazon.ata.music.playlist.service.dependency;

import com.amazon.ata.music.playlist.service.activity.*;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {DaoModule.class})
public interface ServiceComponent {
    CreatePlaylistActivity provideCreatePlaylistActivity();

    GetPlaylistActivity provideGetPlaylistActivity();

    UpdatePlaylistActivity provideUpdatePlaylistActivity();

    AddSongToPlaylistActivity provideAddSongToPlaylistActivity();

    GetPlaylistSongsActivity provideGetPlaylistSongsActivity();
}