package fr.uge.patchwork.view;

import fr.uge.patchwork.model.Piece;
import fr.uge.patchwork.model.Player;
import fr.uge.patchwork.model.TimeBoard;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public final class ConsoleView {

    public static void statusView(Player player1, Player player2, TimeBoard timeBoard,
                                  int idPlayerPrior, ArrayList<Piece> playablePieces){
        /* Display the status of the game */
        System.out.println(player1);
        System.out.println(timeBoard);
        System.out.println(player2);

        /* Displaying the player who must play */
        System.out.println("C'est au tour du joueur " + idPlayerPrior + "\n");

        /* Displaying playable pieces */
        System.out.println("Les pièces jouables sont : \n");
        for (int i = 0; i < 3; i++) {
            System.out.println("Pièce " + i + " : \n" + playablePieces.get(i));
        }
    }

    public static void nextRound(){
        System.out.println("\n========== TOUR SUIVANT ==========\n");
    }

    public static void askingForPiece(){
        System.out.println("Voulez-vous acheter une pièce ? (oui/non)");
    }

    public static void whichPiece(){
        System.out.println("Quelle pièce voulez-vous acheter ? (1, 2, 3)");
    }

    public static void cantBuyPiece(){
        System.out.println("Vous n'avez pas assez de boutons pour acheter cette pièce");
        System.out.println("Quelle pièce voulez-vous acheter ? (1, 2, 3)");
    }

    public static void actionsOnPiece(ArrayList<Piece> playablePieces, int idPiece){
        System.out.println("Vous avez choisi la pièce : \n" + playablePieces.get(idPiece - 1));
        System.out.println("Que voulez-vous en faire ? (actions : rotate/invert/validate)");
    }

    public static void notValid(){
        System.out.println("Vous n'avez pas choisi une action valide");
    }

    public static void notValidPositions(){
        System.out.println("Les entiers entrés ne sont pas valides, veuillez essayer à nouveau " +
                "(ligne colonne)");
    }

    public static void positions(){
        System.out.println("Veuillez choisir une position pour votre pièce ? (ligne colonne)");
    }

    public static void playerMove(int idPlayerPrior, ArrayList<Piece> playablePieces, int idPiece){
        System.out.println("Le joueur " + idPlayerPrior + " a acheté une pièce et l'a placé sur son plateau." +
                " Il a avancé de " + playablePieces.get(idPiece - 1).time() + " cases.");
    }

    public static void cantPlace(){
        System.out.println("Vous ne pouvez pas placer cette pièce à cet endroit. Vous passez donc votre tour.");
    }

    public static void cantPlacePatch(){
        System.out.println("Vous ne pouvez pas placer la pièce à ces positions, veuillez choisir d'autres " +
                "coordonnées (ligne colonne)");
    }

    public static void wasLeader(int idPlayerPrior, int distance){
        System.out.println("Le joueur " + idPlayerPrior + " était en tête, il a donc avancé de " + distance + " case.");
    }

    public static void wasBehind(int idPlayerPrior, int distance){
        System.out.println("Le joueur " + idPlayerPrior + " a décidé de passer son tour. Il a donc dépassé son " +
                "adversaire en parcourant "+ distance + " cases.");
    }

    public static void crossButton(int idPlayerPrior, Map<Integer, Player> players){
        System.out.println("Le joueur " + idPlayerPrior + " a gagné " + players.get(idPlayerPrior).buttonsToEarn()
                + " bouton(s) en passant sur une case bouton.");
    }

    public static void crossPatch(){
        System.out.println("Vous avez gagné un patch spécial 1x1 en passant sur une case dédiée ! " +
                "Veuillez choisir où la placer (ligne colonne).");
    }

    public static void endView(int scorePlayer1, int scorePlayer2){
        if (scorePlayer1 > scorePlayer2) {
            System.out.println("\nLe joueur 1 a gagné avec " + scorePlayer1 + " points contre " + scorePlayer2 +
                    " points.");
        } else if (scorePlayer1 < scorePlayer2) {
            System.out.println("\nLe joueur 2 a gagné avec " + scorePlayer2 + " points contre " + scorePlayer1 +
                    " points.");
        } else {
            System.out.println("\nÉgalité parfaite entre les deux joueurs avec " + scorePlayer1 + " points.");
        }
    }
}
