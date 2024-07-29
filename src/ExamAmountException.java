import java.io.Serial;

public class ExamAmountException extends ExamException{
	//variables
	@Serial
	private static final long serialVersionUID = 1L;
	
	
	public ExamAmountException() {
		super("Can't create Test using less than 10 questions");
	}
}
