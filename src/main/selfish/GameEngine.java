package selfish;

import selfish.deck.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Represents the public class for game state, and represents a play
 * through the game.
 * 
 * @author kunj
 * @version 1.0
 * @since 1.0
 */
public class GameEngine implements java.io.Serializable {
    private Random random = new Random();
    private GameDeck gameDeck = new GameDeck();
    private GameDeck gameDiscard = new GameDeck();
    private SpaceDeck spaceDeck = new SpaceDeck();
    private SpaceDeck spaceDiscard = new SpaceDeck();
    final private static long serialVersionUID = 0;
    private Collection<Astronaut> activePlayers;
    private List<Astronaut> corpses;
    private Astronaut currentPlayer;
    private boolean hasStarted;

    private GameEngine() {
    }

    /**
     * GameEngine: constructor
     * 
     * @param seed      random seed
     * @param gameDeck  path of the gameDeck cards file
     * @param spaceDeck path of the spaceDeck cards file
     * @throws GameException if file not found
     */
    public GameEngine(long seed, String gameDeck, String spaceDeck) throws GameException {
        random.setSeed(seed);
        activePlayers = new ArrayList<>();
        corpses = new ArrayList<>();
        this.gameDeck = new GameDeck(gameDeck);
        this.spaceDeck = new SpaceDeck(spaceDeck);
        this.gameDiscard = new GameDeck();
        this.spaceDiscard = new SpaceDeck();
        this.gameDeck.shuffle(random);
        this.spaceDeck.shuffle(random);
    }

    /**
     * addPlayer: adds players to the game
     * 
     * @param player the name of the player
     * @return int totalplayers
     */
    public int addPlayer(String player) {
        if (hasStarted) {
            throw new IllegalStateException("Game has already started");
        }
        if (activePlayers.size() == 5) {
            throw new IllegalStateException("Max 5 players allowed");
        }
        activePlayers.add(new Astronaut(player, this));
        int totalPlayers = getFullPlayerCount();
        if (currentPlayer != null) {
            totalPlayers++;
        }
        return totalPlayers;
    }

    /**
     * endTurnL ends turn of a player
     * 
     * @return size of active players
     */
    public int endTurn() {
        boolean isThere = false;
        for (Astronaut astro : corpses) {
            if (currentPlayer == astro) {
                isThere = true;
                break;
            }
        }
        if (currentPlayer.isAlive()) {
            activePlayers.add(currentPlayer);
        } else if (currentPlayer.isAlive() == false && isThere == false) {
            corpses.add(currentPlayer);
        }
        currentPlayer = null;
        return activePlayers.size();
    }

    /**
     * gameOver: determines whether game is over or not
     * 
     * @return true if gameOver else false
     */
    public boolean gameOver() {
        if (getWinner() != null || activePlayers.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * getAllPlayers: gets all players whether dead or alive
     * 
     * @return list of all astronauts in the game
     */
    public List<Astronaut> getAllPlayers() {
        List<Astronaut> allPlayers = new ArrayList<>();
        allPlayers.addAll(activePlayers);
        if (currentPlayer != null && currentPlayer.isAlive()) {
            allPlayers.add(currentPlayer);
        }
        if (corpses != null) {
            allPlayers.addAll(corpses);
        }
        boolean isInCorpse = false;
        for (Astronaut astro : corpses) {
            if (currentPlayer != null && astro.toString().equals(currentPlayer.toString())) {
                isInCorpse = true;
                break;
            }
        }
        boolean alreadyIn = false;
        for (Astronaut astro : allPlayers) {
            if (astro != null && currentPlayer != null
                    && (astro.toString()).equals(currentPlayer.toString())) {
                alreadyIn = true;
            }
        }
        if (currentPlayer != null && !alreadyIn && isInCorpse == false && currentPlayer.isAlive() == false) {
            allPlayers.add(currentPlayer);
        }

        return allPlayers;
    }

    /**
     * getCurrentPLayer: gets current player
     * 
     * @return current player
     */
    public Astronaut getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * gets the full player count
     * 
     * @return gets the full player count
     */
    public int getFullPlayerCount() {
        boolean isThere = false;
        for (Astronaut astro : activePlayers) {
            if (currentPlayer == astro) {
                isThere = true;
                if (astro.oxygenRemaining() == 0) {
                    corpses.add(astro);
                }
            }
        }
        if (isThere || (currentPlayer != null && currentPlayer.isAlive() == false)) {
            return activePlayers.size() + corpses.size();
        }
        if (isThere == false && currentPlayer == null) {
            return activePlayers.size() + corpses.size();
        }
        if (isThere == false && currentPlayer.isAlive() == false) {
            return activePlayers.size() + corpses.size() - 1;
        }

        return activePlayers.size() + corpses.size() + 1;
    }

    /**
     * gets gamedeck
     * 
     * @return gamedeck
     */

    public GameDeck getGameDeck() {
        return gameDeck;
    }

    /**
     * gets spacedeck
     * 
     * @return spacedeck
     */
    public SpaceDeck getSpaceDeck() {
        return spaceDeck;
    }

    /**
     * gets game discard
     * 
     * @return gamediscard
     */
    public GameDeck getGameDiscard() {
        return gameDiscard;
    }

    /**
     * gets space discard
     * 
     * @return space discard
     */
    public SpaceDeck getSpaceDiscard() {
        return spaceDiscard;
    }

    /**
     * gets winner of the game
     * 
     * @return the astonaut if winner
     */
    public Astronaut getWinner() {
        for (Astronaut astro : activePlayers) {
            if (astro.hasWon()) {
                return astro;
            }
        }
        return null;
    }

    /**
     * kills player
     * 
     * @param corpse the player to be killed
     */
    public void killPlayer(Astronaut corpse) {
        activePlayers.remove(corpse);
        corpses.add(corpse);
    }

    /**
     * loads state of the game
     * 
     * @param path of the file
     * @return gameengine object
     * @throws GameException if class not found
     */
    public static GameEngine loadState(String path) throws GameException {
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            GameEngine gameEngine = (GameEngine) objectIn.readObject();
            objectIn.close();
            fileIn.close();
            return gameEngine;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
            throw new GameException(path, e);
        }
    }

    /**
     * merges decks
     * 
     * @param deck1 deck to merged with deck2
     * @param deck2 deck to merged with deck1
     */
    public void mergeDecks(Deck deck1, Deck deck2) {
        deck2.shuffle(random);
        while (deck2.size() != 0) {
            deck1.add(deck2.draw());
        }
        deck2 = new GameDeck();
    }

    /**
     * saves the state of the game to a file
     * 
     * @param path of the file
     */
    public void saveState(String path) {
        try (FileOutputStream fileOut = new FileOutputStream(path);
                ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(this);
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    /**
     * splits double oxygen into two single ones
     * 
     * @param dbl oxygen to be split
     * @return array of two single oxys
     */
    public Oxygen[] splitOxygen(Oxygen dbl) {
        if ((this.gameDiscard.size() == 0 && this.gameDeck.size() == 1)
                || (this.gameDiscard.size() == 1 && this.gameDeck.size() == 0)) {
            throw new IllegalStateException();
        }
        int count = 0;
        Oxygen[] oxys = new Oxygen[2];
        List<Card> cardsRemovedFromDeck = new ArrayList<>();
        List<Card> cardsRemovedFromDiscard = new ArrayList<>();

        while (this.gameDeck.size() > 0) {
            Card card = this.gameDeck.draw();
            if (card instanceof Oxygen && ((Oxygen) card).getValue() == 1) {
                oxys[count] = (Oxygen) card;
                count++;
            } else {
                cardsRemovedFromDeck.add(card);
            }
            if (count == 2) {
                break;
            }
        }

        if (count < 2 && this.gameDiscard.size() > 0) {
            while (true) {
                Card card = this.gameDiscard.draw();
                if (card instanceof Oxygen && ((Oxygen) card).getValue() == 1) {
                    oxys[count] = (Oxygen) card;
                    count++;
                } else {
                    cardsRemovedFromDiscard.add(card);
                }
                if (count == 2) {
                    break;
                }
            }
        }
        for (Card card : cardsRemovedFromDeck) {
            this.gameDeck.add(card);
        }
        for (Card card : cardsRemovedFromDiscard) {
            this.gameDiscard.add(card);
        }
        if (count == 2) {
            this.gameDeck.remove(dbl);
            this.gameDiscard.add(dbl);
            return oxys;
        }
        return null;
    }

    /**
     * starts the game
     */
    public void startGame() {
        if (hasStarted || activePlayers.size() == 1 || activePlayers.size() == 6) {
            throw new IllegalStateException();
        }
        hasStarted = true;
        for (Astronaut player : activePlayers) {
            for (int i = 0; i < 4; i++) {
                player.addToHand(new Oxygen(1));
            }

            player.addToHand(new Oxygen(2));
        }
        for (int i = 0; i < 4; i++) {
            for (Astronaut player : activePlayers) {
                player.addToHand(gameDeck.draw());
            }
        }
    }

    /**
     * starts turn of a player
     */
    public void startTurn() {
        if (activePlayers.size() == 0 || hasStarted == false || currentPlayer != null || getWinner() != null) {
            throw new IllegalStateException();
        }
        currentPlayer = ((List<Astronaut>) (this.activePlayers)).get(0);
        activePlayers.remove(currentPlayer);
    }

    /**
     * travels
     * 
     * @param traveller astronaut that travels
     * @return card added to the track
     */
    public Card travel(Astronaut traveller) {
        if (traveller.oxygenRemaining() <= 1) {
            throw new IllegalStateException();
        }
        List<Card> cards = traveller.getHand();
        int noOfOxygensRemoved = 0;
        boolean onesRemoved = false;
        for (Card card : cards) {
            if (card instanceof Oxygen) {
                int value = ((Oxygen) card).getValue();
                if (value == 1) {
                    traveller.getHand().remove(card);
                    noOfOxygensRemoved++;
                }
                if (noOfOxygensRemoved == 2) {
                    onesRemoved = true;
                    break;
                }
            }
        }

        if (onesRemoved == false) {
            for (Card card : cards) {
                if (card instanceof Oxygen) {
                    int value = ((Oxygen) card).getValue();
                    if (value == 2) {
                        traveller.getHand().remove(card);
                        break;
                    }
                }
            }
        }
        if (traveller.oxygenRemaining() == 0) {
            corpses.add(traveller);
            activePlayers.remove(traveller);
        }
        Card card = spaceDeck.draw();
        if (card.toString().equals("Gravitational anomaly") == false) {
            traveller.getTrack().add(card);
        }
        return card;
    }
}
