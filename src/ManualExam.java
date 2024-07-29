import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class ManualExam implements Examable {
	// variables
	protected QuestionPool questionPool;

	// constructor
	public ManualExam(QuestionPool questionPool) {
		this.questionPool = questionPool;
	}

	// creating exam
	public void createExam() throws ExamException{ //shell
		createExam(this.questionPool);
	}
	
	@Override
	public void createExam(QuestionPool questionPool) {
		// collecting date
		LocalDate date = LocalDate.now();
		LocalTime time = LocalTime.now();
		String today = date.getYear() + "_" + date.getMonthValue() + "_" + date.getDayOfMonth() + "_" + time.getHour()
				+ "_" + time.getMinute();

		// opening files
		try {
			File exam = new File("src\\exam_" + today + ".txt");
			System.out.println("Created exam file at: " + exam.getAbsolutePath());
			File solution = new File("src\\solution_" + today + ".txt");
			System.out.println("Created solution file at: " + solution.getAbsolutePath());
			PrintWriter pwExam = new PrintWriter(exam);
			PrintWriter pwSolution = new PrintWriter(solution);
			SelectionQuestion[] questions = this.questionPool.getQuestions();

			// inserting questions
			for (int i = 0; i < questions.length-1; i++) {
				if (!(questions[i] instanceof OpenQuestion)) {
					// writing selection questions
					pwExam.println("Question: " + questions[i].getContent());
					pwSolution.println("Question: " + questions[i].getContent() + "\n(id: "
							+ questions[i].getId() + ", difficulty: " + questions[i].getDifficulty() + ")");
					for (int j = 0; j < 12; j++) {
						if ((questions[i].getAnswers()[j] != null)
								&& (!Objects.equals(questions[i].getAnswers()[j].getContent(), ""))) {
							if ((questions[i].getAnswers()[10].getCorrect()) && (j < 10)) {
								questions[i].getAnswers()[j].setCorrect(false);
							}
							pwExam.println("* " + questions[i].getAnswers()[j].getContent());
							pwSolution.println("* " + questions[i].getAnswers()[j].toString());
						}
					}
					pwExam.println();
					pwSolution.println();
				} else {
					// writing open questions
					pwExam.println("Question: " + questions[i].getContent() + "\n");
					pwSolution.println(questions[i].toString());
				}
			}
			pwExam.close();
			pwSolution.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
