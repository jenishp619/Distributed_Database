package erd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Erd {

    public File[] LoadFiles(StringBuilder sbFile, File file) throws IOException {
        File[] alltables = file.listFiles();
        if (alltables != null) {

            for (File table : alltables) {

                StringBuilder sbTable = new StringBuilder();

                String[] colHeaders = {};
                String columname = "";
                String currColumnName;
                List<String> colist = new ArrayList<>();
                FileReader fileReader = new FileReader(table);
                String tableName = table.getName().substring(0, table.getName().lastIndexOf('.'));
                System.out.println(table.getName().lastIndexOf('.'));

                sbTable.append("Table Name :").append(tableName).append("\n");

                BufferedReader br = new BufferedReader(fileReader);
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    if (strLine.contains("|")) {
                        colHeaders = strLine.split("\\|");
                        currColumnName = colHeaders[0];
                        colist.add(currColumnName);

                    }
                }
                sbTable.append("Column Name:");

                for (int i = 0; i < colist.size(); i++) {
                    int j;
                    if (i < colist.size() - 1) {
                        sbTable.append(colist.get(i)).append(",");
                    } else {
                        sbTable.append(colist.get(i));
                    }
                }

                sbFile.append(sbTable).append("\n");

            }


        } else {
            System.out.println("Database not exist please create it first");
        }
        return alltables;
    }

    public void generateErd() throws IOException {

        Scanner sc = new Scanner(System.in);    //System.in is a standard input stream
        System.out.print("Enter Database Name : ");
        String dbName = sc.nextLine();
        System.out.print("\n");
        File file = new File("../src/main/java/Directory/" + dbName);
        try {
            Path path = Paths.get("../src/main/java/erdDiagram/"  + "ERD.txt");
            StringBuilder sbFile = new StringBuilder();
            LoadFiles(sbFile, file);
            Files.write(path, sbFile.toString().getBytes());
            System.out.println("ERD Generated");
            System.out.println("Path:" + "../src/main/java/erdDiagram" + dbName + "ERD.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

