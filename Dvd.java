public class Dvd extends LibraryItem {
    private static final int LOAN_DAYS = 7;
    private final String director;

    public Dvd(String id, String title, String director) {
        super(id, title);
        this.director = director;
    }

    public String getDirector() { return director; }

    @Override public int loanPeriodDays() { return LOAN_DAYS; }
    @Override public String type() { return "DVD"; }
}