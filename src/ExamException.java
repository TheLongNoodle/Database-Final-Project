import java.io.Serial;

public class ExamException extends Exception{
	//variables
	@Serial
	private static final long serialVersionUID = 1L;
	
	
	public ExamException(String msg) {
		super(msg);
	}

}
