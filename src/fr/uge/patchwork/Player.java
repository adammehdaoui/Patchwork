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

    public boolean buyPiece(Piece piece){
        if(this.buttons >= piece.cost()){
            this.buttons -= piece.cost();
            this.pieces.add(piece);

            //on place la piece sur le board
            this.board.placePiece(piece);

            return true;
        }
        else{
            return false;
        }
    }
}
