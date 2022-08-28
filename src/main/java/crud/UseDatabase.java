package crud;

import Logs.LogWriter;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UseDatabase {
    public String[] use_database(String query, List<DataBase> listOfdatabase) {

        String path = Path.GLOBAL_PATH;
        String[] splitStr = query.split("\\s+");
        String schemaName = (splitStr[splitStr.length - 1]);
        String currentDatabase = path + schemaName;

        File database = new File(currentDatabase);
        for (int i = 0; i < listOfdatabase.size(); i++) {
            if (listOfdatabase.get(i).getDatabaseName().equalsIgnoreCase(schemaName)) {
                if (database.exists() && database.isDirectory()) {
                    System.out.println(" working on data base :" + schemaName);
                    String[] result = new String[2] ;
                    result[0] = currentDatabase;
                    result[1] = Integer.toString(i);

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    LogWriter.login_time = dtf.format(now);
                    LogWriter.writeQueryLogStatus("USE",query, true);

                    return result;
                } else {
                    System.out.println("Data base does not exits CREATE before use");

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    LogWriter.login_time = dtf.format(now);
                    LogWriter.writeQueryLogStatus("USE :",query, false);
                    return null;
                }
            }
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        LogWriter.login_time = dtf.format(now);
        LogWriter.writeQueryLogStatus("USE",query, false);
        return null;
    }
}
