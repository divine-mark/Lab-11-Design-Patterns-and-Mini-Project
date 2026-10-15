public interface Borrowable {
    void borrow(Member member) throws ItemNotAvailableException;
    Loan giveBack();
}