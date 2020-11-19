module org.lairdham {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.lairdham to javafx.fxml;
    exports org.lairdham;
}