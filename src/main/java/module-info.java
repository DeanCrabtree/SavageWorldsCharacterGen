module org.lairdham {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    opens org.lairdham.controllers to javafx.fxml;
    opens org.lairdham.models to com.fasterxml.jackson.databind;
    exports org.lairdham;
}