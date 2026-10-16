import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class AssignmentPortalDemo {
    static class Student {
        final String name;
        Student(String name) { this.name = name; }
    }
    interface AssignmentType {
        String getName();
        double applyPenalty(double awardedMarks, long daysLate);
    }
    static class CodingAssignment implements AssignmentType {
        public String getName() { return "Coding"; }
        public double applyPenalty(double marks, long daysLate) {
            return Math.max(0, marks * (1.0 - 0.10 * daysLate));
        }
    }
    static class WrittenAssignment implements AssignmentType {
        public String getName() { return "Written"; }
        public double applyPenalty(double marks, long daysLate) {
            return Math.max(0, marks * (1.0 - 0.20 * daysLate));
        }
    }
    static class Assignment {
        final String title;
        final LocalDate dueDate;
        final double maxMarks;
        final AssignmentType type;
        Assignment(String title, LocalDate dueDate, double maxMarks, AssignmentType type) {
            this.title = title; this.dueDate = dueDate; this.maxMarks = maxMarks; this.type = type;
        }
    }
    enum Status { SUBMITTED, GRADED }
    static class Submission {
        final Student student;
        final Assignment assignment;
        final LocalDate submittedDate;
        private Status status = Status.SUBMITTED;
        private Double finalMarks;
        Submission(Student student, Assignment assignment, LocalDate date) {
            this.student = student; this.assignment = assignment; this.submittedDate = date;
        }
        Status getStatus() { return status; }
        boolean grade(double awardedMarks) {
            if (status == Status.GRADED) {
                System.out.println("Cannot grade/resubmit: '" + assignment.title + "' has already been graded.");
                return false;
            }
            if (awardedMarks < 0 || awardedMarks > assignment.maxMarks) {
                System.out.println("Invalid marks for " + assignment.title + ".");
                return false;
            }
            long daysLate = Math.max(0, ChronoUnit.DAYS.between(assignment.dueDate, submittedDate));
            finalMarks = assignment.type.applyPenalty(awardedMarks, daysLate);
            status = Status.GRADED;
            String penalty = daysLate == 0 ? "" : String.format(" after %.0f%% late penalty",
                    (100.0 - (finalMarks / awardedMarks * 100.0)));
            System.out.printf("%s graded: %.0f/%.0f%s. Status: %s.%n",
                    student.name, finalMarks, assignment.maxMarks, penalty, status);
            return true;
        }
        boolean resubmit() {
            if (status == Status.GRADED) {
                System.out.println("Cannot resubmit: '" + assignment.title + "' has already been graded.");
                return false;
            }
            System.out.println("Resubmission accepted for '" + assignment.title + "'.");
            return true;
        }
    }
    static Submission submit(Student student, Assignment assignment, LocalDate date) {
        Submission s = new Submission(student, assignment, date);
        long daysLate = Math.max(0, ChronoUnit.DAYS.between(assignment.dueDate, date));
        if (daysLate == 0)
            System.out.printf("%s's submission for '%s' received (on time). Status: %s.%n",
                    student.name, assignment.title, s.getStatus());
        else
            System.out.printf("%s's submission for '%s' received (%d days late). Status: %s.%n",
                    student.name, assignment.title, daysLate, s.getStatus());
        return s;
    }
    public static void main(String[] args) {
        Student asha = new Student("Asha"), ravi = new Student("Ravi");
        Assignment coding = new Assignment("Linked List Lab", LocalDate.of(2026, 3, 10), 50, new CodingAssignment());
        Assignment written = new Assignment("Design Essay", LocalDate.of(2026, 3, 12), 50, new WrittenAssignment());
        Submission a = submit(asha, coding, LocalDate.of(2026, 3, 10));
        Submission r = submit(ravi, written, LocalDate.of(2026, 3, 14));
        a.grade(45);
        r.grade(40);
        a.resubmit();
    }
}
