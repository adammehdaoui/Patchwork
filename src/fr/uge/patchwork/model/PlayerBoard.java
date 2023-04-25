package fr.uge.patchwork.model;

import java.util.ArrayList;

public record PlayerBoard(ArrayList<ArrayList<Boolean>> board) {

    public PlayerBoard() {
        this(new ArrayList<ArrayList<Boolean>>());

        for (int i = 0; i < 9; i++) {
            board.add(new ArrayList<Boolean>());
            for (int j = 0; j < 9; j++) {
                board.get(i).add(false);
            }
        }
    }

    public boolean placePiece(Piece p) {
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(i).size(); j++) {

                // Si la case est vide
                if (!board.get(i).get(j)) {
                    if (i + p.schema().size() <= board.size() && j + p.schema().get(0).size() <= board.get(i).size()) {
                        // On regarde si la pièce peut être placée
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
                            // On place la pièce
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
    }


    /* Retourne la place où l'on peut placer la pièce spéciale 7x7. S'il n'y a pas la place
    pour la placer sur le board, alors on retourne la valeur -1. A TESTER + OPTI */
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

                    // Si les 7x7 cases on été trouvées :
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
