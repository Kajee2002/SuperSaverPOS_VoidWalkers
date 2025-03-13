package org.example;

import java.util.HashMap;
import java.util.Scanner;

public class PosSystem {
    private HashMap<Integer, Bill> completedBills = new HashMap<>();
    private HashMap<Integer, Bill> pendingBills = new HashMap<>();

    public void completeBill(int billNo,Bill bill){
        if(this.completedBills.containsKey(billNo)) {
            System.out.println("Bill is completed");
        }else if(this.pendingBills.containsKey(billNo)){
            this.pendingBills.remove(billNo);
            this.completedBills.put(billNo,bill);
        }
        else{
            this.completedBills.put(billNo,bill);
        }
    }
    public void addBill(int billNo, Bill bill) {
        if (this.completedBills.containsKey(billNo)) {
            System.out.println("Sorry, This Bill was closed.");
            // to do reopen the bill
        } else if (this.pendingBills.containsKey(billNo)) {
            System.out.println("This Bill was already added To Pending");
            return;
        } else {
            this.pendingBills.put(billNo, bill);
            System.out.println("Bill: " + billNo + " has successfully added to Pending.");
        }
    }

    public void retrievePending(Scanner scanner, PosSystem sys) {
        System.out.println("_______________________________");
        System.out.println("Pending Bills");
        System.out.println("_______________________________");
        System.out.println("Bill No ___ Customer Name");

        pendingBills.forEach((key, value) -> {
            System.out.println(String.format("%7d", key) + "     " + value.customerName);
        });
        System.out.println("To Exit type -1");
        System.out.print("Select a Bill (use bill id): ");
        int billNum = scanner.nextInt();
        scanner.nextLine();

        if (billNum == -1) {
            return;
        }
        if (this.pendingBills.containsKey(billNum)) {
            Bill retBill = this.pendingBills.get(billNum);
            System.out.println("\n " + String.format("Bill: %7d", billNum) + " | Customer: " + retBill.customerName);
            System.out.println("1. Continue Adding");
            System.out.println("2. Complete Bill");
            System.out.println("3. Remove Bill");
            System.out.println("4. Return Menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            Console.clear();

            switch (choice) {
                case 1:
                    // continue adding
                    sys.continueAdd(scanner, retBill, sys);
                    break;
                case 2:
                    retBill.generateBill();
                    // complete bill
                    completedBills.put(billNum, retBill);
                    pendingBills.remove(billNum);
                    break;
                case 3:
                    // remove bill
                    this.pendingBills.remove(billNum);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        } else if (this.completedBills.containsKey(billNum)) {
            System.out.println("Sorry, this bill was closed");
        } else {
            System.out.println("Invalid bill number");
        }
    }

    public void continueAdd(Scanner scanner, Bill bill, PosSystem sys) {
        while (true) {
            System.out.print("\nEnter item code (or Enter to finish): ");
            String itemCode = scanner.nextLine();
            if (itemCode.isEmpty()) break;

            System.out.print("Enter quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine();
            Console.clear();

            // add item to bill
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
                    sys.completeBill(bill.billID,bill);
                    break;
                case 2:
                    sys.addBill(bill.billID, bill);
                    return;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}