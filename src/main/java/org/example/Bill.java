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
import java.time.LocalDate;

public class Bill {
    private HashMap<String, Item> itemMap = new HashMap<>();
    private Vector<Vector<Object>> soldItems = new Vector<>();
    public double totalAmount = 0;
    public String customerName;
    public String cashierName;
    public String branchName;
    public int billID;
    public double discount;
    public double totalDiscount;
    public LocalDate date;

    Bill(String cashierName, String branchName, int billID, Double discount) {
        this.customerName = "Guest";
        this.cashierName = cashierName;
        this.discount = discount;
        this.branchName = branchName;
        this.billID = billID;
        this.date = LocalDate.now();
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
            this.totalAmount += itemTotal;
            this.totalDiscount += itemTotal*discount/100;
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
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

            String detailsText = "Date: " + this.date.toString() +" "+ sdf.format(new Date()) +
                    "\nInvoice No: INV-" + String.format("%03d", this.billID)+
                    "\nBranch: " + this.branchName+
                    "\nCashier: " + this.cashierName+
                    "\nCustomer: " + this.customerName+ "\n";

            Paragraph detailsPara = new Paragraph(detailsText,detailFont);
            document.add(detailsPara);

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

            Font boldFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            String totalText = "Total : "+ String.format("%.2f Rs \n",this.totalAmount) +
                    "Discount : " + String.format("%.2f ", this.discount) + "% \n"+
                    "NetTotal : " + String.format("%.2f Rs \n", this.totalAmount - this.totalDiscount);
            Paragraph totalPara = new Paragraph(totalText,boldFont);
            totalPara.setAlignment(Element.ALIGN_RIGHT);
            totalPara.setSpacingBefore(10f);
            document.add(totalPara);


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