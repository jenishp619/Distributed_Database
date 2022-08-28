import Analytics.Analytics;
import Logs.LogWriter;
import crud.DataBase;
import crud.Operations;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import crud.Table;
import erd.Erd;
import export.SQLdump;
import register.Registration;

import static java.lang.System.out;


public class Main {
    public static List<DataBase> listOfdatabase = new ArrayList<>();


    public static void main(String[] args) throws IOException {
        int queryCount = 0;
        String dataBase = null;
        LoadData();
        Scanner sc = new Scanner(System.in);
        try {

            File obj = new File("Registration.txt");

            if (obj.createNewFile()) {
                out.println("File is created");
            }

        } catch (IOException e) {
            out.println("An error occurred.");
            e.printStackTrace();
        }
        int choice;


        int flag = 0;

        while (flag != 3) {

            System.out.println("");
            System.out.println("===>MENU<===");
            System.out.println("1. Registration");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.println("");

            out.println("Enter your Choice");
            choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                Registration user = new Registration();
                user.register();
                flag = user.login();
                if (flag == 1) {
                    break;
                }
            } else if (choice == 2) {
                Registration user = new Registration();
                flag = user.login();
                if (flag == 1) {
                    break;
                }
            } else if (choice == 3) {
                out.println("Exiting");
                flag = 0;
                break;
            } else {
                out.println("Choose Proper Option");
            }
        }

        if (flag != 0) {
            String answer = "";
            while (!answer.equals("5")) {

                System.out.println("");
                System.out.println("===>MENU<===");
                System.out.println("0. Start Transaction");
                System.out.println("1. Write Queries");
                System.out.println("2. Export");
                System.out.println("3. Data Model");
                System.out.println("4. Analytics");
                System.out.println("5. Quit");

                System.out.println("Enter your choice:");

                answer = sc.nextLine();
                out.println(answer);
                out.println("user " + LogWriter.current_user);
                out.println("time :" + LogWriter.login_time);

                Operations operations = new Operations();
                switch (answer) {
                    case "0":

                        Boolean transact = true;

                        System.out.println("Enter the list of queries in the transaction");
                        while (transact) {

                            String query = sc.nextLine();
                            if (!query.toLowerCase().startsWith("end transaction")) {
                                queryCount++;
                                try {
                                    transact = operations.decideOperation(query, listOfdatabase);
                                } catch (Exception e) {
                                    System.err.println("Error Rolling back the transaction.........................");
                                    LoadData();
                                    break;

                                }

                            } else if (query.toLowerCase().startsWith("end transaction")) {
                                writeData();
                                break;
                            }

                        }

                        break;

                    case "1":
                        Boolean exitflag = true;

                        System.out.println("You can now Query the database now, use EXIT command to stop querying");
                        while (exitflag) {

                            queryCount++;
                            String query = sc.nextLine();
                            if (query.toLowerCase().startsWith("use")) {
                                String[] splitStr = query.split("\\s+");
                                dataBase = splitStr[1];
                            }
                            exitflag = operations.decideOperation(query, listOfdatabase);

                            if (!query.toLowerCase().startsWith("create table")) {
                                writeData();
                            }

                        }

                        break;
                    case "2":
                        System.out.println("Export");
                        out.println("Enter data base name");
                        String dbName = sc.nextLine();

                        operations.sql_dump(listOfdatabase, dbName);
                        SQLdump sqLdump = new SQLdump();
                        sqLdump.DumpMain(dbName);
                        break;
                    case "3":
                        System.out.println("Data Model");
                        Erd erd = new Erd();
                        erd.generateErd();
                        break;
                    case "4":
                        System.out.println("Analytics");
                        Analytics analytics = new Analytics();
                        analytics.storeUserQueries(LogWriter.current_user, queryCount, dataBase);
                        analytics.getUpdateAnalytics();

                        break;
                    case "5":
                        System.out.println("Exiting from the system");
                        break;
                    default:
                        System.out.println("Error");
                        break;
                }
            }
        }

        LogWriter.writeGenralLogStatus(listOfdatabase);
    }

    public static void LoadData() {
        String globalPath = "../src/main/java/Directory/";
        File[] dataBases = new File(globalPath).listFiles();

        for (File database : dataBases) {

            DataBase dataBaseObj = new DataBase();
            String databaseName = database.getName();
            String relativePath = globalPath + databaseName + "/";
            File[] tables = new File(relativePath).listFiles();
            dataBaseObj.setDatabaseName(databaseName);

            for (File table : tables) {
                Table tableObj = new Table();
                String tableName = table.getName();
                tableObj.setTableName(tableName);
                dataBaseObj.setListOfTables(tableObj);

                HashMap<String, ArrayList<String>> stringArrayListHashMap = fetchTableData(relativePath + tableName);
                tableObj.setTableData(stringArrayListHashMap);
            }

            listOfdatabase.add(dataBaseObj);
        }
    }


    public static HashMap<String, ArrayList<String>> fetchTableData(String tableName) {
        HashMap<String, ArrayList<String>> tableData = new HashMap<>();

        File readTable = new File(tableName);

        try (BufferedReader br = new BufferedReader(new FileReader(readTable))) {
            String line;
            while ((line = br.readLine()) != null) {
                ArrayList<String> singleColumnValues = new ArrayList<>();
                String[] columnData = line.split("\\|");
                for (int i = 1; i < columnData.length; i++) {
                    singleColumnValues.add(columnData[i]);
                }
                tableData.put(columnData[0], singleColumnValues);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tableData;
    }


    public static void writeData() {
        for (int dataBaseCount = 0; dataBaseCount < listOfdatabase.size(); dataBaseCount++) {
            String setPathForDb = "../src/main/java/Directory/" + listOfdatabase.get(dataBaseCount).getDatabaseName() + "/";
            getAllTables(setPathForDb, dataBaseCount);
        }

    }

    public static void getAllTables(String db, int indexOfDb) {
        for (int tableCount = 0; tableCount < listOfdatabase.get(indexOfDb).getListOfTables().size(); tableCount++) {
            // String currTableName = listOfdatabase.get(indexOfDb).getListOfTables().get(tableCount).getTableName();
            String currTableName = db + listOfdatabase.get(indexOfDb).getListOfTables().get(tableCount).getTableName().replaceFirst("[.][^.]+$", "");
            printToTables(currTableName, tableCount, indexOfDb);
        }

    }

    public static void printToTables(String currTableName, int tableCount, int dbIndex) {

        try {

            PrintWriter writer = new PrintWriter(currTableName + ".txt", "UTF-8");
            // FileWriter writer = new FileWriter(currTableName + ".txt");

            for (String columnName : listOfdatabase.get(dbIndex).getListOfTables().get(tableCount).getTableData().keySet()) {
                writer.print(columnName);
                writer.print("|");

                try {
                    for (int values = 0; values < listOfdatabase.get(dbIndex).getListOfTables().get(tableCount).getTableData().get(columnName).size(); values++) {
                        writer.print(listOfdatabase.get(dbIndex).getListOfTables().get(tableCount).getTableData().get(columnName).get(values));
                        writer.print("|");

                    }
                    writer.println("");
                } catch (Exception e) {
                    writer.println("");
                }
            }

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

