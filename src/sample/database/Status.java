package sample.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Status {
    public static ObservableList<String> statusSearchList = FXCollections.observableArrayList(
            "", "Видано", "Не видано", "На реставрації", "На списання"
    );

    public static ObservableList<String> statusChangeList = FXCollections.observableArrayList(
            "Не видано", "На реставрації", "На списання"
    );
}
