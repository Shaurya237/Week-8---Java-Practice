import java.util.*;

class Student { private final String name; Student(String name){this.name=name;} String getName(){return name;} }
abstract class Question {
    private final String id; private final int points;
    Question(String id,int points){if(points<0)throw new IllegalArgumentException("Points cannot be negative");this.id=id;this.points=points;}
    public String getId(){return id;} public int getPoints(){return points;}
    public abstract boolean isCorrect(String answer);
}
class MultipleChoiceQuestion extends Question {
    private final String correctOption;
    MultipleChoiceQuestion(String id,int points,String correctOption){super(id,points);this.correctOption=correctOption;}
    @Override public boolean isCorrect(String answer){return answer!=null&&correctOption.equalsIgnoreCase(answer.trim());}
}
class TrueFalseQuestion extends Question {
    private final boolean correctAnswer;
    TrueFalseQuestion(String id,int points,boolean correctAnswer){super(id,points);this.correctAnswer=correctAnswer;}
    @Override public boolean isCorrect(String answer){return answer!=null&&Boolean.toString(correctAnswer).equalsIgnoreCase(answer.trim());}
}
class ShortAnswerQuestion extends Question {
    private final String expected;
    ShortAnswerQuestion(String id,int points,String expected){super(id,points);this.expected=expected;}
    @Override public boolean isCorrect(String answer){return answer!=null&&expected.equalsIgnoreCase(answer.trim());}
}
class Examination {
    private final String title; private final List<Question> questions=new ArrayList<>();
    Examination(String title){this.title=title;} void addQuestion(Question q){questions.add(q);} String getTitle(){return title;} List<Question> getQuestions(){return Collections.unmodifiableList(questions);}
}
class Attempt {
    private final Student student; private final Examination exam; private final Map<String,String> answers=new LinkedHashMap<>(); private boolean submitted=false;
    Attempt(Student student,Examination exam){this.student=student;this.exam=exam;System.out.println(exam.getTitle()+" started by "+student.getName()+".");}
    public void answer(String questionId,String answer){if(submitted){System.out.println("Cannot change answers for a submitted examination.");return;} boolean exists=exam.getQuestions().stream().anyMatch(q->q.getId().equals(questionId));if(!exists){System.out.println("Unknown question: "+questionId);return;}answers.put(questionId,answer);System.out.println("Answer recorded for "+questionId+".");}
    public void submit(){if(submitted){System.out.println("Attempt already submitted.");return;}submitted=true;int total=0,max=0;System.out.println(exam.getTitle()+" submitted by "+student.getName()+".");for(Question q:exam.getQuestions()){max+=q.getPoints();boolean correct=q.isCorrect(answers.get(q.getId()));int earned=correct?q.getPoints():0;total+=earned;System.out.println("Result: "+q.getId()+": "+(correct?"Correct":"Incorrect")+" ("+earned+" points)");}System.out.println("Total score: "+total+"/"+max+".");}
}
public class OnlineExaminationDemo {
    public static void main(String[] args){Student s=new Student("Student 1");Examination e=new Examination("Exam A");e.addQuestion(new MultipleChoiceQuestion("Question 1",5,"B"));e.addQuestion(new TrueFalseQuestion("Question 2",5,false));Attempt a=new Attempt(s,e);a.answer("Question 1","C");a.answer("Question 2","True");a.submit();a.answer("Question 1","B");}
}
