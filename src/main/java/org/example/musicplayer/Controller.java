package org.example.musicplayer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    @FXML
    private Label songLabel;
    @FXML
    private Pane pane;
    @FXML
    private ProgressBar songProgressBar;
    @FXML
    private Button playBtn, pauseBtn, resetBtn, previousBtn, nextBtn;
    @FXML
    private ComboBox<String> speedBox;
    @FXML
    private Slider volumeSlider;

    private Media media;
    private MediaPlayer mediaPlayer;
    private File directory;
    private File[] files;

    private ArrayList<File> songs;

    private int songNumber;
    private int[] speeds = {25, 50, 75, 100, 125, 150, 175, 200};

    private Timer timer;
    private TimerTask task;
    private boolean running;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        songs = new ArrayList<File>();

        directory = new File("C:\\Users\\pc\\Desktop\\MusicPlayer\\src\\main\\resources\\org\\example\\musicplayer\\music");

        files = directory.listFiles();

        if (files != null) {
            songs.addAll(Arrays.asList(files));
        }

        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        songLabel.setText(songs.get(songNumber).getName());

        for (int speed : speeds) {
            speedBox.getItems().add(Integer.toString(speed) + "%");
        }

        speedBox.setOnAction(this::changeSpeed);

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {

                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }
        });

    }
    public void playMedia() {

        beginTimer();
        changeSpeed(null);
        mediaPlayer.play();

    }
    public void pauseMedia() {

        cancelTimer();
        mediaPlayer.pause();

    }
    public void resetMedia() {

        songProgressBar.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0));

    }
    public void previousMedia() {

        if (songNumber > 0) {
            songNumber--;

            mediaPlayer.stop();

            if (running) {
                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songLabel.setText(songs.get(songNumber).getName());

            playMedia();
        } else {
            songNumber = songs.size() - 1;

            mediaPlayer.stop();

            if (running) {
                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songLabel.setText(songs.get(songNumber).getName());

            playMedia();
        }
    }
    public void nextMedia() {

        if (songNumber < songs.size() - 1) {
            songNumber++;

            mediaPlayer.stop();

            if (running) {
                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songLabel.setText(songs.get(songNumber).getName());

            playMedia();
        } else {
            songNumber = 0;

            mediaPlayer.stop();

            if (running) {
                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songLabel.setText(songs.get(songNumber).getName());

            playMedia();
        }
    }
    public void changeSpeed(ActionEvent e) {

        if (speedBox.getValue() == null) {
            mediaPlayer.setRate(1);
        } else {
            String value = speedBox.getValue().substring(0, speedBox.getValue().length() - 1);
            mediaPlayer.setRate(Integer.parseInt(value) * 0.01);
        }
    }
    public void beginTimer() {

        timer = new Timer();

        task = new TimerTask() {
            @Override
            public void run() {

                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                songProgressBar.setProgress(current / end);

                if (current / end == 1) {
                    cancelTimer();
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000);
    }
    public void cancelTimer() {
        running = false;
        timer.cancel();
    }

}