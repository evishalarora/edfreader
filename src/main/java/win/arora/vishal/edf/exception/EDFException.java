package win.arora.vishal.edf.exception;

import java.io.IOException;

public class EDFException extends IOException {
    public EDFException(String message) {
        super(message);
    }
    public EDFException(Throwable th) {
        super(th);
    }
}
