package fr.uge.patchwork;

public class Cell {

    private boolean player1;
    private boolean player2;
    private boolean button;

    public Cell(){
        player1 = false;
        player2 = false;
        button = false;
    }

    public void setPlayer1(boolean b){
        player1 = b;
    }

    public void setPlayer2(boolean b){
        player2 = b;
    }

    public void setButton(boolean b){
        button = b;
    }

    public boolean player1(){
        return player1;
    }

    public boolean player2(){
        return player2;
    }

    public boolean button(){
        return button;
    }

}
