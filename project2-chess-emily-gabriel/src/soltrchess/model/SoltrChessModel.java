package soltrchess.model;

import soltrchess.backtracking.Configuration;

import java.io.*;
import java.util.*;

/**
 * The model for the solitaire chess game.
 *
 * @author Emily Burroughs
 */
public class SoltrChessModel implements Configuration {
    /** the number of rows */
    public final static int ROWS = 4;
    /** the number of columns */
    public final static int COLS = 4;

    @Override
    public Collection<Configuration> getSuccessors() {
        LinkedList<Configuration> finish = new LinkedList<>();
        SoltrChessModel copy;
        for(int[] i : queue){
            for(int[] j : queue){
                if(i != j){
                    if(isValidMove(i[0], i[1], j[0], j[1])){
                        copy = new SoltrChessModel(this);
                        copy.makeMove(i[0], i[1], j[0], j[1]);
                        finish.add(copy);
                    }
                }
            }
        }
        return finish;
    }

    public SoltrChessModel(SoltrChessModel copy){
        numPieces = copy.numPieces;
        observers = new LinkedList<>();
        this.board = new Piece[4][4];
        this.queue = new LinkedList<>();
        for(int[] x : copy.queue){
            queue.add(new int[]{x[0], x[1]});
        }
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++) {
                board[i][j] = copy.board[i][j];
            }
        }
    }



    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isGoal() {
        return numPieces == 1;
    }

    public enum Piece {
        B("BISHOP"),
        K("KING"),
        N("KNIGHT"),
        P("PAWN"),
        R("ROOK"),
        Q("QUEEN"),
        NONE("NONE");

        String pieceName;
        Piece(String pn){
            pieceName = pn;
        }

        public String getPieceName(){
            return pieceName;
        }

    }

    /** the game board */
    private Piece[][] board;

    /** the observers of this model */
    private List<Observer<SoltrChessModel, updateInfo>> observers;

    /** keeps track of the current number of pieces on the board */
    public int numPieces;

    private LinkedList<int[]> queue = new LinkedList<>();

    /**
     * Create a new board.
     */
    public SoltrChessModel(String filename) {
        this.board = new Piece[ROWS][COLS];
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String data;
        try {
            for (int i = 0; i < 4; i++) {
                data = br.readLine();
                String[] tokens = data.split(" ");
                for (int j = 0; j < 4; j++) {
                    if (tokens[j].equals("-")){
                        this.board[i][j] = Piece.NONE;
                    }else {
                        this.board[i][j] = Piece.valueOf(tokens[j]);
                        queue.add(new int[]{i, j});
                        numPieces ++;
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.observers = new LinkedList<>();
    }

    public class updateInfo{
        public String info;
    }

    /**
     * The view calls this method to add themselves as an observer of the model.
     *
     * @param observer the observer
     */
    public void addObserver(Observer<SoltrChessModel, updateInfo> observer) {
        this.observers.add(observer);
    }

    /** When the model changes, the observers are notified via their update() method */
    private void notifyObservers(updateInfo info) {
        for (Observer<SoltrChessModel, updateInfo> obs: this.observers ) {
            obs.update(this, info);
        }
    }

    /**
     * Is this a valid move?
     *
     * @param currentCol the current piece's column
     * @param currentRow the current piece's row
     * @param newCol the column the piece is going to move to
     * @param newRow the row the piece is going to move to
     * @return true if move is valid
     */
    public boolean isValidMove(int currentRow, int currentCol, int newRow, int newCol){
        try {
            Piece temp = board[newRow][newCol];
        }catch (ArrayIndexOutOfBoundsException e){
            return false;
        }
        if (board[newRow][newCol] != Piece.NONE){
            switch (board[currentRow][currentCol]){
                case NONE -> {
                    return false;
                }
                case P -> {
                    if(currentRow == newRow+1){
                        return currentCol == newCol-1 || currentCol == newCol+1;
                    }else{
                        return false;
                    }
                }
                case K -> {
                    int rowDiff = Math.abs(newRow - currentRow);
                    int colDiff = Math.abs(newCol - currentCol);
                    if(rowDiff < 2 && colDiff < 2){
                        if(rowDiff != 0 || colDiff != 0) {
                            return true;
                        }
                    }
                    return false;
                }
                case N -> {
                    int rowDiff = Math.abs(newRow - currentRow);
                    int colDiff = Math.abs(newCol - currentCol);
                    if((rowDiff == 1 && colDiff == 2) || (rowDiff == 2 && colDiff == 1)){
                        return true;
                    }
                    return false;
                }
                case R -> {
                    int rowDiff = Math.abs(newRow - currentRow);
                    int colDiff = Math.abs(newCol - currentCol);
                    if(rowDiff == 0 ^ colDiff == 0){
                        return block(currentRow, currentCol, newRow, newCol);
                    }
                }
                case B -> {
                    int rowDiff = Math.abs(newRow - currentRow);
                    int colDiff = Math.abs(newCol - currentCol);
                    if (rowDiff == colDiff && colDiff != 0){
                        return block(currentRow, currentCol, newRow, newCol);
                    }
                }
                case Q -> {
                    int rowDiff = Math.abs(newRow - currentRow);
                    int colDiff = Math.abs(newCol - currentCol);
                    if((rowDiff == colDiff && colDiff != 0) || (rowDiff == 0 ^ colDiff == 0)){
                        return block(currentRow, currentCol, newRow, newCol);
                    }
                }
            }
        }
        return false;
    }

    /**
     * This is a helper function for the movement rule of R, B, and Q.
     *
     * @param currentCol the current piece's column
     * @param currentRow the current piece's row
     * @param newCol the column the piece is going to move to
     * @param newRow the row the piece is going to move to
     * @return true if move is valid
     */
    boolean block(int currentRow, int currentCol, int newRow, int newCol){
        int signRow = (int)Math.signum((float)(newRow - currentRow));
        int signCol = (int)Math.signum((float)(newCol - currentCol));
        int i = currentRow + signRow;
        int j = currentCol + signCol;
        while(i != newRow || j != newCol){
            if (board[i][j] != Piece.NONE){
                return false;
            }
            i += signRow;
            j += signCol;

        }
        return true;
    }

    /**
     * Make a move by moving a piece to a valid spot.
     *
     * @rit.pre the move must be valid
     * @param currentCol the current piece's column
     * @param currentRow the current piece's row
     * @param newCol the column the piece is going to move to
     * @param newRow the row the piece is going to move to
     */
    public void makeMove(int currentRow, int currentCol, int newRow, int newCol){
        Piece temp = board[currentRow][currentCol];
        board[newRow][newCol] = temp;
        board[currentRow][currentCol] = Piece.NONE;
        queue.removeIf(i -> i[0] == currentRow && i[1] == currentCol);
        numPieces --;
        updateInfo Info = new updateInfo();
        if(isGoal()){
            Info.info = board[newRow][newCol].getPieceName() + " to (" +
                    newRow + "," + newCol + ")";
            notifyObservers(Info);
            Info.info = "You won. Congratulations!";
        }else{
            Info.info = board[newRow][newCol].getPieceName() + " to (" +
                    newRow + "," + newCol + ")";
        }
        notifyObservers(Info);
    }

    public Piece getPiece(int currentRow, int currentCol){
        return board[currentRow][currentCol];
    }


    /**
     * Returns a string representation of the board, suitable for printing out.
     * A sample board would be:<br>
     * <br><tt>
     *   0 1 2 3 <br>
     * 0 N - - - <br>
     * 1 R - Q - <br>
     * 2 - - - N <br>
     * 3 B - - - <br>
     * </tt>
     *
     * @return the string representation
     */
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();

        // build the top row with column numbers
        builder.append(' ');
        for (int c = 0; c < COLS; ++c) {
            builder.append(" " + c + ' ');
        }
        builder.append('\n');

        // build remaining rows with row numbers and column values
        for (int row = 0; row < ROWS; ++row) {
            builder.append(row);
            for (int col = 0; col < COLS; ++col) {
                if (this.board[row][col] == Piece.NONE) {
                    builder.append(" - ");
                } else {
                    Piece temp = board[row][col];
                    builder.append(" " + temp.name() + " ");
                }
            }
            builder.append(" " + '\n');
        }

        return builder.toString();
    }

}