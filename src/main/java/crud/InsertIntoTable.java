package crud;

import Logs.LogWriter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InsertIntoTable {
    public void insert_into_table(String query, String currentDatabase, int databaseIndex, List<DataBase> listOfdatabase){
        Boolean status = false;

        String[] splitStr = query.split("\\s+");
        String tableName = (splitStr[2]);
        List<String> columnString = new ArrayList<>();
        List<String> splitColumnString = Arrays.asList(splitStr[3].split(","));
        ArrayList<String> valueString = new ArrayList<>();
        List<String> splitValueString = Arrays.asList(splitStr[5].split(","));
        columnString.add(splitColumnString.get(0).substring(1));

        valueString.add(splitValueString.get(0).substring(1));
        for (int i = 1; i < splitColumnString.size() - 1; i++) {
            columnString.add(splitColumnString.get(i));
            valueString.add(splitValueString.get(i));
        }

        valueString.add(splitValueString.get((splitValueString.size()) - 1).replaceAll(".$", ""));
        columnString.add(splitColumnString.get((splitColumnString.size()) - 1).replaceAll(".$", ""));


        for (int i = 0; i < listOfdatabase.get(databaseIndex).getListOfTables().size(); i++) {
            if ((tableName+".txt").equalsIgnoreCase(listOfdatabase.get(databaseIndex).getListOfTables().get(i).getTableName()) || (tableName).equalsIgnoreCase(listOfdatabase.get(databaseIndex).getListOfTables().get(i).getTableName())) {
                int tableIndex = i;
                for (int column = 0; column < columnString.size(); column++) {
                    if (listOfdatabase.get(databaseIndex).getListOfTables().get(i).getTableData().containsKey(columnString.get(column))) {
                        int columnIndex = column;
                        ArrayList<String> columnList = new ArrayList<>();


                        if(listOfdatabase.get(databaseIndex).getListOfTables().get(i).getTableData().get(columnString.get(column)) != null){
                            columnList = listOfdatabase.get(databaseIndex).getListOfTables().get(i).getTableData().get(columnString.get(column));
                        }

                        columnList.add(valueString.get(columnIndex));

                        listOfdatabase.get(databaseIndex).getListOfTables().get(i).getTableData().put(columnString.get(columnIndex), columnList);
                        status = true;
                    }
                }
            }
        }


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        LogWriter.login_time = dtf.format(now);
        LogWriter.writeQueryLogStatus("INSERT INTO :",query,status);

        String customMessage ="Database :"+currentDatabase+" table: "+tableName+", "+" 1 Record Inserted";
        LogWriter.writeEventLogStatus(customMessage,query);

    }
}
