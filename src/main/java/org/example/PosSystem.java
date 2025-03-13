package org.example;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Scanner;

public class PosSystem {
    public HashMap<Integer, Bill> completedBills = new HashMap<>();
    public HashMap<Integer, Bill> pendingBills = new HashMap<>();
    private double totalSales = 0.0;
    private double totalDiscounts = 0.0;
    private double netRevenue = 0.0;
    private int totalTransactions = 0;


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
                    return;
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

    public void revenueReport(Scanner scanner){
        System.out.print("Enter the startDate: (Enter in this format YYYY-MM-DD)\n->");
        String startDateStr = scanner.nextLine();
        System.out.print("Enter the endDate: (Enter in this format YYYY-MM-DD) \n->");
        String endDateStr = scanner.nextLine();

        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);




        this.completedBills.forEach((key,val)->{
            //validate dates are correct in format & start date & end dates are correctly said
            if(!val.date.isBefore(startDate) && !val.date.isAfter(endDate) ){
                this.totalSales+= val.totalAmount;
                this.totalDiscounts += val.totalDiscount;
                this.netRevenue += val.totalAmount - val.totalDiscount;
                this.totalTransactions ++;
            }
        });

        String reportContent = "📜 Super-Saving Supermarket - Revenue Report\n" +
                "📅 Date Range: " + startDateStr + " - " + endDateStr + "\n" +
                "🛒 Total Sales (Before Discount): " + this.totalSales + " Rs\n" +
                "💰 Total Discounts Given: " + this.totalDiscounts + " Rs\n" +
                "💵 Net Revenue (After Discount): " + this.netRevenue + " Rs\n" +
                "📄 Total Transactions Processed: " + this.totalTransactions;

        System.out.println(reportContent);
        String filename = "Revenue Report "+ startDateStr +" - "+endDateStr+".pdf";
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
            document.add(new Paragraph(reportContent));
            document.close();
            System.out.println("Report saved as " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}