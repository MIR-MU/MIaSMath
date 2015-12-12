package cz.muni.fi.mias;

public class MIaSError extends Error {

    private static final long serialVersionUID = -355428598212227054L;

    public MIaSError(String message) {
        super(message);
    }

    public MIaSError(String message, Throwable cause) {
        super(message, cause);
    }

}
