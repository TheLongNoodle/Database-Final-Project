import java.io.Serial;

public class ExamQuestionsException extends ExamException{
	//variables
	@Serial
	private static final long serialVersionUID = 1L;
	
	
	public ExamQuestionsException() {
		super("There are not enough valid questions for a test");
	}
}
