package org.example;

import java.util.Scanner;

public class AdminSession {
    private String branchName;
    private String cashierName;
    private Double discount = 0.0;

    AdminSession(String branchName, String cashierName) {
        this.branchName = branchName;
        this.cashierName = cashierName;
    }

    String getBranchName() {
        return this.branchName;
    }

    String getCashierName() {
        return this.cashierName;
    }

    Double getDiscount() {
        return this.discount;
    }

    void setBranchName(Scanner scanner) {
        System.out.print("Enter Branch Name: ");
        this.branchName = scanner.nextLine();
    }

    void setCashierName(Scanner scanner) {
        System.out.print("Enter Cashier Name: ");
        this.cashierName = scanner.nextLine();
    }

    void setDiscount(Scanner scanner) {
        System.out.print("Enter the Discount percentage (without %): ");
        this.discount = scanner.nextDouble();
        scanner.nextLine();
    }

    void checkSession(Scanner scanner) {
        if (this.branchName == null && this.cashierName == null) {
            this.setBranchName(scanner);
            this.setCashierName(scanner);
        } else if (this.branchName == null) {
            this.setBranchName(scanner);
        } else if (this.cashierName == null) {
            this.setCashierName(scanner);
        } else {
            System.out.println("Branch: " + this.branchName + " || Cashier: " + this.cashierName + "\n");
        }
    }
}