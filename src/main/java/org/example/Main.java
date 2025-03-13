package org.example;
import java.util.*;

public class Main {
    public static int billID = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AdminSession adminSession = new AdminSession(null, null);
        PosSystem posSys = new PosSystem();

        while (true) {
            System.out.println("Hello Welcome to SuperSaverPOS VoidWalkers");
            adminSession.checkSession(scanner);
            System.out.println("1. Start a new Bill");
            System.out.println("2. Retrieve a pending Bill");
            System.out.println("3. Admin Panel");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            Console.clear();
            switch (choice) {
                case 1:
                    // Start a new bill
                    newBill(scanner, adminSession, posSys);
                    break;
                case 2:
                    // Retrieve a pending bill
                    posSys.retrievePending(scanner, posSys);
                    break;
                case 3:
                    // Admin panel
                    adminPanel(adminSession, scanner,posSys);
                    break;
                case 4:
                    // Exit
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    public static void newBill(Scanner scanner, AdminSession admin, PosSystem posSys) {
        // Initiate bill object
        Bill bill = new Bill(admin.getCashierName(), admin.getBranchName(), billID++, admin.getDiscount());

        System.out.print("Enter customer name (Leave blank if not registered): ");
        String customerName = scanner.nextLine();
        bill.setCustomerName(customerName.isEmpty() ? "Guest" : customerName);

        while (true) {
            System.out.print("\nEnter item code (or Enter to finish): ");
            String itemCode = scanner.nextLine();
            if (itemCode.isEmpty()) break;

            System.out.print("Enter quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine();

            // Add item to bill
            bill.addItemToBill(itemCode, quantity);
        }

        while (true) {
            System.out.println("\n_____________________");
            System.out.println("1. Print Bill");
            System.out.println("2. Mark as Pending");
            System.out.println("3. Close");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            Console.clear();
            switch (choice) {
                case 1:
                    bill.generateBill();
                    posSys.completeBill(bill.billID,bill);
                    return;
                case 2:
                    posSys.addBill(bill.billID, bill);
                    return;
                case 3:
                    return;
                default:
                    System.out.println("Enter a correct choice");
            }
        }
    }

    public static void adminPanel(AdminSession admin, Scanner scanner,PosSystem sys) {
        String branchName = admin.getBranchName();
        String cashierName = admin.getCashierName();
        Double discount = admin.getDiscount();
        System.out.println("\nBranch Name: " + branchName + "\nCashier Name: " + cashierName + "\nDiscount: " + discount + "%\n");
        while (true) {
            System.out.println("1. Change Branch Name");
            System.out.println("2. Change Cashier Name");
            System.out.println("3. Change Discount");
            System.out.println("4. Generate Revenue Report");
            System.out.println("5. Return Menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            Console.clear();

            switch (choice) {
                case 1:
                    admin.setBranchName(scanner);
                    break;
                case 2:
                    admin.setCashierName(scanner);
                    break;
                case 3:
                    admin.setDiscount(scanner);
                    break;
                case 4:
                    sys.revenueReport(scanner);
                    //generate revenue report

                    break;

                case 5:
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }


}