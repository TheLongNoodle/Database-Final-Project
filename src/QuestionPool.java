import java.io.Serial;
import java.util.Arrays;
import java.io.Serializable;

public class QuestionPool implements Serializable{
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 1L;
	// variables
	protected SelectionQuestion[] questions;
	
	// constructor
	public QuestionPool() {
		this.questions = new SelectionQuestion[1];
	}
	
	// usual get/set commands
	public SelectionQuestion[] getQuestions() {
		return this.questions;
	}
	
	public SelectionQuestion getQuestion(int index) {
		return this.questions[index - 1];
	}
	
	public void setAnswer(String answer, int index) {
		if (this.questions[index - 1] instanceof OpenQuestion) {
			this.questions[index - 1].setAnswer(answer);
		}
	}
	
	// has checks
	public Boolean hasQuestions() {
        return this.questions.length > 1;
    }

	public Boolean hasOpenQuestions() {
		if (this.questions.length > 1) {
            for (SelectionQuestion question : this.questions) {
                if (question instanceof OpenQuestion) {
                    return true;
                }
            }
		}
		return false;
	}

	public Boolean hasSelectionQuestions() {
		if (this.questions.length > 1) {
            for (SelectionQuestion question : this.questions) {
                if ((!(question instanceof OpenQuestion)) && (question != null)) {
                    return true;
                }
            }
		}
		return false;
	}

	// adding question
	public void addQuestion(String content, int difficulty, String answer) {
		this.questions[this.questions.length - 1] = new OpenQuestion(content, difficulty, new Answer(answer,true));
		this.questions = Arrays.copyOf(this.questions, (this.questions.length + 1));
		this.questions = sortArray();
    }

	public void addQuestion(String content, int difficulty) {
		this.questions[this.questions.length - 1] = new SelectionQuestion(content, difficulty);
		this.questions = Arrays.copyOf(this.questions, (this.questions.length + 1));
		this.questions = sortArray();
    }

	// removing question
	public void deleteQuestion(int index) {
		this.questions[index - 1] = null;
		this.questions = sortArray();
		this.questions = Arrays.copyOf(this.questions, this.questions.length - 1);

	}

	// adding answer to question
	public void addAnswer(int question, Answer answer) {
		this.questions[question - 1].addAnswer(answer);
		this.questions = sortArray();
	}

	// removing answer from question
	public void deleteAnswer(int question, int answer) {
		this.questions[question - 1].deleteAnswer(answer);
		this.questions = sortArray();
	}

	// changing question content
	public void changeQuestionContent(int index, String content) {
		this.questions[index - 1].setContent(content);
	}

	// data to string (only questions)
	@Override
	public String toString() {
		this.questions = sortArray();
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < this.questions.length; i++) {
			if (this.questions[i] != null) {
				str.append("Question ").append(i + 1).append(": ").append(this.questions[i].getContent()).append("\n");
			}
		}
		return str.toString();
	}

	// data to string (including answers)
	public String toStringFull() {
		this.questions = sortArray();
		StringBuilder str = new StringBuilder();
        for (SelectionQuestion question : this.questions) {
            if (question != null) {
                str.append(question).append("\n");
            }
        }
		return str.toString();
	}

	// data to string (Only selection)
	public String toStringSelection() {
		this.questions = sortArray();
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < this.questions.length; i++) {
			if ((this.questions[i] != null) && (!(this.questions[i] instanceof OpenQuestion))) {
				str.append("Question ").append(i + 1).append(": ").append(this.questions[i].getContent()).append("\n");
			}
		}
		return str.toString();
	}

	// data to string (Only open)
	public String toStringOpen() {
		this.questions = sortArray();
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < this.questions.length; i++) {
			if ((this.questions[i] != null) && (this.questions[i] instanceof OpenQuestion)) {
				str.append("Question ").append(i + 1).append(": ").append(this.questions[i].getContent()).append("\n");
			}
		}
		return str.toString();
	}

	// sorts array
	public SelectionQuestion[] sortArray() {
		for (int i = 0; i < this.questions.length; i++) {
			if (this.questions[i] == null) {
				for (int k = i + 1; k < this.questions.length; k++) {
					this.questions[k - 1] = this.questions[k];
				}
			}
		}
		return this.questions;
	}
}
