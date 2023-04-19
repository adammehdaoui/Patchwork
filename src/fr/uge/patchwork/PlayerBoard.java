package fr.uge.patchwork;

public record PlayerBoard(boolean[][] board) {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (boolean[] booleans : board) {
            for (boolean aBoolean : booleans) {
                sb.append(aBoolean ? "■" : "□");
            }
            sb.append("\n");
        }
        return sb.toString();
    }


    // on essaie de placer la piece sur le board
    public boolean placePiece(Piece p) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {

                //si la case est vide
                if (!board[i][j]) {
                    if (i + p.schema().size() <= board.length && j + p.schema().get(0).size() <= board[i].length) {
                        // on regarde si la pièce peut être placée
                        boolean canBePlaced = true;

                        for (int k = 0; k < p.schema().size(); k++) {
                            for (int l = 0; l < p.schema().get(k).size(); l++) {
                                if (board[i + k][j + l] && p.schema().get(k).get(l)) {
                                    canBePlaced = false;
                                }
                            }
                        }

                        if (canBePlaced) {
                            // on place la pièce
                            for (int k = 0; k < p.schema().size(); k++) {
                                for (int l = 0; l < p.schema().get(k).size(); l++) {
                                    board[i + k][j + l] = p.schema().get(k).get(l);
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
}
