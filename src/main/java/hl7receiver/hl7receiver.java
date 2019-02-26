package hl7receiver;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.HL7Service;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;

public class hl7receiver {

    public static void main(String args[]) {

        final int PORT_NUMBER = 9014;
        // In HAPI, almost all things revolve around a context object
        HapiContext context = new DefaultHapiContext();
        try {
            Parser parser = new PipeParser();
            HL7Service ourHl7Server = context.newServer(PORT_NUMBER, false);
            // You can set up routing rules for your HL7 listener by extending the
            // AppRoutingData class like this
            ourHl7Server.registerApplication(new RegisterEventRouter(), new ourSimpleApplication());
            ourHl7Server.setShutdownTimeout(10000);
            ourHl7Server.startAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}


