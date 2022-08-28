package crud;

import Logs.LogWriter;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CreateTable {
    public boolean create_table(String query, String currentDatabase, int databaseIndex, List<DataBase> listOfdatabase){



        HashMap<String, ArrayList<String>> tableMap = new HashMap<>();
        String[] splitStr = query.split("\\s+");
        String tableName = (splitStr[2]);

        for (DataBase d: listOfdatabase) {
            for (Table t:d.getListOfTables()) {
                if(t.getTableName().equals(tableName)){
                    System.out.println("Table already exists");
                    return false;
                }
            }
        }

        List<String> columnString = new ArrayList<>();
        List<String> splitString = Arrays.asList(splitStr[3].split(","));
        columnString.add(splitString.get(0).substring(1));
        columnString.add(splitString.get((splitString.size()) - 1).replaceAll(".$", ""));

        Table table = new Table();
        table.setTableName(tableName);

        for (int i = 1; i < splitString.size() - 1; i++) {
            columnString.add(splitString.get(i));
        }

        for (int i = 0; i < columnString.size() ; i++) {
            tableMap.put(columnString.get(i), null);
        }
        table.setTableData(tableMap);

        listOfdatabase.get(databaseIndex).setListOfTables(table);

        //------------remove--------------------------
        try{

            // validate if same table name exists

            FileWriter myWriter = new FileWriter(currentDatabase +"/" + tableName + ".txt");

            for(String s : table.tableData.keySet()){
                myWriter.write(s +"|"+ "\n");
            }

            myWriter.close();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            LogWriter.writeQueryLogStatus("CREATE TABLE",query,true);

            String customMessage ="Database :"+currentDatabase+" Table: "+tableName+" "+" Table Created";
            LogWriter.writeEventLogStatus(customMessage,query);
        }
        catch (Exception e){
            e.printStackTrace();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            LogWriter.writeQueryLogStatus("CREATE TABLE :",query,true);
            return false;
        }

        if(table!=null){
            return true;
        }
        else{
            return false;
        }
    }
}
