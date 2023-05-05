package fr.uge.patchwork.controller;

import fr.uge.patchwork.model.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
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
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        System.out.println("Lancement du jeu...\n");

        /* Creation of player boards */
        PlayerBoard playerBoard1 = new PlayerBoard();
        PlayerBoard playerBoard2 = new PlayerBoard();

        /* Creation of players */
        Player player1 = new Player(1, playerBoard1);
        Player player2 = new Player(2, playerBoard2);

        /* Storage of players in a Map */
        Map<Integer, Player> players = Map.of(player1.getId(), player1, player2.getId(), player2);

        /* Position of the pieces to be placed */
        int x;
        int y;

        PieceList pieceList = new PieceList();

        Scanner sc = new Scanner(System.in);
        System.out.println("Choisissez votre version de jeu (base ou complet) :");
        String str = sc.nextLine();

        /* Base game */
        if(str.equals("base")){
            /* Creation of a piece diagram */
            var schema = new ArrayList<ArrayList<Boolean>>();
            schema.add(new ArrayList<>(List.of(true, true)));
            schema.add(new ArrayList<>(List.of(true, true)));

            Piece piece1 = new Piece(schema, 3, 4, 1);
            Piece piece2 = new Piece(schema, 2, 2, 0);

            /* Creation of a table of 40 pieces (pieces 1 and 2 alternated) */
            for (int i = 0; i < 20; i++) {
                pieceList.addPiece(piece1);
                pieceList.addPiece(piece2);
            }

        }
        /* Complete game */
        else if(str.equals("complet")){
            /* Creation of the pieces of the complete game version */
            var file = new ReadFile(Path.of("pieces.txt"));
            pieceList = file.read();
        }
        /* Invalid version */
        else{
            System.out.println("Vous n'avez pas choisi une version valide du jeu.");
            System.exit(0);
        }

        pieceList.placeNeutral();

        /* Creation of the game board */
        TimeBoard timeBoard = new TimeBoard(player1, player2);

        /* Game loop */
        while(!timeBoard.endGame()){
            /* Display of playerboards en timeboard */
            System.out.println(player1);
            System.out.println(timeBoard);
            System.out.println("\n");
            System.out.println(player2);

            /* Retrieving the ID of the player who must play */
            int idPlayerPrio = timeBoard.turnOf();

            System.out.println("C'est au tour du joueur " + idPlayerPrio + "\n");

            /* Displaying playable pieces */
            ArrayList<Piece> playablePieces = pieceList.nextPieces();
            System.out.println("Les pièces jouables sont :");
            for (int i = 0; i < 3; i++) {
                System.out.println("Pièce " + i + " : \n" + playablePieces.get(i));
            }

            /* Asking the player if they want to buy a piece */
            System.out.println("Voulez-vous acheter une pièce ? (oui/non)");
            String str2 = sc.nextLine();
            if (str2.equals("oui")) {
                /* Asking the player which piece they want to buy */
                System.out.println("Quelle pièce voulez-vous acheter ? (1, 2, 3)");
                int idPiece = sc.nextInt();
                sc.nextLine();

                while(playablePieces.get(idPiece - 1).cost() > players.get(idPlayerPrio).getButtons()){
                    System.out.println("Vous n'avez pas assez de boutons pour acheter cette pièce");

                    System.out.println("Quelle pièce voulez-vous acheter ? (1, 2, 3)");
                    idPiece = sc.nextInt();
                    sc.nextLine();
                }

                /* Display the piece he wants to buy and ask him if he wants to rotate, invert or validate */
                System.out.println("Vous avez choisi la pièce : \n" + playablePieces.get(idPiece - 1));
                System.out.println("Que voulez-vous en faire ? (rotate/invert/validate)");
                String str3 = sc.nextLine();

                while(!str3.equals("validate")){
                    if(str3.equals("rotate")){
                        /* Replacement of the piece by the rotate piece */
                        playablePieces.set(idPiece - 1, playablePieces.get(idPiece - 1).rotate());
                    }
                    else if(str3.equals("invert")){
                        /* Replacement of the piece with the invert piece */
                        playablePieces.set(idPiece - 1, playablePieces.get(idPiece - 1).invert());
                    }
                    else{
                        System.out.println("Vous n'avez pas choisi une action valide");
                    }
                    System.out.println("Votre piece : \n" + playablePieces.get(idPiece - 1));
                    System.out.println("Que voulez-vous en faire ? (rotate/invert/validate)");
                    str3 = sc.nextLine();
                }

                /* Asking the user where he wants to place the piece on his board */
                System.out.println("Où voulez-vous placer la pièce ? (x y)");
                x = sc.nextInt();
                y = sc.nextInt();
                sc.nextLine();

                if(players.get(idPlayerPrio).buyPiece(playablePieces.get(0), x, y)){
                    /* Moving the player and getting the number of buttons passed and then adding the buttons won */
                    int buttonToEarn = timeBoard.movePlayer(players.get(idPlayerPrio), playablePieces.get(0).time());

                    for (int i = 0; i < buttonToEarn; i++) {
                        players.get(idPlayerPrio).addButtons(players.get(idPlayerPrio).getEarnedButton());
                    }

                    pieceList.removePiece(0);
                    System.out.println("Le joueur " + idPlayerPrio + " a acheté la pièce");
                } else {
                    /* Moving the player in front of his opponent */
                    int distance = timeBoard.distance() + 1;
                    timeBoard.movePlayer(players.get(idPlayerPrio), distance);
                    /* Winning the distance in buttons */
                    players.get(idPlayerPrio).addButtons(distance);
                    System.out.println("Le joueur " + idPlayerPrio + " n'a pas pu acheter la pièce, il avance de " + distance + " cases");
                }
            } else {
                /* Moving the player in front of his opponent */
                int distance = timeBoard.distance() + 1;
                timeBoard.movePlayer(players.get(idPlayerPrio), distance);
                /* Winning the distance in buttons */
                players.get(idPlayerPrio).addButtons(distance);
                System.out.println("Le joueur " + idPlayerPrio + " n'a pas acheté de pièce, il avance de " + distance + " case(s)");
            }
        }

        /* Display of the end of the game */
        System.out.println("FIN DU JEU");
    }

}