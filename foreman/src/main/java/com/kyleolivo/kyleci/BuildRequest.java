package com.kyleolivo.kyleci;

import org.apache.commons.lang3.Validate;

import java.net.URI;

public class BuildRequest {
    private final URI repoURL;

    public BuildRequest(URI repoURL) {
        this.repoURL = Validate.notNull(repoURL, "null repoURL");
    }

    public URI getRepoURL() {
        return repoURL;
    }
}
