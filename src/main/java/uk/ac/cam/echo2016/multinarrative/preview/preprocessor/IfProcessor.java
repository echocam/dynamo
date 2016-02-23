package uk.ac.cam.echo2016.multinarrative.preview.preprocessor;

import java.util.function.Function;

import uk.ac.cam.echo2016.multinarrative.dev.Debug;

public class IfProcessor implements Function<String, String> {

    private boolean end = false;
    private boolean ifTrue;

    public IfProcessor(boolean b) {
        ifTrue = b;
    }

    @Override
    public String apply(String t) {
        if (end) {
            return t;
        } else if (t.startsWith("#endif")) {
            Debug.logInfo("ENDIF",5,Debug.SYSTEM_PREVIEW);
            end = true;
            return null;
        } else if (ifTrue || t.startsWith("#")) {
            return t;
        } else {
            return null;
        }
    }

}
