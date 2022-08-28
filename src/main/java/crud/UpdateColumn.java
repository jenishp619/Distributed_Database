package crud;

import Analytics.Analytics;
import Logs.LogWriter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UpdateColumn {
    public void update_column(String query, String currentDatabase, int databaseIndex, List<DataBase> listOfdatabase){

        System.out.println("update column");
        System.out.println(query);
        Boolean status = false;
        int count=0;

        //update student set Name = 'Devam' where Id=2
        String[] updateQuery = query.split(" ");
        String updateColumnName = updateQuery[3];
        String updateColumnValue = updateQuery[5];
        String conditionColumnName = updateQuery[7];
        String conditionColumnValue = updateQuery[9];


        //list<University==> <student obj ==>  <HashMap <columnName, value>>>
        for(DataBase database : listOfdatabase){
            if((database.DatabaseName).equalsIgnoreCase(String.valueOf(listOfdatabase.get(databaseIndex).getDatabaseName()))){
                for(int i=0;i<listOfdatabase.get(databaseIndex).getListOfTables().size();i++){
                    if(listOfdatabase.get(databaseIndex).getListOfTables().get(i).getTableName().equalsIgnoreCase((updateQuery[1]) + ".txt") || listOfdatabase.get(databaseIndex).getListOfTables().get(i).getTableName().equalsIgnoreCase((updateQuery[1]))){
                        listOfdatabase.get(databaseIndex).getListOfTables().get(i).getTableData().get(conditionColumnName);
                        for(int j=0; j<listOfdatabase.get(databaseIndex).getListOfTables().get(i).getTableData().get(conditionColumnName).size();j++){
                            if(listOfdatabase.get(databaseIndex).getListOfTables().get(i).getTableData().get(conditionColumnName).get(j).equalsIgnoreCase(conditionColumnValue)){

                                status = true;
                                listOfdatabase.get(databaseIndex).getListOfTables().get(i).getTableData().get(conditionColumnName).get(j);
                                ArrayList<String> arr = new ArrayList<>();
                                arr.addAll(listOfdatabase.get(databaseIndex).getListOfTables().get(i).getTableData().get(updateColumnName));
                                arr.set(j, updateColumnValue);
                                listOfdatabase.get(databaseIndex).getListOfTables().get(i).getTableData().put(updateColumnName, arr);
                                count ++;
                            }
                        }
                    }
                }
            }
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        LogWriter.login_time = dtf.format(now);
        LogWriter.writeQueryLogStatus("UPDATE :",query,status);
        String customMessage ="Database :"+currentDatabase+" table: "+updateQuery[1]+" Column :"
                +updateColumnName+" update values from "+conditionColumnValue+" to "+updateColumnValue+
                " ,Records updated :"+count;
        LogWriter.writeEventLogStatus(customMessage,query);
        Analytics update = new Analytics();
        update.setUpdateAnalytics(updateQuery[1],count);
    }
}
