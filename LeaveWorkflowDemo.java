import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

abstract class Employee {
    private final String name;
    protected Employee(String name) { if(name==null||name.isBlank()) throw new IllegalArgumentException("Name required"); this.name=name; }
    public String getName(){return name;}
    public abstract boolean isLeaveDurationAllowed(long days);
}
class FullTimeEmployee extends Employee {
    FullTimeEmployee(String name){super(name);}
    @Override public boolean isLeaveDurationAllowed(long days){return days>0 && days<=30;}
}
class PartTimeEmployee extends Employee {
    PartTimeEmployee(String name){super(name);}
    @Override public boolean isLeaveDurationAllowed(long days){return days>0 && days<=10;}
}
class Contractor extends Employee {
    Contractor(String name){super(name);}
    @Override public boolean isLeaveDurationAllowed(long days){return days>0 && days<=5;}
}
enum LeaveStatus { PENDING, APPROVED, REJECTED }
class LeaveRequest {
    private final Employee employee; private final LocalDate start,end; private LeaveStatus status=LeaveStatus.PENDING;
    LeaveRequest(Employee employee, LocalDate start, LocalDate end) {
        if(employee==null||start==null||end==null||end.isBefore(start)) throw new IllegalArgumentException("Invalid leave request");
        long days=ChronoUnit.DAYS.between(start,end)+1;
        if(!employee.isLeaveDurationAllowed(days)) throw new IllegalArgumentException("Leave duration violates employee policy");
        this.employee=employee; this.start=start; this.end=end;
    }
    public LeaveStatus getStatus(){return status;}
    public Employee getEmployee(){return employee;}
    public String dateRange(){return start+" to "+end;}
    public boolean review(boolean approve) {
        if(status!=LeaveStatus.PENDING) {System.out.println("Cannot review a request that is already "+status+"."); return false;}
        status=approve?LeaveStatus.APPROVED:LeaveStatus.REJECTED; return true;
    }
    public boolean changeStatus(LeaveStatus next) {
        if(status!=LeaveStatus.PENDING || next==LeaveStatus.PENDING) {System.out.println("Cannot change leave request status from "+status+" to "+next+".");return false;}
        status=next;return true;
    }
}
public class LeaveWorkflowDemo {
    public static void main(String[] args) {
        Employee john=new FullTimeEmployee("John"), jane=new PartTimeEmployee("Jane");
        LeaveRequest r1=new LeaveRequest(john,LocalDate.of(2026,1,1),LocalDate.of(2026,1,5));
        System.out.println("Leave request submitted for John (Jan 1-5). Status: "+r1.getStatus()+"."); r1.review(true);
        System.out.println("John's leave request (Jan 1-5) approved. Status: "+r1.getStatus()+"."); r1.changeStatus(LeaveStatus.PENDING);
        LeaveRequest r2=new LeaveRequest(jane,LocalDate.of(2026,2,10),LocalDate.of(2026,2,11));
        System.out.println("Leave request submitted for Jane (Feb 10-11). Status: "+r2.getStatus()+"."); r2.review(false);
        System.out.println("Jane's leave request (Feb 10-11) rejected. Status: "+r2.getStatus()+".");
    }
}
