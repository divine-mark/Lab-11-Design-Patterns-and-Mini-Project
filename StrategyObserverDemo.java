import java.util.ArrayList;
import java.util.List;


// STRATEGY

// Checkout holds a DiscountStrategy that can be swapped at runtime.
// Because DiscountStrategy has one abstract method, lambdas work.

interface DiscountStrategy {
    double apply(double price);
}

class Checkout {

    private DiscountStrategy strategy;

    Checkout(DiscountStrategy strategy) {
        this.strategy = strategy;
    }

    void setStrategy(DiscountStrategy strategy) {
        this.strategy = strategy;
    }

    double total(double price) {
        return strategy.apply(price);
    }
}
// OBSERVER

// GradeBook maintains a list of listeners. When a grade is
// published, every listener's onGradePublished is called.

interface GradeListener {
    void onGradePublished(String course, int mark);
}

class GradeBook {

    private final List<GradeListener> listeners = new ArrayList<>();

    void subscribe(GradeListener listener) {
        listeners.add(listener);
    }

    void publish(String course, int mark) {
        // EXPLAIN: notify every subscriber — the GradeBook doesn't know
        // or care what each one does with the notification
        for (GradeListener listener : listeners) {
            listener.onGradePublished(course, mark);
        }
    }
}


// DEMO
public class StrategyObserverDemo {

    public static void main(String[] args) {

        // --- Strategy demo: swap the discount at runtime ---
        Checkout checkout = new Checkout(price -> price); // no discount
        System.out.println("Regular: " + checkout.total(200));

        checkout.setStrategy(price -> price * 0.90); // 10% student discount
        System.out.println("Student: " + checkout.total(200));

        // --- Observer demo: multiple subscribers receive the notification ---
        GradeBook book = new GradeBook();

        book.subscribe((course, mark) ->
            System.out.println("Student portal: " + course + " = " + mark));

        book.subscribe((course, mark) -> {
            if (mark < 50) {
                System.out.println("Advisor alert: " + course + " failed");
            }
        });

        book.publish("OOP", 72);
        book.publish("Databases", 45);
    }
}