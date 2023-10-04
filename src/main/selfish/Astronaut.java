
package selfish;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import selfish.deck.*;

/**
 * Represents player in the game.
 * 
 * @author kunj
 * @version 1.0
 * @since 1.0
 */
public class Astronaut implements java.io.Serializable {
    private String name;
    private GameEngine game;
    private List<Card> actions = new ArrayList<>();
    private List<Oxygen> oxygens = new ArrayList<>();
    private Collection<Card> track = new ArrayList<>();
    final private static long serialVersionUID = 0;

    /**
     * Astronaut: creates astronaut and sets the name and game
     * 
     * @param name name of the astronaut object to be created
     * @param game gameEngine instance
     */
    public Astronaut(String name, GameEngine game) {
        this.name = name;
        this.game = game;
    }

    /**
     * Gets the name of the player
     * 
     * @return name of the player
     */
    public String toString() {
        if (this.isAlive()) {
            return this.name;
        }
        return this.name + " (is dead)";
    }

    /**
     * addToHand Adds card to astronaut's hand
     * 
     * @param card The card to add to the astronaut's hand
     */
    public void addToHand(Card card) {
        if (card instanceof Oxygen) {
            oxygens.add((Oxygen) card);
        } else {
            actions.add(card);
        }
    }

    /**
     * addToTrack: adds card played by the astronaut to the track
     * 
     * @param card SpaceCard added on track
     */
    public void addToTrack(Card card) {
        track.add(card);
    }

    /**
     * removes one oxygen
     * 
     * @return number of oxygens remaining
     */
    public int breathe() {
        GameDeck gameDiscard = game.getGameDiscard();
        for (Card card : this.getHand()) {
            if (card instanceof Oxygen && ((Oxygen) (card)).getValue() == 1) {
                gameDiscard.add(card);
                this.oxygens.remove(card);
                break;
            } else if (card instanceof Oxygen && ((Oxygen) (card)).getValue() == 2) {
                Oxygen newCard = new Oxygen(1);
                this.oxygens.remove(card);
                this.oxygens.add(newCard);
                gameDiscard.add(newCard);
                break;
            }
        }
        if (oxygenRemaining() == 0) {
            game.killPlayer(this);
        }
        return oxygenRemaining();
    }

    /**
     * distanceFromShip: returns the distance of the player from the ship
     * 
     * @return int: distance from the ship
     */
    public int distanceFromShip() {
        return 6 - this.getTrack().size();
    }

    /**
     * getActions: gets list of action cards of the astronaut
     * 
     * @return List<Card>: list of action cards of the astronaut
     */
    public List<Card> getActions() {
        Collections.sort(actions);
        return actions;
    }

    /**
     * getActionsStr cards of the player as string
     * 
     * @param enumerated     boolean value whether to include occurences or not
     * @param excludeShields to exclude shields or not
     * @return cards of the player as string
     */
    public String getActionsStr(boolean enumerated, boolean excludeShields) {
        List<Card> cards = this.getHand();
        String ans = "";
        if (cards == null) {
            return ans;
        }
        if (!enumerated) {
            if (excludeShields) {
                char alpha = 'A';
                for (int i = 0; i < cards.size(); i++) {
                    Card currentCard = cards.get(i);
                    if (currentCard.toString().equals("Shield") || (currentCard instanceof Oxygen)) {
                        continue;
                    }
                    int countCurrentCard = this.hasCard(currentCard.toString());
                    if (ans.contains(currentCard.toString()) == false) {
                        if (countCurrentCard > 1) {
                            ans += countCurrentCard + "x " + currentCard.toString()
                                    + ", ";
                        } else {
                            ans += currentCard.toString() + ", ";
                        }

                        alpha++;
                    }
                }
            } else {
                for (int i = 0; i < cards.size(); i++) {
                    Card currentCard = cards.get(i);
                    if ((currentCard instanceof Oxygen)) {
                        continue;
                    }
                    int countCurrentCard = this.hasCard(currentCard.toString());
                    if (ans.contains(currentCard.toString()) == false) {

                        if (countCurrentCard > 1) {
                            ans += countCurrentCard + "x " + currentCard.toString() + ", ";
                        } else {
                            ans += currentCard.toString() + ", ";
                        }
                    }
                }
            }

        } else {
            if (excludeShields) {
                char alpha = 'A';
                for (int i = 0; i < cards.size(); i++) {
                    Card currentCard = cards.get(i);
                    if (currentCard.toString().equals("Shield") || (currentCard instanceof Oxygen)) {
                        continue;
                    }
                    if (ans.contains(currentCard.toString()) == false) {
                        ans += "[" + alpha + "]" + " " + currentCard.toString();
                        alpha++;
                    }
                }
            } else {
                char alpha = 'A';
                for (int i = 0; i < cards.size(); i++) {
                    Card currentCard = cards.get(i);
                    if ((currentCard instanceof Oxygen)) {
                        continue;
                    }
                    if (ans.contains(currentCard.toString()) == false) {

                        ans += "[" + alpha + "] " + currentCard.toString() + ", ";
                        alpha++;
                    }
                }
            }
        }

        if (ans.length() > 0 && ans.substring(ans.length() - 2).equals(", ")) {
            return ans.substring(0, ans.length() - 2);
        }
        return ans;
    }

    /**
     * getHand: gets list of cards in astronaut's hand
     * 
     * @return List<Card> gets list of cards in astronaut's hand
     */
    public List<Card> getHand() {
        List<Card> cards = new ArrayList<>();
        cards.addAll(actions);
        cards.addAll(oxygens);
        Collections.sort(cards);
        return cards;
    }

    /**
     * getHandStr: gets all the cards of the astronaut as a string
     * 
     * @return String: gets all the cards of the astronaut as a string
     */
    public String getHandStr() {
        List<Card> cards = this.getHand();
        String ans = "";
        int ones = 0;
        int doubles = 0;
        for (Card card : cards) {
            if (card instanceof Oxygen && ((Oxygen) (card)).getValue() == 1) {
                ones++;
            } else if (card instanceof Oxygen && ((Oxygen) (card)).getValue() == 2) {
                doubles++;
            } else {
                ans += card.toString();
            }
        }
        String twos = "";
        String singles = "";
        if (doubles > 1) {
            twos += doubles + "x " + "Oxygen(2), ";
        } else if (doubles == 1 && ones > 0) {
            twos += "Oxygen(2), ";
        } else if (doubles == 1 && ones == 0) {
            twos += "Oxygen(2)";
        }

        if (ones > 1) {
            singles += ones + "x " + "Oxygen(1); ";
        }
        if (ones == 1) {
            singles += "Oxygen(1); ";
        }
        return twos + singles + ans;
    }

    /**
     * getTrack: returns collection of cards in the astronaut's track
     * 
     * @return Collection of Track cards of the astronaut
     */
    public Collection<Card> getTrack() {
        return track;
    }

    /**
     * hack: remove the specified card from an astronaut's hand
     * 
     * @param card card to be removed from an astronaut's hand
     */
    public void hack(Card card) {
        boolean found = false;
        if (card == null) {
            throw new IllegalArgumentException();
        }
        if (card instanceof Oxygen) {
            found = true;
            this.oxygens.remove(card);
        }
        if (this.actions.contains(card)) {
            found = true;
            this.actions.remove(card);
        }
        if (this.oxygens.size() == 0) {
            this.actions.clear();
        }
        if (oxygenRemaining() == 0) {
            game.killPlayer(this);
        }
        if (!found) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * hack: remove the specified card from an astronaut's hand
     * 
     * @param card card passed as a string
     * @return cards of the player
     */
    public Card hack(String card) {
        List<Card> actionCards = this.actions;
        List<Oxygen> oxyCards = this.oxygens;
        boolean isFound = false;
        Card c = new Card();
        if (card == null) {
            throw new IllegalArgumentException();
        }
        for (Card cards : actionCards) {
            if (card.equals(cards.toString())) {
                c = cards;
                isFound = true;
                this.actions.remove(cards);
                break;
            }
        }

        if (card.equals("Oxygen(1)")) {
            for (Oxygen oxys : oxyCards) {
                if (oxys.getValue() == 1) {
                    isFound = true;
                    this.oxygens.remove(oxys);
                    break;
                }
            }
        }
        if (card.equals("Oxygen(2)")) {
            for (Oxygen oxys : oxyCards) {
                if (oxys.getValue() == 2) {
                    isFound = true;
                    this.oxygens.remove(oxys);
                    break;
                }
            }
        }
        if (this.oxygens.size() == 0) {
            this.actions.clear();
        }
        if (oxygenRemaining() == 0) {
            game.killPlayer(this);
        }
        if (!isFound) {
            throw new IllegalArgumentException();
        }
        return c;
    }

    /**
     * hasCard: returns the count of the occurence of the card in the hand
     * 
     * @param card card to be searched
     * @return count of the cards
     */
    public int hasCard(String card) {
        int count = 0;
        for (Card cards : this.getHand()) {
            if (card.equals(cards.toString())) {
                count++;
            }
        }
        return count;
    }

    /**
     * hasMeltedEyeballs: return true if the last card is the solar flare
     * 
     * @return boolean return true if the last card is the solar flare
     */
    public boolean hasMeltedEyeballs() {
        List<Card> track = (List<Card>) this.getTrack();
        if (track.get(track.size() - 1).toString().equals("Solar flare")) {
            return true;
        }
        return false;
    }

    /**
     * hasWon: returns true if the player wins
     * 
     * @return boolean returns true if the player wins
     */
    public boolean hasWon() {
        if (this.distanceFromShip() == 0 && this.isAlive()) {
            return true;
        }
        return false;
    }

    /**
     * isAlive: returns true if the astronaut is alive
     * 
     * @return boolean returns true if the astronaut is alive
     */
    public boolean isAlive() {
        List<Card> cards = this.getHand();
        if (cards == null || cards.size() == 0) {
            return false;
        }
        for (Card card : cards) {
            if (card instanceof Oxygen) {
                return true;
            }
        }
        return false;
    }

    /**
     * laserBlast: removes and returns the last card
     * 
     * @return Card removes and returns the last card
     */
    public Card laserBlast() {
        try {
            Card card = ((List<Card>) (this.getTrack())).get(this.getTrack().size() - 1);
            this.getTrack().remove(card);
            return card;
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * oxygenRemaing: returns the value of the total oxygen remaining in astronaut's
     * hand
     * 
     * @return int returns the value of the total oxygen remaining in astronaut's
     *         hand
     */
    public int oxygenRemaining() {
        List<Card> cards = this.getHand();
        int totalOxygens = 0;
        for (Card card : cards) {
            if (card instanceof Oxygen && ((Oxygen) (card)).getValue() == 1) {
                totalOxygens++;
            } else if (card instanceof Oxygen && ((Oxygen) (card)).getValue() == 2) {
                totalOxygens += 2;
            }
        }
        return totalOxygens;
    }

    /**
     * peekAtTrack: returns the last card in the astronaut's track
     * 
     * @return Card returns the last card in the astronaut's track
     */
    public Card peekAtTrack() {
        List<Card> card = (List<Card>) this.getTrack();
        if (card.size() == 0) {
            return null;
        }
        return card.get(card.size() - 1);
    }

    /**
     * siphon: removes and returns a single-value Oxygen cards from an astronaut's
     * hand
     * 
     * @return Oxygen emoves and returns a single-value Oxygen cards from an
     *         astronaut's hand
     */
    public Oxygen siphon() {
        boolean singleRemoved = false;
        Card cardRemoved = new Card();
        for (Card card : this.getHand()) {
            if (card instanceof Oxygen && ((Oxygen) (card)).getValue() == 1) {
                cardRemoved = card;
                this.oxygens.remove(card);
                singleRemoved = true;
                break;
            }
        }
        if (!singleRemoved) {
            for (Card card : this.getHand()) {
                if (card instanceof Oxygen && ((Oxygen) (card)).getValue() == 2) {
                    Oxygen newCard = new Oxygen(1);
                    cardRemoved = newCard;
                    this.oxygens.remove(card);
                    this.oxygens.add(newCard);
                    break;
                }
            }
        }
        if (oxygenRemaining() == 0) {
            game.killPlayer(this);
        }
        return (Oxygen) cardRemoved;
    }

    /**
     * steal: removes and returns a random Card from an astronaut's hand
     * 
     * @return Card removes and returns a random Card from an astronaut's hand
     */
    public Card steal() {
        Card card;
        List<Card> cards = this.getHand();
        Random random = new Random();
        int listSize = cards.size();
        if (cards.size() == 1) {
            card = cards.get(0);
        } else {
            int randomIndex = random.nextInt(listSize);
            card = cards.get(randomIndex);
        }
        if (card instanceof Oxygen) {
            this.oxygens.remove(card);
        }
        this.getActions().remove(card);
        if (this.getHand().size() <= 0) {
            game.killPlayer(this);
        }
        return card;
    }

    /**
     * swapTrack: swaps an astronaut's track with the track of the specified
     * Astronaut
     * 
     * @param swapee the astronaut to be swaped with
     */
    public void swapTrack(Astronaut swapee) {
        List<Card> cards = (List<Card>) this.getTrack();
        List<Card> swapCards = (List<Card>) swapee.getTrack();
        List<Card> temp = new ArrayList<Card>(cards);
        this.getTrack().clear();
        this.getTrack().addAll(swapCards);
        swapee.getTrack().clear();
        swapee.getTrack().addAll(temp);
    }

}
