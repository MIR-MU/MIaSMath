package cz.muni.fi.mias;

public class MiasError extends Error {

    private static final long serialVersionUID = -355428598212227054L;

    public MiasError(String message) {
        super(message);
    }

    public MiasError(String message, Throwable cause) {
        super(message, cause);
    }

}
