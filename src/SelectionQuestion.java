import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class SelectionQuestion implements Serializable {
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 1L;
	//variables
	private static int counter;
	protected int id;
	protected String content;
	protected Answer[] answers;
	protected String difficulty;
	protected Boolean isValid;

	// constructor
	public SelectionQuestion(String content, int difficulty) {
		this.isValid = false;
		this.content = content;
		this.id = ++counter;
		switch (difficulty) {
		case 1: {
			this.difficulty = "Easy";
			break;
		}
		case 2: {
			this.difficulty = "Medium";
			break;
		}
		case 3: {
			this.difficulty = "Hard";
			break;
		}
		}
		this.answers = new Answer[12];
		answers[10] = new Answer("More than one answer is true", false);
		answers[11] = new Answer("All of the answers are false", false);
	}

	// usual get/set commands
	public String getContent() {
		return this.content;
	}

	public int getId() {
		return this.id;
	}

	public String getDifficulty() {
		return this.difficulty;
	}

	public int getDifficultyInt() {
        return switch (difficulty) {
            case "Easy" -> 1;
            case "Medium" -> 2;
            case "Hard" -> 3;
            default -> 0;
        };
    }

	public final Answer[] getAnswers() {
		return this.answers;
	}
	
	public Answer getAnswer() {
		return null;
	}
	
	public final Answer getAnswer(int index) {
		return this.answers[index - 1];
	}

	public int getAnswerCount() {
		int count = 0;
		for (int i = 0; i < 10; i++) {
			if ((this.answers[i] != null) && (!Objects.equals(this.answers[i].getContent(), ""))) {
				count++;
			}
		}
		return count;
	}

	public void setAnswer(String answer) {
	}

	public void setContent(String content) {
		this.content = content;
	}

	// adding answer
	public final void addAnswer(Answer answer) {
		for (int i = 0; i < 10; i++) {
			if ((this.answers[i] == null) || (Objects.equals(this.answers[i].getContent(), ""))) {
				this.answers[i] = answer;
				answers[10].setCorrect(multipleTrues());
				answers[11].setCorrect(allFalse());
				return;
			}
		}
		System.out.println("This question has already 10 answers, aborting...");
	}

	// removing answer
	public final void deleteAnswer(int index) {
		this.answers[index - 1] = null;
		this.answers = sortArray();
	}

	// data to string
	public String toString() {
		this.answers = sortArray();
		StringBuilder str = new StringBuilder();
		str.append("Question: ").append(this.content).append("\n");
		str.append("(id: ").append(this.id).append(", difficulty: ").append(this.difficulty).append(")\n");
		for (int i = 0; i < 10; i++) {
			if ((this.answers[i] != null) && (!Objects.equals(this.answers[i].getContent(), ""))) {
				str.append((i + 1)).append(")").append(this.answers[i].toString()).append("\n");
			}
		}
		return str.toString();
	}

	// sorts array
	public final Answer[] sortArray() {
		for (int i = 0; i < 10; i++) {
			if ((this.answers[i] == null) || (Objects.equals(this.answers[i].getContent(), ""))) {
				for (int k = i + 1; k < 10; k++) {
					this.answers[k - 1] = this.answers[k];
				}
			}
		}
		this.answers[10].setCorrect(multipleTrues());
		this.answers[11].setCorrect(allFalse());
		return this.answers;
	}

	// check if more than one answer is true
	public final boolean multipleTrues() {
		int counter = 0;
		for (int i = 0; i < 10; i++) {
			if ((this.answers[i] != null) && (!Objects.equals(this.answers[i].getContent(), ""))) {
				if (this.answers[i].getCorrect()) {
					counter++;
				}
			}
			if (counter >= 2) {
				return true;
			}
		}
		return false;
	}
	
	// returns number of trues
	public final int getNumOfTrues() {
		int counter = 0;
		for (int i = 0; i < 10; i++) {
			if ((this.answers[i] != null) && (!Objects.equals(this.answers[i].getContent(), ""))) {
				if (this.answers[i].getCorrect()) {
					counter++;
				}
			}
		}
		return counter;
	}

	// check if all answers are false
	public final boolean allFalse() {
		for (int i = 0; i < 10; i++) {
			if ((this.answers[i] != null) && (!Objects.equals(this.answers[i].getContent(), ""))) {
				if (this.answers[i].getCorrect()) {
					return false;
				}
			}
		}
		return true;
	}

}
