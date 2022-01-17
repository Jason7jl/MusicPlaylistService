package com.amazon.ata.music.playlist.service.models.requests;

import java.util.Objects;

public class UpdatePlaylistRequest {
    private String id;
    private String name;
    private String customerId;

    public UpdatePlaylistRequest() {
    }

    public UpdatePlaylistRequest(String id, String name, String customerId) {
        this.id = id;
        this.name = name;
        this.customerId = customerId;
    }

    public UpdatePlaylistRequest(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.customerId = builder.customerId;
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

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdatePlaylistRequest that = (UpdatePlaylistRequest) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, customerId);
    }

    @Override
    public String toString() {
        return "UpdatePlaylistRequest{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", customerId='" + customerId + '\'' +
                '}';
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private String id;
        private String name;
        private String customerId;

        private Builder() {

        }

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

        public UpdatePlaylistRequest build() { return new UpdatePlaylistRequest(this); }
    }
}
