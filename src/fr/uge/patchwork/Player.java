package fr.uge.patchwork;

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
        return this.id;
    }

    public void addButtons(int buttons){
        this.buttons += buttons;
    }

    //essaie de placer la pièce et si elle rentre, on l'ajoute à sa liste et il passe à la caisse
    public boolean buyPiece(Piece piece){
        if(this.buttons >= piece.cost()){
            //on essaie de placer la piece sur le board
            if(this.board.placePiece(piece)){
                this.buttons -= piece.cost();
                this.pieces.add(piece);
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

    //parcourt sa liste de pièces et renvoie le total de boutons qu'il doit gagner
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
        sb.append("Player ").append(this.id).append(", ");
        sb.append("buttons: ").append(this.buttons).append(", ");
        sb.append("board: ").append("\n").append(this.board).append("\n");
        return sb.toString();
    }
}
