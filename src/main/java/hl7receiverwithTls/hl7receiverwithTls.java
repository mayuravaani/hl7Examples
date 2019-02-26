package hl7receiverwithTls;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.HL7Service;
import ca.uhn.hl7v2.hoh.sockets.CustomCertificateTlsSocketFactory;
import ca.uhn.hl7v2.hoh.util.HapiSocketTlsFactoryWrapper;
import ca.uhn.hl7v2.hoh.util.KeystoreUtils;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class hl7receiverwithTls {
    public static void main(String[] args) throws InterruptedException {

        final int PORT_NUMBER = 9014;
        final String tlsKeystoreFilepath = "src/main/resources/keystore.jks";
        final String tlsKeystorePassphrase = "changeit";
        final String tlsKeystoreType = "JKS";
        // In HAPI, almost all things revolve around a context object
        HapiContext context = new DefaultHapiContext();
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
            Parser parser = new PipeParser();
            HL7Service ourHl7Server = context.newServer(PORT_NUMBER, true);
            // You can set up routing rules for your HL7 listener by extending the
            // AppRoutingData class
            ourHl7Server.registerApplication(new RegisterEventRouter(), new ourSimpleApplication());
            ourHl7Server.setShutdownTimeout(10000);
            ourHl7Server.startAndWait();
    }
}
