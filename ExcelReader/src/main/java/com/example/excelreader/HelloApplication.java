package com.example.excelreader;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HelloApplication extends Application {
    private static ArrayList<SaleProducts> products = new ArrayList<>();
    private static ObservableList<SaleProducts> obProducts = FXCollections.observableArrayList();
    private static XYChart.Series<String, Number> series;

    @Override
    public void start(Stage stage) throws IOException {
        Button btn = new Button("Выберите файл");

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter excelFilter = new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls");
        fileChooser.getExtensionFilters().add(excelFilter);

        TableView<SaleProducts> table = new TableView<>();
        table.setItems(obProducts);

        TableColumn<SaleProducts, Integer> columnId = new TableColumn<>("Номер");
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<SaleProducts, String> columnName = new TableColumn<>("Название");
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<SaleProducts, Integer> columnPrice = new TableColumn<>("Цена");
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<SaleProducts, Integer> columnQuantity = new TableColumn<>("Количество");
        columnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<SaleProducts, Integer> columnFinalPrice = new TableColumn<>("Итоговая цена");
        columnFinalPrice.setCellValueFactory(new PropertyValueFactory<>("finalPrice"));

        TableColumn<SaleProducts, LocalDate> columnDate = new TableColumn<>("Дата");
        columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        table.getColumns().addAll(columnId, columnName, columnPrice, columnQuantity, columnFinalPrice, columnDate);

        CategoryAxis xAxis = new CategoryAxis();

        xAxis.setCategories(FXCollections.observableArrayList(Arrays.asList(
                "январь", "февраль", "март", "апрель", "май", "июнь", "июль", "август", "сентябрь", "октябрь", "ноябрь", "декабрь")));
        xAxis.setLabel("Месяц");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Прибыль");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Месячная прибыль");

        series = new XYChart.Series<>();
        barChart.getData().add(series);

        barChart.setLegendVisible(false);

        btn.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                obProducts.clear();
                readExcelFile(selectedFile);
                updateBarChart();
            }
        });
        VBox vBox = new VBox(10, btn, table, barChart);
        vBox.setAlignment(Pos.CENTER);
        VBox.setVgrow(table, Priority.ALWAYS);

        Scene scene = new Scene(vBox, 1000, 700);
        stage.setTitle("ExcelReader!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void readExcelFile(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            rowIterator.next();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                int id = (int) row.getCell(0).getNumericCellValue();
                String name = row.getCell(1).getStringCellValue();
                int price = (int) row.getCell(2).getNumericCellValue();
                int quantity = (int) row.getCell(3).getNumericCellValue();
                int finalPrice = (int) row.getCell(4).getNumericCellValue();

                LocalDate date;
                Cell dateCell = row.getCell(5);
                if (DateUtil.isCellDateFormatted(dateCell)) {
                    Date excelDate = dateCell.getDateCellValue();
                    date = excelDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                } else {
                    date = LocalDate.of(1900, 1, 1).plusDays((long) dateCell.getNumericCellValue() - 2);
                }

                obProducts.add(new SaleProducts(id, name, price, quantity, finalPrice, date));
                products.add(new SaleProducts(id, name, price, quantity, finalPrice, date));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateBarChart() {
        series.getData().clear();

        ArrayList<String> months = new ArrayList<>();
        months.add("январь");
        months.add("февраль");
        months.add("март");
        months.add("апрель");
        months.add("май");
        months.add("июнь");
        months.add("июль");
        months.add("август");
        months.add("сентябрь");
        months.add("октябрь");
        months.add("ноябрь");
        months.add("декабрь");

        int[] profitByMonth = new int[12];

        for (int i = 0; i < products.size(); i++) {
            int monthIndex = products.get(i).getDate().getMonthValue() - 1;
            profitByMonth[monthIndex] += products.get(i).getFinalPrice();
        }

        for (int i = 0; i < 12; i++) {
            String monthName = months.get(i);
            int profit = profitByMonth[i];
            series.getData().add(new XYChart.Data<>(monthName, profit));
        }
    }
}
