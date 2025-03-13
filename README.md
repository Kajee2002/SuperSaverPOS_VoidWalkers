# SuperSaverPOS - VoidWalkers

## Overview

SuperSaverPOS is a Point of Sale (POS) system designed for supermarket management. This application allows administrators to manage bills, add items to bills, and generate invoices in PDF format. The application also provides an admin panel for managing branch details, cashier information, and discount settings.

This project was developed as part of our Program Construction Module lab assignment.

## Features

- Start a new bill and add items to it.
- Retrieve and continue working on pending bills.
- Generate and print bills as PDF documents.
- Admin panel for managing branch name, cashier name, and discount percentage.

## How to Run

1. **Clone the repository:**
    ```sh
    git clone [https://github.com/YOUR_USERNAME/SuperSaverPOS.git](https://github.com/Kajee2002/SuperSaverPOS_VoidWalkers.git)
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
- `PosSystem.java`: Handles the management of bills (both completed and pending).
- `Bill.java`: Represents a bill and handles the addition of items and generation of PDF invoices.
- `Item.java`: Represents an item in the store with its details.
- `AdminSession.java`: Manages the admin session, including branch name, cashier name, and discount settings.

## Usage

- **Start a new bill:** Adds items to a new bill and generates a PDF invoice.
- **Retrieve a pending bill:** Retrieve a pending bill to continue adding items or complete the bill.
- **Admin panel:** Manage branch name, cashier name, and discount percentage.

## Contributors

[![Static Badge](https://img.shields.io/badge/kajatheepan-p?style=plastic&link=https%3A%2F%2Fgithub.com%2FKajee2002)](https://github.com/kajee2002)      [![Static Badge](https://img.shields.io/badge/Nalina-G?style=plastic)]()      [![Static Badge](https://img.shields.io/badge/Thilaksan-T?style=plastic)]()       [![Static Badge](https://img.shields.io/badge/Babijana-J?style=plastic)]()

