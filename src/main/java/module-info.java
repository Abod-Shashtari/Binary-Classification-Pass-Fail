module org.ml.binaryclassification {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens org.ml.binaryclassification to javafx.fxml;
    exports org.ml.binaryclassification;
}