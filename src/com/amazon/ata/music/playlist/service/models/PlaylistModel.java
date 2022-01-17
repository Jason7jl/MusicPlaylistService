package com.amazon.ata.music.playlist.service.models;

import java.util.List;
import java.util.Objects;

public class PlaylistModel {
    private String id;
    private String name;
    private String customerId;
    private int songCount;
    private List<String> tags;

    public PlaylistModel() {

    }

    public PlaylistModel(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.customerId = builder.customerId;
        this.songCount = builder.songCount;
        this.tags = builder.tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getSongCount() {
        return songCount;
    }

    public void setSongCount(int songCount) {
        this.songCount = songCount;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistModel that = (PlaylistModel) o;
        return songCount == that.songCount &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(customerId, that.customerId) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, customerId, songCount, tags);
    }

    @Override
    public String toString() {
        return "PlaylistModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", customerId='" + customerId + '\'' +
                ", songCount=" + songCount +
                ", tags=" + tags +
                '}';
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private String id;
        private String name;
        private String customerId;
        private int songCount;
        private List<String> tags;

        public Builder withId(String idToUse) {
            this.id = idToUse;
            return this;
        }

        public Builder withName(String nameToUse) {
            this.name = nameToUse;
            return this;
        }

        public Builder withCustomerId(String customerIdToUse) {
            this.customerId = customerIdToUse;
            return this;
        }

        public Builder withSongCount(int songCountToUse) {
            this.songCount = songCountToUse;
            return this;
        }

        public Builder withTags(List<String> tagsToUse) {
            this.tags = tagsToUse;
            return this;
        }

        public PlaylistModel build() {return new PlaylistModel(this);}
    }
}
