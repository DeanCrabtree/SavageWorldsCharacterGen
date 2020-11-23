module org.lairdham {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    opens org.lairdham.controllers to javafx.fxml;
    opens org.lairdham to com.fasterxml.jackson.databind;
    opens org.lairdham.models to javafx.base, com.fasterxml.jackson.databind;
    exports org.lairdham;
}