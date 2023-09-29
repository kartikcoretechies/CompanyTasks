import java.util.Scanner;

class UserDataInitializer {
    public static UserData initializeUserData(Scanner scanner, int id) {
        String name;
        int age = 0;
        int hydrationGoal = 0;

        // Input user data and validate age and hydration goal as integers
        System.out.print("Enter your name: ");
        name = scanner.nextLine();

        boolean validAge = false;
        while (!validAge) {
            System.out.print("Enter your age: ");
            String ageInput = scanner.nextLine().trim(); // Trim leading/trailing spaces
            if (isInteger(ageInput)) {
                age = Integer.parseInt(ageInput);
                if (age > 0 && age < 150) { // Age should be realistic
                    validAge = true;
                } else {
                    System.out.println("Invalid input. Age must be a realistic number.");
                }
            } else {
                System.out.println("Invalid input. Age must be a number.");
            }
        }

        boolean validHydrationGoal = false;
        while (!validHydrationGoal) {
            System.out.print("Enter your hydration goal (ml): ");
            String goalInput = scanner.nextLine().trim(); // Trim leading/trailing spaces
            if (isInteger(goalInput)) {
                hydrationGoal = Integer.parseInt(goalInput);
                if (hydrationGoal > 0 && hydrationGoal < 100000) { // Hydration goal should be realistic
                    validHydrationGoal = true;
                } else {
                    System.out.println("Invalid input. Hydration goal must be a realistic number.");
                }
            } else {
                System.out.println("Invalid input. Hydration goal must be a number.");
            }
        }

        return new UserData(id, name, age, hydrationGoal);
    }
    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void editUserData(UserData user, Scanner scanner) {
        System.out.println("Current User Data:");
        System.out.println("Name: " + user.getName());
        System.out.println("Age: " + user.getAge());
        System.out.println("Hydration Goal: " + user.getHydrationGoal());
        System.out.println("Total Intake: " + user.getTotalIntake());

        System.out.println("Enter new user data:");

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        user.setName(name);

        boolean validAge = false;
        while (!validAge) {
            System.out.print("Enter your age: ");
            String ageInput = scanner.nextLine();
            if (isInteger(ageInput)) {
                int age = Integer.parseInt(ageInput);
                user.setAge(age);
                validAge = true;
            } else {
                System.out.println("Invalid input. Age must be a number.");
            }
        }

        boolean validHydrationGoal = false;
        while (!validHydrationGoal) {
            System.out.print("Enter your hydration goal (ml): ");
            String goalInput = scanner.nextLine();
            if (isInteger(goalInput)) {
                int hydrationGoal = Integer.parseInt(goalInput);
                user.setHydrationGoal(hydrationGoal);
                validHydrationGoal = true;
            } else {
                System.out.println("Invalid input. Hydration goal must be a number.");
            }
        }

        System.out.println("User data updated.");
    }
}