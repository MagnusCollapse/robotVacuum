package vacuumCleanerRobot;

import java.awt.Point;
import java.util.Scanner;
import vacuumCleanerRobot.MOMGame;
import java.util.Scanner;

public class MOMAppConsole {
    public static void main(String[] args) {
        try {
            
            MOMGame game = new MOMGame();
            Thread gameThread = new Thread(game);
            gameThread.start();
            
            Thread.sleep(1000);
            
            MOMMainApp app = MOMMainApp.getInstance();
            Scanner scanner = new Scanner(System.in);

            System.out.println("=== MOM-Based Vacuum Robot Control System ===");
            System.out.println("Message-Oriented Middleware architecture active!");
            System.out.println("Hardware and software components are decoupled via message broker.");

            while (true) {
                System.out.println("\n=== MOM Robot Control ===");
                System.out.println("1. Start Auto Clean (via MOM)");
                System.out.println("2. Activate Pet Mode (via MOM)");
                System.out.println("3. Schedule Task");
                System.out.println("4. Check Firmware");
                System.out.println("5. Stop Cleaning (via MOM)");
                System.out.println("6. Robot Status (via MOM)");
                System.out.println("7. Manual Control (via MOM)");
                System.out.println("8. Exit");
                System.out.print("Select option: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        app.getRemoteControl().triggerCleaning("AUTO");
                        System.out.println("Auto cleaning message sent via MOM!");
                        break;
                    case 2:
                        app.getRemoteControl().triggerCleaning("PET");
                        System.out.println("Pet mode message sent via MOM!");
                        break;
                    case 3:
                        System.out.print("Enter task configuration: ");
                        String config = scanner.nextLine();
                        app.getScheduler().scheduleTask(config);
                        break;
                    case 4:
                        app.getDeviceConnector().checkCompatibility();
                        break;
                    case 5:
                        app.getRemoteControl().stopCleaning();
                        System.out.println("Stop cleaning message sent via MOM!");
                        break;
                    case 6:
                        System.out.println("Requesting status via MOM...");
                        String status = app.getRemoteControl().getRobotStatus();
                        System.out.println("Current Status: " + status);
                        break;
                    case 7:
                        System.out.println("Manual Control Mode (via MOM)");
                        System.out.print("Enter X speed (-5 to 5): ");
                        double speedX = scanner.nextDouble();
                        System.out.print("Enter Y speed (-5 to 5): ");
                        double speedY = scanner.nextDouble();
                        app.getRemoteControl().setRobotSpeed(speedX, speedY);
                        System.out.println("Manual speed command sent via MOM: (" + speedX + ", " + speedY + ")");
                        break;
                    case 8:
                        scanner.close();
                        System.out.println("Shutting down MOM system...");
                        app.shutdown();
                        System.exit(0);
                        return;
                    default:
                        System.out.println("Invalid selection");
                }
            }
        } catch (Exception e) {
            System.err.println("Error starting MOM system: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

