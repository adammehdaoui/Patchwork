package fr.uge.patchwork.model;

import java.util.ArrayList;

/**
 * Class representing the list of pieces available in the game.
 */
public class PieceList {
    private ArrayList<Piece> pieces;
    private int neutralPiece;

    /**
     * Constructor of the class PieceList.
     */
    public PieceList(){
        pieces = new ArrayList<Piece>();
        neutralPiece = 0;
    }

    public void addPiece(Piece piece){
        pieces.add(piece);
    }

    /**
     * Searching for the piece with the least true to place the neutralPiece
     */
    public void placeNeutral(){
        int res = 0;
        for (Piece piece : pieces) {
            if(piece.countTrue() < pieces.get(res).countTrue()){
                res = pieces.indexOf(piece);
            }
        }
        neutralPiece = res;
    }

    /**
     * Returns the list of the next three available pieces.
     * @return : the list of the next three available pieces.
     */
    public ArrayList<Piece> nextPieces(){
        ArrayList<Piece> res = new ArrayList<>(3);
        for (int i=0; i< 3; i++) {
            res.add(pieces.get((neutralPiece + i + 1) % pieces.size()));
        }

        return res;
    }

    /**
     * Remove a piece from the list of available pieces.
     * @param index : index of the piece to remove.
     */
    public void removePiece(int index){
        pieces.remove((neutralPiece + index) % pieces.size());
        neutralPiece = (neutralPiece + index) % pieces.size();
    }
}