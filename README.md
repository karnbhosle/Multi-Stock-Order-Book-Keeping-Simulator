📊 Multi-Stock Order Book Simulator
📌 Overview

The Multi-Stock Order Book Simulator is a Java-based academic project developed as part of an MCA – Advanced Data Structures (ADS) course.
It simulates the working of a real-world stock exchange by implementing order books, order matching, and trade execution using efficient data structures and a Java Swing-based graphical interface.

🎯 Project Objectives

To understand the working of stock market order books

To apply Advanced Data Structures in a real-world scenario

To demonstrate price-time priority order matching

To integrate backend logic with a graphical frontend

To simulate multi-user and multi-stock trading

🏦 Supported Stocks

The simulator supports the following stocks, each with its own independent order book:

Google

Facebook

Tesla

Reliance

Mahindra

👥 Multi-User Support

Each order is placed using a User ID

Trades record both Buyer ID and Seller ID

Enables realistic multi-user trading simulation

⚙️ Technologies Used

Programming Language: Java

GUI Framework: Java Swing

Data Structures:

TreeMap (Red-Black Tree)

Queue (FIFO for time priority)

HashMap (stock-wise order books)

🧠 Advanced Data Structures Used
Data Structure	Purpose
TreeMap	Maintains sorted BUY and SELL orders
Queue (ArrayDeque)	Preserves time priority (FIFO)
List	Stores transaction history
Map	Manages multiple stock order books
🔁 Order Matching Logic

BUY orders match with the lowest available SELL price

SELL orders match with the highest available BUY price

Supports partial and full order execution

Uses a greedy matching algorithm

🖥️ Application Features

Place BUY and SELL orders

Select stock to trade

Real-time order book visualization

Trade history with User IDs

Trading statistics (total trades, volume)

Clean and interactive Swing UI

🏗️ Project Architecture
User Interface (Java Swing)
        |
        v
Order Book Logic (ADS)
        |
        v
Trade Execution & Statistics


The project follows an MVC-like separation, keeping UI and business logic independent.

▶️ How to Run the Project
Prerequisites

Java JDK 8 or higher

Any IDE (VS Code / IntelliJ / Eclipse) or Command Line

Steps
javac OrderBookSwingSimulator.java
java OrderBookSwingSimulator

📂 Project Structure
OrderBookSwingSimulator.java
README.md

👨‍👩‍👦 Group Members & Roles

Jeevan – Problem Statement & System Architecture

Prajwal – Advanced Data Structures

Mohit – Order Matching Algorithm & User ID Concept

Aditya – Java Swing Frontend & UI Design

Karna – Trade History, Statistics & Integration

🎓 Academic Relevance

This project demonstrates:

Practical use of Advanced Data Structures

Algorithmic problem solving

GUI-based system design

Real-world application of ADS concepts

🚀 Future Enhancements

Market orders (Buy/Sell at best price)

Order cancellation and modification

User portfolio management

Database integration

JavaFX or Web-based frontend

📜 License

This project is developed for educational purposes only.
