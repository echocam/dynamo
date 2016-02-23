package uk.ac.cam.echo2016.multinarrative.preview.preprocessor;

import java.util.function.Function;

public class IfProcessor implements Function<String, String> {

    private boolean end = false;

    @Override
    public String apply(String t) {
        if (end) {
            return t;
        } else {
            if (t.startsWith("#endif")) {
                end = true;
            }
            return null;
        }
    }

}
