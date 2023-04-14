package fr.uge.patchwork;

import java.util.ArrayList;

public class Player {

    private final int id;
    private int buttons;
    private ArrayList<Piece> pieces;
    private PlayerBoard board;

    public Player(int id){
        this.id = id;
        buttons = 5;
    }

}
