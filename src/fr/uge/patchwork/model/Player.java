package fr.uge.patchwork.model;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Class dedicated to the representation of a player of the game.
 */
public class Player {

    private final int id;
    private int buttons;

    private boolean specialPiece;
    private final ArrayList<Piece> pieces;
    private final PlayerBoard board;

    /**
     * Constructor of the class Player.
     * @param id ID of the player
     * @param board player's board
     */
    public Player(int id, PlayerBoard board){
        if(id != 1 && id != 2)
            throw new IllegalArgumentException("L'ID du joueur doit être 1 ou 2.");

        Objects.requireNonNull(board);

        this.id = id;
        this.buttons = 5;
        this.pieces = new ArrayList<>();
        this.board = board;
    }

    public int getId(){
        return id;
    }

    public int getButtons(){
        return buttons;
    }

    public void setSpecialPiece(boolean b){
        specialPiece = b;
    }

    public ArrayList<Piece> getPieces(){
        return pieces;
    }

    public PlayerBoard getBoard(){
        return board;
    }

    public void addButtons(int buttons){
        this.buttons += buttons;
    }

    /**
     * Buy a piece and place it on the player's board.
     * @param piece piece to buy
     * @param x x coordinate
     * @param y y coordinate
     * @return true if the piece has been bought and placed, false otherwise
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
     * Calculate the number of buttons a player can earn.
     * @return number of buttons earned by the player
     */
    public int buttonsToEarn(){
        int sum = 0;

        for (Piece piece : pieces) {
            sum += piece.button();
        }

        return sum;
    }

    /**
     * Calculate the score of the player according to the rules of the Patchwork game.
     * @return score of the player
     */
    public int score(){
        int score = 0;
        score += buttons;

        if(specialPiece){
            score += 7;
        }

        score -= board.countEmptyCells();

        return score;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Joueur ").append(id).append(", ");
        sb.append("Boutons : ").append(buttons).append(", ");
        sb.append("Board :\n").append(board).append("\n");
        return sb.toString();
    }
}
