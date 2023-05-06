package fr.uge.patchwork.model;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the list of pieces available in the game.
 */
public class PieceSet {
    private ArrayList<Piece> pieces;
    private int neutralPiece;

    /**
     * Constructor of the class PieceList.
     */
    public PieceSet(){
        pieces = new ArrayList<Piece>();
        neutralPiece = 0;
    }

    public void init(String gameVersion) throws IOException, IllegalArgumentException {
        /* Base game */
        if(gameVersion.equals("1")){
            /* Creation of a piece diagram */
            var schema = new ArrayList<ArrayList<Boolean>>();
            schema.add(new ArrayList<>(List.of(true, true)));
            schema.add(new ArrayList<>(List.of(true, true)));

            Piece piece1 = new Piece(schema, 3, 4, 1);
            Piece piece2 = new Piece(schema, 2, 2, 0);

            /* Creation of a table of 40 pieces (pieces 1 and 2 alternated) */
            for (int i = 0; i < 20; i++) {
                this.addPiece(piece1);
                this.addPiece(piece2);
            }
        }
        /* Complete game */
        else if(gameVersion.equals("2")){
            /* Creation of the pieces of the complete game version */
            var file = new PieceFactory(Path.of("pieces.txt"));
            file.read(this);
        }
        /* Invalid version */
        else{
            throw new IllegalArgumentException("Invalid game version");
        }

        this.placeNeutral();
    }

    /**
     * Add a piece to the list of available pieces.
     * @param piece : piece to add
     */
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