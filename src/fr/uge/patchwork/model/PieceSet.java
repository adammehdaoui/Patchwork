package fr.uge.patchwork.model;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Class representing the list of pieces available in the game.
 */
public final class PieceSet {
    private final ArrayList<Piece> pieces;
    private int neutralPiece;

    /**
     * Constructor of the class PieceList.
     */
    public PieceSet(){
        pieces = new ArrayList<>();
        neutralPiece = 0;
    }

    /**
     * Get the list of pieces depending on the game version.
     * @param gameVersion version of the game
     * @throws IOException error while reading the file
     * @throws IllegalArgumentException invalid game version
     */
    public void init(String gameVersion) throws IOException, IllegalArgumentException {
        /* Base game */
        switch (gameVersion) {
            case "1" -> {
                /* Creation of the pieces of the basic game version */
                var file = new PieceFactory(Path.of("base.txt"));
                file.read(this);
            }

            /* Complete game */
            case "2" -> {
                /* Creation of the pieces of the complete game version */
                var file = new PieceFactory(Path.of("complet.txt"));
                file.read(this);
            }

            /* Custom game */
            case "3" -> {
                /* Creation of the pieces of the custom game version */
                var file = new PieceFactory(Path.of("custom.txt"));
                file.read(this);
            }

            /* Invalid version */
            default -> throw new IllegalArgumentException("Invalid game version");
        }

        this.placeNeutral();
    }

    /**
     * Add a piece to the list of available pieces.
     * @param piece piece to add
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
     * @return the list of the next three available pieces.
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
     * @param index index of the piece to remove.
     */
    public void removePiece(int index){
        pieces.remove((neutralPiece + index) % pieces.size());
        neutralPiece = (neutralPiece + index) % pieces.size();
    }

}