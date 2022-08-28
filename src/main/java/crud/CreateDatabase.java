package crud;

import Logs.LogWriter;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CreateDatabase {
    public boolean create_database(String query, List<DataBase> listOfdatabase){
        String path = Path.GLOBAL_PATH;
        String[] splitStr = query.split("\\s+");
        String schemaName = (splitStr[splitStr.length - 1]);
        File f1 = new File(path + schemaName);
        boolean bool2 = f1.mkdirs();
        DataBase db = new DataBase(schemaName);
        listOfdatabase.add(db);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        LogWriter.writeQueryLogStatus("CREATE SCHEMA :",query,bool2);



        if(bool2 == true){
            String customMessage ="New Database created :"+schemaName;
            LogWriter.writeEventLogStatus(customMessage,query);
            return true;
        }
        else{
            return false;
        }
    }
}
