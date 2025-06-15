package vacuumCleanerRobot;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        MOMGame game = new MOMGame();
        Thread gameThread = new Thread(game);
        gameThread.run();
    }
}
