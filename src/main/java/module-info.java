module org.example.musicplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens org.example.musicplayer to javafx.fxml;
    exports org.example.musicplayer;
}