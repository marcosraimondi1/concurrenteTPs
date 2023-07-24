package Logger;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Logger {
    private PrintWriter pw;
    private final String file_path;
    public Logger(String file_path) {
        this.file_path = file_path;
        try {
            FileWriter file = new FileWriter(file_path);
            pw = new PrintWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(String s){
        pw.printf(s);
        pw.flush();
    }

    public boolean validateLog(String regex,String replace){
        File file = new File(file_path);
        String log = "";
        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                log += scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(log);
        String out = matcher.replaceAll(replace);
        String prev = "";

        while (true){
            matcher = pattern.matcher(out);
            out = matcher.replaceAll(replace);
            if (Objects.equals(out,prev))
                break;
            prev = out;
        }

        if (!Objects.equals(out, "")){
            System.out.println("LOG VALIDATION FAILED");
            System.out.println(out);
            return false;
        }

        System.out.println("LOG VALIDATION SUCCESS");
        return true;
    }

}
