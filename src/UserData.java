import java.io.*;

class UserData {
    private int id;
    private String name;
    private int age;
    private int hydrationGoal;
    private int totalIntake;
    private int reminderInterval; // in seconds
    private Thread reminderThread;

    // Constructor with an ID, name, age, and hydration goal
    public UserData(int id, String name, int age, int hydrationGoal) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.hydrationGoal = hydrationGoal;
        this.totalIntake = 0;
        this.reminderInterval = 1800; // Default reminder interval: 30 minutes (1800 seconds)
    }

    // Constructor with name, age, and hydration goal (no ID)
    public UserData(String name, int age, int hydrationGoal) {
        this.name = name;
        this.age = age;
        this.hydrationGoal = hydrationGoal;
        this.totalIntake = 0;
        this.reminderInterval = 1800; // Default reminder interval: 30 minutes (1800 seconds)
    }

    // Getter and setter methods for fields...

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getHydrationGoal() {
        return hydrationGoal;
    }

    public int getTotalIntake() {
        return totalIntake;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setHydrationGoal(int hydrationGoal) {
        this.hydrationGoal = hydrationGoal;
    }

    public void setReminderInterval(int reminderInterval) {
        this.reminderInterval = reminderInterval;
    }

    public void recordIntake(int intake) {
        totalIntake += intake;
    }

    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt", true))) {
            writer.newLine();
            writer.write("Name: " + name);
            writer.newLine();
            writer.write("Age: " + age);
            writer.newLine();
            writer.write("Hydration Goal: " + hydrationGoal);
            writer.newLine();
            writer.write("Total Intake: " + totalIntake);
            writer.newLine();
            writer.write("Reminder Interval: " + reminderInterval + " seconds");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toggleReminder() {
        if (reminderThread == null || !reminderThread.isAlive()) {
            reminderThread = new Thread(new Reminder(this));
            reminderThread.start();
        } else {
            reminderThread.interrupt(); // Interrupt the thread to stop it
            System.out.println("Reminders stopped.");
        }
    }

    public static UserData loadByUsername(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            UserData user = null; // Initialize user as null

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Name: ") && line.substring("Name: ".length()).trim().equals(username)) {
                    // Found the user, initialize with default values
                    user = new UserData(username, 0, 0);

                    // Loop through lines to load user data
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("Age: ")) {
                            user.setAge(Integer.parseInt(line.substring("Age: ".length()).trim()));
                        } else if (line.startsWith("Hydration Goal: ")) {
                            user.setHydrationGoal(Integer.parseInt(line.substring("Hydration Goal: ".length()).trim()));
                        } else if (line.startsWith("Total Intake: ")) {
                            user.totalIntake = Integer.parseInt(line.substring("Total Intake: ".length()).trim());
                        } else if (line.startsWith("Reminder Interval: ")) {
                            String intervalLine = line.substring("Reminder Interval: ".length()).trim();
                            user.setReminderInterval(Integer.parseInt(intervalLine.split(" ")[0]));
                        } else if (line.startsWith("Name: ")) {
                            // If we encounter a new user's data, break out of the loop
                            break;
                        }
                    }
                    break; // Exit the loop after finding the user
                }
            }
            return user; // Return the found user or null if not found
        } catch (IOException | NumberFormatException e) {
            return null;
        }
    }

    public void setTotalIntake(int totalIntake) {
        this.totalIntake = totalIntake;
    }
}
