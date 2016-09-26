package com.kyleolivo.kyleci.poll;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class BuildPoller implements Runnable {

    private final AmazonSQS sqs;
    private final URI queueUrl;
    private final BuildMessageHandler messageHandler;
    private final ObjectMapper objectMapper;

    public BuildPoller(AmazonSQS sqs, URI queueUrl, BuildMessageHandler messageHandler, ObjectMapper objectMapper) {
        this.sqs = Validate.notNull(sqs, "null sqs");
        this.queueUrl = Validate.notNull(queueUrl, "null queue url");
        this.messageHandler = Validate.notNull(messageHandler, "null build service");
        this.objectMapper = Validate.notNull(objectMapper, "null object mapper");
    }

    @Override
    public void run() {
        ReceiveMessageRequest request = new ReceiveMessageRequest(queueUrl.toString());

        ReceiveMessageResult response = sqs.receiveMessage(request);

        List<Message> messages = response.getMessages();

        for (Message msg: messages) {
            BuildMessage buildMessage = readMessage(msg.getBody());

            messageHandler.handle(buildMessage);

            DeleteMessageRequest deleteRequest = new DeleteMessageRequest(queueUrl.toString(), msg.getReceiptHandle());
            sqs.deleteMessage(deleteRequest);
        }
    }

    private BuildMessage readMessage(String body) {
        try {
            return objectMapper.readValue(body, BuildMessage.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
