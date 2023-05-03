package fr.uge.patchwork.model;

import java.util.ArrayList;

/**
 * Classe dédiée à la représentation du plateau de jeu ("TimeBoard").
 */
public class TimeBoard {
    private final ArrayList<Cell> cells;
    final static int nbCases = 54;

    /* On stocke ici le dernier joueur qui a joué */
    private Player priorityPlayer;

    /**
     * Constructeur de la classe TimeBoard.
     * @param player1 : joueur 1
     * @param player2 : joueur 2
     */
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

    /**
     * Renvoie le joueur prioritaire (le dernier joueur qui a joué).
     * @return : joueur prioritaire
     */
    public Player getPriorityPlayer(){
        return priorityPlayer;
    }

    /**
     * Déplace le joueur en fonction du nombre de cases indiqué.
     * @param player : joueur à déplacer
     * @param move : nombre de cases à avancer
     * @return : nombre de boutons entre l'ancienne et la nouvelle position du joueur
     * @throws ClassNotFoundException : joueur non trouvé
     */
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

    /**
     * Renvoie le nombre de boutons entre deux cases.
     * @param start : case de départ
     * @param end : case d'arrivée
     * @return : nombre de boutons entre les deux cases
     */
    public int nbButton(int start, int end){
        int res = 0;
        for(int i=start; i<end; i++){
            if(cells.get(i).button()){
                res++;
            }
        }
        return res;
    }

    /**
     * Renvoie la distance entre les deux joueurs.
     * @return : distance entre les deux joueurs
     */
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

    /**
     * Renvoie vrai si les deux joueurs sont sur la dernière case.
     * @return : vrai si les deux joueurs sont sur la dernière case
     */
    public boolean endGame(){
        return cells.get(nbCases - 1).player1() != null && cells.get(nbCases - 1).player2() != null;
    }

    /**
     * Renvoie le joueur qui doit jouer.
     * @return : joueur qui doit jouer
     */
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
