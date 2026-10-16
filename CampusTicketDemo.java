import java.time.LocalDateTime;
import java.util.*;

public class CampusTicketDemo {
    static class Customer {
        final String name;
        Customer(String name) { this.name = name; }
    }
    interface Seat {
        String getId();
        double getPrice();
    }
    static class RegularSeat implements Seat {
        private final String id;
        RegularSeat(String id) { this.id = id; }
        public String getId() { return id; }
        public double getPrice() { return 150; }
    }
    static class PremiumSeat implements Seat {
        private final String id;
        PremiumSeat(String id) { this.id = id; }
        public String getId() { return id; }
        public double getPrice() { return 250; }
    }
    static class ReclinerSeat implements Seat {
        private final String id;
        ReclinerSeat(String id) { this.id = id; }
        public String getId() { return id; }
        public double getPrice() { return 400; }
    }
    static class Show {
        final String name;
        final LocalDateTime startTime;
        private final Set<String> bookedSeatIds = new HashSet<>();
        Show(String name, LocalDateTime startTime) { this.name = name; this.startTime = startTime; }
        boolean areAvailable(List<Seat> seats) {
            Set<String> requested = new HashSet<>();
            for (Seat s : seats) {
                if (bookedSeatIds.contains(s.getId()) || !requested.add(s.getId())) {
                    System.out.println("Seat " + s.getId() + " is already booked for this show.");
                    return false;
                }
            }
            return true;
        }
        void reserve(List<Seat> seats) { for (Seat s : seats) bookedSeatIds.add(s.getId()); }
        void release(List<Seat> seats) { for (Seat s : seats) bookedSeatIds.remove(s.getId()); }
    }
    static class Booking {
        final Customer customer;
        final Show show;
        final List<Seat> seats;
        private boolean cancelled;
        Booking(Customer customer, Show show, List<Seat> seats) {
            this.customer = customer; this.show = show; this.seats = new ArrayList<>(seats);
        }
        double total() { return seats.stream().mapToDouble(Seat::getPrice).sum(); }
        boolean cancel(LocalDateTime now) {
            if (cancelled) { System.out.println("Booking is already cancelled."); return false; }
            if (!now.isBefore(show.startTime)) {
                System.out.println("Cannot cancel: the show has already started.");
                return false;
            }
            show.release(seats);
            cancelled = true;
            System.out.printf("%s's booking cancelled. Seats %s released.%n", customer.name, seatIds());
            return true;
        }
        String seatIds() {
            List<String> ids = new ArrayList<>();
            for (Seat s : seats) ids.add(s.getId());
            return String.join(", ", ids);
        }
    }
    static Booking book(Customer c, Show show, List<Seat> seats) {
        if (seats.isEmpty() || seats.size() > 6) {
            System.out.println("Booking must contain between 1 and 6 seats.");
            return null;
        }
        if (!show.areAvailable(seats)) return null;
        show.reserve(seats);
        Booking b = new Booking(c, show, seats);
        System.out.printf("Booking confirmed for %s: %s. Total: ₹%.2f.%n", c.name, b.seatIds(), b.total());
        return b;
    }
    public static void main(String[] args) {
        Customer asha = new Customer("Asha"), ravi = new Customer("Ravi"), neha = new Customer("Neha");
        Show show = new Show("7 PM show", LocalDateTime.of(2026, 10, 16, 19, 0));
        Booking a = book(asha, show, Arrays.asList(new RegularSeat("A1"), new RegularSeat("A2"), new PremiumSeat("F5")));
        book(ravi, show, Collections.singletonList(new RegularSeat("A2")));
        book(ravi, show, Collections.singletonList(new ReclinerSeat("R1")));
        a.cancel(LocalDateTime.of(2026, 10, 16, 18, 0));
        book(neha, show, Collections.singletonList(new RegularSeat("A2")));
    }
}
