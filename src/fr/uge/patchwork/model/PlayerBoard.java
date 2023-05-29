package fr.uge.patchwork.model;

import java.util.ArrayList;

/**
 * Class representing a cell of the board (which is a 9x9 grid).
 * @param board board of the player
 */
public record PlayerBoard(ArrayList<ArrayList<Boolean>> board) {

    /**
     * Constructor of the class PlayerBoard.
     */
    public PlayerBoard() {
        this(new ArrayList<>());

        for (int i = 0; i < 9; i++) {
            board.add(new ArrayList<>());
            for (int j = 0; j < 9; j++) {
                board.get(i).add(false);
            }
        }
    }

    /**
     * Place a piece on the player's board.
     * @param p piece to place
     * @param x coordinate x
     * @param y coordinate y
     * @return true if the piece has been placed, false otherwise
     */
    public boolean placePiece(Piece p, int x, int y) {
        if (x + p.schema().size() <= board.size() && y + p.schema().get(0).size() <= board.get(x).size()) {
            /* Checking if the piece can be placed */
            boolean canBePlaced = true;

            for (int k = 0; k < p.schema().size(); k++) {
                for (int l = 0; l < p.schema().get(k).size(); l++) {
                    if (board.get(x + k).get(y + l) && p.schema().get(k).get(l)) {
                        canBePlaced = false;
                        break;
                    }
                }
            }

            if (canBePlaced) {
                /* Place the piece */
                for (int k = 0; k < p.schema().size(); k++) {
                    for (int l = 0; l < p.schema().get(k).size(); l++) {
                        if(p.schema().get(k).get(l)) {
                            board.get(x + k).set(y + l, true);
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the player can earn the special piece (he filled a 7x7 square).
     * @return true if the player can earn the special piece, false otherwise
     */
    public boolean isSpecialPieceEarnable(){
        int count = 0;

        for(int i = 0; i < board.size(); i++){
            for(int j = 0; j < board.get(i).size(); j++){

                if(board.get(i).get(j) && i+7 < board.size() && j+7 < board.get(i).size()){
                    outerLoop:
                    for(int k = i; k < board.size(); k++){
                        for(int l = j; l < board.get(k).size(); l++){
                            if(board.get(k).get(l)){
                                count++;
                            }
                            else{
                                count = 0;
                                break outerLoop;
                            }
                            if(count == 49){
                                return true;
                            }
                        }
                    }
                }

            }
        }

        return false;
    }

    /**
     * Count the number of empty cells on the player board.
     * @return number of empty cells
     */
    public int countEmptyCells(){
        int count = 0;

        for (ArrayList<Boolean> booleans : board) {
            for (Boolean aBoolean : booleans) {
                if (!aBoolean) {
                    count++;
                }
            }
        }

        return count;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ArrayList<Boolean> booleans : board) {
            for (Boolean aBoolean : booleans) {
                sb.append(aBoolean ? "■" : "□");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
