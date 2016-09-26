package com.kyleolivo.kyleci;

import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class BuildRequestTest {

    @Test
    public void correctConstruction() throws Exception {
        URI repoURL = new URI("http://www.google.com");
        BuildRequest buildRequest = new BuildRequest(repoURL);
        assertEquals(repoURL, buildRequest.getRepoURL());
    }

    @Test(expected = NullPointerException.class)
    public void nullRepoURL() throws Exception {
        new BuildRequest(null);
    }

}