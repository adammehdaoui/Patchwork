package fr.uge.patchwork.model;

import java.util.ArrayList;

/**
 * Class dedicated to the representation of a cell of the game board ("TimeBoard").
 */
public class Cell {

    private Player player1;
    private Player player2;
    private boolean button;
    private boolean patchwork;

    /**
     * Constructor of the class Cell.
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
    public void setPatchwork(boolean b) {
        patchwork = b;
    }
    /**
     * Free the player from the cell.
     * @param player : player to free
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
     * Return the list of players on the cell.
     * @return : list of players on the cell
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

    public boolean patchwork(){
        return patchwork;
    }
}
