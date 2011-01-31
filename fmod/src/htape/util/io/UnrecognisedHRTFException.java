package htape.util.io;

/**
 * Created by IntelliJ IDEA.
 * User: ben
 * Date: 1/28/11
 * Time: 9:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class UnrecognisedHRTFException extends Exception {

    private String filename;

    public UnrecognisedHRTFException(String filename) {
        this.filename = filename;
    }

    @Override
    public String getMessage() {
        return "Couldn't recognise HRTF file format: " + filename;
    }
}
