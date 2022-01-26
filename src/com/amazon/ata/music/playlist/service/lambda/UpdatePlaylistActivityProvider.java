package com.amazon.ata.music.playlist.service.lambda;

import com.amazon.ata.music.playlist.service.activity.UpdatePlaylistActivity;
import com.amazon.ata.music.playlist.service.dependency.DaggerServiceComponent;
import com.amazon.ata.music.playlist.service.dependency.ServiceComponent;
import com.amazon.ata.music.playlist.service.models.requests.UpdatePlaylistRequest;
import com.amazon.ata.music.playlist.service.models.results.UpdatePlaylistResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class UpdatePlaylistActivityProvider implements RequestHandler<UpdatePlaylistRequest, UpdatePlaylistResult> {



    ServiceComponent dagger = DaggerServiceComponent.create();
    UpdatePlaylistActivity updatePlaylistActivity = dagger.provideUpdatePlaylistActivity();

    public UpdatePlaylistActivityProvider() {

    }

    @Override
    public UpdatePlaylistResult handleRequest(final UpdatePlaylistRequest updatePlaylistRequest, Context context) {
        return updatePlaylistActivity.handleRequest(updatePlaylistRequest, context);
    }


}
