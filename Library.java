import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Library {

    private final Map<String, LibraryItem> items = new HashMap<>();
    private final Map<String, Member> members = new HashMap<>();

    private static final Path ITEMS_FILE   = Path.of("library-items.csv");
    private static final Path MEMBERS_FILE = Path.of("library-members.csv");
    private static final Path LOANS_FILE   = Path.of("library-loans.csv");

    // ---------- Item & member management ----------
    public void addItem(LibraryItem item) {
        if (items.containsKey(item.getId()))
            throw new IllegalArgumentException("Item ID already exists: " + item.getId());
        items.put(item.getId(), item);
    }

    public void registerMember(Member member) {
        if (members.containsKey(member.getId()))
            throw new IllegalArgumentException("Member ID already exists: " + member.getId());
        members.put(member.getId(), member);
    }

    // ---------- Borrow / return ----------
    public Loan borrow(String memberId, String itemId)
            throws ItemNotAvailableException, MemberLimitExceededException {
        Member member = members.get(memberId);
        if (member == null) throw new IllegalArgumentException("No such member: " + memberId);

        LibraryItem item = items.get(itemId);
        if (item == null) throw new IllegalArgumentException("No such item: " + itemId);

        if (!member.canBorrow())
            throw new MemberLimitExceededException(
                member.getName() + " already has " + member.getLoanCount() + " active loans");

        item.borrow(member); // throws ItemNotAvailableException if borrowed
        member.addLoan(item.getCurrentLoan());
        return item.getCurrentLoan();
    }

    public Loan giveBack(String itemId) {
        LibraryItem item = items.get(itemId);
        if (item == null) throw new IllegalArgumentException("No such item: " + itemId);
        if (item.isAvailable()) throw new IllegalStateException("Item is not currently borrowed");

        Loan finished = item.giveBack();
        Member member = members.get(finished.memberId());
        if (member != null) member.removeLoan(itemId);
        return finished;
    }

    // ---------- Queries using streams ----------
    public List<LibraryItem> searchByTitle(String text) {
        String needle = text.toLowerCase();
        return items.values().stream()
            .filter(i -> i.getTitle().toLowerCase().contains(needle))
            .sorted(Comparator.comparing(LibraryItem::getTitle))
            .collect(Collectors.toList());
    }

    public List<LibraryItem> listOverdue() {
        LocalDate today = LocalDate.now();
        return items.values().stream()
            .filter(i -> !i.isAvailable())
            .filter(i -> i.getCurrentLoan().isOverdue(today))
            .sorted(Comparator.comparing(i -> i.getCurrentLoan().dueDate()))
            .collect(Collectors.toList());
    }

    public Collection<LibraryItem> allItems()   { return items.values(); }
    public Collection<Member>      allMembers() { return members.values(); }

    // ---------- Persistence ----------
    public void save() throws IOException {
        List<String> itemLines = new ArrayList<>();
        for (LibraryItem i : items.values()) {
            String extra = (i instanceof Book b) ? b.getAuthor() : ((Dvd) i).getDirector();
            itemLines.add(String.join(",",
                i.getId(), i.type(), i.getTitle(), extra, String.valueOf(i.isAvailable())));
        }
        Files.write(ITEMS_FILE, itemLines);

        List<String> memberLines = new ArrayList<>();
        for (Member m : members.values()) memberLines.add(m.getId() + "," + m.getName());
        Files.write(MEMBERS_FILE, memberLines);

        List<String> loanLines = new ArrayList<>();
        for (LibraryItem i : items.values()) {
            if (!i.isAvailable() && i.getCurrentLoan() != null) loanLines.add(i.getCurrentLoan().toCsv());
        }
        Files.write(LOANS_FILE, loanLines);
    }

    public void load() throws IOException {
        items.clear();
        members.clear();

        if (Files.exists(ITEMS_FILE)) {
            for (String line : Files.readAllLines(ITEMS_FILE)) {
                if (line.isBlank()) continue;
                String[] p = line.split(",", -1);
                LibraryItem item = p[1].equals("Book")
                    ? new Book(p[0], p[2], p[3])
                    : new Dvd(p[0], p[2], p[3]);
                items.put(item.getId(), item);
            }
        }

        if (Files.exists(MEMBERS_FILE)) {
            for (String line : Files.readAllLines(MEMBERS_FILE)) {
                if (line.isBlank()) continue;
                String[] p = line.split(",", -1);
                Member m = new Member(p[0], p[1]);
                members.put(m.getId(), m);
            }
        }

        if (Files.exists(LOANS_FILE)) {
            for (String line : Files.readAllLines(LOANS_FILE)) {
                if (line.isBlank()) continue;
                Loan l = Loan.fromCsv(line);
                Member m = members.get(l.memberId());
                LibraryItem i = items.get(l.itemId());
                if (m != null && i != null && i.isAvailable()) {
                    i.restoreLoan(l, m);
                    m.addLoan(l);
                }
            }
        }
    }
}