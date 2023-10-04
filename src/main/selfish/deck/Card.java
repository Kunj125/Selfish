package selfish.deck;

/**
 * Represents cards.
 * 
 * @author kunj
 * @version 1.0
 * @since 1.0
 */
public class Card implements java.io.Serializable, Comparable<Card> {
    private String name;
    private String description;
    final private static long serialVersionUID = 0;

    /**
     * Card
     * Empty Constructor
     */
    public Card() {

    }

    /**
     * Card constructor
     * 
     * @param name        of the card
     * @param description of the card
     */
    public Card(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * getDescription gets description of the card
     * 
     * @return description of the card
     */
    public String getDescription() {
        return description;
    }

    /**
     * toString gets name of the card
     * 
     * @return name of the card
     */
    public String toString() {
        return name;
    }

    /**
     * compareTo compares the card
     * 
     * @param o another instance of card to be compared with
     * @return sorted result
     */
    public int compareTo(Card o) {
        if (this instanceof Oxygen) {
            this.name = "Oxygen";
        }
        if (o instanceof Oxygen) {
            o.name = "Oxygen";
        }
        if (this instanceof Oxygen && o instanceof Oxygen) {
            return this.toString().compareTo(o.toString());
        }
        return this.name.compareTo(o.name);
    }
}
