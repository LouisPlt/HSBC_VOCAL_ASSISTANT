package amazon;

import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;
import config.Configuration;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by louis on 05/11/16.
 */
public class HSBCSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {

    private static final Set<String> supportedApplicationIds;

    static {

        supportedApplicationIds = new HashSet<String>();
        //TODO: Fix secret App id
        try {
            supportedApplicationIds.add(Configuration.getAPPID());
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        System.setProperty(Sdk.DISABLE_REQUEST_SIGNATURE_CHECK_SYSTEM_PROPERTY, "true");
        System.setProperty(Sdk.SUPPORTED_APPLICATION_IDS_SYSTEM_PROPERTY, "");
        System.setProperty(Sdk.TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY, "");
        */
    }

    public HSBCSpeechletRequestStreamHandler(Speechlet speechlet, Set<String> supportedApplicationIds) {
        super(speechlet, supportedApplicationIds);
    }
    public HSBCSpeechletRequestStreamHandler() {

        super(new HSBCSpeechlet(), supportedApplicationIds);
    }
}
