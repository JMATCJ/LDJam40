package com.github.jmatcj.ld40;

import com.github.jmatcj.ld40.gui.Button;
import com.github.jmatcj.ld40.gui.Text;
import com.github.jmatcj.ld40.util.AssetLoader;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Map;

public class LDJam40 extends Application {
    private Game game;

    @Override
    public void init() throws Exception {
        // Load resources
        // At this stage, the stage (heh) hasn't been shown yet
        AssetLoader.initialize(getParameters().getRaw().contains("-nomusic"));
        game = new Game();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas canvas = new Canvas(1280, 720);
        Scene scene = new Scene(new Group(canvas));
        primaryStage.setScene(scene);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        scene.setOnMouseClicked(event -> game.onClick(event));

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                game.update(now);

                gc.clearRect(0, 0, 1280, 720);
                gc.drawImage(AssetLoader.getImage("background_one.png"), 0, 0);
                for (Map.Entry<Button, Text> e : game.getButtonsOnDisplay().entrySet()) {
                    gc.drawImage(e.getKey().getImage(), e.getKey().getX(), e.getKey().getY());
                    e.getValue().draw(gc);
                }
            }
        }.start();

        primaryStage.show();

        //TODO: Look into OGG later.
        if (!getParameters().getRaw().contains("-nomusic")) {
            MediaPlayer player = new MediaPlayer(AssetLoader.getMusic("main.mp3"));
            player.setVolume(0.2);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.play();
        }
    }

    @Override
    public void stop() throws Exception {

    }

    public static void main(String[] args) {
        launch(LDJam40.class, args);
    }
}
