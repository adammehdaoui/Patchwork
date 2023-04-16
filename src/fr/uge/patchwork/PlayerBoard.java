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
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {

                //si la case est vide
                if (!this.board[i][j]) {
                    if (i + p.schema().length <= this.board.length && j + p.schema()[0].length <= this.board[i].length) {
                        // on regarde si la pièce peut être placée
                        boolean canBePlaced = true;

                        for (int k = 0; k < p.schema().length; k++) {
                            for (int l = 0; l < p.schema()[k].length; l++) {
                                if (this.board[i + k][j + l] && p.schema()[k][l]) {
                                    canBePlaced = false;
                                }
                            }
                        }

                        if (canBePlaced) {
                            // on place la pièce
                            for (int k = 0; k < p.schema().length; k++) {
                                for (int l = 0; l < p.schema()[k].length; l++) {
                                    this.board[i + k][j + l] = p.schema()[k][l];
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
