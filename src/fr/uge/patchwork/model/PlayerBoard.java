package fr.uge.patchwork.model;

import java.util.ArrayList;

/**
 * Class representing a cell of the board (which is a 9x9 grid).
 * @param board : board of the player
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
     * @param p : piece to place
     * @param x : coordinate x
     * @param y : coordinate y
     * @return : true if the piece has been placed, false otherwise
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
                        board.get(x + k).set(y + l, p.schema().get(k).get(l));
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the place where we can place the special 7x7 piece. If there is no place to place it on the board, then
     * we return the value -1.
     * @return : the place where we can place the special 7x7 piece or -1 if there is no place to place it
     */
    public int specialPiecePlace(){
        boolean find = false;
        int count = 0;

        for(int i=0; i<board.size(); i++){
            for(int e=0; e<board.get(i).size(); e++){
                if(!board.get(i).get(e)){

                    for(int j = i; j<i+7 && j<board().size() && !find; j++){
                        for(int x = e; x<x+7 && x<board.get(e).size() && !find; x++){
                            if(board.get(j).get(x)){
                                find = true;
                                count = 0;
                            }

                            count++;
                        }
                    }

                    /* If the 7x7 cases have been found */
                    if(count == 49){
                        return i;
                    }

                }
            }
        }

        return -1;
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
