import java.io.Serial;
import java.io.Serializable;

public class Subject  implements Serializable {
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 1L;
	//variables
	protected String name;
	protected QuestionPool questionPool;
	protected AnswerPool answerPool;
	
	//constructor
	public Subject(String name, QuestionPool questionPool, AnswerPool answerPool) {
		this.name = name;
		this.questionPool = questionPool;
		this.answerPool = answerPool;
	}
	
	//usual get/set commands
	public String getName() {
		return this.name;
	}
	
	public QuestionPool getQuestionPool() {
		return this.questionPool;
	}
	
	public AnswerPool getAnswerPool() {
		return this.answerPool;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setQuestionPool(QuestionPool questionPool) {
		this.questionPool = questionPool;
	}
	
	public void setAnswerPool(AnswerPool answerPool) {
		this.answerPool = answerPool;
	}
}
