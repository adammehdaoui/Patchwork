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


}
