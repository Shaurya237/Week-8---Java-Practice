public class FitZoneDemo {
    static class Member {
        final String name;
        private Membership membership;
        Member(String name) { this.name = name; }
        Membership buy(MembershipPlan plan) {
            membership = new Membership(this, plan);
            System.out.printf("%s membership created for %s. Fee: ₹%.2f. Status: %s.%n",
                    plan.getName(), name, plan.calculateFee(), membership.getStatus());
            return membership;
        }
    }
    interface MembershipPlan {
        String getName();
        int getMonths();
        double getDiscount();
        default double calculateFee() { return 1000.0 * getMonths() * (1.0 - getDiscount()); }
    }
    static class MonthlyPlan implements MembershipPlan {
        public String getName() { return "Monthly"; }
        public int getMonths() { return 1; }
        public double getDiscount() { return 0; }
    }
    static class QuarterlyPlan implements MembershipPlan {
        public String getName() { return "Quarterly"; }
        public int getMonths() { return 3; }
        public double getDiscount() { return 0.10; }
    }
    static class AnnualPlan implements MembershipPlan {
        public String getName() { return "Annual"; }
        public int getMonths() { return 12; }
        public double getDiscount() { return 0.25; }
    }
    enum Status { ACTIVE, FROZEN, EXPIRED }
    static class Membership {
        final Member member;
        final MembershipPlan plan;
        private Status status = Status.ACTIVE;
        Membership(Member member, MembershipPlan plan) { this.member = member; this.plan = plan; }
        Status getStatus() { return status; }
        void checkIn() {
            if (status == Status.ACTIVE) System.out.println(member.name + " checked in successfully.");
            else System.out.println("Check-in denied: " + member.name + "'s membership is " + status + ".");
        }
        void freeze() {
            if (status == Status.ACTIVE) {
                status = Status.FROZEN;
                System.out.println(member.name + "'s membership frozen. Status: " + status + ".");
            } else if (status == Status.EXPIRED) {
                System.out.println("Cannot freeze an Expired membership.");
            } else {
                System.out.println("Membership is already Frozen.");
            }
        }
        void unfreeze() {
            if (status == Status.FROZEN) {
                status = Status.ACTIVE;
                System.out.println(member.name + "'s membership unfrozen. Status: " + status + ".");
            } else if (status == Status.EXPIRED) {
                System.out.println("Cannot unfreeze an Expired membership.");
            } else {
                System.out.println("Membership is already Active.");
            }
        }
        void expire() {
            if (status != Status.EXPIRED) {
                status = Status.EXPIRED;
                System.out.println(member.name + "'s membership expired. Status: " + status + ".");
            }
        }
    }
    public static void main(String[] args) {
        Member asha = new Member("Asha"), ravi = new Member("Ravi");
        Membership a = asha.buy(new QuarterlyPlan());
        Membership r = ravi.buy(new MonthlyPlan());
        a.checkIn();
        a.freeze();
        a.checkIn();
        r.expire();
        r.freeze();
    }
}
