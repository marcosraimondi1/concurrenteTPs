package Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
    private PrintWriter pw;

    public Logger(String file_path) {
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


}
