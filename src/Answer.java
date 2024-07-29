import java.io.Serial;
import java.io.Serializable;

public class Answer implements Serializable{
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 1L;
	//variables
	private String content;
	private boolean correct;
	
	// constructor
	public Answer(String content, boolean correct) {
		this.content = content;
		this.correct = correct;
	}
	
	// usual get/set commands
	public void setContent(String content) {
		this.content = content;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	public String getContent() {
		return this.content;
	}

	public boolean getCorrect() {
		return this.correct;
	}

	// data to string
	@Override
	public String toString() {
		return (this.content + " [" + this.correct + "]");
	}
}
