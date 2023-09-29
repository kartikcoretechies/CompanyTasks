import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserDataRegistry {
    private List<UserData> userList = new ArrayList<>();

    public UserDataRegistry() {
        loadUsersFromFile();
    }


    public UserData loadByUsername(String username) {
        for (UserData user : userList) {
            if (user.getName().equals(username)) {
                return user;
            }
        }
        return null; // User not found
    }

    public void addUser(UserData user) {
        userList.add(user);
    }

    public List<String> getRegisteredUsernames() {
        List<String> usernames = new ArrayList<>();
        for (UserData user : userList) {
            usernames.add(user.getName());
        }
        return usernames;
    }

    // Check if a user ID already exists
    public boolean isUserIdExists(int id) {
        for (UserData user : userList) {
            if (user.getId() == id) {
                return true;
            }
        }
        return false;
    }
    private void loadUsersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            UserData currentUser = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Name: ")) {
                    if (currentUser != null) {
                        userList.add(currentUser);
                    }
                    String username = line.substring("Name: ".length()).trim();
                    currentUser = new UserData(username, 0, 0);
                } else if (currentUser != null) {
                    if (line.startsWith("Age: ")) {
                        currentUser.setAge(Integer.parseInt(line.substring("Age: ".length()).trim()));
                    } else if (line.startsWith("Hydration Goal: ")) {
                        currentUser.setHydrationGoal(Integer.parseInt(line.substring("Hydration Goal: ".length()).trim()));
                    } else if (line.startsWith("Total Intake: ")) {
                        currentUser.setTotalIntake(Integer.parseInt(line.substring("Total Intake: ".length()).trim()));
                    } else if (line.startsWith("Reminder Interval: ")) {
                        String intervalLine = line.substring("Reminder Interval: ".length()).trim();
                        currentUser.setReminderInterval(Integer.parseInt(intervalLine.split(" ")[0]));
                    }
                }
            }

            if (currentUser != null) {
                userList.add(currentUser); // Add the last loaded user
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // Custom method to load all users with the same username
    public List<UserData> loadAllByUsername(String username) {
        List<UserData> matchingUsers = new ArrayList<>();
        for (UserData user : userList) {
            if (user.getName().equals(username)) {
                matchingUsers.add(user);
            }
        }
        System.out.println("Loaded users with username " + username + ": " + matchingUsers);
        return matchingUsers;
    }
}
