import java.util.*;

public class HostelLaundryDemo {
    static class Student {
        private final String name;
        Student(String name) { this.name = name; }
        String getName() { return name; }
    }

    interface WashType {
        String getName();
        int getDurationMinutes();
        double getCharge();
    }
    static class QuickWash implements WashType {
        public String getName() { return "Quick"; }
        public int getDurationMinutes() { return 30; }
        public double getCharge() { return 20.0; }
    }
    static class NormalWash implements WashType {
        public String getName() { return "Normal"; }
        public int getDurationMinutes() { return 45; }
        public double getCharge() { return 30.0; }
    }
    static class HeavyWash implements WashType {
        public String getName() { return "Heavy"; }
        public int getDurationMinutes() { return 60; }
        public double getCharge() { return 45.0; }
    }
    // Add a new wash type by implementing WashType; booking logic stays unchanged.
    static class WashCycle {
        final Student student;
        final WashingMachine machine;
        final WashType type;
        WashCycle(Student student, WashingMachine machine, WashType type) {
            this.student = student; this.machine = machine; this.type = type;
        }
    }
    static class WashingMachine {
        private final String id;
        private WashCycle activeCycle;
        WashingMachine(String id) { this.id = id; }
        String getId() { return id; }
        boolean isFree() { return activeCycle == null; }
        boolean startWash(Student student, WashType type) {
            if (!isFree()) {
                System.out.println("Machine " + id + " is currently busy.");
                return false;
            }
            activeCycle = new WashCycle(student, this, type);
            System.out.printf("%s wash started on %s for %s (%d min). Charge: ₹%.2f.%n",
                    type.getName(), id, student.getName(), type.getDurationMinutes(), type.getCharge());
            return true;
        }
        void completeCycle() {
            if (isFree()) {
                System.out.println(id + " has no active cycle.");
                return;
            }
            System.out.println(id + " cycle completed.");
            activeCycle = null;
            System.out.println(id + " is now free.");
        }
    }

    public static void main(String[] args) {
        Student asha = new Student("Asha"), ravi = new Student("Ravi"), neha = new Student("Neha");
        WashingMachine m1 = new WashingMachine("M1"), m2 = new WashingMachine("M2");
        m1.startWash(asha, new QuickWash());
        m1.startWash(ravi, new HeavyWash());
        m2.startWash(ravi, new HeavyWash());
        m1.completeCycle();
        m1.startWash(neha, new NormalWash());
    }
}
