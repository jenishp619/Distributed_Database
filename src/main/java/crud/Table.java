package crud;

import java.util.ArrayList;
import java.util.HashMap;

public class Table {
    String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public HashMap<String, ArrayList<String>> getTableData() {
        return tableData;
    }

    public void setTableData(HashMap<String, ArrayList<String>> tableData) {
        this.tableData = tableData;
    }

    HashMap<String, ArrayList<String>> tableData = new HashMap<>();

    public void readTableFile() {

    }
}
