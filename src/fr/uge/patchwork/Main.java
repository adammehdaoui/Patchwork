package fr.uge.patchwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println("Lancement du jeu...\n");

        // Création des plateaux des joueurs
        PlayerBoard playerBoard1 = new PlayerBoard(new boolean[9][9]);
        PlayerBoard playerBoard2 = new PlayerBoard(new boolean[9][9]);

        // Création des joueurs
        Player player1 = new Player(1, playerBoard1);
        Player player2 = new Player(2, playerBoard2);

        // On stocke les joueurs dans une map pour pouvoir les récupérer plus facilement
        Map<Integer, Player> players = Map.of(player1.getId(), player1, player2.getId(), player2);

        // Création d'un schéma de pièce (un carré de 2x2)
        var schema = new ArrayList<ArrayList<Boolean>>();
        schema.add(new ArrayList<>(List.of(true, true)));
        schema.add(new ArrayList<>(List.of(true, true)));

        // Création des pièces
        Piece piece1 = new Piece(schema, 3, 4, 1);
        Piece piece2 = new Piece(schema, 2, 2, 0);

        // Création d'un tableau de 40 pieces (piece 1 et 2 alternées)
        PieceList pieces = new PieceList();
        for (int i = 0; i < 20; i++) {
            pieces.addPiece(piece1);
            pieces.addPiece(piece2);
        }
        pieces.placeNeutral();

        // Création du plateau de jeu
        TimeBoard timeBoard = new TimeBoard(player1, player2);

        // Boucle de jeu
        while(!timeBoard.endGame()){

            // Affichage des playerboards et de le timeboard
            System.out.println(player1);
            System.out.println(timeBoard);
            System.out.println(player2);

            // On récupère l'id du joueur qui doit jouer
            int idPlayerPrio = timeBoard.turnOf();

            System.out.println("C'est au tour du joueur " + idPlayerPrio + "\n");

            // On affiche les pieces jouables
            ArrayList<Piece> playablePieces = pieces.nextPieces();
            System.out.println("Les pièces jouables sont :");
            for (int i = 0; i < 3; i++) {
                System.out.println("Pièce " + i + " : \n" + playablePieces.get(i));
            }

            // Le joueur id achète la pièce 1

            if(players.get(idPlayerPrio).buyPiece(playablePieces.get(0))){
                //on déplace le joueur et recupere le nombre de boutons passés puis on ajoute les boutons gagnés
                int buttonToEarn = timeBoard.movePlayer(players.get(idPlayerPrio), playablePieces.get(0).time());

                for (int i = 0; i < buttonToEarn; i++) {
                    players.get(idPlayerPrio).addButtons(players.get(idPlayerPrio).getEarnedButton());
                }

                // On retire la piece du tableau de pieces
                pieces.removePiece(0);
                System.out.println("Le joueur " + idPlayerPrio + " a acheté la pièce");
            } else {
                //on avance le joueur devant son adversaire
                int distance = timeBoard.distance() + 1;
                timeBoard.movePlayer(players.get(idPlayerPrio), distance);
                //il gagne la distance en boutons
                players.get(idPlayerPrio).addButtons(distance);
                System.out.println("Le joueur " + idPlayerPrio + " n'a pas pu acheter la pièce, il avance de " + distance + " cases");
            }

        }

        // Affiche fin de jeu
        System.out.println("FIN DU JEU");

    }

}