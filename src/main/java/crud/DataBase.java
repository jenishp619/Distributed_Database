package crud;

import java.util.ArrayList;

public class DataBase {
    String DatabaseName;
    private ArrayList<Table> listOfTables = new ArrayList();

    public DataBase() {

    }

    public DataBase(String databaseName) {
       DatabaseName=databaseName;
    }

    public String getDatabaseName() {
        return DatabaseName;
    }

    public void setDatabaseName(String databaseName) {
        DatabaseName = databaseName;
    }

    public ArrayList<Table> getListOfTables() {
        return listOfTables;
    }

    public void setListOfTables(Table listOfTables) {
        this.listOfTables.add(listOfTables);
    }
}
