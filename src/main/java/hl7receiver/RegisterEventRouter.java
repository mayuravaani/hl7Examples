package hl7receiver;

import ca.uhn.hl7v2.protocol.ApplicationRouter;

public class RegisterEventRouter implements ApplicationRouter.AppRoutingData {

    public String getMessageType() {

        return "*";
    }

    public String getTriggerEvent() {

        return "*";
    }

    public String getProcessingId() {

        return "*";
    }

    public String getVersion() {

        return "*";
    }

}
