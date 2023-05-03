package fr.uge.patchwork.model;

import java.util.ArrayList;

/**
 * Classe dédiée à la représentation d'un joueur du jeu.
 */
public class Player {

    private final int id;
    private int buttons;
    private ArrayList<Piece> pieces;
    private PlayerBoard board;

    /**
     * Constructeur de la classe Player.
     * @param id : identifiant du joueur
     * @param board : plateau du joueur
     */
    public Player(int id, PlayerBoard board){
        this.id = id;
        this.buttons = 5;
        this.pieces = new ArrayList<Piece>();
        this.board = board;
    }

    public int getId(){
        return id;
    }

    public int getButtons(){
        return buttons;
    }

    public void addButtons(int buttons){
        this.buttons += buttons;
    }

    /* Ancienne version de la méthode buyPiece, qui ne prend pas en compte les coordonnées */
    /*public boolean buyPiece(Piece piece){
        if(buttons >= piece.cost()){
            // On essaie de placer la piece sur le board
            if(board.placePiece(piece)){
                buttons -= piece.cost();
                pieces.add(piece);
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }*/

    /**
     * Achète une pièce et la place sur le plateau du joueur.
     * @param piece : pièce à acheter
     * @param x : coordonnée x
     * @param y: coordonnée y
     * @return : true si la pièce a été achetée et placée, false sinon
     */
    public boolean buyPiece(Piece piece, int x, int y){
        if(buttons >= piece.cost()){
            /* On essaie de placer la piece sur le board */
            if(board.placePiece(piece, x, y)){
                buttons -= piece.cost();
                pieces.add(piece);
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    /**
     * Retourne le plateau du joueur.
     * @return : plateau du joueur
     */
    public int getEarnedButton(){
        int res = 0;
        for (Piece piece : pieces) {
            res += piece.button();
        }
        return res;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Player ").append(id).append(", ");
        sb.append("Buttons : ").append(buttons).append(", ");
        sb.append("Board :\n").append(board).append("\n");
        return sb.toString();
    }
}
