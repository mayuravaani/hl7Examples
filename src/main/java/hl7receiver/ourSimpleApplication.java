package hl7receiver;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.protocol.ReceivingApplication;
import ca.uhn.hl7v2.protocol.ReceivingApplicationException;

import java.io.IOException;
import java.util.Map;

public class ourSimpleApplication implements ReceivingApplication {

    PipeParser pipeParser = new PipeParser();

    public Message processMessage(Message message, Map map) throws ReceivingApplicationException, HL7Exception {

        System.out.println("Received Message\n" + pipeParser.encode(message));
        Message response = null;
        try {
            response = message.generateACK();
            System.out.println("Sent Response\n" + pipeParser.encode(response));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public boolean canProcess(Message message) {

        return true;
    }
}
