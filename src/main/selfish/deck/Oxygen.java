package selfish.deck;

/**
 * Represents oxygen cards and extends card.
 * 
 * @author kunj
 * @version 1.0
 * @since 1.0
 */

public class Oxygen extends Card {
    private int value;
    final static private long serialVersionUID = 0;

    /**
     * Constructor
     * 
     * @param value of the oxygen
     */
    public Oxygen(int value) {
        this.value = value;
    }

    /**
     * gets value of the oxygen - 1 or 2
     * 
     * @return int value of the oxygen
     */
    public int getValue() {
        return this.value;
    }

    /**
     * to name and the value
     * 
     * @return String name and the value
     */
    public String toString() {
        return "Oxygen(" + value + ")";
    }
}
