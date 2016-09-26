package com.kyleolivo.kyleci.poll;

import com.kyleolivo.kyleci.BuildRequest;
import com.kyleolivo.kyleci.BuildService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BuildMessageHandlerTest {

    @Mock BuildService buildService;

    BuildMessageHandler handler;

    @Before
    public void setUp() {
        handler = new BuildMessageHandler(buildService);
    }

    @Test
    public void handle() {
        handler.handle(new BuildMessage("repoUrl"));

        ArgumentCaptor<BuildRequest> requestCaptor = ArgumentCaptor.forClass(BuildRequest.class);
        verify(buildService).perform(requestCaptor.capture());

        assertEquals("repoUrl", requestCaptor.getValue().getRepoURL().toString());
    }

}
