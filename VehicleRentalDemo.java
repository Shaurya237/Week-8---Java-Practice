import java.util.*;

abstract class Vehicle {
    private final String id;
    private boolean available = true;
    protected Vehicle(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Vehicle ID cannot be blank");
        this.id = id;
    }
    public String getId() { return id; }
    public boolean isAvailable() { return available; }
    void setAvailable(boolean available) { this.available = available; }
    public abstract double calculateRentalCharge(int days);
}
class Sedan extends Vehicle {
    Sedan(String id) { super(id); }
    @Override public double calculateRentalCharge(int days) { return days * 50.0; }
}
class SUV extends Vehicle {
    SUV(String id) { super(id); }
    @Override public double calculateRentalCharge(int days) { return days * 80.0; }
}
class Truck extends Vehicle {
    Truck(String id) { super(id); }
    @Override public double calculateRentalCharge(int days) { return days * 120.0; }
}
class Customer {
    private final String name;
    Customer(String name) { if (name == null || name.isBlank()) throw new IllegalArgumentException("Customer name required"); this.name = name; }
    public String getName() { return name; }
}
class Rental {
    private final Customer customer; private final Vehicle vehicle; private final int days;
    Rental(Customer customer, Vehicle vehicle, int days) { this.customer=customer; this.vehicle=vehicle; this.days=days; }
    public double getCharge() { return vehicle.calculateRentalCharge(days); }
    public Customer getCustomer() { return customer; }
    public Vehicle getVehicle() { return vehicle; }
}
class RentalSystem {
    private final Map<String, Rental> active = new HashMap<>();
    public void rent(Customer c, Vehicle v, int days) {
        if (days <= 0) throw new IllegalArgumentException("Days must be positive");
        if (!v.isAvailable() || active.containsKey(v.getId())) { System.out.println(v.getId()+" is currently unavailable."); return; }
        Rental r = new Rental(c,v,days); active.put(v.getId(),r); v.setAvailable(false);
        System.out.printf("%s rented successfully by %s. Rental charge: $%.2f%n", v.getId(), c.getName(), r.getCharge());
    }
    public void returnVehicle(Customer c, Vehicle v) {
        Rental r=active.get(v.getId());
        if (r == null || r.getCustomer()!=c) { System.out.println("No active rental for this customer and vehicle."); return; }
        active.remove(v.getId()); v.setAvailable(true); System.out.println(v.getId()+" returned by "+c.getName()+".");
    }
}
public class VehicleRentalDemo {
    public static void main(String[] args) {
        RentalSystem system=new RentalSystem(); Customer c1=new Customer("Customer 1"), c2=new Customer("Customer 2"), c3=new Customer("Customer 3");
        Vehicle sedan=new Sedan("Sedan A"), suv=new SUV("SUV B");
        system.rent(c1,sedan,3); system.rent(c2,sedan,2); system.returnVehicle(c1,sedan); system.rent(c3,suv,5);
    }
}
