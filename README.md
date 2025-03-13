# SuperSaverPOS - VoidWalkers

## Overview

SuperSaverPOS is a command-line-based Java program designed for supermarket billing management. It processes text-based input and output (CLI I/O), allowing administrators to manage bills, add items to bills, and generate invoices in PDF format. The program also includes functionality for managing branch details, cashier information, and discount settings.

This project was developed as part of my **2nd semester Program Construction Module lab assignment**.


## Features

- Start a new bill and add items to it.
- Retrieve and continue working on pending bills.
- Generate and print bills as PDF documents.
- Admin panel for managing branch name, cashier name, and discount percentage.
- Generate revenue reports for a specified date range.

## How to Run

1. **Clone the repository:**
    ```sh
    git clone https://github.com/Kajee2002/SuperSaverPOS_VoidWalkers.git
    cd SuperSaverPOS_VoidWalkers
    ```

2. **Build and run the project:**
    ```sh
    javac -d bin src/org/example/*.java
    java -cp bin org.example.Main
    ```

3. **Follow the on-screen instructions to use the POS system.**

## Dependencies

- [iText](https://itextpdf.com/en): A library for creating and manipulating PDF documents in Java. Ensure you have the iText library included in your classpath.

## Files

- `Main.java`: The entry point of the application.
- `PosSystem.java`: Handles the management of bills (both completed and pending) and generates revenue reports.
- `Bill.java`: Represents a bill and handles the addition of items and generation of PDF invoices.
- `Item.java`: Represents an item in the store with its details.
- `AdminSession.java`: Manages the admin session, including branch name, cashier name, and discount settings.
- `Console.java`: Utility class for clearing the console.

## Usage

- **Start a new bill:** Adds items to a new bill and generates a PDF invoice.
- **Retrieve a pending bill:** Retrieve a pending bill to continue adding items or complete the bill.
- **Admin panel:** Manage branch name, cashier name, and discount percentage.
- **Generate revenue report:** Generate a revenue report for a specified date range.

## Contributors

- [![Static Badge](https://img.shields.io/badge/Kajatheepan-P?style=social&logo=github)](https://github.com/kajee2002)             
- [![Static Badge](https://img.shields.io/badge/Nalina-g?style=social&logo=github)]()         
- [![Static Badge](https://img.shields.io/badge/Thilaksan-t?style=social&logo=github)]()        
- [![Static Badge](https://img.shields.io/badge/Babijana-j?style=social&logo=github)]()
