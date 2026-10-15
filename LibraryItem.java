import java.time.LocalDate;

public abstract class LibraryItem implements Borrowable {
    private final String id;
    private final String title;
    private boolean available = true;
    private Member currentBorrower;
    private Loan currentLoan;

    protected LibraryItem(String id, String title) {
        if (id == null || id.isBlank())     throw new IllegalArgumentException("ID required");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Title required");
        this.id = id;
        this.title = title.trim();
    }

    public abstract int loanPeriodDays();
    public abstract String type();

    public String getId() { return id; }
    public String getTitle() { return title; }
    public boolean isAvailable() { return available; }
    public Member getCurrentBorrower() { return currentBorrower; }
    public Loan getCurrentLoan() { return currentLoan; }

    @Override
    public void borrow(Member member) throws ItemNotAvailableException {
        if (!available) throw new ItemNotAvailableException(
            "Item " + id + " is already borrowed by " + currentBorrower.getName());
        this.available = false;
        this.currentBorrower = member;
        this.currentLoan = new Loan(id, member.getId(),
            LocalDate.now(), LocalDate.now().plusDays(loanPeriodDays()));
    }

    @Override
    public Loan giveBack() {
        Loan finished = this.currentLoan;
        this.available = true;
        this.currentBorrower = null;
        this.currentLoan = null;
        return finished;
    }

    // Package-private, used by Library when restoring from disk
    void restoreLoan(Loan loan, Member borrower) {
        this.available = false;
        this.currentBorrower = borrower;
        this.currentLoan = loan;
    }

    @Override
    public String toString() {
        String status = available ? "available" : "borrowed by " + currentBorrower.getName();
        return String.format("%s[%s] \"%s\" (%s)", type(), id, title, status);
    }
}