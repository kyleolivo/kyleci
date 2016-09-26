package com.kyleolivo.kyleci.poll;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URI;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BuildPollerTest {

    @Mock AmazonSQS sqs;
    @Mock BuildMessageHandler messageHandler;

    BuildPoller poller;
    URI queueUrl;
    ObjectMapper objectMapper;
    ArgumentCaptor<BuildMessage> buildMessageCaptor;
    ArgumentCaptor<ReceiveMessageRequest> receiveMessageCaptor;

    @Before
    public void setUp() {
        receiveMessageCaptor = ArgumentCaptor.forClass(ReceiveMessageRequest.class);
        buildMessageCaptor = ArgumentCaptor.forClass(BuildMessage.class);
        queueUrl = URI.create("http://google.com");
        objectMapper = new ObjectMapper();
        poller = new BuildPoller(sqs, queueUrl, messageHandler, objectMapper);
    }

    @Test
    public void run() throws Exception {
        ReceiveMessageResult response = new ReceiveMessageResult()
                .withMessages(msg("http://repoUrl0.com", "receipt0"), msg("http://repoUrl1.com", "receipt1"));
        when(sqs.receiveMessage(any(ReceiveMessageRequest.class))).thenReturn(response);

        poller.run();

        verify(sqs).receiveMessage(receiveMessageCaptor.capture());
        assertEquals("http://google.com", receiveMessageCaptor.getValue().getQueueUrl());

        verify(messageHandler, times(2)).handle(buildMessageCaptor.capture());
        List<BuildMessage> messages = buildMessageCaptor.getAllValues();
        assertEquals("http://repoUrl0.com", messages.get(0).getRepoUrl().toString());
        assertEquals("http://repoUrl1.com", messages.get(1).getRepoUrl().toString());

        verify(sqs).deleteMessage(new DeleteMessageRequest("http://google.com", "receipt0"));
        verify(sqs).deleteMessage(new DeleteMessageRequest("http://google.com", "receipt1"));
    }

    @Test(expected = AmazonSQSException.class)
    public void failureToReceive() throws Exception {
        AmazonSQSException sqsException = new AmazonSQSException("message");
        when(sqs.receiveMessage(any(ReceiveMessageRequest.class))).thenThrow(sqsException);

        poller.run();
    }

    @Test(expected = AmazonSQSException.class)
    public void failureToDelete() throws Exception {
        ReceiveMessageResult response = new ReceiveMessageResult()
                .withMessages(msg("http://repoUrl0.com", "receipt0"), msg("http://repoUrl1.com", "receipt1"));
        when(sqs.receiveMessage(any(ReceiveMessageRequest.class))).thenReturn(response);

        AmazonSQSException sqsException = new AmazonSQSException("message");
        when(sqs.deleteMessage(any(DeleteMessageRequest.class))).thenThrow(sqsException);

        poller.run();
    }

    @Test
    public void failureToParseBody() throws Exception {
        ReceiveMessageResult response = new ReceiveMessageResult()
                .withMessages(new Message().withBody("malformedJson"));
        when(sqs.receiveMessage(any(ReceiveMessageRequest.class))).thenReturn(response);

        try {
            poller.run();
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof JsonParseException);
        }
    }

    private Message msg(String repoUrl, String receipt) throws JsonProcessingException {
        BuildMessage message = new BuildMessage(repoUrl);
        String json = objectMapper.writeValueAsString(message);

        return new Message()
                .withBody(json)
                .withReceiptHandle(receipt);
    }

}