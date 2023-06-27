package Logger;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class LoggerTest {

    @Test
    void log() {
        String s = "T1";
        String s2 = "T2";
        Logger logger = new Logger(".\\data\\log.txt");
        logger.log(s);
        logger.log(s2);
        File file = new File(".\\data\\log.txt");
        try {
            Scanner scanner = new Scanner(file);
            String line = scanner.nextLine();
            assertEquals(s+s2, line);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}