import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class AnswerPool implements Serializable {
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 1L;
	//variables
	private Answer[] answers;
	
	// constructor
	public AnswerPool() {
		this.answers = new Answer[1];
	}
	
	// usual get/set commands
	public Answer[] getAnswers() {
		return this.answers;
	}
	
	public Answer getAnswer(int index) {
		return this.answers[index - 1];
	}
	
	// has checks
	public Boolean hasAnswers() {
        return this.answers.length > 1;
    }
	
	// adding answer
	public void addAnswer(String content, boolean correct) {
		this.answers[this.answers.length - 1] = new Answer(content, correct);
		this.answers = Arrays.copyOf(this.answers, (this.answers.length + 1));
		this.answers = sortArray();
    }
	
	// removing answer
	public void deleteAnswer(int index) {
		this.answers[index - 1].setContent("");
		this.answers[index - 1].setCorrect(false);
		this.answers = sortArray();
		this.answers = Arrays.copyOf(this.answers, this.answers.length - 1);
	}
	
	// data to string
	public String toString() {
		this.answers = sortArray();
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < this.answers.length; i++) {
			if ((this.answers[i] != null) && (!Objects.equals(this.answers[i].getContent(), ""))) {
				str.append("Answer ").append(i + 1).append(": ").append(this.answers[i].getContent()).append(" [").append(this.answers[i].getCorrect()).append("]\n");
			}
		}
		return str.toString();
	}
	
	// changing answer content
	public void changeAnswerContent(int index, String content, boolean correct) {
		this.answers[index - 1].setContent(content);
		this.answers[index - 1].setCorrect(correct);
	}
	
	// sorts array
	public Answer[] sortArray() {
		for (int i = 0; i < this.answers.length; i++) {
			if ((this.answers[i] == null) || (Objects.equals(this.answers[i].getContent(), ""))) {
				for (int k = i + 1; k < this.answers.length; k++) {
					this.answers[k - 1] = this.answers[k];
				}
			}
		}
		return this.answers;
	}
}
