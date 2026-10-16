import java.time.LocalDate;
import java.util.*;

class HotelCustomer { private final String name; HotelCustomer(String name){this.name=name;} String getName(){return name;} }
abstract class Room {
    private final String roomNumber; private final double nightlyRate;
    Room(String roomNumber,double nightlyRate){if(roomNumber==null||roomNumber.isBlank()||nightlyRate<0)throw new IllegalArgumentException("Invalid room");this.roomNumber=roomNumber;this.nightlyRate=nightlyRate;}
    String getRoomNumber(){return roomNumber;} public abstract double calculatePrice(long nights);
    protected double basePrice(long nights){return nightlyRate*nights;}
}
class StandardRoom extends Room { StandardRoom(String n){super(n,100);} @Override public double calculatePrice(long nights){return basePrice(nights);} }
class DeluxeRoom extends Room { DeluxeRoom(String n){super(n,160);} @Override public double calculatePrice(long nights){return basePrice(nights)*1.10;} }
class Suite extends Room { Suite(String n){super(n,250);} @Override public double calculatePrice(long nights){return basePrice(nights)*1.20;} }
class Reservation {
    private final HotelCustomer customer; private final Room room; private final LocalDate checkIn,checkOut,cancellationDeadline; private boolean active=true;
    Reservation(HotelCustomer c,Room r,LocalDate in,LocalDate out,LocalDate deadline){if(c==null||r==null||in==null||out==null||!out.isAfter(in))throw new IllegalArgumentException("Invalid reservation");customer=c;room=r;checkIn=in;checkOut=out;cancellationDeadline=deadline;}
    boolean overlaps(LocalDate in,LocalDate out){return active&&in.isBefore(checkOut)&&out.isAfter(checkIn);}
    boolean isActive(){return active;} void cancel(LocalDate today){if(!active){System.out.println("Reservation is already inactive.");return;}if(today.isAfter(cancellationDeadline)){System.out.println("Cancellation deadline has passed.");return;}active=false;System.out.println("Reservation for "+customer.getName()+", "+room.getRoomNumber()+" ("+checkIn+" to "+checkOut+") cancelled successfully.");}
    double price(){return room.calculatePrice(java.time.temporal.ChronoUnit.DAYS.between(checkIn,checkOut));}
    Room getRoom(){return room;} LocalDate getCheckIn(){return checkIn;} LocalDate getCheckOut(){return checkOut;}
}
class HotelBookingSystem {
    private final List<Reservation> reservations=new ArrayList<>();
    boolean isAvailable(Room room,LocalDate in,LocalDate out){return reservations.stream().noneMatch(r->r.isActive()&&r.getRoom()==room&&r.overlaps(in,out));}
    Reservation reserve(HotelCustomer c,Room r,LocalDate in,LocalDate out,LocalDate deadline){if(!isAvailable(r,in,out)){System.out.println(r.getRoomNumber()+" is not available from "+in+" to "+out+".");return null;}Reservation res=new Reservation(c,r,in,out,deadline);reservations.add(res);System.out.printf("Reservation confirmed for %s, %s (%s to %s). Price: $%.2f%n",c.getName(),r.getRoomNumber(),in,out,res.price());return res;}
}
public class HotelBookingDemo {
    public static void main(String[] args){HotelBookingSystem h=new HotelBookingSystem();HotelCustomer a=new HotelCustomer("Customer A"),b=new HotelCustomer("Customer B"),c=new HotelCustomer("Customer C");Room standard=new StandardRoom("Standard Room 101"),deluxe=new DeluxeRoom("Deluxe Room 201");LocalDate jan1=LocalDate.of(2027,1,1),jan5=LocalDate.of(2027,1,5);System.out.println(standard.getRoomNumber()+" is available from "+jan1+" to "+jan5+".");Reservation r=h.reserve(a,standard,jan1,jan5,LocalDate.of(2026,12,25));h.reserve(b,standard,LocalDate.of(2027,1,3),LocalDate.of(2027,1,7),LocalDate.of(2026,12,25));r.cancel(LocalDate.of(2026,12,20));h.reserve(c,deluxe,LocalDate.of(2027,2,10),LocalDate.of(2027,2,12),LocalDate.of(2027,2,1));}
}
