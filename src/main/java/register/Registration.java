package register;

import Logs.LogWriter;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static java.lang.System.out;

public class Registration {
    public static String md5Hash(String message) {
        String md5 = "";
        if (null == message)
            return null;

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");//Create MessageDigest object for MD5
            digest.update(message.getBytes(), 0, message.length());//Update input string in message digest
            md5 = new BigInteger(1, digest.digest()).toString(16);//Converts message digest value in base 16 (hex)

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }

    public void register() throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);

        out.println("Enter User Name: ");
        String Uname = sc.nextLine();
        out.println(Uname);

        out.println("Enter Password: ");
        String Pass = sc.nextLine();
        out.println(Pass);
        String hash = md5Hash(Pass);

        out.println("Confirm Password: ");
        String ConPass = sc.nextLine();
        out.println(ConPass);
        Uname = Uname.trim();
        Pass = Pass.trim();
        ConPass = ConPass.trim();
        out.println("Enter your question");
        String securityQuestion = sc.nextLine();
        out.println("Enter your answer");
        String securityAnswer = sc.nextLine();

        String x = Uname + " " + Pass;
        if (Pass.equals(ConPass)) {

            File f = new File("Registration.txt");
            Scanner content = new Scanner(f);

            int flag = 0;

            while (content.hasNextLine()) {
                String data = content.nextLine();
                if (data.equals(x)) {
                    out.println("Already Registered");
                    break;
                }
            }
            if (flag == 0) {
                try {
                    BufferedWriter out = new BufferedWriter(new FileWriter("Registration.txt", true));
                    out.write(Uname + " " + hash + " " + securityQuestion + " " + securityAnswer + "\n");
                    out.close();
                } catch (IOException e) {
                    out.println("exception occoured" + e);
                }

                out.println("Successfully Registered");
                out.println("Please login");
            }


        } else {
            out.println("Recheck");
            out.println("Registration, unsuccessful");

        }
    }

    public int login() {

        Scanner sc = new Scanner(System.in);

        out.println("Enter User Name: ");
        String Uname = sc.nextLine();
        out.println(Uname);

        out.println("Enter Password: ");
        String Pass = sc.nextLine();
        out.println(Pass);
        Uname = Uname.trim();
        Pass = Pass.trim();
        String x = Uname + " " + Pass;
        String hashtext = md5Hash(Pass);
        String newX = Uname + " " + hashtext;

        try {

            File f = new File("Registration.txt");
            Scanner content = new Scanner(f);
            int flag = 0;
            while (content.hasNextLine()) {

                String data = content.nextLine();
                if (data.length() > 0) {
                    out.println(data.length());
                    String[] a = data.split(" ");
                    if (a.length > 0) {
                        data = a[0] + " " + a[1];

                    }
                }


                if (data.equals(newX)) {
                    out.println("Login Successful");
                    out.println("Welcome to the Application.");

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();

                    LogWriter.current_user = Uname;
                    LogWriter.login_time = dtf.format(now);
                    LogWriter.event_details = LogWriter.login_time +" "+LogWriter.current_user+" Logged in for Querying Execution started";
                    LogWriter.writeGenralLog();

                    flag = 1;
                    return flag;
                }
            }
            if (flag == 0) {
                out.println("Login Failed");
            }
           return flag;
          //  content.close();
        } catch (FileNotFoundException e) {
            out.println("Error.");
            e.printStackTrace();
        }
        sc.close();

        return 0;
    }
}