package network.common;

/**
 * Created by J2N on 16. 6. 17..
 */
public class NetworkException extends Exception {

    public NetworkException(String message) {
        super(message);
    }

    public NetworkException() {
        super("sorry, network Module UnKnow Exception..");
    }
}

