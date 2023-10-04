import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import selfish.GameEngine;
import selfish.GameException;
import selfish.deck.GameDeck;
import selfish.deck.SpaceDeck;

public class GameDriver {

    /**
     * A helper function to centre text in a longer String.
     * 
     * @param width The length of the return String.
     * @param s     The text to centre.
     * @return A longer string with the specified text centred.
     */
    public static String centreString(int width, String s) {
        return String.format("%-" + width + "s", String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
    }

    public GameDriver() {
    }

    
    /** 
     * @param args
     * @throws GameException
     */
    public static void main(String[] args) throws GameException {
        try {
            File file = new File("../../io/art.txt");
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
              String line = reader.nextLine();
              System.out.println(line);
            }
            reader.close();
          } catch (FileNotFoundException e) {
            System.out.println("File not found.");
          }

        ArrayList<String> names = new ArrayList<String>();
        Scanner in = new Scanner(System.in);
        System.out.print("Player 1 name? ");
        names.add(in.nextLine());
        System.out.println();
        System.out.print("Player 2 name? ");
        names.add(in.nextLine());
        System.out.println();
        int noOfPlayers = 2;
        while (noOfPlayers < 5) {
            System.out.println("Add another? [Y]es/[N]o ");
            if (in.nextLine().toUpperCase().equals("Y")) {
                System.out.print("Player " + (noOfPlayers + 1) + " name? ");
                names.add(in.nextLine());
                noOfPlayers++;
            } else {
                break;
            }
        }
        System.out.print("After a dazzling (but doomed) space mission, ");
        for (int i = 0; i < names.size(); i++) {
            if (i == names.size() - 2) {
                System.out.print(names.get(i) + " and ");
            } else if (i == names.size() - 1) {
                System.out.print(names.get(i));
            } else {
                System.out.print(names.get(i) + ", ");
            }
        }
        System.out.print(
                " are floating in space and their Oxygen supplies are running low. Only the first back to the ship will survive!");
        in.close();
        long seed = 10;
        GameEngine gameEngine = new GameEngine(seed, "../../io/ActionCards.txt", "../../io/SpaceCards.txt");
        
    }
}