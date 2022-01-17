package com.amazon.ata.music.playlist.service.helpers;

import com.amazonaws.services.atacurriculummusicplaylistservice.ATACurriculumMusicPlaylistService;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.CreatePlaylistRequest;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.CreatePlaylistResult;
import com.amazonaws.services.atacurriculummusicplaylistservice.model.SongModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.amazon.ata.music.playlist.service.helpers.PlaylistAssertions.assertCreatePlaylistResultMatchesRequest;

public class PlaylistTestHelper {
    public static final List<SongModel> AVAILABLE_TRACKS = Collections.unmodifiableList(Arrays.asList(
        // 0
        new SongModel().withAsin("B00000000")
            .withTrackNumber(1)
            .withAlbum("ATA Smooth Jams")
            .withTitle("firstNameLast"),
        // 1
        new SongModel().withAsin("B00000000")
            .withTrackNumber(2)
            .withAlbum("ATA Smooth Jams")
            .withTitle("spiderman"),
        // 2
        new SongModel().withAsin("B00000000")
            .withTrackNumber(3)
            .withAlbum("ATA Smooth Jams")
            .withTitle("curveballs"),
        // 3
        new SongModel().withAsin("B00000000")
            .withTrackNumber(4)
            .withAlbum("ATA Smooth Jams")
            .withTitle("whereImFrom"),
        // 4
        new SongModel().withAsin("B00000000")
            .withTrackNumber(5)
            .withAlbum("ATA Smooth Jams")
            .withTitle("wahoo"),
        // 5
        new SongModel().withAsin("B000LP5FSG")
            .withTrackNumber(2)
            .withAlbum("WINCING THE NIGHT AWAY")
            .withTitle("Australia"),
        // 6
        new SongModel().withAsin("B004CAWKXE")
            .withTrackNumber(2)
            .withAlbum("Danger Days: The True Lives of the Fabulous Killjoys")
            .withTitle("Na Na Na (Na Na Na Na Na Na Na Na Na)"),
        // 7
        new SongModel().withAsin("B019HKJTCI")
            .withTrackNumber(6)
            .withAlbum("Money")
            .withTitle("Dark Side of the Moon"),
        // 8
        new SongModel().withAsin("B07NJ3H27X")
            .withTrackNumber(1)
            .withAlbum("Cuz I Love You")
            .withTitle("Cuz I Love You"),
        // 9
        new SongModel().withAsin("B07NJ3H27X")
            .withTrackNumber(7)
            .withAlbum("Cuz I Love You")
            .withTitle("Tempo"),
        // 10
        new SongModel().withAsin("B07VSG6V59")
            .withTrackNumber(2)
            .withAlbum("All Mirrors")
            .withTitle("All Mirrors")
    ));

    /**
     * Private constructor to avoid instantiating this utility class.
     */
    private PlaylistTestHelper() {
    }

    /**
     * Utility method to create an empty playlist with a given name. Allows clean setup and no teardown.
     * Fails an assertion if the playlist is not created.
     * @param name The name of the playlist to create.
     * @param client The ATACurriculumMusicPlaylistServiceClient to call.
     * @return The ID of the newly created playlist.
     */
    public static String createEmptyPlaylist(String name, ATACurriculumMusicPlaylistService client) {
        CreatePlaylistRequest createPlaylistRequest = new CreatePlaylistRequest()
            .withName(name)
            .withCustomerId("TCT Customer ID")
            .withTags("TCT tag", "TCT tag 2");
        CreatePlaylistResult result = client.createPlaylist(createPlaylistRequest);
        assertCreatePlaylistResultMatchesRequest(createPlaylistRequest, result);
        return result.getPlaylist().getId();
    }
}
