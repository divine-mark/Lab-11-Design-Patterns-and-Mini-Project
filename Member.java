import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Member {
    private static final int MAX_LOANS = 3;

    private final String id;
    private final String name;
    private final List<Loan> activeLoans = new ArrayList<>();

    public Member(String id, String name) {
        if (id == null || id.isBlank())     throw new IllegalArgumentException("ID required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name required");
        this.id = id;
        this.name = name.trim();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public List<Loan> getActiveLoans() { return Collections.unmodifiableList(activeLoans); }
    public int getLoanCount() { return activeLoans.size(); }
    public boolean canBorrow() { return activeLoans.size() < MAX_LOANS; }

    public void addLoan(Loan loan) { activeLoans.add(loan); }
    public void removeLoan(String itemId) { activeLoans.removeIf(l -> l.itemId().equals(itemId)); }

    @Override
    public String toString() {
        return String.format("Member[%s] %s (%d active loan(s))", id, name, activeLoans.size());
    }
}