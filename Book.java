public class Book extends LibraryItem {
    private static final int LOAN_DAYS = 21;
    private final String author;

    public Book(String id, String title, String author) {
        super(id, title);
        this.author = author;
    }

    public String getAuthor() { return author; }

    @Override public int loanPeriodDays() { return LOAN_DAYS; }
    @Override public String type() { return "Book"; }
}