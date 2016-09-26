package com.kyleolivo.kyleci.poll;

import com.kyleolivo.kyleci.BuildRequest;
import com.kyleolivo.kyleci.BuildService;
import org.apache.commons.lang3.Validate;

public class BuildMessageHandler {

    private final BuildService buildService;

    public BuildMessageHandler(BuildService buildService) {
        this.buildService = Validate.notNull(buildService, "null build service");
    }

    public void handle(BuildMessage message) {
        BuildRequest buildRequest = new BuildRequest(message.getRepoUrl());

        buildService.perform(buildRequest);
    }

}
