package selfish;

/**
 * Represents exceptions.
 * 
 * @author kunj
 * @version 1.0
 * @since 1.0
 */
public class GameException extends Exception implements java.io.Serializable {
    /**
     * constructor of game exception
     * 
     * @param msg of the rror
     * @param e   error object
     */
    public GameException(String msg, Throwable e) {
        super(msg, e);
    }
}
