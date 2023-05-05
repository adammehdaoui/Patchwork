package fr.uge.patchwork.controller;

import fr.uge.patchwork.model.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/**
 * Class containing all controller methods of the game.
 */
public interface Game {

    /**
     * Controller method to display the status of the game.
     * @param player1 : player 1
     * @param player2 : player 2
     * @param pieceList : list of pieces
     * @param timeBoard : game board
     */
    public static void status(Player player1, Player player2, PieceList pieceList,
                              TimeBoard timeBoard){
        /* Display the status of the game */
        System.out.println(player1);
        System.out.println(timeBoard);
        System.out.println(player2);

        /* Retrieving the ID of the player who must play */
        int idPlayerPrior = timeBoard.turnOf();

        System.out.println("C'est au tour du joueur " + idPlayerPrior + "\n");

        /* Displaying playable pieces */
        ArrayList<Piece> playablePieces = pieceList.nextPieces();
        System.out.println("Les pièces jouables sont : \n");
        for (int i = 0; i < 3; i++) {
            System.out.println("Pièce " + i + " : \n" + playablePieces.get(i));
        }
    }

    /**
     * Controller method to manage the game loop.
     * @param pieceList : list of pieces
     * @param players : Map of players by ID
     * @param timeBoard : game board
     */
    public static void loop(PieceList pieceList, Map<Integer, Player> players,
                            TimeBoard timeBoard) {
        int idPlayerPrior = timeBoard.turnOf();

        /* Asking the player if they want to buy a piece */
        Scanner sc = new Scanner(System.in);
        System.out.println("Voulez-vous acheter une pièce ? (oui/non)");
        String str = sc.nextLine();

        if (str.equals("oui")) {
            Game.buy(pieceList, players, timeBoard, idPlayerPrior);
        } else {
            Game.pass(players, timeBoard, idPlayerPrior);
        }
    }

    /**
     * Controller method to manage the case where the player wants to buy a piece.
     * @param pieceList : list of pieces
     * @param players : Map of players by ID
     * @param timeBoard : game board
     * @param idPlayerPrior : ID of the player who must play
     */
    public static void buy(PieceList pieceList, Map<Integer, Player> players,
                           TimeBoard timeBoard, int idPlayerPrior){
        int x, y;
        ArrayList<Piece> playablePieces = pieceList.nextPieces();
        Scanner sc = new Scanner(System.in);

        /* Asking the player which piece they want to buy */
        System.out.println("Quelle pièce voulez-vous acheter ? (1, 2, 3)");
        int idPiece = sc.nextInt();
        sc.nextLine();

        while(playablePieces.get(idPiece - 1).cost() > players.get(idPlayerPrior).getButtons()){
            System.out.println("Vous n'avez pas assez de boutons pour acheter cette pièce");

            System.out.println("Quelle pièce voulez-vous acheter ? (1, 2, 3)");
            idPiece = sc.nextInt();
            sc.nextLine();
        }

        /* Display the piece he wants to buy and ask him if he wants to rotate, invert or validate */
        System.out.println("Vous avez choisi la pièce : \n" + playablePieces.get(idPiece - 1));
        System.out.println("Que voulez-vous en faire ? (rotate/invert/validate)");
        String str = sc.nextLine();

        while(!str.equals("validate")){
            if(str.equals("rotate")){
                /* Replacement of the piece by the rotate piece */
                playablePieces.set(idPiece - 1, playablePieces.get(idPiece - 1).rotate());
            }
            else if(str.equals("invert")){
                /* Replacement of the piece with the invert piece */
                playablePieces.set(idPiece - 1, playablePieces.get(idPiece - 1).invert());
            }
            else{
                System.out.println("Vous n'avez pas choisi une action valide");
            }
            System.out.println("Votre pièce courante : \n" + playablePieces.get(idPiece - 1));
            System.out.println("Que voulez-vous en faire ? (rotate/invert/validate)");
            str = sc.nextLine();
        }

        /* Asking the user where he wants to place the piece on his board */
        System.out.println("Où voulez-vous placer cette pièce ? (x y)");
        x = sc.nextInt();
        y = sc.nextInt();
        sc.nextLine();

        if(players.get(idPlayerPrior).buyPiece(playablePieces.get(0), x, y)){
            /* Moving the player and getting the number of buttons passed and then adding the buttons won */
            int buttonToEarn = timeBoard.movePlayer(players.get(idPlayerPrior), playablePieces.get(0).time());

            for (int i = 0; i < buttonToEarn; i++) {
                players.get(idPlayerPrior).addButtons(players.get(idPlayerPrior).getEarnedButton());
            }

            pieceList.removePiece(0);
            System.out.println("Le joueur " + idPlayerPrior + " a acheté la pièce");
        } else {
            overtake(players, timeBoard, idPlayerPrior);
        }
    }

    /**
     * Controller method to manage the case where the player wants to overtake his opponent.
     * @param players : players Map by ID
     * @param timeBoard : game board
     * @param idPlayerPrior : ID of the player who must play
     */
    public static void overtake(Map<Integer, Player> players, TimeBoard timeBoard, int idPlayerPrior){
        /* Moving the player in front of his opponent */
        int distance = timeBoard.distance() + 1;
        timeBoard.movePlayer(players.get(idPlayerPrior), distance);
        /* Winning the distance in buttons */
        players.get(idPlayerPrior).addButtons(distance);
        System.out.println("Le joueur " + idPlayerPrior + " n'a pas pu acheter la pièce, il avance de " + distance + " cases");
    }

    /**
     * Controller method to manage the case where the player wants to pass.
     * @param players : players Map by ID
     * @param timeBoard : game board
     * @param idPlayerPrior : ID of the player who must play
     */
    public static void pass(Map<Integer, Player> players, TimeBoard timeBoard, int idPlayerPrior){
        /* Moving the player in front of his opponent */
        int distance = timeBoard.distance() + 1;
        timeBoard.movePlayer(players.get(idPlayerPrior), distance);
        /* Winning the distance in buttons */
        players.get(idPlayerPrior).addButtons(distance);
        System.out.println("Le joueur " + idPlayerPrior + " n'a pas acheté de pièce, il avance de " + distance + " case(s)");
    }

}
