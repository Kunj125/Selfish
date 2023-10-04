package selfish.deck;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import selfish.GameException;

/**
 * Represents deck of action and oxygen cards - game deck.
 * 
 * @author kunj
 * @version 1.0
 * @since 1.0
 */
public class GameDeck extends Deck {
    /** A constant that represents an Hack suit in a SpaceDeck. */

    public static final String HACK_SUIT = "Hack suit";
    /** A constant that represents an Hole in suit in a SpaceDeck. */
    public static final String HOLE_IN_SUIT = "Hole in suit";

    /** A constant that represents an Laser blast in a SpaceDeck. */
    public static final String LASER_BLAST = "Laser blast";

    /** A constant that represents an Oxygen in a SpaceDeck. */
    public static final String OXYGEN = "Oxygen";

    /** A constant that represents an Oxygen(1) in a SpaceDeck. */
    public static final String OXYGEN_1 = "Oxygen(1)";

    /** A constant that represents an Oxygen(2) in a SpaceDeck. */
    public static final String OXYGEN_2 = "Oxygen(2)";

    /** A constant that represents an Oxygen siphon in a SpaceDeck. */
    public static final String OXYGEN_SIPHON = "Oxygen siphon";

    /** A constant that represents Rocket booster in a SpaceDeck. */
    public static final String ROCKET_BOOSTER = "Rocket booster";

    /** A constant that represents Sheild in a SpaceDeck. */
    public static final String SHIELD = "Sheild";

    /** A constant that represents Tether in a SpaceDeck. */

    public static final String TETHER = "Tether";
    /** A constant that represents Tractor beam in a SpaceDeck. */
    public static final String TRACTOR_BEAM = "Tractor beam";
    /** A constant that represents an asteroid field in a SpaceDeck. */

    private static final long serialVersionUID = 0;

    /**
     * empty constructor
     */
    public GameDeck() {
    }

    /**
     * GameDeck constructor
     * 
     * @param path to the file containing cards
     * @throws GameException if filenotfound
     */
    public GameDeck(String path) throws GameException {
        try {
            this.add(loadCards(path));
        } catch (Exception e) {
            throw new GameException("File not found", new FileNotFoundException());
        }
        for (int i = 0; i < 10; i++) {
            this.add(new Oxygen(2));
        }
        for (int i = 0; i < 38; i++) {
            this.add(new Oxygen(1));
        }
    }

    /**
     * draws oxygen from cards
     * 
     * @param value of the oxygen
     * @return Oxygen card
     */
    public Oxygen drawOxygen(int value) {
        List<Card> cardsDrawn = new ArrayList<>();
        Card answer;
        while (true) {
            Card card = this.draw();
            if ((card instanceof Oxygen && (((Oxygen) card).getValue() == value)) == false) {
                cardsDrawn.add(card);
            } else {
                answer = card;
                break;
            }
        }
        for (int i = cardsDrawn.size() - 1; i >= 0; i--) {
            this.add(cardsDrawn.get(i));
        }
        return (Oxygen) answer;
    }

    /**
     * splits dbl oxygen into singles
     * 
     * @param dbl the oxygen to be split
     * @return an array of single oxys
     */
    public Oxygen[] splitOxygen(Oxygen dbl) {
        if (dbl.getValue() == 1) {
            throw new IllegalArgumentException();
        }
        Oxygen[] singles = new Oxygen[2];
        this.add(dbl);
        Card card1 = this.draw();
        Card card2 = this.draw();
        Card card3 = this.draw();
        this.add(card1);
        this.add(card2);
        this.add(card3);
        for (int i = 0; i < 2; i++) {
            Oxygen oxy = drawOxygen(1);
            singles[i] = oxy;
        }
        return singles;
    }
}
