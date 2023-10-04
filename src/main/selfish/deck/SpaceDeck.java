package selfish.deck;

import java.io.FileNotFoundException;
import java.util.List;

import selfish.GameException;

/**
 * Represents deck of space cards.
 * 
 * @author kunj
 * @version 1.0
 * @since 1.0
 */
public class SpaceDeck extends Deck {

    /**
     * A constant that represents an asteroid field in a SpaceDeck
     */
    public static final String ASTEROID_FIELD = "Asteroid field";
    /**
     * A constant that represents an asteroid field in a SpaceDeck
     */
    public static final String BLANK_SPACE = "Blank Space";
    /**
     * A constant that represents Cosmic radiation in a SpaceDeck.
     */
    public static final String COSMIC_RADIATION = "Cosmic radiation";
    /**
     * A constant that represents Gravitational anomaly in a SpaceDeck.
     */
    public static final String GRAVITATIONAL_ANOMALY = "Gravitational anomaly";
    /**
     * A constant that represents Hyperspace in a SpaceDeck.
     */
    public static final String HYPERSPACE = "Hyperspace";
    /**
     * A constant that represents Meteoroid in a SpaceDeck.
     */
    public static final String METEOROID = "Meteoroid";
    /**
     * A constant that represents Mysterious nebula in a SpaceDeck.
     */
    public static final String MYSTERIOUS_NEBULA = "Mysterious nebula";
    /**
     * A constant that represents Solar flare in a SpaceDeck.
     */
    public static final String SOLAR_FLARE = "Solar flare";
    /**
     * A constant that represents an Useful junk in a SpaceDeck.
     */
    public static final String USEFUL_JUNK = "Useful junk";
    /**
     * A constant that represents a Wormhole in a SpaceDeck.
     */
    public static final String WORMHOLE = "Wormhole";

    private static final long serialVersionUID = 0;

    /**
     * empty constructor
     */
    public SpaceDeck() {
    }

    /**
     * spacedeck constructor
     * 
     * @param path of the file containing space cards
     * @throws GameException if file not found
     */
    public SpaceDeck(String path) throws GameException {
        try {
            add(loadCards(path));
        } catch (Exception e) {
            throw new GameException("File not found", new FileNotFoundException());
        }
    }
}
