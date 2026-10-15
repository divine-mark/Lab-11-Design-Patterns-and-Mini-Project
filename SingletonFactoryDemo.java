
// SINGLETON
// Guarantees exactly one instance of AppConfig exists.
// The constructor is private, so no one outside this class can
// create another instance. The static getInstance() returns the
// one and only instance.
class AppConfig {

    // created once, when the class is first loaded
    private static final AppConfig INSTANCE = new AppConfig();

    private final String institution = "Example University";

    // private constructor blocks 'new AppConfig()' from outside
    private AppConfig() { }

    // the only way to get the instance
    public static AppConfig getInstance() {
        return INSTANCE;
    }

    public String getInstitution() {
        return institution;
    }
}

// FACTORY

// Callers ask the factory for a Notification by name.
// They never see or depend on the concrete classes.

interface Notification {
    void send(String message);
}

class EmailNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}

class SmsNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}

class NotificationFactory {

    // EXPLAIN: returns the interface type, not a concrete class
    static Notification create(String type) {
        return switch (type.toLowerCase()) {
            case "email" -> new EmailNotification();
            case "sms"   -> new SmsNotification();
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }
}


// DEMO

public class SingletonFactoryDemo {

    public static void main(String[] args) {

        // --- Singleton demo ---
        AppConfig a = AppConfig.getInstance();
        AppConfig b = AppConfig.getInstance();

        System.out.println("Same instance? " + (a == b) + " -> " + a.getInstitution());

        // --- Factory demo ---
        NotificationFactory.create("email").send("Lab 11 is open");
        NotificationFactory.create("SMS").send("Lab 11 is open");

        // Try an unknown type to see the factory's error handling
        try {
            NotificationFactory.create("pigeon").send("Test");
        } catch (IllegalArgumentException e) {
            System.out.println("Factory rejected: " + e.getMessage());
        }
    }
}