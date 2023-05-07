package fr.uge.patchwork.model;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * Class dedicated to the representation of the game board ("TimeBoard").
 */
public class TimeBoard {

    private final ArrayList<Cell> cells;
    private final static int nbCases = 54;

    private boolean specialPieceAvailable;

    /* Storing here the last player who played */
    private final Player priorityPlayer;

    /**
     * Constructor of the class TimeBoard.
     * @param player1 : player 1
     * @param player2 : player 2
     */
    public TimeBoard(Player player1, Player player2) {
        Objects.requireNonNull(player1);
        Objects.requireNonNull(player2);

        cells = new ArrayList<>();

        /* Creating the cells and placing the buttons every 6 cells */
        for(int i=0; i<nbCases; i++){
            cells.add(new Cell());

            if(i % 6 == 5){
                cells.get(i).setButton(true);
            }

            if(i == 20 || i == 26 || i == 33 || i == 45 || i == 51){
                cells.get(i).setPatch(true);
            }
        }

        /* Positioning the players on the first cell of the TimeBoard (beginning of the game) */
        cells.get(0).setPlayer(player1);
        cells.get(0).setPlayer(player2);

        priorityPlayer = player1;

        specialPieceAvailable = true;
    }

    public boolean isSpecialPieceAvailable(){
        return specialPieceAvailable;
    }

    public void setSpecialPieceAvailable(boolean b){
        specialPieceAvailable = b;
    }

    /**
     * Returns the ID of the player who is in front of the other. If both players are on the same cell, returns 0.
     * @return : 0 if both players are on the same cell, 1 if player 1 is in front of player 2, 2 if player 2 is in front of player 1
     */
    public int isInFront(){
        for(int i=0; i<nbCases; i++){
            if(cells.get(i).player1()!=null && cells.get(i).player2()!=null){
                return 0;
            }
            else if(cells.get(i).player1()!=null){
                return 1;
            }
            else if(cells.get(i).player2()!=null){
                return 2;
            }
        }

        return -1;
    }

    /**
     * Moves the player according to the number of cells indicated.
     *
     * @param player : player to move
     * @param move   : number of cells to move
     * @throws IllegalArgumentException : argument player not found in the main board
     */
    public void movePlayer(Player player, int move) throws IllegalArgumentException {
        int index = -1;
        int moveTo;

        /* Searching for the player in the Time Board */
        for(int i=0; i<nbCases; i++){
            if(cells.get(i).getPlayer(player) != null){
                index = i;
                break;
            }
        }

        if(index<0){
            throw new IllegalArgumentException("Le joueur n'a pas été trouvé dans le board principal");
        }

        /* Freeing the player from the cell and moving it to the new cell */
        cells.get(index).freePlayer(player);

        if(index+move >= nbCases){
            moveTo=nbCases-1;
        }
        else{
            moveTo=index+move;
        }

        cells.get(moveTo).setPlayer(player);

        /* If the player crossed a button or a patch, remove it from the cell */
        for(int e=index; e<moveTo; e++){
            if(cells.get(e).patch()){
                cells.get(e).setPatch(false);
            }
        }
    }

    /**
     * Predict the movement (before cleaning the time board) of a player according to the number of cells indicated.
     * @param player : player to move
     * @param move : number of cells to move
     * @return : a map containing the start and end cell of the player
     * @throws IllegalArgumentException : argument player not found in the main board
     */
    public Map<String, Integer> predictMovement(Player player, int move) throws IllegalArgumentException {
        int index = -1;
        int moveTo;

        /* Searching for the player in the Time Board */
        for(int i=0; i<nbCases; i++){
            if(cells.get(i).getPlayer(player) != null){
                index = i;
                break;
            }
        }

        if(index<0){
            throw new IllegalArgumentException("Le joueur n'a pas été trouvé dans le board principal");
        }

        if(index+move >= nbCases){
            moveTo=nbCases-1;
        }
        else{
            moveTo=index+move;
        }

        return Map.of("start", index, "end", moveTo);
    }

    /**
     * Returns the number of buttons between two cells.
     *
     * @param start : starting cell
     * @param end   : ending cell
     */
    public int nbButton(int start, int end){
        int count = 0;
        for(int i=start; i<end; i++){
            if(cells.get(i).button()){
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the number of patches between two cells.
     * @param start : starting cell
     * @param end : ending cell
     * @return : number of patches between the two cells
     */
    public int nbPatch(int start, int end){
        int count = 0;
        for(int i=start; i<end; i++){
            if(cells.get(i).patch()){
                count++;
                cells.get(i).setPatch(false);
            }
        }
        return count;
    }

    /**
     * Returns the distance between the two players.
     * @return : distance between the two players
     */
    public int distance(){
        int posP1 = 0;
        int posP2 = 0;

        for(int i=0; i<nbCases; i++){
            if(cells.get(i).player1() != null){
                posP1 = i;
            }
            if(cells.get(i).player2() != null){
                posP2 = i;
            }
        }

        return Math.abs(posP2 - posP1);
    }

    /**
     * Returns true if the two players are on the last cell.
     * @return : true if the two players are on the last cell
     */
    public boolean endGame(){
        return cells.get(nbCases - 1).player1() != null && cells.get(nbCases - 1).player2() != null;
    }

    /**
     * Returns the player who must play.
     * @return : player who must play
     */
    public int turnOf(){
        int posP1 = 0;
        int posP2 = 0;

        for(int i=0; i<nbCases; i++){
            if(cells.get(i).player1() != null){
                posP1 = i;
            }
            if(cells.get(i).player2() != null){
                posP2 = i;
            }
        }

        if(posP1 == posP2){
            return priorityPlayer.getId();
        }
        else if(posP1 < posP2){
            return 1;
        }
        else{
            return 2;
        }
    }

    @Override
    public String toString(){
        int i=1;
        StringBuilder sb = new StringBuilder();

        sb.append("TimeBoard :\n------------------------------------\n");

        for(Cell cell : cells){
            sb.append("|");

            if(cell.patch()){
                sb.append("■");
            }
            else{
                sb.append(" ");
            }

            if(cell.button()){
                sb.append("°");
            }
            else{
                sb.append(" ");
            }

            if(cell.player1() != null){
                sb.append("1");
            }
            else{
                sb.append(" ");
            }

            if(cell.player2() != null){
                sb.append("2");
            }
            else{
                sb.append(" ");
            }

            sb.append("|");

            if(i%6 == 0 && i!=0){
                sb.append("\n").append("------------------------------------").append("\n");
            }

            i++;
        }

        return sb.toString();
    }

}
