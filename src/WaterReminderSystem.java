import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;
import java.util.Random;

public class WaterReminderSystem {
    private static UserData currentUser = null;
    private static UserDataRegistry userRegistry = new UserDataRegistry();
    private static boolean stopReminder = false; // Flag to stop the reminder

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Thread reminderThread = null;

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Login");
            System.out.println("2. Add user data");
            System.out.println("3. View/Edit user details");
            System.out.println("4. Set/Change reminder interval");
            System.out.println("5. Start/Stop reminders");
            System.out.println("6. Logout");
            System.out.println("7. View Registered Users");
            System.out.println("8. Exit");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid numeric option (1-8).");
                scanner.nextLine(); // Consume the invalid input
                continue;
            }

            switch (choice) {
                case 1:
                    // If the user chooses to login
                    if (currentUser == null) {
                        System.out.print("Enter your username: ");
                        String username = scanner.nextLine().trim();
                        if (username.isEmpty() || username.contains(" ")) {
                            System.out.println("Invalid username. Please enter a username without spaces.");
                            continue;
                        }

                        List<UserData> matchingUsers = userRegistry.loadAllByUsername(username);

                        if (matchingUsers.isEmpty()) {
                            System.out.println("User not found. Please add user data.");
                        } else if (matchingUsers.size() == 1) {
                            currentUser = matchingUsers.get(0);
                            System.out.println("Welcome, " + currentUser.getName() + "!");
                        } else {
                            // Multiple users found with the same username
                            System.out.println("Multiple users found with the same username. Please choose one:");

                            for (int i = 0; i < matchingUsers.size(); i++) {
                                System.out.println((i + 1) + ". " + matchingUsers.get(i).getName());
                            }

                            int userChoice;
                            try {
                                userChoice = Integer.parseInt(scanner.nextLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input. Please enter a valid numeric choice.");
                                continue;
                            }

                            if (userChoice >= 1 && userChoice <= matchingUsers.size()) {
                                currentUser = matchingUsers.get(userChoice - 1);
                                System.out.println("Welcome, " + currentUser.getName() + "!");
                            } else {
                                System.out.println("Invalid choice. Please choose a valid user.");
                            }
                        }
                    } else {
                        System.out.println("You are already logged in as " + currentUser.getName() + ".");
                    }
                    break;

                case 2:
                    // If the user chooses to add user data
                    if (currentUser == null) {
                        // Generate a random unique ID
                        int newUserId = generateRandomUniqueId();

                        currentUser = UserDataInitializer.initializeUserData(scanner, newUserId);
                        userRegistry.addUser(currentUser);
                        currentUser.saveToFile();
                        System.out.println("User data saved with ID: " + newUserId);
                    } else {
                        System.out.println("You are already logged in as " + currentUser.getName() + ".");
                    }
                    break;

                case 3:
                    // If the user chooses to view/edit user details
                    if (currentUser != null) {
                        UserDataInitializer.editUserData(currentUser, scanner);
                        currentUser.saveToFile();
                        System.out.println("User data saved.");
                    } else {
                        System.out.println("Please log in first.");
                    }
                    break;

                case 4:
                    // If the user chooses to set/change reminder interval
                    if (currentUser != null) {
                        System.out.print("Enter reminder interval in seconds: ");
                        int newInterval = readIntWithValidation(scanner);
                        currentUser.setReminderInterval(newInterval);
                        System.out.println("Reminder interval updated to " + newInterval + " seconds.");
                    } else {
                        System.out.println("Please log in first.");
                    }
                    break;

                case 5:
                    // If the user chooses to start/stop reminders
                    if (currentUser != null) {
                        System.out.println("Choose an option:");
                        System.out.println("1. Start reminders");
                        System.out.println("2. Stop reminders");

                        int reminderChoice;
                        try {
                            reminderChoice = scanner.nextInt();
                            scanner.nextLine(); // Consume the newline character
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Please enter 1 to start or 2 to stop reminders.");
                            scanner.nextLine(); // Consume the invalid input
                            continue;
                        }

                        switch (reminderChoice) {
                            case 1:
                                if (reminderThread == null || !reminderThread.isAlive()) {
                                    // Start the reminder thread
                                    stopReminder = false; // Reset the flag
                                    reminderThread = new Thread(new Reminder(currentUser));
                                    reminderThread.start();
                                    System.out.println("Reminders started.");
                                } else {
                                    System.out.println("Reminders are already running.");
                                }
                                break;

                            case 2:
                                if (reminderThread != null && reminderThread.isAlive()) {
                                    // Stop the reminder thread gracefully
                                    stopReminder = true; // Set the flag to stop
                                    reminderThread.interrupt();
                                    System.out.println("Reminders stopped.");
                                } else {
                                    System.out.println("Reminders are not running.");
                                }
                                break;
                        }
                    } else {
                        System.out.println("Please log in first.");
                    }
                    break;

                case 6:
                    // If the user chooses to logout
                    currentUser = null;
                    System.out.println("You have been logged out.");
                    break;

                case 7:
                    // If the user chooses to view registered users
                    List<String> registeredUsernames = userRegistry.getRegisteredUsernames();
                    if (registeredUsernames.isEmpty()) {
                        System.out.println("No users registered yet.");
                    } else {
                        System.out.println("Registered Users:");
                        for (String username : registeredUsernames) {
                            System.out.println(username);
                        }
                    }
                    break;

                case 8:
                    // If the user chooses to exit
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid option. Please choose a valid option (1-8).");
            }
        }
    }

    private static int generateRandomUniqueId() {
        Random random = new Random();
        int id;
        do {
            id = random.nextInt(100000); // Adjust the range as needed
        } while (userRegistry.isUserIdExists(id)); // Ensure uniqueness
        return id;
    }

    private static int readIntWithValidation(Scanner scanner) {
        int value = 0;
        boolean validInput = false;
        while (!validInput) {
            try {
                value = Integer.parseInt(scanner.nextLine());
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
        return value;
    }
}
