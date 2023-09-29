class Reminder implements Runnable {
    private UserData userData;

    public Reminder(UserData userData) {
        this.userData = userData;
    }

    @Override
    public void run() {
        while (userData.getTotalIntake() < userData.getHydrationGoal()) {
            int remainingHydration = userData.getHydrationGoal() - userData.getTotalIntake();
            System.out.println("Remaining hydration: " + remainingHydration + " ml");
            System.out.println("Remember to drink water!");

            try {
                Thread.sleep(5000); // Sleep for 5 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore the interrupted status
            }
        }
    }
}