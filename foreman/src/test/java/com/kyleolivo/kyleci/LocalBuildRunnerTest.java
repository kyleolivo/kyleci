package com.kyleolivo.kyleci;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.File;

import static org.mockito.Mockito.mock;

public class LocalBuildRunnerTest {
    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Mock
    File buildScript;

    @Test
    public void name() throws Exception {
        LocalBuildRunner buildRunner = new LocalBuildRunner(buildScript);

        buildRunner.run();


    }
}