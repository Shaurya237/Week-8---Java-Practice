import java.util.*;

public class NoticeBoardDemo {
    static class Student {
        final String name, department;
        private final List<NotificationChannel> preferredChannels = new ArrayList<>();
        Student(String name, String department) { this.name = name; this.department = department; }
        void addPreferredChannel(NotificationChannel channel) {
            if (channel != null) preferredChannels.add(channel);
        }
        List<NotificationChannel> getPreferredChannels() {
            return Collections.unmodifiableList(preferredChannels);
        }
    }
    interface NotificationChannel {
        String getName();
        void send(Student student, Notice notice);
    }
    static class EmailChannel implements NotificationChannel {
        public String getName() { return "Email"; }
        public void send(Student s, Notice n) { System.out.println("[Email → " + s.name + "] " + n.title); }
    }
    static class SmsChannel implements NotificationChannel {
        public String getName() { return "SMS"; }
        public void send(Student s, Notice n) { System.out.println("[SMS → " + s.name + "] " + n.title); }
    }
    static class AppChannel implements NotificationChannel {
        public String getName() { return "App"; }
        public void send(Student s, Notice n) { System.out.println("[App → " + s.name + "] " + n.title); }
    }
    // Add another channel by implementing NotificationChannel.
    static class Notice {
        final String title;
        final Set<String> targetDepartments;
        Notice(String title, Set<String> targetDepartments) {
            if (title == null || title.trim().isEmpty())
                throw new IllegalArgumentException("Notice title is required.");
            if (targetDepartments == null || targetDepartments.isEmpty())
                throw new IllegalArgumentException("At least one target department is required.");
            this.title = title.trim();
            this.targetDepartments = new LinkedHashSet<>(targetDepartments);
        }
    }
    static class NoticeBoard {
        private final List<Student> students = new ArrayList<>();
        void register(Student s) { students.add(s); }
        void postNotice(String title, Set<String> departments) {
            final Notice notice;
            try {
                notice = new Notice(title, departments);
            } catch (IllegalArgumentException e) {
                System.out.println("Cannot post notice: " + e.getMessage());
                return;
            }
            System.out.println("Notice '" + notice.title + "' posted to " +
                    String.join(", ", notice.targetDepartments) + ".");
            for (Student student : students) {
                if (notice.targetDepartments.contains(student.department)) {
                    for (NotificationChannel channel : student.getPreferredChannels()) {
                        channel.send(student, notice);
                    }
                }
            }
        }
    }
    public static void main(String[] args) {
        NoticeBoard board = new NoticeBoard();
        Student asha = new Student("Asha", "CSE");
        asha.addPreferredChannel(new EmailChannel());
        asha.addPreferredChannel(new AppChannel());
        Student ravi = new Student("Ravi", "ECE");
        ravi.addPreferredChannel(new SmsChannel());
        board.register(asha);
        board.register(ravi);
        board.postNotice("Lab Closed Tomorrow", new LinkedHashSet<>(Arrays.asList("CSE")));
        board.postNotice("Fee Deadline Extended", new LinkedHashSet<>(Arrays.asList("CSE", "ECE")));
        board.postNotice("Sports Day", Collections.emptySet());
    }
}
