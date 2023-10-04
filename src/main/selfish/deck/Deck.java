package selfish.deck;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import selfish.GameException;

/**
 * Represents deck of cards.
 * 
 * @author kunj
 * @version 1.0
 * @since 1.0
 */
public abstract class Deck implements java.io.Serializable {
    private Collection<Card> cards = new ArrayList<>();
    final private static long serialVersionUID = 0;

    /**
     * Deck
     * empty constructor
     */
    protected Deck() {
    }

    /**
     * loadCards loads the cards using stringToCards
     * 
     * @param path path of the file
     * @return list of cards
     * @throws GameException if filenotfound
     */
    protected static List<Card> loadCards(String path) throws GameException {
        List<Card> cards = new ArrayList<>();
        if (path.equals("")) {
            return null;
        }

        int a = 0;
        File file = new File(path);
        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (a != 0) {
                    int noOfCards = Integer.parseInt(String.valueOf(line.charAt((line.length() - 1))));
                    Card[] these_cards = stringToCards(line);
                    for (int i = 0; i < noOfCards; i++) {
                        cards.add(these_cards[i]);
                    }
                }
                a++;
            }
            reader.close();
        } catch (NumberFormatException | FileNotFoundException e) {
            throw new GameException("File not found", new FileNotFoundException());
        }
        return cards;

    }

    /**
     * stringToCards string to cards
     * 
     * @param str line from the file
     * @return array of cards
     */
    protected static Card[] stringToCards(String str) {
        int noOfCards = Integer.parseInt(String.valueOf(str.charAt((str.length() - 1))));
        String card = "";
        int index = 0;
        String desc = "";
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != ';') {
                card += str.charAt(i);
                index = i;
            } else {
                break;
            }
        }
        for (int i = index + 3; i < str.length() - 3; i++) {
            desc += str.charAt(i);
        }
        Card[] this_card = new Card[noOfCards];
        for (int i = 0; i < noOfCards; i++) {
            this_card[i] = new Card(card, desc);
        }
        return this_card;
    }

    /**
     * add a card to cards
     * 
     * @param card to be added to cards
     * @return the number of cards
     */
    public int add(Card card) {
        this.cards.add(card);
        if (cards != null) {
            return this.cards.size();
        } else {
            return 0;
        }
    }

    /**
     * add a list of cards to cards
     * 
     * @param cards to be added to cards
     * @return the number of cards
     */
    protected int add(List<Card> cards) {
        if (cards != null) {
            for (Card card : cards) {
                add(card);
            }
        }
        if (this.cards != null) {
            return this.cards.size();
        }
        return 0;
    }

    /**
     * draw: draws the last card
     * 
     * @return the removed card
     */
    public Card draw() {
        try {
            Card card = ((List<Card>) cards).get(cards.size() - 1);
            cards.remove(card);
            return card;
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalStateException();
        }
    }

    /**
     * remove: removes card from the cards
     * 
     * @param card to be removed from cards
     */
    public void remove(Card card) {
        for (Card a : cards) {
            if (a == card) {
                cards.remove(card);
                break;
            }
        }
    }

    /**
     * shuffle: shuffles the cards
     * 
     * @param random random object for shuffle
     */
    public void shuffle(Random random) {
        Collections.shuffle((List<Card>) this.cards, random);
    }

    /**
     * size: size of the cards
     * 
     * @return int: size of the cards
     */
    public int size() {
        if (this.cards == null) {
            return 0;
        }
        return this.cards.size();
    }
}
