package fr.uge.patchwork.model;

import java.util.ArrayList;

public class Player {

    private final int id;
    private int buttons;
    private ArrayList<Piece> pieces;
    private PlayerBoard board;

    public Player(int id, PlayerBoard board){
        this.id = id;
        this.buttons = 5;
        this.pieces = new ArrayList<Piece>();
        this.board = board;
    }

    public int getId(){
        return id;
    }

    public void addButtons(int buttons){
        this.buttons += buttons;
    }

    // Essaie de placer la pièce et si elle rentre, on l'ajoute à sa liste puis il passe à la caisse.
    public boolean buyPiece(Piece piece){
        if(buttons >= piece.cost()){
            // On essaie de placer la piece sur le board
            if(board.placePiece(piece)){
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

    // Parcourt sa liste de pièces et renvoie le total de boutons qu'il doit gagner
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
