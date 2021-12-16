package soltrchess.ptui;

import javafx.stage.FileChooser;
import soltrchess.backtracking.Backtracker;
import soltrchess.backtracking.Configuration;
import soltrchess.model.Observer;
import soltrchess.model.SoltrChessModel;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


/**
 * The plain text UI for SoltrChess.  This class encapsulates both
 * the View and Controller portions of the MVC architecture.
 *
 * @author Emily Burroughs
 */
public class SoltrChessPTUI implements Observer<SoltrChessModel, SoltrChessModel.updateInfo> {

    public SoltrChessModel board;

    private String filename;

    /**
     * Construct the PTUI
     *
     * @param filename the file name
     */
    public SoltrChessPTUI(String filename) {
        this.board = new SoltrChessModel(filename);
        String array[] = filename.split("/");
        int length = array.length;
        this.filename = array[length-1];
        initializeView();
    }

    private void initializeView() {
        this.board.addObserver(this);
    }

    /**
     * The main command loop.
     */
    public void run() {
        System.out.println("Connect Four PTUI\n");
        System.out.println("Game File: " + filename);
        update(this.board, null);
        String choice = "";
        try (Scanner in = new Scanner( System.in )) {
            while (!choice.equals("quit")) {
                System.out.println("[move,new,restart,hint,solve,quit]> ");
                choice = in.next();
                if (choice.equals("move")) {
                    System.out.println("source row? ");
                    int currentRow = in.nextInt();
                    System.out.println("source col? ");
                    int currentCol = in.nextInt();
                    System.out.println("dest row? ");
                    int newRow = in.nextInt();
                    System.out.println("dest col? ");
                    int newCol = in.nextInt();
                    if (this.board.isValidMove(currentRow, currentCol,
                            newRow, newCol)) {
                        this.board.makeMove(currentRow, currentCol,
                                newRow, newCol);
                        String pieceName = board.getPiece(currentRow, currentCol).getPieceName();
                    } else {
                        System.out.println("Invalid move.");
                        System.out.println(board);
                    }
                }else if (choice.equals("new")){
                    System.out.println("game file name: ");
                    filename = in.next();
                    System.out.println("New Game " + filename);
                    this.board = new SoltrChessModel("data\\" + filename);
                    System.out.println(board);
                    initializeView();
                }else if (choice.equals("restart")){
                    this.board = new SoltrChessModel("data\\" + filename);
                    System.out.println(board);
                    initializeView();
                }else if (choice.equals("hint")){
                    Backtracker backtracker = new Backtracker();
                    List<Configuration> solve = backtracker.solveWithPath(board);
                    if(!board.isGoal()) {
                        board = (SoltrChessModel) solve.get(1);
                        initializeView();
                    }
                    System.out.println(board);
                    if(board.isGoal()) {
                        System.out.println("You won. Congratulations!");
                    }
                }else if (choice.equals("solve")) {
                    Backtracker backtracker = new Backtracker();
                    List<Configuration> solve = backtracker.solveWithPath(board);
                    if(solve != null) {
                        int counter = 0;
                        for (Configuration i : solve) {
                            if(counter != 0) {
                                System.out.println("Step " + counter);
                                board = (SoltrChessModel) i;
                                System.out.println(board);
                            }
                            counter++;
                        }
                        System.out.println("You won. Congratulations!");
                    }else{
                        System.out.println("No solution");
                    }
                }
            }
        }
    }

    @Override
    public void update(SoltrChessModel soltrChessModel, SoltrChessModel.updateInfo updateInfo) {

        if (updateInfo != null) {
            System.out.println(updateInfo.info);
            if (!updateInfo.info.equals("You won. Congratulations!")) {
                System.out.println(this.board);
            }
        }else{
            System.out.println(this.board);
        }
    }

    public static void main(String[] args) {
        SoltrChessPTUI game = new SoltrChessPTUI("data\\" + args[0]);
        game.run();
    }
}
