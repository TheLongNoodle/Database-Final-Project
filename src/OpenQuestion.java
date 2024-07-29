import java.io.Serial;
import java.io.Serializable;

public class OpenQuestion extends SelectionQuestion implements Serializable {
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 1L;
	//variables
	private final Answer answer;

	// constructor
	public OpenQuestion(String content, Integer difficulty, Answer answer) {
		super(content, difficulty);
		this.answer = answer;
	}

	// usual get/set commands
	@Override
	public Answer getAnswer() {
		return this.answer;
	}

	public void setAnswer(String answer) {
		this.answer.setContent(answer);
	}

	//data to string
	@Override
	public String toString() {
        return "Question: " + this.content + "\n" +
                "(id: " + this.id + ", difficulty: " + this.difficulty + ")\n" +
                "Answer: " + this.answer.getContent() + "\n";
	}
}
