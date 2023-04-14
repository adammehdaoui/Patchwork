package fr.uge.patchwork;

public class Main {

    public static void main(String[] args){
        System.out.println("Lancement du jeu...\n");

        // Création des plateaux des joueurs
        PlayerBoard playerBoard1 = new PlayerBoard(new boolean[9][9]);
        PlayerBoard playerBoard2 = new PlayerBoard(new boolean[9][9]);

        // Création des joueurs
        Player player1 = new Player(1, playerBoard1);
        Player player2 = new Player(2, playerBoard2);

        // Création d'un schéma de pièce (un carré de 2x2)
        boolean[][] schema = new boolean[2][2];
        java.util.Arrays.fill(schema[0], true);
        java.util.Arrays.fill(schema[1], true);

        // Création des pièces
        Piece piece1 = new Piece(schema, 3, 4, 1);
        Piece piece2 = new Piece(schema, 2, 2, 0);

        // Création du plateau de jeu
        TimeBoard timeBoard = new TimeBoard();

        // Le joueur 1 achete la piece 1
        player1.buyPiece(piece1);

        // Le joueur 2 achete la piece 1 et 2
        player2.buyPiece(piece1);
        player2.buyPiece(piece2);

        //print du jeu
        System.out.println(timeBoard);
        System.out.println(playerBoard1);
        System.out.println(playerBoard2);




    }

}