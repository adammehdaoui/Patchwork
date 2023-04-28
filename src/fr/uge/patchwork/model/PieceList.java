package fr.uge.patchwork.model;

import java.util.ArrayList;

public class PieceList {
    private ArrayList<Piece> pieces;
    private int neutralPiece;

    public PieceList(){
        pieces = new ArrayList<Piece>();
        neutralPiece = 0;
    }

    public void addPiece(Piece piece){
        pieces.add(piece);
    }

    // On cherche la pièce avec le moins de true pour y placer la neutralPiece :
    public void placeNeutral(){
        int res = 0;
        for (Piece piece : pieces) {
            if(piece.countTrue() < pieces.get(res).countTrue()){
                res = pieces.indexOf(piece);
            }
        }
        neutralPiece = res;
    }

    // On renvoie les 3 pièces qui suivent la neutralPiece.
    public ArrayList<Piece> nextPieces(){
        ArrayList<Piece> res = new ArrayList<>(3);
        for (int i=0; i< 3; i++) {
            res.add(pieces.get((neutralPiece + i + 1) % pieces.size()));
        }

        return res;
    }

    public void removePiece(int index){
        pieces.remove((neutralPiece + index) % pieces.size());
        neutralPiece = (neutralPiece + index) % pieces.size();
    }
}

//on verifie qu'il peut ahceter la piece
//on priint la piece et demande si il veut rotate invert ou validate
//si il valide il entre des coordonnées
//si la piece ne rentre pas, on lui dit et on lui redemande rotate...