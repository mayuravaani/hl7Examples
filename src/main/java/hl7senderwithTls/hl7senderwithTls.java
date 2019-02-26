package hl7senderwithTls;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.hoh.sockets.CustomCertificateTlsSocketFactory;
import ca.uhn.hl7v2.hoh.util.HapiSocketTlsFactoryWrapper;
import ca.uhn.hl7v2.hoh.util.KeystoreUtils;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class hl7senderwithTls {

    public static void main(String[] args) throws HL7Exception, LLPException, IOException {

        final int PORT_NUMBER = 9014;
        final String tlsKeystoreFilepath = "src/main/resources/keystore.jks";
        final String tlsKeystorePassphrase = "changeit";
        final String tlsKeystoreType = "JKS";

        // In HAPI, almost all things revolve around a context object
        HapiContext context = new DefaultHapiContext();

        String adtMessage = "MSH|^~\\&|NES|NINTENDO|TESTSYSTEM|TESTFACILITY|20010101000000||ADT^A01|Q123456789T123456789X123456|P|2.3\r" +
                "EVN|A01|20010101000000|||^KOOPA^BOWSER\r";
        try {
            //To validate the keystore
            KeyStore keyStore = KeystoreUtils.loadKeystore(tlsKeystoreFilepath, tlsKeystorePassphrase);
            KeyStore.getInstance(tlsKeystoreType);
            KeystoreUtils.validateKeystoreForTlsSending(keyStore);
            CustomCertificateTlsSocketFactory tlsFac = new CustomCertificateTlsSocketFactory(tlsKeystoreType,
                    tlsKeystoreFilepath, tlsKeystorePassphrase);
            context.setSocketFactory(new HapiSocketTlsFactoryWrapper(tlsFac));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        // create a new MLLP client over the specified port
        Connection connection = context.newClient("localhost", PORT_NUMBER, true);

        // The initiator which will be used to transmit our message
        Initiator initiator = connection.getInitiator();

        // send the created HL7 message over the connection established
        Parser parser = context.getPipeParser();
        System.out.println("Sending message:" + "\n" + adtMessage);
        Message response = initiator.sendAndReceive(parser.parse(adtMessage));

        // display the message response received from the remote party
        String responseString = parser.encode(response);
        System.out.println("Received response:\n" + responseString.replaceAll("\r", "\n"));

    }
}
