package hl7sender;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;

public class hl7sender {

    public static void main(String[] args) {

        final int PORT_NUMBER = 9014;

        // In HAPI, almost all things revolve around a context object
        HapiContext context = new DefaultHapiContext();

        try {
            String adtMessage = "MSH|^~\\&|NES|NINTENDO|TESTSYSTEM|TESTFACILITY|20010101000000||ADT^A01|Q123456789T123456789X123456|P|2.3\r" +
                    "EVN|A01|20010101000000|||^KOOPA^BOWSER\r" +
                    "PID|1||123456789|0123456789^AA^^JP|BROS^MARIO||19850101000000|M|||123 FAKE STREET^MARIO \\T\\ LUIGI BROS PLACE^TOADSTOOL KINGDOM^NES^A1B2C3^JP^HOME^^1234|1234|(555)555-0123^HOME^JP:1234567|||S|MSH|12345678|||||||0|||||N\r" +
                    "NK1|1|PEACH^PRINCESS|SO|ANOTHER CASTLE^^TOADSTOOL KINGDOM^NES^^JP|(123)555-1234|(123)555-2345|NOK\r" +
                    "NK1|2|TOADSTOOL^PRINCESS|SO|YET ANOTHER CASTLE^^TOADSTOOL KINGDOM^NES^^JP|(123)555-3456|(123)555-4567|EMC\r" +
                    "PV1|1|O|ABCD^EFGH||||123456^DINO^YOSHI^^^^^^MSRM^CURRENT^^^NEIGHBOURHOOD DR NBR|^DOG^DUCKHUNT^^^^^^^CURRENT||CRD|||||||123456^DINO^YOSHI^^^^^^MSRM^CURRENT^^^NEIGHBOURHOOD DR NBR|AO|0123456789|1|||||||||||||||||||MSH||A|||20010101000000\r" +
                    "IN1|1|PAR^PARENT||||LUIGI\r";
            // create a new MLLP client over the specified port
            Connection connection = context.newClient("localhost", PORT_NUMBER, false);

            // The initiator which will be used to transmit our message
            Initiator initiator = connection.getInitiator();

            // send the created HL7 message over the connection established
            Parser parser = context.getPipeParser();
            System.out.println("Sending message:" + "\n" + adtMessage);
            Message response = initiator.sendAndReceive(parser.parse(adtMessage));

            // display the message response received from the remote party
            String responseString = parser.encode(response);
            System.out.println("Received response:\n" + responseString.replaceAll("\r", "\n"));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

