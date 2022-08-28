package crud;

import Logs.LogWriter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeleteRow {
    public void delete_row(String query, String currentDatabase, int databaseIndex, List<DataBase> listOfdatabase){

        HashMap<String, ArrayList<String>> deleteMap = new HashMap<>();

        //DELETE FROM movies WHERE movie_id = 18;
        String[] splitStr = query.split("\\s+");
        String tableName = splitStr[2];
        String columnName = splitStr[4];
        String eleToBeDeleted = splitStr[6];
        int finalDelete=0;
        int count=0;
        int deleteRecordsCount=0;

        for (int i = 0; i < listOfdatabase.get(databaseIndex).getListOfTables().size(); i++) {
            if ((tableName + ".txt").equalsIgnoreCase(listOfdatabase.get(databaseIndex).getListOfTables().get(i).getTableName()) || (tableName).equalsIgnoreCase(listOfdatabase.get(databaseIndex).getListOfTables().get(i).getTableName())) {
                count=i;
                deleteMap = listOfdatabase.get(databaseIndex).getListOfTables().get(i).getTableData();
                List<String> deleteElements = new ArrayList<>();
                deleteElements = deleteMap.get(columnName);
                for(int finIndexForDelete =0 ; finIndexForDelete<deleteElements.size();finIndexForDelete++){
                    if(eleToBeDeleted.equalsIgnoreCase(deleteElements.get(finIndexForDelete))){
                        finalDelete=finIndexForDelete;
                        deleteRecordsCount++;

                        if(finalDelete > -1){

                            boolean flag=false;
                            for (int l = 0; l < listOfdatabase.get(databaseIndex).getListOfTables().get(count).getTableData().values().size(); l++) {
                                for (String s : listOfdatabase.get(databaseIndex).getListOfTables().get(count).getTableData().keySet()) {
                                    listOfdatabase.get(databaseIndex).getListOfTables().get(count).getTableData().get(s).remove(finalDelete);
                                    flag=true;
                                }
                                if(flag){
                                    break;
                                }
                            }

                    }
                }
            }

        }

        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        LogWriter.login_time = dtf.format(now);
        LogWriter.writeQueryLogStatus("DELETE :",query,true);


        LogWriter.writeEventLogStatus("DELETED records from table "+tableName+", "+deleteRecordsCount+" Rows deleted",query);

    }
}
