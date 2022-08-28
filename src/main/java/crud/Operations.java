package crud;

import Logs.LogWriter;

import java.io.*;
import java.util.*;

public class Operations {
    public static final String GlobalPath = "../src/main/java/Directory/";
    String currentDatabase = "";

    public String getCurrentDatabase() {
        return currentDatabase;
    }

    public void setCurrentDatabase(String currentDatabase) {
        this.currentDatabase = currentDatabase;
    }

    int databaseIndex;

    public int getDatabaseIndex() {
        return databaseIndex;
    }

    public void setDatabaseIndex(int databaseIndex) {
        this.databaseIndex = databaseIndex;
    }

    public void sql_dump(List<DataBase> listOfdatabase, String dbName) {
        try {

            UseDatabase useDatabase = new UseDatabase();
            String[] result = useDatabase.use_database("use " + dbName , listOfdatabase);
            if (result != null) {
                String currentDatabase = result[0];
                setCurrentDatabase(currentDatabase);
                setDatabaseIndex(Integer.parseInt(result[1]));
            } else {
                System.out.println("Database does not exist");
            }

            PrintWriter writer = new PrintWriter("../src/main/java/constants/metadata/Metadata.txt", "UTF-8");
            for (int listTable = 0; listTable < listOfdatabase.get(getDatabaseIndex()).getListOfTables().size(); listTable++) {
                String currTableNum = listOfdatabase.get(getDatabaseIndex()).getListOfTables().get(listTable).getTableName();

                for (String s : listOfdatabase.get(getDatabaseIndex()).getListOfTables().get(listTable).getTableData().keySet()) {
                    writer.print(currTableNum.replace(".txt",""));
                    writer.print("|");
                    writer.print(s);
                    writer.print("|");
                    writer.print("varchar");
                    writer.println("");
                }
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Boolean decideOperation(String query, List<DataBase> listOfdatabase) throws IOException {
        Boolean status = true;
        query = query.toLowerCase();
        if (currentDatabase == "" && !query.startsWith("use")) {
            System.out.println("Hint* : No Data base set, set the data base using, USE command" + currentDatabase);
        }

        if (query.equals("exit")) {
            return false;
        } else if (query.startsWith("create schema")) {
            CreateDatabase createDatabase = new CreateDatabase();

            boolean result = createDatabase.create_database(query, listOfdatabase);
            if (result) {
                System.out.println("Database created successfully");
            } else {
                System.out.println("Error creating the database");
            }

        } else if (query.startsWith("use")) {
            UseDatabase useDatabase = new UseDatabase();
            String[] result = useDatabase.use_database(query, listOfdatabase);
            if (result != null) {
                String currentDatabase = result[0];
                setCurrentDatabase(currentDatabase);
                setDatabaseIndex(Integer.parseInt(result[1]));
            } else {
                System.out.println("Database does not exist");
            }

        } else if (query.startsWith("create table") && validateIfDatabaseIsSelected(query)) {
            CreateTable createTable = new CreateTable();
            boolean result = createTable.create_table(query, currentDatabase, getDatabaseIndex(), listOfdatabase);
            if (result) {
                System.out.println("Table created successfully");
            } else {
                System.out.println("Error creating the table");
            }

        } else if (query.startsWith("insert into") && validateIfDatabaseIsSelected(query)) {
            InsertIntoTable insertIntoTable = new InsertIntoTable();
            insertIntoTable.insert_into_table(query, currentDatabase, getDatabaseIndex(), listOfdatabase);

        } else if (query.startsWith("select") && validateIfDatabaseIsSelected(query)) {
            SelectFromTable selectFromTable = new SelectFromTable();
            selectFromTable.select_from_table(query, currentDatabase, getDatabaseIndex(), listOfdatabase);

        } else if (query.startsWith("update") && validateIfDatabaseIsSelected(query)) {
            UpdateColumn updateColumn = new UpdateColumn();
            updateColumn.update_column(query, currentDatabase, getDatabaseIndex(), listOfdatabase);

        } else if (query.startsWith("delete") && validateIfDatabaseIsSelected(query)) {
            DeleteRow deleteRow = new DeleteRow();
            deleteRow.delete_row(query, currentDatabase, getDatabaseIndex(), listOfdatabase);

        } else {
            System.out.println(" \n" +
                    "Available commands : \n" + "1) CREATE SCHEMA\n" +
                    "2) USE SCHEMA\n" +
                    "3) CREATE TABLE\n" +
                    "4) INSERT INTO table_name\n" +
                    "5) SELECT FROM table_name\n" +
                    "6) UPDATE\n" +
                    "7) DELETE\n" +
                    "8) EXIT to stop querying");
        }
        return status;
    }

    public String setDatabaseInUse(String databaseInUse) {
        return null;
    }

    public Boolean validateIfDatabaseIsSelected(String query) {

        if (getCurrentDatabase() == "") {
            System.out.println("DATA BASE NOT SELECTED");
            LogWriter.writeQueryLogStatus("",query, false);
            return false;
        } else {
            System.out.println("Working on data base :" + currentDatabase);
            return true;
        }
    }
}