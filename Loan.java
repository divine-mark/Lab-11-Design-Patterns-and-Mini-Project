import java.time.LocalDate;

public record Loan(String itemId, String memberId, LocalDate borrowDate, LocalDate dueDate) {
    public boolean isOverdue(LocalDate today) { return today.isAfter(dueDate); }

    public String toCsv() {
        return itemId + "," + memberId + "," + borrowDate + "," + dueDate;
    }

    public static Loan fromCsv(String line) {
        String[] p = line.split(",");
        return new Loan(p[0], p[1], LocalDate.parse(p[2]), LocalDate.parse(p[3]));
    }
}