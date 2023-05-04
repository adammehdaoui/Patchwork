package fr.uge.patchwork.controller;

import fr.uge.patchwork.model.*;
/*import fr.uge.patchwork.view.*;*/

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Contrôleur principal du jeu.
 */
public class Main {

    /**
     * Méthode principale du jeu.
     * @param args : arguments de la ligne de commande
     * @throws ClassNotFoundException : si la classe n'est pas trouvée
     * @throws FileNotFoundException : si le fichier n'est pas trouvé
     */
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        System.out.println("Lancement du jeu...\n");

        /* Création des plateaux des joueurs */
        PlayerBoard playerBoard1 = new PlayerBoard();
        PlayerBoard playerBoard2 = new PlayerBoard();

        /* Création des joueurs */
        Player player1 = new Player(1, playerBoard1);
        Player player2 = new Player(2, playerBoard2);

        /* On stocke les joueurs dans une Map */
        Map<Integer, Player> players = Map.of(player1.getId(), player1, player2.getId(), player2);

        /* Position des pièces à placer */
        int x;
        int y;

        PieceList pieceList = new PieceList();

        Scanner sc = new Scanner(System.in);
        System.out.println("Choisissez votre version de jeu (base ou complet) :");
        String str = sc.nextLine();

        /* Jeu de base */
        if(str.equals("base")){
            /* Création d'un schéma de pièce (un carré de 2x2) */
            var schema = new ArrayList<ArrayList<Boolean>>();
            schema.add(new ArrayList<>(List.of(true, true)));
            schema.add(new ArrayList<>(List.of(true, true)));

            Piece piece1 = new Piece(schema, 3, 4, 1);
            Piece piece2 = new Piece(schema, 2, 2, 0);

            /* Création d'un tableau de 40 pièces (pièces 1 et 2 alternées) */
            for (int i = 0; i < 20; i++) {
                pieceList.addPiece(piece1);
                pieceList.addPiece(piece2);
            }

        }
        /* Jeu complet */
        else if(str.equals("complet")){
            /* Création des pièces du vrai jeu */
            var file = new ReadFile(Path.of("allPieces.txt"));
            pieceList = file.read();
        }
        /* Version invalide */
        else{
            System.out.println("Vous n'avez pas choisi une version valide du jeu.");
            System.exit(0);
        }

        pieceList.placeNeutral();

        /* Création du plateau de jeu */
        TimeBoard timeBoard = new TimeBoard(player1, player2);

        /* Boucle de jeu */
        while(!timeBoard.endGame()){
            /* Affichage des playerboards et du timeboard */
            System.out.println(player1);
            System.out.println(timeBoard);
            System.out.println("\n");
            System.out.println(player2);

            /* On récupère l'id du joueur qui doit jouer */
            int idPlayerPrio = timeBoard.turnOf();

            System.out.println("C'est au tour du joueur " + idPlayerPrio + "\n");

            /* On affiche les pièces jouables */
            ArrayList<Piece> playablePieces = pieceList.nextPieces();
            System.out.println("Les pièces jouables sont :");
            for (int i = 0; i < 3; i++) {
                System.out.println("Pièce " + i + " : \n" + playablePieces.get(i));
            }

            /* On demande au joueur si il veut acheter une pièce */
            System.out.println("Voulez-vous acheter une pièce ? (oui/non)");
            String str2 = sc.nextLine();
            if (str2.equals("oui")) {
                /* On demande au joueur quelle pièce il veut acheter */
                System.out.println("Quelle pièce voulez-vous acheter ? (1, 2, 3)");
                int idPiece = sc.nextInt();
                sc.nextLine();

                while(playablePieces.get(idPiece - 1).cost() > players.get(idPlayerPrio).getButtons()){
                    System.out.println("Vous n'avez pas assez de boutons pour acheter cette pièce");

                    System.out.println("Quelle pièce voulez-vous acheter ? (1, 2, 3)");
                    idPiece = sc.nextInt();
                    sc.nextLine();
                }

                /* On affiche la pièce qu'il veut acheter et on lui demande s'il veut rotate,
                invert ou validate */
                System.out.println("Vous avez choisi la pièce : \n" + playablePieces.get(idPiece - 1));
                System.out.println("Que voulez-vous en faire ? (rotate/invert/validate)");
                String str3 = sc.nextLine();

                while(!str3.equals("validate")){
                    if(str3.equals("rotate")){
                        /* Remplace la pièce par la pièce rotate */
                        playablePieces.set(idPiece - 1, playablePieces.get(idPiece - 1).rotate());
                    }
                    else if(str3.equals("invert")){
                        /* Remplace la pièce par la pièce invert */
                        playablePieces.set(idPiece - 1, playablePieces.get(idPiece - 1).invert());
                    }
                    else{
                        System.out.println("Vous n'avez pas choisi une action valide");
                    }
                    System.out.println("Votre piece : \n" + playablePieces.get(idPiece - 1));
                    System.out.println("Que voulez-vous en faire ? (rotate/invert/validate)");
                    str3 = sc.nextLine();
                }

                /* On demande à l'utilisateur où il veut placer la pièce sur son board */
                System.out.println("Ou voulez-vous placer la pièce ? (x y)");
                x = sc.nextInt();
                y = sc.nextInt();
                sc.nextLine();

                /* Le joueur {id} achète la pièce 1 */
                if(players.get(idPlayerPrio).buyPiece(playablePieces.get(0), x, y)){
                    /* On déplace le joueur et récupère le nombre de boutons passés puis on ajoute les boutons gagnés */
                    int buttonToEarn = timeBoard.movePlayer(players.get(idPlayerPrio), playablePieces.get(0).time());

                    for (int i = 0; i < buttonToEarn; i++) {
                        players.get(idPlayerPrio).addButtons(players.get(idPlayerPrio).getEarnedButton());
                    }

                    pieceList.removePiece(0);
                    System.out.println("Le joueur " + idPlayerPrio + " a acheté la pièce");
                } else {
                    /* On avance le joueur devant son adversaire */
                    int distance = timeBoard.distance() + 1;
                    timeBoard.movePlayer(players.get(idPlayerPrio), distance);
                    /* Il gagne la distance en boutons */
                    players.get(idPlayerPrio).addButtons(distance);
                    System.out.println("Le joueur " + idPlayerPrio + " n'a pas pu acheter la pièce, il avance de " + distance + " cases");
                }

                //TODO changer la fonction buyPiece pour qu'elle prenne en parametre les coordonnées
                //TODO ajouter dans chaque question un "passé mon tour" si le joueur veut abandonner.
                //TODO finir le else

            } else {

                //TODO pas besoin de else ?

            }
        }

        /* Affichage de la fin du jeu */
        System.out.println("FIN DU JEU");
    }

}