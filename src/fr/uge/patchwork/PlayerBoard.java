package fr.uge.patchwork;

import java.util.ArrayList;
import java.util.List;

public record PlayerBoard(ArrayList<ArrayList<Boolean>> board) {

    // On essaie de placer la piece sur le board
    public boolean placePiece(Piece p) {
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(i).size(); j++) {

                //si la case est vide
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
