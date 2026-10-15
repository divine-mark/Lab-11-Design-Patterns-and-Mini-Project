import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class LibraryApp {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Library library = new Library();
        try {
            library.load();
            System.out.println("Library data loaded from disk.");
        } catch (IOException e) {
            System.out.println("Could not load data: " + e.getMessage());
        }

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choice: ");

            try {
                switch (choice) {
                    case 1 -> addItem(library);
                    case 2 -> registerMember(library);
                    case 3 -> borrow(library);
                    case 4 -> giveBack(library);
                    case 5 -> listOverdue(library);
                    case 6 -> searchByTitle(library);
                    case 7 -> listAll(library);
                    case 8 -> { saveAndExit(library); running = false; }
                    default -> System.out.println("Unknown choice");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("=== Library Management ===");
        System.out.println("1. Add item");
        System.out.println("2. Register member");
        System.out.println("3. Borrow item");
        System.out.println("4. Return item");
        System.out.println("5. List overdue items");
        System.out.println("6. Search by title");
        System.out.println("7. List all");
        System.out.println("8. Save and exit");
    }

    private static void addItem(Library library) {
        String type = readString("Type (book/dvd): ").toLowerCase();
        String id = readString("ID: ");
        String title = readString("Title: ");
        if (type.equals("book")) {
            library.addItem(new Book(id, title, readString("Author: ")));
        } else if (type.equals("dvd")) {
            library.addItem(new Dvd(id, title, readString("Director: ")));
        } else {
            System.out.println("Unknown type");
            return;
        }
        System.out.println("Item added.");
    }

    private static void registerMember(Library library) {
        library.registerMember(new Member(readString("Member ID: "), readString("Name: ")));
        System.out.println("Member registered.");
    }

    private static void borrow(Library library) {
        String memberId = readString("Member ID: ");
        String itemId   = readString("Item ID: ");
        try {
            Loan loan = library.borrow(memberId, itemId);
            System.out.println("Borrowed! Due " + loan.dueDate());
        } catch (ItemNotAvailableException | MemberLimitExceededException e) {
            System.out.println("Cannot borrow: " + e.getMessage());
        }
    }

    private static void giveBack(Library library) {
        Loan finished = library.giveBack(readString("Item ID: "));
        System.out.println("Returned. Was due " + finished.dueDate());
    }

    private static void listOverdue(Library library) {
        List<LibraryItem> overdue = library.listOverdue();
        if (overdue.isEmpty()) { System.out.println("Nothing overdue."); return; }
        System.out.println("Overdue items:");
        overdue.forEach(i -> System.out.println("  " + i));
    }

    private static void searchByTitle(Library library) {
        List<LibraryItem> results = library.searchByTitle(readString("Search text: "));
        if (results.isEmpty()) { System.out.println("No matches."); return; }
        results.forEach(i -> System.out.println("  " + i));
    }

    private static void listAll(Library library) {
        System.out.println("Items:");
        library.allItems().forEach(i -> System.out.println("  " + i));
        System.out.println("Members:");
        library.allMembers().forEach(m -> System.out.println("  " + m));
    }

    private static void saveAndExit(Library library) {
        try { library.save(); System.out.println("Saved. Bye."); }
        catch (IOException e) { System.out.println("Save failed: " + e.getMessage()); }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        try { return Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }
}