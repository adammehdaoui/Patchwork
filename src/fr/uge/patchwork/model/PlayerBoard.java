package fr.uge.patchwork.model;

import java.util.ArrayList;

/**
 * Class representing a cell of the board (celui où il va placer ses pièces).
 * @param board : plateau du joueur
 */
public record PlayerBoard(ArrayList<ArrayList<Boolean>> board) {

    /**
     * Constructeur de la classe PlayerBoard.
     */
    public PlayerBoard() {
        this(new ArrayList<ArrayList<Boolean>>());

        for (int i = 0; i < 9; i++) {
            board.add(new ArrayList<Boolean>());
            for (int j = 0; j < 9; j++) {
                board.get(i).add(false);
            }
        }
    }

    /* Ancienne méthode de placement de pièce sur le plateau (sans coordonnées) */
    /*public boolean placePiece(Piece p) {
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(i).size(); j++) {
                if (!board.get(i).get(j)) {
                    if (i + p.schema().size() <= board.size() && j + p.schema().get(0).size() <= board.get(i).size()) {
                        boolean canBePlaced = true;

                        for (int k = 0; k < p.schema().size(); k++) {
                            for (int l = 0; l < p.schema().get(k).size(); l++) {
                                if (board.get(i + k).get(j + l) && p.schema().get(k).get(l)) {
                                    canBePlaced = false;
                                    break;
                                }
                            }
                        }

                        if (canBePlaced) {
                            for (int k = 0; k < p.schema().size(); k++) {
                                for (int l = 0; l < p.schema().get(k).size(); l++) {
                                    board.get(i + k).set(j + l, p.schema().get(k).get(l));
                                }
                            }
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }*/

    /**
     * Place une pièce sur le plateau du joueur.
     * @param p : pièce à placer
     * @param x : coordonnée x
     * @param y : coordonnée y
     * @return : true si la pièce a été placée, false sinon
     */
    public boolean placePiece(Piece p, int x, int y) {
        if (x + p.schema().size() <= board.size() && y + p.schema().get(0).size() <= board.get(x).size()) {
            /* On regarde si la pièce peut être placée */
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
                /* On place la pièce */
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

    /* Retourne la place où l'on peut placer la pièce spéciale 7x7. S'il n'y a pas la place
    pour la placer sur le board, alors on retourne la valeur -1. A TESTER + OPTI */

    /**
     * Retourne la place où l'on peut placer la pièce spéciale 7x7. S'il n'y a pas la place pour la placer sur le board,
     * alors on retourne la valeur -1.
     * @return : la place où l'on peut placer la pièce spéciale 7x7. S'il n'y a pas la place pour la placer sur le board,
     * alors on retourne la valeur -1.
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

                    /* Si les 7x7 cases on été trouvées : */
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
