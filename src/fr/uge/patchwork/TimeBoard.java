package fr.uge.patchwork;

import java.util.ArrayList;

public class TimeBoard {
    private final ArrayList<Cell> cells;
    final static int nbCases = 54;

    //le dernier joueur qui a joué
    private int lastMove;

    public TimeBoard() {
        cells = new ArrayList<>();

        //on crée les cases et on place les boutons toutes les 6 cases
        for(int i=0; i<nbCases; i++){
            cells.add(new Cell());

            //on place les boutons de manière régulière
            if(i%6==5 && i>0){
                cells.get(i).setButton(true);
            }
        }

        //on place les joueurs à la première cellule du TimeBoard (début du jeu)
        cells.get(0).setPlayer1(true);
        cells.get(0).setPlayer2(true);

        lastMove = 1;
    }

    public int getLastMove(){
        return this.lastMove;
    }

    //déplace le joueur playerId de move cases et renvoie le nombre de boutons qu'il a traversé
    public int movePlayer(int playerId, int move){
        this.lastMove = playerId;

        for(int i=0; i<nbCases; i++){
            if(playerId == 1){
                if(cells.get(i).player1()){
                    cells.get(i).setPlayer1(false);
                    if(i+move >= nbCases){
                        cells.get(nbCases-1).setPlayer1(true);
                        return nbButton(i, nbCases);
                    }
                    else{
                        cells.get(i+move).setPlayer1(true);
                        return nbButton(i, i+move);
                    }
                }
            }
            else{
                if(cells.get(i).player2()){
                    cells.get(i).setPlayer2(false);
                    if(i+move >= nbCases){
                        cells.get(nbCases-1).setPlayer2(true);
                        return nbButton(i, nbCases);
                    }
                    else{
                        cells.get(i+move).setPlayer2(true);
                        return nbButton(i, i+move);
                    }
                }
            }
        }
        return 0;
    }

    //renvoie le nombre de bouton entre 2 indices
    public int nbButton(int start, int end){
        int res = 0;
        for(int i=start; i<end; i++){
            if(cells.get(i).button()){
                res++;
            }
        }
        return res;
    }

    //renvoie la valeur absolue de la distance entre les deux joueurs
    public int distance(){
        int player1 = 0;
        int player2 = 0;
        for(int i=0; i<nbCases; i++){
            if(cells.get(i).player1()){
                player1 = i;
            }
            if(cells.get(i).player2()){
                player2 = i;
            }
        }
        return Math.abs(player2 - player1);
    }

    //renvoie true si les deux joueurs ont atteint la fin du plateau
    public boolean endGame(){
        return cells.get(nbCases-1).player1() && cells.get(nbCases-1).player2();
    }

    //renvoie le numéro du joueur qui doit jouer
    public int turnOf(){
        int player1 = 0;
        int player2 = 0;
        for(int i=0; i<nbCases; i++){
            if(cells.get(i).player1()){
                player1 = i;
            }
            if(cells.get(i).player2()){
                player2 = i;
            }
        }
        if(player1 == player2){
            return lastMove;
        }
        else if(player1 < player2){
            return 1;
        }
        else{
            return 2;
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("|");

        for(Cell cell : cells){
            if(cell.button()){
                sb.append("°");
            }
            if(cell.player1()){
                sb.append("1");
            }
            if(cell.player2()){
                sb.append("2");
            }

            sb.append("|");
        }

        return sb.toString();
    }

}
