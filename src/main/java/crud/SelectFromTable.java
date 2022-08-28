package crud;

import Logs.LogWriter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectFromTable {
    public void select_from_table(String query, String currentDatabase, int databaseIndex, List<DataBase> listOfdatabase) {
        String[] splitStr = query.split("\\s+");


        HashMap<String, ArrayList<String>> selectMap = new HashMap<>();
        String tableName = splitStr[3];
        Boolean tableFound =false;
        int numberOfRecords = 0;


        if (splitStr[1].equalsIgnoreCase("*")) {
            for (int j = 0; j < listOfdatabase.get(databaseIndex).getListOfTables().size(); j++) {
                if ((tableName + ".txt").equalsIgnoreCase(listOfdatabase.get(databaseIndex).getListOfTables().get(j).getTableName()) || (tableName).equalsIgnoreCase(listOfdatabase.get(databaseIndex).getListOfTables().get(j).getTableName())) {
                    selectMap = listOfdatabase.get(databaseIndex).getListOfTables().get(j).getTableData();

                    for (String key : selectMap.keySet()) {
                        for (int valueToPrint = 0; valueToPrint < selectMap.get(key).size(); valueToPrint++) {
                            numberOfRecords++;
                            System.out.println(key + ":" + selectMap.get(key).get(valueToPrint));
                        }
                    }
                    tableFound = true;
                }
            }
        } else {
            String columnName = splitStr[5];
            String value = splitStr[7];
            String finalValueToFind;
            value = value.substring(1);
            value = value.replaceAll(".$", "");
            finalValueToFind = value;
            String matchValue = splitStr[1];

            List<String> valuesList = new ArrayList<>();
            int findIndex = 0;

            for (int i = 0; i < listOfdatabase.get(databaseIndex).getListOfTables().size(); i++) {
                if ((tableName + ".txt").equalsIgnoreCase(listOfdatabase.get(databaseIndex).getListOfTables().get(i).getTableName()) || (tableName).equalsIgnoreCase(listOfdatabase.get(databaseIndex).getListOfTables().get(i).getTableName())) {
                    selectMap = listOfdatabase.get(databaseIndex).getListOfTables().get(i).getTableData();
                    for (String s : selectMap.keySet()) {


                        if (columnName.equalsIgnoreCase(s)) {
                            valuesList = selectMap.get(s);
                            for (int fetchSelect = 0; fetchSelect < valuesList.size(); fetchSelect++) {

                                if (valuesList.get(fetchSelect).equalsIgnoreCase(finalValueToFind)) {
                                    findIndex = fetchSelect;

                                }
                            }
                        }
                    }
                    for (String s : selectMap.keySet()) {
                        if (s.equalsIgnoreCase(matchValue)) {
                            selectMap.get(s).get(findIndex);
                            numberOfRecords++;
                            System.out.println(selectMap.get(s).get(findIndex));
                        }
                    }
                    tableFound = true;
                }
            }
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        LogWriter.login_time = dtf.format(now);
        LogWriter.writeQueryLogStatus("SELECT :",query,tableFound);

        if (tableFound){
            String customMessage ="Database :"+currentDatabase+" table: "+tableName+", "+numberOfRecords+" number of Records selected";
            LogWriter.writeEventLogStatus(customMessage,query);
        }

    }
}
