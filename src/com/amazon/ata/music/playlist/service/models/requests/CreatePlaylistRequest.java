package com.amazon.ata.music.playlist.service.models.requests;

import java.util.List;
import java.util.Objects;

public class CreatePlaylistRequest {
    private String name;
    private String customerId;
    private List<String> tags;

    public CreatePlaylistRequest(String name, String customerId, List<String> tags) {
        this.name = name;
        this.customerId = customerId;
        this.tags = tags;
    }

    public CreatePlaylistRequest() {
    }

    public CreatePlaylistRequest(Builder builder) {
        this.name = builder.name;
        this.customerId = builder.customerId;
        this.tags = builder.tags;
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
        CreatePlaylistRequest that = (CreatePlaylistRequest) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(customerId, that.customerId) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, customerId, tags);
    }

    @Override
    public String toString() {
        return "CreatePlaylistRequest{" +
                "name='" + name + '\'' +
                ", customerId='" + customerId + '\'' +
                ", tags=" + tags +
                '}';
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private String name;
        private String customerId;
        private List<String> tags;

        private Builder() {

        }

        public Builder withName(String nameToUse) {
            this.name = nameToUse;
            return this;
        }

        public Builder withCustomerId(String customerIdToUse) {
            this.customerId = customerIdToUse;
            return this;
        }

        public Builder withTags(List<String> tagsToUse) {
            this.tags = tagsToUse;
            return this;
        }

        public CreatePlaylistRequest build() { return new CreatePlaylistRequest(this); }
    }
}
