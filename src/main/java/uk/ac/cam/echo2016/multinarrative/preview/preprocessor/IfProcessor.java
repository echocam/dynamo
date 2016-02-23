package uk.ac.cam.echo2016.multinarrative.preview.preprocessor;

import java.util.function.Function;

import uk.ac.cam.echo2016.multinarrative.dev.Debug;

public class IfProcessor implements Function<String, String> {

    private int end = 1;
    private boolean ifTrue;

    public IfProcessor(boolean b) {
        ifTrue = b;
    }

    @Override
    public String apply(String t) {
        if (end==0) {
            return t;
        } else if (t.startsWith("#endif")) {
            Debug.logInfo("ENDIF", 5, Debug.SYSTEM_PREVIEW);
            end--;
            return null;
        } else if (ifTrue) {
            return t;
        } else {
            if (t.startsWith("#if")) {
                Debug.logInfo("IF", 5, Debug.SYSTEM_PREVIEW);
                end++;
            }
            return null;
        }
    }

}
