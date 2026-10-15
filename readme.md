# Object-Oriented Programming in Java — Lab 11: Design Patterns and Mini-Project

**Author:** Divine Mark Makhomeya  
**Registration Number:** H250333E  
**Course:** Object Oriented Programming  
**Date:** [Insert Date]

This repository contains my solutions for **Lab 11** of the OOP in Java lab manual.
The lab covers four design patterns (Singleton, Factory, Strategy, Observer) and a
mini-project that combines everything from Labs 1–10.

## Lab Objectives
- Recognise four common patterns (Singleton, Factory, Strategy, Observer) and the problem each solves.
- Apply SOLID-style thinking: single responsibility, open for extension, program to interfaces.
- Combine classes, interfaces, collections, exceptions and file I/O in one working application.

## Part 1 — Design Patterns

### Singleton and Factory
See `SingletonFactoryDemo.java`.

- `AppConfig` is a **Singleton** — one shared instance, private constructor, static getter.
- `NotificationFactory` is a **Factory** — returns an interface type based on a string.

**Expected Output:**
Same instance? true -> Example University
Email: Lab 11 is open
SMS: Lab 11 is open


### Strategy and Observer
See `StrategyObserverDemo.java`.

- `Checkout` uses a **Strategy** — swappable discount algorithm.
- `GradeBook` uses **Observer** — subscribers are notified when grades are published.

**Expected Output:**
Regular: 200.0
Student: 180.0
Student portal: OOP = 72
Student portal: Databases = 45
Advisor alert: Databases failed


## Part 2 — Mini-Project: Library Management System

### Features
- Abstract `LibraryItem` with `Book` (21-day loan) and `Dvd` (7-day loan) subclasses.
- `Borrowable` interface with `borrow(Member)` and `giveBack()`.
- Custom checked exceptions: `ItemNotAvailableException`, `MemberLimitExceededException` (max 3 loans).
- Items and members stored in `Map<String, ...>` keyed by id.
- CSV persistence: items, members, and active loans survive restarts.
- Console menu: add item, register member, borrow, return, list overdue, search by title (streams), list all, save & exit.

### Class Structure
LibraryItem (abstract, implements Borrowable)
├── Book (loanPeriodDays = 21)
└── Dvd (loanPeriodDays = 7)

Borrowable (interface)
borrow(Member) throws ItemNotAvailableException
giveBack() -> Loan

Member (id, name, activeLoans)
Loan (record: itemId, memberId, borrowDate, dueDate)
Library (core service: maps, borrow/return, search, overdue, save/load)
LibraryApp (console menu)


### Sample Session
=== Library Management ===

Add item

Register member

Borrow item

Return item

List overdue items

Search by title

List all

Save and exit
Choice: 1
Type (book/dvd): book
ID: B001
Title: Clean Code
Author: Robert Martin
Item added.

Choice: 3
Member ID: M001
Item ID: B001
Borrowed! Due 2026-11-04

Choice: 6
Search text: clean
Book[B001] "Clean Code" (borrowed by Tariro)


### Marking Guide Coverage

| Criterion | Marks | Where |
|---|---|---|
| Correct class design (encapsulation, inheritance, interface, abstraction) | 25 | `LibraryItem` hierarchy, `Borrowable`, private fields + validation |
| Polymorphism demonstrated meaningfully | 10 | `loanPeriodDays()` overridden in `Book`/`Dvd`, dispatched at run time |
| Custom exceptions and robust error handling | 15 | `ItemNotAvailableException`, `MemberLimitExceededException`, try/catch in `LibraryApp` |
| Collections and streams used appropriately | 15 | `HashMap` for lookups, streams in `searchByTitle` and `listOverdue` |
| File persistence works across runs | 15 | `Library.save()` / `Library.load()` write and read CSV files |
| Code quality: naming, comments, no duplication | 10 | Clear names, `// EXPLAIN:` comments, helper methods avoid repetition |
| Report and class diagram | 10 | See class structure section above |

## Files

**Design pattern demos:**
- `SingletonFactoryDemo.java`
- `StrategyObserverDemo.java`

**Mini-project:**
- `Borrowable.java`
- `ItemNotAvailableException.java`
- `MemberLimitExceededException.java`
- `Loan.java`
- `Member.java`
- `LibraryItem.java`
- `Book.java`
- `Dvd.java`
- `Library.java`
- `LibraryApp.java`
