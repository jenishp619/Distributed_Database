package export;

import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SQLdump {
    List<String> tablename = new ArrayList<String>();

    List<String> readDBase(String dbName) throws Exception {

        File[] files = new File("..\\src\\main\\java\\Directory\\" + dbName).listFiles();
        for (File file : files) {
            if (file.isFile()) {
                String name = file.getName();
                tablename.add(name.substring(0, name.lastIndexOf('.')));
            } else {
                System.out.println("File not found");
            }
        }

        List<String> dbDatabaseList = new ArrayList<String>();

        File dbFile = new File("../src/main/java/constants/metadata/Metadata.txt");
        // File dbFile = new File(dbName);
        Scanner dbScanner = new Scanner(dbFile);
        System.out.println(tablename);
        while (dbScanner.hasNextLine()) {
            dbDatabaseList.add(dbScanner.nextLine());
        }

        return dbDatabaseList;
    }

    List<String> GenerateColumnList(List<String> dbDataList) {

        List<String> colList = new ArrayList<String>();
        for (String rec : dbDataList) {

            String[] dbrec = rec.split("\\|");

            colList.add(dbrec[0]);
        }
        List<String> colListNew = new ArrayList<String>(
                new HashSet<>(colList));
        return colListNew;

    }

    String generateQuery(List<String> dbRecords, String tbName) {

        List<String> columnsArray = new ArrayList<String>();
        List<String> columndatatype = new ArrayList<String>();

        for (String tbRec : dbRecords) {

            String[] dbrec = tbRec.split("\\|");
            if (Objects.equals(dbrec[0], tbName)) {
                columnsArray.add(dbrec[1]);
                if (Objects.equals(dbrec[2], "int")) {
                    columndatatype.add("INT");
                } else {
                    columndatatype.add("VARCHAR(" + "255" + ")");
                }
            }
        }

        System.out.println(columnsArray);
        System.out.println(columndatatype);
        String query = "CREATE TABLE " + tbName + " (";
        for (int j = 0; j < columnsArray.size(); j++) {
            query = query + columnsArray.get(j) + " " + columndatatype.get(j) + " ";
            if (j < (columnsArray.size() - 1)) {
                query = query + ",";
            }
        }

        query = query + ");";
        return query;
    }


    List<String> sort(List<String> queries) {

        List<String> createQueryArray = new ArrayList<String>();
        List<String> refQueryArray = new ArrayList<String>();

        for (String query : queries) {

            if (query.contains("REFERENCES")) {
                refQueryArray.add(query);
            } else {
                createQueryArray.add(query);
            }
        }

        List<String> finalQueryArray = Stream.concat(createQueryArray.stream(), refQueryArray.stream()).collect(Collectors.toList());

        return finalQueryArray;
    }

    List<String> createInsert(List<String> tableRecords, String tbName) {

        List<String> insertQue = new ArrayList<String>();

        for (int k = 1; k < tableRecords.size(); k++) {

            String str1 = "INSERT INTO " + tbName + " VALUES (";
            String temp = "";
            List<String> commonCols = Arrays.asList(tableRecords.get(k).split("\\|"));
            for (int m = 1; m < commonCols.size(); m++) {

                temp = temp + "'" + commonCols.get(m) + "'";
                if (m != commonCols.size() - 1) {
                    temp = temp + ",";
                }

            }

            str1 = str1 + temp + ");";
            insertQue.add(str1);

        }

        return insertQue;
    }

    List<String> createListforIns(String dbname , String str) throws Exception {
        List<String> strList = new ArrayList<>();
        File fh = new File("../src/main/java/Directory/" + dbname + "/" + str);
        Scanner dbSc = new Scanner(fh);

        while (dbSc.hasNextLine()){
            strList.add(dbSc.nextLine());
        }
        return strList;

    }

    public void DumpMain(String dbname) {
        try{
            List<String> dbDataList = readDBase(dbname);
            List<String> dbColList = GenerateColumnList(dbDataList);
            List<String> queries = new ArrayList<String>();

            for (String rec : dbColList) {
                queries.add(generateQuery(dbDataList, rec));
                //queries.add(createInsert(dbColList,rec));
            }

            List<String> finalArray = sort(queries);

            for (String col : tablename) {
                List<String> dbCols = createListforIns(dbname, col + ".txt");
                List<String> rec = createInsert(dbCols, col);
                finalArray.addAll(rec);
            }

            for (String s : finalArray) {

                System.out.println(s);
            }

            FileWriter writer = new FileWriter("DB_DUMP.sql");
            for (String strs : finalArray) {
                writer.write(strs + System.lineSeparator());
            }
            writer.close();


            for (String s : finalArray) {

                System.out.println(s);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}