package Analytics;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Analytics {

    public void storeUserQueries(String currUser, int count, String database) throws IOException {


        FileInputStream fstream = new FileInputStream("../src/main/java/Analytics/ANALYTICS.TXT");
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        String finalLine = "";
        Boolean flag = false;
        int checkNull = 0;
        List<String[]> LINES = new ArrayList<>();
        int lineCount = 0;
        while ((strLine = br.readLine()) != null) {
            checkNull++;
            String[] splitStr = strLine.split("\\s+");
            LINES.add(splitStr);

        }

        if (checkNull>2) {
            for (int size = 0; size < LINES.size(); size++) {
                if (LINES.get(size)[1].equalsIgnoreCase(currUser) && count > 0 && LINES.get(size)[6].equalsIgnoreCase(database) && String.valueOf(count) != LINES.get(size)[1]) {
                    LINES.get(size)[3] = String.valueOf(count);
                    System.out.println(LINES.get(size)[3]);
                    flag = true;


                }
            }
        } else {
            FileWriter analytics = new FileWriter("../src/main/java/Analytics/ANALYTICS.TXT");
            finalLine = "user " + currUser + " submitted " + count + " queries for " + database + " running on Virtual Machine 2 ";
            analytics.write(finalLine);
            System.out.println(finalLine);
            analytics.close();
        }


        if (flag) {
            FileWriter analytics = new FileWriter("../src/main/java/Analytics/ANALYTICS.TXT");
            for (int writeLine = 0; writeLine < LINES.size(); writeLine++) {
                String line = "";
                for (int writeWords = 0; writeWords < LINES.get(writeLine).length; writeWords++) {

                    line = line + LINES.get(writeLine)[writeWords] + " ";

                }
                finalLine = finalLine + line + "\n";

            }
            analytics.write(finalLine);
            analytics.close();
            System.out.println(finalLine);
        }


    }

    public void getUpdateAnalytics(){
        try{
            FileInputStream fstream = new FileInputStream("../src/main/java/Analytics/UPDATEAN.txt");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;

            while ((strLine = br.readLine()) != null) {
                System.out.println(strLine);

                String[] setline = strLine.split("\\|");

                System.out.println("Table "+setline[0] +" has been updated "+setline[1]+" times");

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void setUpdateAnalytics(String tableName ,int addCount){
        try{
            FileInputStream fstream = new FileInputStream("../src/main/java/Analytics/UPDATEAN.txt");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String adding;
            String strLine;
            String finalString = "";
            boolean exist = false;

            ArrayList<String> data = new ArrayList<>();

            while ((strLine = br.readLine()) != null) {

                System.out.println(strLine);
                String[] setline = strLine.split("\\|");

                if(setline[0].equalsIgnoreCase(tableName)){

                    int count = Integer.parseInt(setline[1])+addCount;
                    adding = setline[0]+"|"+count;
                    data.add(adding);
                    exist = true;

                }else {
                    adding = setline[0]+"|"+setline[1];
                    data.add(adding);
                }

            }

            if(!exist){
                data.add(tableName+"|"+addCount);
            }

            fstream.close();

            FileWriter analytics = new FileWriter("../src/main/java/Analytics/UPDATEAN.txt");

            for (String line: data) {
                finalString += line+"\n";
            }

            analytics.write(finalString);
            analytics.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}