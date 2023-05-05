package fr.uge.patchwork.model;

import java.util.ArrayList;

/**
 * Class dedicated to the representation of a player of the game.
 */
public class Player {

    private final int id;
    private int buttons;
    private ArrayList<Piece> pieces;
    private PlayerBoard board;

    /**
     * Constructor of the class Player.
     * @param id : ID of the player
     * @param board : player's board
     */
    public Player(int id, PlayerBoard board){
        this.id = id;
        this.buttons = 5;
        this.pieces = new ArrayList<Piece>();
        this.board = board;
    }

    public int getId(){
        return id;
    }

    public int getButtons(){
        return buttons;
    }

    public void addButtons(int buttons){
        this.buttons += buttons;
    }

    /**
     * Buy a piece and place it on the player's board.
     * @param piece : piece to buy
     * @param x : x coordinate
     * @param y: y coordinate
     * @return : true if the piece has been bought and placed, false otherwise
     */
    public boolean buyPiece(Piece piece, int x, int y){
        if(buttons >= piece.cost()){
            /* Trying to place the piece on the board */
            if(board.placePiece(piece, x, y)){
                buttons -= piece.cost();
                pieces.add(piece);
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    /**
     * Returns the number of buttons earned by the player.
     * @return : number of buttons earned by the player
     */
    public int getEarnedButton(){
        int res = 0;
        for (Piece piece : pieces) {
            res += piece.button();
        }
        return res;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Player ").append(id).append(", ");
        sb.append("Buttons : ").append(buttons).append(", ");
        sb.append("Board :\n").append(board).append("\n");
        return sb.toString();
    }
}
