package com.kyleolivo.kyleci;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.URI;

import static org.junit.Assert.*;

public class BuildRequestTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void correctConstruction() throws Exception {
        URI repoURL = new URI("http://www.google.com");
        BuildRequest buildRequest = new BuildRequest(repoURL);
        assertEquals(repoURL, buildRequest.getRepoURL());
    }

    @Test
    public void nullRepoURL() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("null repoURL");

        new BuildRequest(null);
    }
}