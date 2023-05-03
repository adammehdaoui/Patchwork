package fr.uge.patchwork.model;

import java.util.ArrayList;

/**
 * Classe dédiée à la représentation d'une cellule du plateau de jeu ("TimeBoard").
 */
public class Cell {

    private Player player1;
    private Player player2;
    private boolean button;

    /**
     * Constructeur de la classe Cell.
     */
    public Cell(){
        player1 = null;
        player2 = null;
        button = false;
    }

    public Player getPlayer(Player player){
        if(player.getId() == 1)
            return player1;
        else
            return player2;
    }
    public void setPlayer(Player player){
        if(player.getId() == 1)
            player1 = player;
        else
            player2 = player;
    }

    public void setButton(boolean b){
        button = b;
    }

    /**
     * Libère le joueur de la cellule.
     * @param player : joueur à libérer
     */
    public void freePlayer(Player player){
        if(player.getId() == 1){
            player1 = null;
        }
        else{
            player2 = null;
        }
    }

    /**
     * Renvoie la liste des joueurs présents sur la cellule.
     * @return : liste des joueurs présents sur la cellule
     */
    public ArrayList<Player> getPlayers(){
        ArrayList<Player> players = new ArrayList<Player>(2);
        if(player1 != null)
            players.add(player1);
        if(player2 != null)
            players.add(player2);
        return players;
    }

    public Player player1(){
        return player1;
    }

    public Player player2(){
        return player2;
    }

    public boolean button(){
        return button;
    }
}
