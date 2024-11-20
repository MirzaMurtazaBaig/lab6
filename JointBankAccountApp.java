package task1;

class BankAccount {
    private int balance = 50000; // Initial balance
    
    // Synchronized method to ensure thread-safe withdrawal
    public synchronized void withdraw(String user, int amount) {
        // Check if enough balance is available
        if (balance >= amount) {
            System.out.println(user + " is withdrawing " + amount);
            try {
                // Simulate some processing time
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            balance -= amount;
            System.out.println(user + " successfully withdrew " + amount);
            System.out.println("Remaining balance: " + balance);
        } else {
            System.out.println(user + " failed to withdraw " + amount + " due to insufficient balance.");
        }
    }

    public int getBalance() {
        return balance;
    }
}

class User extends Thread {
    private BankAccount account;
    private String userName;
    private int amount;

    // Constructor to initialize user and withdrawal details
    public User(BankAccount account, String userName, int amount) {
        this.account = account;
        this.userName = userName;
        this.amount = amount;
    }

    @Override
    public void run() {
        // Attempt to withdraw the specified amount
        account.withdraw(userName, amount);
    }
}

public class JointBankAccountApp {
    public static void main(String[] args) {
        // Create a shared BankAccount object
        BankAccount account = new BankAccount();
        
        // Create two users with different withdrawal amounts
        User userA = new User(account, "User A", 45000);
        User userB = new User(account, "User B", 20000);
        
        // Start both threads (users)
        userA.start();
        userB.start();
        
        // Wait for both threads to finish
        try {
            userA.join();
            userB.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Final balance after both transactions
        System.out.println("Final balance: " + account.getBalance());
    }
}
