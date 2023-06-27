package Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
    private PrintWriter pw;
    private FileWriter file;
    public Logger(String file_path) {
        try {
            file = new FileWriter(file_path);
            pw = new PrintWriter(file);
            pw.printf("Hello World\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(String s){
        pw.printf(s);
    }


}
