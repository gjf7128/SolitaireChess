package soltrchess.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import soltrchess.backtracking.Backtracker;
import soltrchess.backtracking.Configuration;
import soltrchess.model.Observer;
import soltrchess.model.SoltrChessModel;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static soltrchess.model.SoltrChessModel.COLS;
import static soltrchess.model.SoltrChessModel.ROWS;

/**
 * The GUI for our solitaire chess game!
 *
 * @author gjf7128@rit.edu (Gabriel FitzPatrick)
 * @author Emily Burroughs
 */

public class SoltrChessGUI extends Application implements Observer<SoltrChessModel, SoltrChessModel.updateInfo> {
    public SoltrChessModel board; //our board from the model
    public Label messages; //game info at the top
    /** image files for our pieces and empty spaces */
    public Image bishop = new Image(getClass().getResourceAsStream("resources/bishop.png"));
    public Image blue = new Image(getClass().getResourceAsStream("resources/blue.png"));
    public Image dark = new Image(getClass().getResourceAsStream("resources/dark.png"));
    public Image king = new Image(getClass().getResourceAsStream("resources/king.png"));
    public Image knight = new Image(getClass().getResourceAsStream("resources/knight.png"));
    public Image light = new Image(getClass().getResourceAsStream("resources/light.png"));
    public Image pawn = new Image(getClass().getResourceAsStream("resources/pawn.png"));
    public Image queen = new Image(getClass().getResourceAsStream("resources/queen.png"));
    public Image rook = new Image(getClass().getResourceAsStream("resources/rook.png"));
    public Image white = new Image(getClass().getResourceAsStream("resources/white.png"));
    public ChessButton button; //our buttons which fill the gridpane
    private String filename; //the filename which we grab from args and filechooser
    public boolean isMove = false;//boolean to handle making moves
    public int currRow;
    public int currCol;
    public BorderPane borderPane;//our borderpane had our gridpane and messages inside
    public HBox topInfo;//our hbox for our messages at the top

    /**
     * Our helper class to hold our buttons which aid in populating the board
     * and making the pieces move.
     */
    public class ChessButton extends Button {
        public int row;
        public int col;

        /**
         * Constructor for our chessButton helper class.
         *
         * @param row the row
         * @param col the column
         */
        public ChessButton(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    /**
     * Our initializeView just adds an observer.
     */
    private void initializeView() {
        this.board.addObserver(this);
    }

    /**
     * Helper class to make our gridPane which fills with buttons of game
     * pieces and empty spaces
     */
    public GridPane makeGridpane() {
        GridPane gridPane = new GridPane();
        int counter = 0;//counter to help fill our buttons with either blue or white
        for (int row = 0; row < ROWS; ++row) {
            counter++;
            for (int col = 0; col < COLS; ++col) {
                button = new ChessButton(row,col);
                if (counter % 2 == 0) {
                    if (this.board.getPiece(row,col).equals(SoltrChessModel.Piece.N)) {
                        Node image;
                        StackPane stackPane = new StackPane();
                        ImageView darkImage = new ImageView(dark);
                        ImageView knightImage = new ImageView(knight);
                        stackPane.getChildren().addAll(darkImage,knightImage);
                        image = stackPane;
                        button.setGraphic(image);
                    }
                    if (this.board.getPiece(row,col).equals(SoltrChessModel.Piece.B)) {
                        Node image;
                        StackPane stackPane = new StackPane();
                        ImageView darkImage = new ImageView(dark);
                        ImageView bishopImage = new ImageView(bishop);
                        stackPane.getChildren().addAll(darkImage,bishopImage);
                        image = stackPane;
                        button.setGraphic(image);
                    }
                    if (this.board.getPiece(row,col).equals(SoltrChessModel.Piece.K)) {
                        Node image;
                        StackPane stackPane = new StackPane();
                        ImageView darkImage = new ImageView(dark);
                        ImageView kingImage = new ImageView(king);
                        stackPane.getChildren().addAll(darkImage,kingImage);
                        image = stackPane;
                        button.setGraphic(image);
                    }
                    if (this.board.getPiece(row,col).equals(SoltrChessModel.Piece.P)) {
                        Node image;
                        StackPane stackPane = new StackPane();
                        ImageView darkImage = new ImageView(dark);
                        ImageView pawnImage = new ImageView(pawn);
                        stackPane.getChildren().addAll(darkImage,pawnImage);
                        image = stackPane;
                        button.setGraphic(image);
                    }
                    if (this.board.getPiece(row,col).equals(SoltrChessModel.Piece.R)) {
                        Node image;
                        StackPane stackPane = new StackPane();
                        ImageView darkImage = new ImageView(dark);
                        ImageView rookImage = new ImageView(rook);
                        stackPane.getChildren().addAll(darkImage,rookImage);
                        image = stackPane;
                        button.setGraphic(image);
                    }
                    if (this.board.getPiece(row,col).equals(SoltrChessModel.Piece.Q)) {
                        Node image;
                        StackPane stackPane = new StackPane();
                        ImageView darkImage = new ImageView(dark);
                        ImageView queenImage = new ImageView(queen);
                        stackPane.getChildren().addAll(darkImage,queenImage);
                        image = stackPane;
                        button.setGraphic(image);
                    }
                    if (this.board.getPiece(row,col).equals(SoltrChessModel.Piece.NONE)) {
                        ImageView dimage = new ImageView(dark);
                        button.setGraphic(dimage);
                    }
                }
                else {
                    if (this.board.getPiece(row,col).equals(SoltrChessModel.Piece.N)) {
                        ImageView knightImage = new ImageView(knight);
                        button.setGraphic(knightImage);
                    }
                    if (this.board.getPiece(row,col).equals(SoltrChessModel.Piece.B)) {
                        ImageView bishopImage = new ImageView(bishop);
                        button.setGraphic(bishopImage);
                    }
                    if (this.board.getPiece(row,col).equals(SoltrChessModel.Piece.K)) {
                        ImageView kingImage = new ImageView(king);
                        button.setGraphic(kingImage);
                    }
                    if (this.board.getPiece(row,col).equals(SoltrChessModel.Piece.P)) {
                        ImageView pawnImage = new ImageView(pawn);
                        button.setGraphic(pawnImage);
                    }
                    if (this.board.getPiece(row,col).equals(SoltrChessModel.Piece.R)) {
                        ImageView rookImage = new ImageView(rook);
                        button.setGraphic(rookImage);
                    }
                    if (this.board.getPiece(row,col).equals(SoltrChessModel.Piece.Q)) {
                        ImageView queenImage = new ImageView(queen);
                        button.setGraphic(queenImage);
                    }
                    if (this.board.getPiece(row,col).equals(SoltrChessModel.Piece.NONE)) {
                        ImageView limage = new ImageView(light);
                        button.setGraphic(limage);
                    }
                }
                counter++;
                gridPane.add(button, col, row);
                int finalRow = row;
                int finalCol = col;
                button.setOnAction((event) -> {
                    if (!board.isGoal()) {
                        if (!isMove) {
                            currRow = finalRow;
                            currCol = finalCol;
                            messages.setText("Source Selected: (" + currRow + "," + currCol + ")");
                            isMove = true;
                        }
                        else {
                            if (board.isValidMove(currRow,currCol, finalRow, finalCol)) {
                                board.makeMove(currRow, currCol, finalRow, finalCol);
                            }else{
                                messages.setText("Invalid move.");
                            }
                            isMove = false;
                        }
                    }
                });
            }
        }
        return gridPane;
    }

    /**
     * Helper class to start a new game. We call this in our setOnAction inside
     * of the start method.
     *
     * @param file the file
     */
    private void startNewGame(String file) {
        this.board = new SoltrChessModel(file);
        initializeView();
        borderPane.setCenter(makeGridpane());
    }

    /**
     * Initializing the gameboard. We pull the command line arguments in here
     * to set the board to the proper file.
     */
    public void init(){
        Parameters params = getParameters();
        filename = params.getRaw().get(0);
        this.board = new SoltrChessModel(filename);
        initializeView();
    }

    /**
     * Our start method is similar to a main method in which it sets up the
     * entirety of our GUI by setting up borderPane and calling makeGridPane
     * to be put inside of our borderPane. setOnActions are utilized for
     * new game and restart functionality.
     *
     * @param stage our stage
     */
    @Override
    public void start(Stage stage) {
        borderPane = new BorderPane();
        FileChooser fileChooser = new FileChooser();
        GridPane gridPane = makeGridpane();
        HBox hbox = new HBox();
        topInfo = new HBox();
        messages = new Label("Puzzle ID: " + filename);
        Button start = new Button("NewGame");
        topInfo.getChildren().addAll(messages);
        topInfo.setAlignment(Pos.CENTER);
        borderPane.setTop(topInfo);
        start.setOnAction(e-> {
            fileChooser.setInitialDirectory(new File("data"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            filename = selectedFile.getAbsolutePath();
            startNewGame(filename);
            messages.setText("New Game ");
        });
        Button restart = new Button("Restart");
        restart.setOnAction(e-> {
            startNewGame(filename);
            messages.setText("New Game " + filename);
                });
        Button hint = new Button("Hint");
        hint.setOnAction(e-> {
            Backtracker backtracker = new Backtracker();
            List<Configuration> solve = backtracker.solveWithPath(board);
            if(!board.isGoal()) {
                board = (SoltrChessModel) solve.get(1);
                initializeView();
            }
            borderPane.setCenter(makeGridpane());
            if(board.isGoal()) {
                messages.setText("You won. Congratulations!");
            }
        });
        Button solve = new Button("Solve");
        solve.setOnAction(e-> {
            Solver sol = new Solver( this );
            sol.start();
        });
        hbox.getChildren().addAll(start,restart,hint,solve);
        hbox.setAlignment(Pos.CENTER);
        borderPane.setBottom(hbox);
        borderPane.setCenter(gridPane);
        Scene scene = new Scene(borderPane);
        stage.setTitle("Solitaire Chess Emily and Gabriel");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }


    /**
     * An alternate thread class.
     */
    private static class Solver extends Thread {

        private SoltrChessGUI gui;

        public Solver( SoltrChessGUI gui ) {
            this.gui = gui;
        }

        @Override
        public void run() {
            Backtracker backtracker = new Backtracker();
            List<Configuration> solution = backtracker.solveWithPath(this.gui.board);
            if(solution != null) {
                int counter = 0;
                for (Configuration i : solution) {
                    if(counter != 0) {
                        try {
                            Thread.sleep(500);
                            int finalCounter = counter;
                            javafx.application.Platform.runLater(() -> {
                                this.gui.messages.setText("Step " + finalCounter);
                                this.gui.board = (SoltrChessModel) i;
                                this.gui.initializeView();
                                this.gui.borderPane.setCenter(this.gui.makeGridpane());
                            });
                        }
                        catch( InterruptedException ie ) {
                        }

                    }
                    counter++;
                }
                javafx.application.Platform.runLater(() ->
                    this.gui.messages.setText("You won. Congratulations!"));

            }else{
                javafx.application.Platform.runLater(() ->
                    this.gui.messages.setText("No solution"));

            }
        }
    }

    /**
     * Our update method makes sure to update the GUI when we're making moves
     * and restarting and making a new game.
     *
     * @param board our gameboard from the model
     * @param data the data for handling our messages at the top
     */
    @Override
    public void update(SoltrChessModel board, SoltrChessModel.updateInfo data) {
        System.out.println(board);
        borderPane.setCenter(makeGridpane());
        if (data != null) {
            messages.setText(data.info);
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}