package Logger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoggerTest {

    @Test
    void log() {
        Logger logger = new Logger(".\\data\\log.txt");
        logger.log("Hello World");

    }
}