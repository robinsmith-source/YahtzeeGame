package org.example.Game;
import java.util.Scanner;

/**
 * Game Class for the Yahtzee Gameplay.
 */
public class Game {

    /**
     * Current player index.
     */
    private int currentPlayerIndex = 0;

    /**
     * Player Array for the player instances.
     */
    private final Player[] players;

    /**
     * Scanner Instance for the Game.
     */
    private final Scanner scan = new Scanner(System.in);

    /**
     * Default Game constructor. Initializes the players.
     * @param players Player Array for the player instances.
     *
     * @see Main#initPlayers()
     */
    public Game(Player[] players) {
        this.players = players;
    }

    /**
     * Method to play the game.
     */
    public void play() {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < players.length; j++) {
                System.out.printf("""
                        
                        +~~~~~~~~~~~~~~~~~~~~~~~~~~~~+
                        | %-26s |
                        +~~~~~~~~~~~~~~~~~~~~~~~~~~~~+
                        """, currentPlayer().getName()+" it's your turn!");


                System.out.println(currentPlayer().getScorecard());
                Dice.roll(currentPlayer().getDice());
                System.out.println(Dice.showDice(currentPlayer().getDice()));

                System.out.println(currentPlayer().score.possibleCombinationsScoresToString());

                rerollDice();

                int indexOfCombination;
                do {
                    System.out.print("Choose a category to score: ");
                    indexOfCombination = scan.nextInt();
                    scan.nextLine(); //Clears the Scanner buffer.
                } while (indexOfCombination < 1 || indexOfCombination > 13 || currentPlayer().score.isCombinationScored(indexOfCombination));
                currentPlayer().score.setOnCombination(indexOfCombination);
                System.out.println(currentPlayer().getScorecard());

                nextPlayer();
            }
        }
        for (final Player player : players) {
            System.out.println(player.getScorecard());
        }
        System.out.println("Game finished!");
        System.out.println("Winner: " + getWinner().getName());
        System.out.println("Final Score: " + getWinner().score.getPlayerFinalScore());
    }

    /**
     * Method to reroll Dice (2nd and 3rd roll)
     */
    private void rerollDice() {
        for (int ln = 1; ln <= 2; ln++) {
            String input;

            do {
                System.out.print("Put in the dice you want to roll seperated with a ','.\nEnter to skip! (1-5): ");
                input = scan.nextLine();
            } while (!input.matches("[1-5,]*"));

            if (input.isEmpty()) {
                if (ln == 1) {
                    return;
                } else {
                System.out.println("Skipped!\n");
                }
            } else {
                String[] diceToReRoll = input.split(",");
                for (String s : diceToReRoll) {
                    int diceNum = Integer.parseInt(s);
                    Dice.roll(currentPlayer().getDice()[diceNum-1]);
                }
            }

            System.out.println(Dice.showDice(currentPlayer().getDice()));
            System.out.println(currentPlayer().score.possibleCombinationsScoresToString());
        }
    }

    /**
     * Method to cycle through the players indices. Current player object can only be gotten by currentPlayer().
     *
     * @see Game#currentPlayer()
     */
    private void nextPlayer() {
        if (players.length - 1 == currentPlayerIndex) {
            currentPlayerIndex = 0;
        } else {
            currentPlayerIndex++;
        }
    }

    /**
     * Method to get the current Player Object.
     *
     * @return Player Object with the current turn.
     */
    private Player currentPlayer() {
        return players[currentPlayerIndex];
    }

    /**
     * Method to get the winner of the game.
     *
     * @return Player Object with the highest score (Winner).
     */
    private Player getWinner() {
        int highestScore = 0;
        Player winner = null;
        for (final Player player : players) {
            if (player.score.getPlayerFinalScore() > highestScore) {
                winner = player;
            }
        }
        return winner;
    }
}