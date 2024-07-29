import java.io.Serial;

public class ExamSelectionException extends ExamException{
	//variables
	@Serial
	private static final long serialVersionUID = 1L;
	
	
	public ExamSelectionException() {
		super("Can't create Test using a selection question with less than 4 answers");
	}
}
