package com.kyleolivo.kyleci.poll;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.Validate;

import java.net.URI;

public class BuildMessage {

    @JsonProperty("repoUrl")
    private final URI repoUrl;

    @JsonCreator
    public BuildMessage(@JsonProperty("repoUrl") String repoUrl) {
        this.repoUrl = URI.create(Validate.notNull(repoUrl, "null repo url"));
    }

    public URI getRepoUrl() {
        return repoUrl;
    }

}
