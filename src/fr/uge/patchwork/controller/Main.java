package fr.uge.patchwork.controller;

import fr.uge.patchwork.model.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

/**
 * Main controller of the game.
 */
public class Main {

    /**
     * Principal method of the game.
     * @param args : command line arguments
     * @throws ClassNotFoundException :  if the class is not found
     * @throws FileNotFoundException : if the file is not found
     */
    public static void main(String[] args) throws ClassNotFoundException, IOException{
        /* Creating the game with the necessary parameters */

        /* Creation of player boards */
        PlayerBoard playerBoard1 = new PlayerBoard();
        PlayerBoard playerBoard2 = new PlayerBoard();
        /* Creation of players */
        Player player1 = new Player(1, playerBoard1);
        Player player2 = new Player(2, playerBoard2);
        /* Creation of the game board */
        TimeBoard timeBoard = new TimeBoard(player1, player2);
        /* Storage of players in a Map */
        Map<Integer, Player> players = Map.of(player1.getId(), player1, player2.getId(), player2);
        /* Creation of a list of pieces */
        PieceSet pieceSet = new PieceSet();

        /* Asking the user which version of the game he wants to play */
        Scanner sc = new Scanner(System.in);
        System.out.println("Choisissez votre version de jeu (base ou complet) :");
        String str = sc.nextLine();

        /* Initialization of the list of pieces depending on the version chosen */
        pieceSet.init(str);

        System.out.print("\nLANCEMENT DU JEU AVEC LA VERSION : " + str + "\n\n");

        /* Game loop */
        while(!timeBoard.endGame()){
            Game.status(player1, player2, pieceSet, timeBoard);

            Game.loop(pieceSet, players, timeBoard);

            System.out.println("\n========TOUR SUIVANT========\n");
        }

        /* Display of the end of the game */
        System.out.println("FIN DU JEU");
    }

}