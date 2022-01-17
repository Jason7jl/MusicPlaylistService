package com.amazon.ata.music.playlist.service.models.requests;

import java.util.Objects;

public class GetPlaylistRequest {
    private String id;

    public GetPlaylistRequest() {

    }

    public GetPlaylistRequest(Builder builder) {
        this.id = builder.id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetPlaylistRequest that = (GetPlaylistRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "GetPlaylistRequest{" +
                "id='" + id + '\'' +
                '}';
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private String id;

        private Builder() {

        }

        public Builder withId(String idToUse) {
            this.id = idToUse;
            return this;
        }

        public GetPlaylistRequest build() { return new GetPlaylistRequest(this); }
    }
}
