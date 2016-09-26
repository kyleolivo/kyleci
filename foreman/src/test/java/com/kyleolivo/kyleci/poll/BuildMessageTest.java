package com.kyleolivo.kyleci.poll;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BuildMessageTest {

    @Test
    public void correctConstruction() {
        BuildMessage buildMessage = new BuildMessage("http://google.com");

        assertEquals("http://google.com", buildMessage.getRepoUrl().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidRepoUrl() {
        new BuildMessage("http://${invalidRepoUrl}");
    }

    @Test(expected = NullPointerException.class)
    public void nullRepoUrl() {
        new BuildMessage(null);
    }

}