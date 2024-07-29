import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

public class SubjectPool implements Serializable {
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 1L;
	// variables
	protected Subject[] subjects;

	// constructor
	public SubjectPool() {
		this.subjects = new Subject[1];
	}

	// usual get/set commands
	public Subject[] getSubjects() {
		return this.subjects;
	}

	public Subject getSubject(int index) {
		return this.subjects[index - 1];
	}

	public AnswerPool getAnswerPool(int index) {
		return this.subjects[index - 1].getAnswerPool();
	}

	public QuestionPool getQuestionPool(int index) {
		return this.subjects[index - 1].getQuestionPool();
	}

	public void setAnswerPool(AnswerPool answerPool, int index) {
		this.subjects[index - 1].setAnswerPool(answerPool);
	}

	public void setQuestionPool(QuestionPool questionPool, int index) {
		this.subjects[index - 1].setQuestionPool(questionPool);
	}

	// adding subject
	public int addSubject(String name) {
		this.subjects[this.subjects.length - 1] = new Subject(name, new QuestionPool(), new AnswerPool());
		this.subjects = Arrays.copyOf(this.subjects, (this.subjects.length + 1));
		sortSubjects();
		return this.subjects.length - 1;
	}

	// data to string (only names)
	@Override
	public String toString() {
		sortSubjects();
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < this.subjects.length; i++) {
			if (this.subjects[i] != null) {
				str.append((i + 1)).append(") ").append(this.subjects[i].getName()).append("\n");
			}
		}
		return str.toString();
	}

	// sorts array
	public void sortSubjects() {
		for (int i = 0; i < this.subjects.length; i++) {
			if (this.subjects[i] == null) {
				for (int k = i + 1; k < this.subjects.length; k++) {
					this.subjects[k - 1] = this.subjects[k];
				}
			}
		}
	}

}
