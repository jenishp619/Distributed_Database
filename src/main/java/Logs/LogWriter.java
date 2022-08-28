package Logs;

import crud.DataBase;
import crud.Table;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static java.lang.System.out;

public class LogWriter {

    public static String current_user = "";
    public static String login_time="";
    public static String event_details="";
    public static String path = "../src/main/java/Logs/";

    public LogWriter(){
    }

    public static void writeGenralLog(){
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(LogWriter.path+"GenralLogs.txt", true));
            out.write( event_details + "\n");
            out.close();
        } catch (IOException e) {
            out.println("exception occurred while writing General Logs" + e);
        }
    }

    public static void writeGenralLogStatus(List<DataBase> Data){

        String final_data = "";
        String table_data = "";
        int table_counter = 0;

        final_data = "Database status @"+login_time+"\n";

        for (DataBase status:Data) {
            final_data += "Database Name = "+status.getDatabaseName()+" ,Number of tables = ";
            table_counter = 0;
            table_data="";
            for (Table table : status.getListOfTables()) {
                table_counter++;
                table_data += table.getTableName().replaceFirst("[.][^.]+$", "")+":: "+table.getTableData().size()+" ";
            }

            final_data += table_counter+" ,Table name & size ="+table_data+"\n";
        }
        try {
            BufferedWriter eventLog = new BufferedWriter(new FileWriter(LogWriter.path+"GenralLogs.txt", true));
            eventLog.write(  final_data+"\n");
            eventLog.close();
        } catch (IOException e) {
            System.err.println("exception occurred while writing General Logs" + e);
        }
    }


    public static void writeQueryLogStatus(String operation,String Query,boolean status){

        String statusString ="";

        if(status){
            statusString = "Valid query";
        }else{
            statusString = "Failed Query";
        }
        try {
            BufferedWriter eventLog = new BufferedWriter(new FileWriter(LogWriter.path+"QueryLogs.txt", true));
            eventLog.write(login_time+" "+current_user+" Operation = "+operation+" User Query = "+Query+", Status ="+statusString+"\n");
            eventLog.close();
        } catch (IOException e) {
            System.err.println("exception occurred while writing General Logs" + e);
        }
    }


    public static void writeEventLogStatus(String LogDetails, String Query){

        try {
            BufferedWriter eventLog = new BufferedWriter(new FileWriter(LogWriter.path+"EventLogs.txt", true));
            eventLog.write(login_time+" "+LogDetails+" User Query = "+Query+"\n");
            eventLog.close();
        } catch (IOException e) {
            System.err.println("exception occurred while writing General Logs " + e);
        }
    }
}
