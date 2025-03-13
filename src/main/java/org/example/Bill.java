package org.example;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

public class Bill {
    private HashMap<String, Item> itemMap = new HashMap<>();
    private Vector<Vector<Object>> soldItems = new Vector<>();
    public double totalAmount = 0;
    public String customerName;
    public String cashierName;
    public String branchName;
    public int billID;
    public double discount;

    Bill(String cashierName, String branchName, int billID, Double discount) {
        this.customerName = "Guest";
        this.cashierName = cashierName;
        this.discount = discount;
        this.branchName = branchName;
        this.billID = billID;
        String csvFilePath = "items.csv";
        loadItemsFromCSV(csvFilePath);
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    // Read CSV and load items into HashMap
    private void loadItemsFromCSV(String csvFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] fields = line.split(",");

                if (fields.length < 6) continue; // Ensure valid format

                String itemCode = fields[0].trim();
                double price = Double.parseDouble(fields[1].trim());
                String weightOrSize = fields[2].trim();
                String manufactureDate = fields[3].trim();
                String expiryDate = fields[4].trim();
                String manufacturer = fields[5].trim();

                itemMap.put(itemCode, new Item(itemCode, price, weightOrSize, manufactureDate, expiryDate, manufacturer));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add item to bill
    public void addItemToBill(String itemCode, int quantity) {
        Item item = itemMap.get(itemCode);
        if (item != null) {
            double itemTotal = item.getNetPrice(quantity);
            totalAmount += itemTotal;

            Vector<Object> thisItem = new Vector<>();
            thisItem.add(item.itemCode);
            thisItem.add(quantity);
            thisItem.add(item.price);
            thisItem.add(itemTotal);

            soldItems.add(thisItem);

            System.out.println(quantity + " x " + item.itemCode + " added. Subtotal: " + itemTotal + " Rs.\n");
        } else {
            System.out.println("Item Code " + itemCode + " not found.");
        }
    }

    public void printBill() {
        Date currDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = formatter.format(currDate);

        System.out.println("\n----------------------------------------");
        System.out.println("POS System - VoidWalkers SuperMarket");
        System.out.println("Branch: " + this.branchName);
        System.out.println("Cashier: " + this.cashierName);
        System.out.println("Customer: " + this.customerName);
        System.out.println("Date: " + formattedDateTime);
        System.out.println("Bill ID: " + this.billID);
        System.out.println("----------------------------------------");
        System.out.printf("%-10s %-10s %-10s %s%n", "Item", "Quantity", "Unit Price", "Total");
        System.out.println("----------------------------------------");

        soldItems.forEach((obj) -> {
            System.out.printf("%-10s %-10d %-10.2f %.2f%n", obj.get(0), obj.get(1), obj.get(2), obj.get(3));
        });

        System.out.println("----------------------------------------");
        System.out.printf("%-30s %.2f Rs%n", "Total:", getTotalBill());
        System.out.println("----------------------------------------");
    }

    public double getTotalBill() {
        return totalAmount;
    }

    public void generateBill() {
        String filePath = "Bill" + this.billID + ".pdf";
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph("INVOICE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Invoice details
            Font detailFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Paragraph date = new Paragraph("Date: " + sdf.format(new Date()), detailFont);
            Paragraph invoiceNumber = new Paragraph("Invoice No: INV-" + String.format("%03d", this.billID), detailFont);
            document.add(date);
            document.add(invoiceNumber);
            document.add(new Paragraph("\n"));
            Paragraph branchName = new Paragraph("Branch: " + this.branchName, detailFont);
            Paragraph cashierName = new Paragraph("Cashier: " + this.cashierName, detailFont);
            Paragraph customerName = new Paragraph("Customer: " + this.customerName, detailFont);
            document.add(branchName);
            document.add(cashierName);
            document.add(customerName);
            document.add(new Paragraph("\n"));

            // Table setup
            PdfPTable table = new PdfPTable(4);

            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setWidths(new float[]{3, 1, 2, 2}); // Custom column widths
            table.getDefaultCell().setBorder(Rectangle.BOTTOM);
            table.getDefaultCell().setBorderWidth(1f);
            table.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

            // Table headers
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);

            PdfPCell h1 = new PdfPCell(new Paragraph("Item", headerFont));
            PdfPCell h2 = new PdfPCell(new Paragraph("Quantity", headerFont));
            PdfPCell h3 = new PdfPCell(new Paragraph("Unit Price (Rs)", headerFont));
            PdfPCell h4 = new PdfPCell(new Paragraph("Total (Rs)", headerFont));
            PdfPCell[] headers = {h1, h2, h3, h4};
            for (PdfPCell header : headers) {
                header.setBorder(Rectangle.BOTTOM);
                header.setBorderWidth(2f); // Border thickness
                header.setBorderColor(BaseColor.LIGHT_GRAY); // Light Gray border
                header.setPadding(8); // Space inside the cell
            }

            table.addCell(h1);
            table.addCell(h2);
            table.addCell(h3);
            table.addCell(h4);

            // Table rows (items)
            soldItems.forEach((obj) -> {
                table.addCell((String) obj.get(0));
                table.addCell(String.valueOf(obj.get(1)));
                table.addCell(String.format("%.2f", Double.parseDouble(String.valueOf(obj.get(2))))); // Unit Price (formatted as currency)
                table.addCell(String.format("%.2f", Double.parseDouble(String.valueOf(obj.get(3))))); // Total Price (formatted as currency)
            });
            document.add(table);

            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            document.add(new Paragraph("Discount: " + String.format("%.2f", this.discount) + "%", boldFont));
            Paragraph total = new Paragraph("Total Amount: " + String.format("%.2f Rs", this.totalAmount), new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
            total.setAlignment(Element.ALIGN_RIGHT);
            total.setSpacingBefore(10f);
            document.add(total);

            Paragraph thanks = new Paragraph("Thank you for shopping with us!", new Font(Font.FontFamily.HELVETICA, 12));
            thanks.setAlignment(Element.ALIGN_CENTER);
            thanks.setSpacingBefore(20f);
            document.add(thanks);

            document.close();
            System.out.println("PDF bill generated successfully: " + filePath);

        } catch (FileNotFoundException | DocumentException e) {
            throw new RuntimeException(e);
        }

    }
}