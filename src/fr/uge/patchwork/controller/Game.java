package fr.uge.patchwork.controller;

import fr.uge.patchwork.model.*;

import java.util.ArrayList;
import java.util.InputMismatchException;
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
    static void status(Player player1, Player player2, PieceSet pieceList,
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
     * @param gameVersion : version of the game
     */
    static void progress(PieceSet pieceList, Map<Integer, Player> players,
                            TimeBoard timeBoard, String gameVersion) {
        int idPlayerPrior = timeBoard.turnOf();
        System.out.println("\n==========TOUR SUIVANT==========\n");

        Game.status(players.get(1), players.get(2), pieceList, timeBoard);

        /* Asking the player if they want to buy a piece */
        Scanner sc = new Scanner(System.in);
        System.out.println("Voulez-vous acheter une pièce ? (oui/non)");
        String str = sc.nextLine();

        if (str.equals("oui")) {
            Game.buy(pieceList, players, timeBoard, idPlayerPrior);
        } else {
            Game.overtake(players, timeBoard, idPlayerPrior);
        }

        if(gameVersion.equals("2")){
            if(players.get(1).getBoard().isSpecialPieceEarnable() && timeBoard.isSpecialPieceAvailable()){
                    players.get(1).setSpecialPiece(true);
                    timeBoard.setSpecialPieceAvailable(false);
                    System.out.println("Le joueur 1 a gagné la tuile spéciale.");
            }

            if(players.get(2).getBoard().isSpecialPieceEarnable() && timeBoard.isSpecialPieceAvailable()){
                players.get(2).setSpecialPiece(true);
                timeBoard.setSpecialPieceAvailable(false);
                System.out.println("Le joueur 2 a gagné la tuile spéciale.");
            }
        }
    }

    /**
     * Controller method to manage the case where the player wants to buy a piece.
     * @param pieceList     : list of pieces
     * @param players       : Map of players by ID
     * @param timeBoard     : game board
     * @param idPlayerPrior : ID of the player who must play
     */
    static void buy(PieceSet pieceList, Map<Integer, Player> players,
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
        System.out.println("Que voulez-vous en faire ? (actions : rotate/invert/validate)");
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
        System.out.println("Veuillez choisir une position pour votre pièce ? (x y)");
        x = sc.nextInt();
        y = sc.nextInt();
        sc.nextLine();

        if(players.get(idPlayerPrior).buyPiece(playablePieces.get(0), x, y)){
            /* Moving the player and getting the number of buttons passed and then adding the buttons won */
            timeBoard.movePlayer(players.get(idPlayerPrior), playablePieces.get(0).time());
            pieceList.removePiece(0);
            System.out.println("Le joueur " + idPlayerPrior + " a acheté une pièce et l'a placé sur son plateau.");
        } else {
            overtake(players, timeBoard, idPlayerPrior);
        }
    }

    /**
     * Controller method to manage the case where the player wants to pass.
     * @param players       : players Map by ID
     * @param timeBoard     : game board
     * @param idPlayerPrior : ID of the player who must play
     */
    static void overtake(Map<Integer, Player> players, TimeBoard timeBoard, int idPlayerPrior){
        int buttonsEarned, patchesEarned;

        /* Predicting movement before cleaning the board */
        int distance = timeBoard.distance() + 1;
        Map<String, Integer> movement = timeBoard.predictMovement(players.get(idPlayerPrior), distance);

        /* The player gets the number of buttons crossed */
        buttonsEarned = timeBoard.nbButton(movement.get("start"), movement.get("end"));
        players.get(idPlayerPrior).addButtons(buttonsEarned);
        if(buttonsEarned > 0){
            System.out.println("Le joueur " + idPlayerPrior + " a gagné " + buttonsEarned + " bouton(s) en passant.");
        }

        /* The player earns the patches he has passed */
        patchesEarned = timeBoard.nbPatch(movement.get("start"), movement.get("end"));
        if(patchesEarned > 0){
            reward(players, patchesEarned, idPlayerPrior);
        }

        /* Winning the distance in buttons */
        players.get(idPlayerPrior).addButtons(distance);

        /* Moving the player in front of his opponent */
        timeBoard.movePlayer(players.get(idPlayerPrior), distance);

        System.out.println("Le joueur " + idPlayerPrior + " dépasse son adversaire, il avance de " + distance + " case(s).");
    }

    /**
     * Controller method to manage the case where the player is rewarded with patches.
     * @param players : players Map by ID
     * @param patchesEarned : number of patches earned
     * @param idPlayerPrior : ID of the player who must play
     */
    static void reward(Map<Integer, Player> players, int patchesEarned, int idPlayerPrior){
        Scanner sc = new Scanner(System.in);
        int x = -1;
        int y = -1;
        boolean validIntegers = false;

        if(patchesEarned > 0){
            for (int i = 0; i < patchesEarned; i++) {
                System.out.println("Vous avez gagné une pièce spéciale 1x1! Veuillez choisir où la placer (x y).");

                while(!validIntegers){
                    try {
                        x = sc.nextInt();
                        y = sc.nextInt();
                        sc.nextLine();
                        validIntegers = true;
                    } catch (InputMismatchException e){
                        System.out.println("Les entiers entrés ne sont pas valides, veuillez essayer à nouveau (x y).");
                        sc.nextLine();
                    }
                }


                var schema = new ArrayList<ArrayList<Boolean>>();
                var row = new ArrayList<Boolean>();
                row.add(true);
                schema.add(row);
                var square = new Piece(schema, 0, 0, 0);

                while(!players.get(idPlayerPrior).buyPiece(square, x, y)){
                    System.out.println("Vous ne pouvez pas placer la pièce à ces positions, veuillez choisir d'autres coordonnées (x y).");
                    x = sc.nextInt();
                    y = sc.nextInt();
                }
            }
        }
    }

    /**
     * Controller method to display the winner of the game.
     * @param players : players Map by ID
     */
    static void end(Map<Integer, Player> players){
        int scorePlayer1 = players.get(1).score();
        int scorePlayer2 = players.get(2).score();

        if(scorePlayer1 > scorePlayer2){
            System.out.println("\nLe joueur 1 a gagné avec " + scorePlayer1 + " points contre " + scorePlayer2 +
                    " points.");
        } else if(scorePlayer1 < scorePlayer2){
            System.out.println("\nLe joueur 2 a gagné avec " + scorePlayer2 + " points contre " + scorePlayer1 +
                    " points.");
        } else {
            System.out.println("\nÉgalité parfaite entre les deux joueurs avec " + scorePlayer1 + " points.");
        }
    }

}
