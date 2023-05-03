package fr.uge.patchwork.model;

import java.util.ArrayList;

/**
 * Classe représentant la liste des pièces disponibles dans le jeu.
 */
public class PieceList {
    private ArrayList<Piece> pieces;
    private int neutralPiece;

    /**
     * Constructeur de la classe PieceList.
     */
    public PieceList(){
        pieces = new ArrayList<Piece>();
        neutralPiece = 0;
    }

    public void addPiece(Piece piece){
        pieces.add(piece);
    }

    /**
     * Recherche de la pièce avec le moins de true pour y placer la neutralPiece
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
     * Retourne la liste des trois prochaines pièces disponibles.
     * @return : la liste des trois prochaines pièces disponibles.
     */
    public ArrayList<Piece> nextPieces(){
        ArrayList<Piece> res = new ArrayList<>(3);
        for (int i=0; i< 3; i++) {
            res.add(pieces.get((neutralPiece + i + 1) % pieces.size()));
        }

        return res;
    }

    /**
     * Retire une pièce de la liste des pièces disponibles.
     * @param index : l'index de la pièce à retirer.
     */
    public void removePiece(int index){
        pieces.remove((neutralPiece + index) % pieces.size());
        neutralPiece = (neutralPiece + index) % pieces.size();
    }
}