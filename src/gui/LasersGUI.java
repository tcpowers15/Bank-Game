package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;
import java.io.File;
import java.nio.file.Paths;
import javafx.scene.shape.Rectangle;

import model.*;

/**
 * The main class that implements the JavaFX UI.   This class represents
 * the view/controller portion of the UI.  It is connected to the model
 * and receives updates from it.
 *
 * @author Sean Strout @ RIT CS
 * @author Brian Powers & Trevor Powers
 */
public class LasersGUI extends Application implements Observer {
    /** The UI's connection to the model */
    private LasersModel model;

    /** this can be removed - it is used to demonstrates the button toggle */
    private static boolean status = true;

    private BorderPane window;
    private Label toplabel;
    private HBox hbox;
    private GridPane grid;
    private HBox buttom;
    private Stage primaryStage;

    @Override
    public void init() throws Exception {
        // the init method is run before start.  the file name is extracted
        // here and then the model is created.
        try {
            Parameters params = getParameters();
            String filename = params.getRaw().get(0);
            this.model = new LasersModel(filename);
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
            System.exit(-1);
        }
        this.model.addObserver(this);
    }

    /**
     * A private utility function for setting the background of a button to
     * an image in the resources subdirectory.
     *
     * @param button the button control
     * @param bgImgName the name of the image file
     */
    private void setButtonBackground(Button button, String bgImgName) {
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image( getClass().getResource("resources/" + bgImgName).toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        button.setBackground(background);
    }

    /**
     * This is a private demo method that shows how to create a button
     * and attach a foreground image with a background image that
     * toggles from yellow to red each time it is pressed.
     *
     * @param stage the stage to add components into
     */
    private void buttonDemo(Stage stage) {
        // this demonstrates how to create a button and attach a foreground and
        // background image to it.
        Button button = new Button();
        Image laserImg = new Image(getClass().getResourceAsStream("resources/laser.png"));
        ImageView laserIcon = new ImageView(laserImg);
        button.setGraphic(laserIcon);
        setButtonBackground(button, "yellow.png");
        button.setOnAction(e -> {
            // toggles background between yellow and red
            if (!status) {
                setButtonBackground(button, "yellow.png");
            } else {
                setButtonBackground(button, "red.png");
            }
            status = !status;
        });

        Scene scene = new Scene(button);
        stage.setScene(scene);
    }

    /**
     * The
     * @param stage the stage to add UI components into
     */
    private void init(Stage stage) {
        // TODO
        this.primaryStage = stage;
        this.hbox = new HBox();
        this.buttom = new HBox();
        this.grid = new GridPane();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO
        init(primaryStage);  // do all your UI initialization here
        BorderPane window = new BorderPane();
        window.setPadding(new Insets(20, 20, 20, 20));


        setTop();

        setGrid();

        setHBox();
        //mooooooorrrrrreeeeeeeee event handlers

        window.setTop(this.hbox);
        window.setBottom(this.buttom);
        window.setCenter(this.grid);

        primaryStage.setTitle("Lasers");
        primaryStage.setScene(new Scene(window));
        primaryStage.show();
    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO
        Platform.runLater(
                () -> {
                    UpdateGrid();
                    UpdateLabel();
                    primaryStage.sizeToScene();
                }
        );
    }

    public void setGrid(){
        this.grid.setPadding(new Insets(10, 10, 10, 10));
        this.grid.setHgap(10);
        this.grid.setVgap(10);
        int rows = this.model.getrows();
        int cols = this.model.getcols();
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){

                int row = i;
                int col = j;
                if (this.model.getPos(j, i) == 'L'){
                    Button button = new Button();
                    button.setPadding(new Insets(0, 0, 0, 0));
                    button.setShape(new Rectangle(40, 40));
                    button.setPrefSize(30, 30);
                    Image laserImg = new Image(getClass().getResourceAsStream("resources/laser.png"));
                    ImageView laserIcon = new ImageView(laserImg);
                    button.setGraphic(laserIcon);
                    setButtonBackground(button, "yellow.png");
                    button.setOnAction(e -> {
                        this.model.remove(row, col);
                        //this.toplabel.setText(this.model.getOuts());
                    });

                    this.grid.add(button, j, i);
                }

                else if (this.model.getPos(j, i) == '*'){
                    Button button = new Button();
                    button.setPadding(new Insets(0, 0, 0, 0));
                    button.setShape(new Rectangle(40, 40));
                    button.setPrefSize(30, 30);
                    Image lbeamImg = new Image(getClass().getResourceAsStream("resources/beam.png"));
                    ImageView beamIcon = new ImageView(lbeamImg);
                    button.setGraphic(beamIcon);
                    setButtonBackground(button, "yellow.png");
                    button.setOnAction(e -> {
                        this.model.add(row, col);
                        //this.toplabel.setText(this.model.getOuts());
                    });

                    this.grid.add(button, j, i);
                }

                else if(this.model.getPos(j, i) == '0'){
                    Button button = new Button();
                    button.setPadding(new Insets(0, 0, 0, 0));
                    button.setPrefSize(30, 30);
                    Image pillar0Img = new Image(getClass().getResourceAsStream("resources/pillar0.png"));
                    ImageView pillar0Icon = new ImageView(pillar0Img);
                    button.setGraphic(pillar0Icon);
                    setButtonBackground(button, "white.png");
                    button.setOnAction(e -> {
                        this.model.add(row, col);
                    });

                    this.grid.add(button, j, i);
                }

                else if(this.model.getPos(j, i) == '1'){
                    Button button = new Button();
                    button.setPadding(new Insets(0, 0, 0, 0));
                    button.setPrefSize(30, 30);
                    Image pillar1Img = new Image(getClass().getResourceAsStream("resources/pillar1.png"));
                    ImageView pillar1Icon = new ImageView(pillar1Img);
                    button.setGraphic(pillar1Icon);
                    setButtonBackground(button, "white.png");
                    button.setOnAction(e -> {
                        this.model.add(row, col);
                    });

                    this.grid.add(button, j, i);
                }

                else if(this.model.getPos(j, i) == '2'){
                    Button button = new Button();
                    button.setPadding(new Insets(0, 0, 0, 0));
                    button.setPrefSize(30, 30);
                    Image pillar2Img = new Image(getClass().getResourceAsStream("resources/pillar2.png"));
                    ImageView pillar2Icon = new ImageView(pillar2Img);
                    button.setGraphic(pillar2Icon);
                    setButtonBackground(button, "white.png");
                    button.setOnAction(e -> {
                        this.model.add(row, col);
                    });

                    this.grid.add(button, j, i);
                }

                else if(this.model.getPos(j, i) == '3'){
                    Button button = new Button();
                    button.setPadding(new Insets(0, 0, 0, 0));
                    button.setPrefSize(30, 30);
                    Image pillar3Img = new Image(getClass().getResourceAsStream("resources/pillar3.png"));
                    ImageView pillar3Icon = new ImageView(pillar3Img);
                    button.setGraphic(pillar3Icon);
                    setButtonBackground(button, "white.png");
                    button.setOnAction(e -> {
                        this.model.add(row, col);
                    });

                    this.grid.add(button, j, i);
                }

                else if(this.model.getPos(j, i) == '4'){
                    Button button = new Button();
                    button.setPadding(new Insets(0, 0, 0, 0));
                    button.setPrefSize(30, 30);
                    Image pillar4Img = new Image(getClass().getResourceAsStream("resources/pillar4.png"));
                    ImageView pillar4Icon = new ImageView(pillar4Img);
                    button.setGraphic(pillar4Icon);
                    setButtonBackground(button, "white.png");
                    button.setOnAction(e -> {
                        this.model.add(row, col);
                    });

                    this.grid.add(button, j, i);
                }

                else if(this.model.getPos(j, i) == 'X'){
                    Button button = new Button();
                    button.setPadding(new Insets(0, 0, 0, 0));
                    button.setPrefSize(30, 30);
                    Image pillarxImg = new Image(getClass().getResourceAsStream("resources/pillarX.png"));
                    ImageView pillarxIcon = new ImageView(pillarxImg);
                    button.setGraphic(pillarxIcon);
                    setButtonBackground(button, "white.png");
                    button.setOnAction(e -> {
                        this.model.add(row, col);
                    });

                    this.grid.add(button, j, i);
                }

                else{
                    Button button = new Button();
                    button.setPadding(new Insets(0, 0, 0, 0));
                    button.setShape(new Rectangle(40, 40));
                    button.setPrefSize(30, 30);
                    setButtonBackground(button, "white.png");
                    button.setOnAction(e -> {
                        this.model.add(row, col);
                    });
                    this.grid.setAlignment(Pos.CENTER);

                    this.grid.add(button, j, i);
                }
            }
        }
    }

    public void setHBox(){
        this.buttom.setPadding(new Insets(20, 20, 20, 20));
        this.buttom.setAlignment(Pos.CENTER);
        Button Check = new Button("Check");
        Check.setShape(new Rectangle(80, 40));
        Check.setPrefSize(80, 40);
        Button Hint = new Button("Hint");
        Hint.setShape(new Rectangle(80, 40));
        Hint.setPrefSize(80, 40);
        Button Solve = new Button("Solve");
        Solve.setShape(new Rectangle(80, 40));
        Solve.setPrefSize(80, 40);
        Button Restart = new Button("Restart");
        Restart.setShape(new Rectangle(80, 40));
        Restart.setPrefSize(80, 40);
        Button Load = new Button("Load");
        Load.setShape(new Rectangle(80, 40));
        Load.setPrefSize(80, 40);

        ((Button)Check).setOnAction((ActionEvent e) -> {
            this.model.verify();
        });
        ((Button)Hint).setOnAction((ActionEvent e) -> {
            this.model.hint();
        });
        ((Button)Solve).setOnAction((ActionEvent e) -> {
            this.model.solve();
        });
        ((Button)Restart).setOnAction((ActionEvent e) -> {
            try {
                this.model.restart();
            }catch(FileNotFoundException e1){
                System.out.println(e1);
            }
        });
        ((Button)Load).setOnAction((ActionEvent e) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            File file = fileChooser.showOpenDialog(primaryStage);
            this.model.setFilename(Paths.get(file.getAbsolutePath()).getFileName().toString());
            try {
                this.model.restart();
            }catch(FileNotFoundException e1){
                System.out.println(e1);
            }
            this.model.load();
        });

        this.buttom.getChildren().addAll(Check, Hint, Solve, Restart, Load);
    }

    public  void setTop(){
        Label toplabel = new Label(this.model.getFilename() + " loaded");
        toplabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        toplabel.setTextFill((Color.BLACK));
        this.hbox.getChildren().add(toplabel);
        this.hbox.setPadding(new Insets(0, 0, 20, 0));
        this.hbox.setAlignment(Pos.CENTER);

    }

    public void UpdateGrid(){
        setGrid();
    }

    public void UpdateLabel(){
        Label l = new Label(this.model.getOuts());
        this.hbox.getChildren().remove(0);
        this.hbox.getChildren().add(l);
    }
}
