package fr.uge.patchwork.model;

import java.util.ArrayList;

public class TimeBoard {
    private final ArrayList<Cell> cells;
    final static int nbCases = 54;

    /* Le dernier joueur qui a joué */
    private Player priorityPlayer;

    public TimeBoard(Player player1, Player player2) {
        cells = new ArrayList<>();

        /* On crée les cases et on place les boutons toutes les 6 cases */

        for(int i=0; i<nbCases; i++){
            cells.add(new Cell());

            /* On place les boutons de manière régulière */
            if(i % 6 == 5){
                cells.get(i).setButton(true);
            }
        }

        /* On place les joueurs à la première cellule du TimeBoard (début du jeu) */
        cells.get(0).setPlayer(player1);
        cells.get(0).setPlayer(player2);

        priorityPlayer = player1;
    }

    public Player getPriorityPlayer(){
        return priorityPlayer;
    }

    /* Déplace le joueur playerId de move cases et renvoie le nombre de boutons qu'il a traversé */
    public int movePlayer(Player player, int move) throws ClassNotFoundException {
        int index = -1;
        int moveTo;

        for(int i=0; i<nbCases; i++){
            if(cells.get(i).getPlayer(player) != null){
                index = i;
                break;
            }
        }

        if(index<0){
            throw new ClassNotFoundException("Le joueur n'a pas été trouvé dans le board principal");
        }

        cells.get(index).freePlayer(player);

        if(index+move >= nbCases){
            moveTo=nbCases-1;
        }
        else{
            moveTo=index+move;
        }

        cells.get(moveTo).setPlayer(player);

        return nbButton(index, moveTo);
    }

    // Renvoie le nombre de bouton entre 2 indices
    public int nbButton(int start, int end){
        int res = 0;
        for(int i=start; i<end; i++){
            if(cells.get(i).button()){
                res++;
            }
        }
        return res;
    }

    // Renvoie la valeur absolue de la distance entre les deux joueurs
    public int distance(){
        int posP1 = 0;
        int posP2 = 0;

        for(int i=0; i<nbCases; i++){
            if(cells.get(i).player1() != null){
                posP1 = i;
            }
            if(cells.get(i).player2() != null){
                posP2 = i;
            }
        }

        return Math.abs(posP2 - posP1);
    }

    // Renvoie true si les deux joueurs ont atteint la fin du plateau
    public boolean endGame(){
        return cells.get(nbCases - 1).player1() != null && cells.get(nbCases - 1).player2() != null;
    }

    // Renvoie l'id du joueur qui doit jouer
    public int turnOf(){
        int posP1 = 0;
        int posP2 = 0;

        for(int i=0; i<nbCases; i++){
            if(cells.get(i).player1() != null){
                posP1 = i;
            }
            if(cells.get(i).player2() != null){
                posP2 = i;
            }
        }

        if(posP1 == posP2){
            return priorityPlayer.getId();
        }
        else if(posP1 < posP2){
            return 1;
        }
        else{
            return 2;
        }
    }

    @Override
    public String toString(){
        int i=1;
        StringBuilder sb = new StringBuilder();

        sb.append("TimeBoard :\n------------------------------\n");

        for(Cell cell : cells){
            sb.append("|");

            if(cell.button()){
                sb.append("°");
            }
            else{
                sb.append(" ");
            }

            if(cell.player1() != null){
                sb.append("1");
            }
            else{
                sb.append(" ");
            }

            if(cell.player2() != null){
                sb.append("2");
            }
            else{
                sb.append(" ");
            }

            sb.append("|");

            if(i%6 == 0 && i!=0){
                sb.append("\n").append("------------------------------").append("\n");
            }

            i++;
        }

        return sb.toString();
    }

}
