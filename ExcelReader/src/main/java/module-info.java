module com.example.excelreader {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.ooxml;
    requires java.datatransfer;


    opens com.example.excelreader to javafx.fxml;
    exports com.example.excelreader;
}