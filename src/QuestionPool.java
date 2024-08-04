import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.Serializable;

public class QuestionPool implements Serializable {

    // variables
    private int sid;
    private static Statement stmt;

    // constructor
    public QuestionPool(Connection con) throws SQLException {
        stmt = con.createStatement();
    }

    // usual get/set commands
    public int getQuestionsLen() throws SQLException {
        String query = "SELECT COUNT(*) as total FROM question WHERE sid = " + sid;
        ResultSet resultSet = stmt.executeQuery(query);
        return resultSet.next() ? resultSet.getInt("total") : 0;
    }

    public void setAnswer(String answer, int qid) throws SQLException {
        int aid = findAnswer(answer);
        String query = "UPDATE open_question SET aid = " + aid + " WHERE qid = " + qid + "AND sid = " + sid;
        stmt.executeUpdate(query);
    }

    public void setSID(int sid) {
        this.sid = sid;
    }

    // has checks
    public Boolean hasQuestions() throws SQLException {
        String query = "SELECT COUNT(*) as total FROM question WHERE sid = " + sid;
        ResultSet resultSet = stmt.executeQuery(query);
        int total = resultSet.next() ? resultSet.getInt("total") : 0;
        return total > 0;
    }

    public Boolean hasOpenQuestions() throws SQLException {
        String query = "SELECT COUNT(*) as total FROM question WHERE is_selection = FALSE AND sid = " + sid;
        ResultSet resultSet = stmt.executeQuery(query);
        int total = resultSet.next() ? resultSet.getInt("total") : 0;
        return total > 0;
    }

    public Boolean hasSelectionQuestions() throws SQLException {
        String query = "SELECT COUNT(*) as total FROM question WHERE is_selection = TRUE AND sid = " + sid;
        ResultSet resultSet = stmt.executeQuery(query);
        int total = resultSet.next() ? resultSet.getInt("total") : 0;
        return total > 0;
    }

    // adding question
    public void addQuestion(String content, int difficulty, String answer, Boolean bool) throws SQLException {
        String query = "INSERT INTO question (sid, difficulty, question_text, is_selection) VALUES (" + sid + ", " + difficulty + ", '" + content + "', " + bool + ")";
        stmt.executeUpdate(query);
        if (!bool) {
            int aid = findAnswer(answer);
            query = "SELECT qid FROM question WHERE question_text = '" + content + "' AND sid = " + sid;
            ResultSet resultSet = stmt.executeQuery(query);
            resultSet.next();
            int qid = resultSet.getInt("qid");
            query = "INSERT INTO open_question (qid, aid) VALUES (" + qid + ", " + aid + ")";
            stmt.executeUpdate(query);
        }

    }

    // look if answer already exists
    public int findAnswer(String answer) throws SQLException {
        String query = "SELECT aid FROM answer WHERE answer_text = '" + answer + "' AND sid = " + sid;
        ResultSet resultSet = stmt.executeQuery(query);
        int aid;
        if (resultSet.next()) {
            System.out.println("Found existing answer in database!");
        } else {
            System.out.println("Didn't find answer in database, crating a new one...");
            query = "INSERT INTO answer (sid, answer_text) VALUES (" + sid + ", '" + answer + "')";
            stmt.executeUpdate(query);
            query = "SELECT aid FROM answer WHERE answer_text = '" + answer + "' AND sid = " + sid;
            resultSet = stmt.executeQuery(query);
            resultSet.next();
        }
        aid = resultSet.getInt("aid");
        return aid;
    }

    // removing question
    public void deleteQuestion(int qid) throws SQLException {
        String query = "SELECT COUNT(*) as total FROM exam_question WHERE qid = " + qid;
        ResultSet resultSet = stmt.executeQuery(query);
        int total = resultSet.next() ? resultSet.getInt("total") : 0;
        if (total > 0) {
            System.out.println("Can't remove question as it's a part of an exam");
            return;
        }
        query = "Delete from question WHERE qid = " + qid;
        stmt.executeUpdate(query);
    }

    // adding answer to selection question
    public void addAnswer(int qid, int aid, Boolean bool) throws SQLException {
        String query = "INSERT INTO selection_question (qid, aid, is_correct) VALUES (" + qid + ", " + aid + ", " + bool + ")";
        stmt.executeUpdate(query);
    }

    // removing answer from question
    public void deleteAnswer(int qid, int aid) throws SQLException {
        String query = "DELETE FROM selection_question WHERE qid = " + qid + " AND aid = " + aid;
        stmt.executeUpdate(query);
    }

    // changing question content
    public void changeQuestionContent(int qid, String content) throws SQLException {
        String query = "UPDATE question SET question_text = '" + content + "' WHERE qid = " + qid + "AND sid = " + sid;
        stmt.executeUpdate(query);
    }

    //checking if a question is open
    public Boolean isOpen(int qid) throws SQLException {
        String query = "SELECT is_selection FROM question WHERE qid = " + qid;
        ResultSet resultSet = stmt.executeQuery(query);
        resultSet.next();
        return !resultSet.getBoolean("is_selection");
    }

    // data to string (only questions)
    @Override
    public String toString() {
        String query = "SELECT * FROM question WHERE sid = " + sid;
        StringBuilder str = new StringBuilder();
        try {
            ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                str.append("Question ").append(resultSet.getInt("qid")).append(": ").append(resultSet.getString("question_text")).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return str.toString();
    }

    // data to string (including answers)
    public String toStringFull(Connection con) {
        try {
            String query = "SELECT * FROM question WHERE sid = " + sid;
            StringBuilder str = new StringBuilder();
            Statement stmt2 = con.createStatement();
            ResultSet resultSet = stmt2.executeQuery(query);
            while (resultSet.next()) {
                str.append(toStringSpecificFull(resultSet.getInt("qid")));
            }
            stmt2.close();
            return str.toString();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return "";
    }

    // data to string (Only selection)
    public String toStringSelection() {
        String query = "SELECT * FROM question WHERE sid = " + sid;
        StringBuilder str = new StringBuilder();
        try {
            ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                if (resultSet.getBoolean("is_selection")) {
                    str.append("Question ").append(resultSet.getInt("qid")).append(" (").append(getDifficulty(resultSet.getInt("difficulty"))).append("): ").append(resultSet.getString("question_text")).append("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return str.toString();
    }

    // data to string (Only open)
    public String toStringOpen() {
        String query = "SELECT * FROM question WHERE sid = " + sid;
        StringBuilder str = new StringBuilder();
        try {
            ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                if (!resultSet.getBoolean("is_selection")) {
                    str.append("Question ").append(resultSet.getInt("qid")).append(" (").append(getDifficulty(resultSet.getInt("difficulty"))).append("): ").append(resultSet.getString("content")).append("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return str.toString();
    }

    // specific question to string
    public String toStringSpecificFull(int qid) {
        try {
            String query = "SELECT * FROM question WHERE sid = " + sid + " AND qid = " + qid;
            StringBuilder str = new StringBuilder();
            ResultSet resultSet = stmt.executeQuery(query);
            resultSet.next();
            str.append("Question ").append(qid).append(" (").append(getDifficulty(resultSet.getInt("difficulty"))).append("): ").append(resultSet.getString("question_text")).append("\n");
            if (resultSet.getBoolean("is_selection")) {
                query = "SELECT selection_question.is_correct, answer.answer_text FROM answer JOIN selection_question ON answer.aid = selection_question.aid WHERE selection_question.qid = " + qid + " AND answer.sid = " + sid;
                ResultSet tempSet = stmt.executeQuery(query);
                int i = 1;
                str.append("Answers:").append("\n");
                while (tempSet.next()) {
                    str.append(i).append(") ").append(tempSet.getString("answer_text")).append(" [").append(tempSet.getBoolean("is_correct")).append("]\n");
                    System.out.println(str);
                    i++;
                }
            } else {
                query = "SELECT answer.answer_text FROM open_question JOIN answer ON open_question.aid = answer.aid WHERE open_question.qid = " + qid + " AND answer.sid = " + sid;
                ResultSet tempSet = stmt.executeQuery(query);
                tempSet.next();
                str.append("Answer: ").append(tempSet.getString("answer_text")).append("\n");
            }
            str.append("\n");
            return str.toString();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return "";
    }

    public String toStringSpecific(int qid) {
        try {
            String query = "SELECT * FROM question WHERE sid = " + sid + " AND qid = " + qid;
            StringBuilder str = new StringBuilder();
            ResultSet resultSet = stmt.executeQuery(query);
            resultSet.next();
            str.append("Question ").append(qid).append(" (").append(getDifficulty(resultSet.getInt("difficulty"))).append("): ").append(resultSet.getString("question_text")).append("\n");
            if (resultSet.getBoolean("is_selection")) {
                query = "SELECT selection_question.is_correct, answer.answer_text FROM answer JOIN selection_question ON answer.aid = selection_question.aid WHERE selection_question.qid = " + qid + " AND answer.sid = " + sid;
                ResultSet tempSet = stmt.executeQuery(query);
                int i = 1;
                str.append("Answers:").append("\n");
                while (tempSet.next()) {
                    str.append(i).append(") ").append(tempSet.getString("answer_text")).append("\n");
                    i++;
                }
            } else {
                str.append("Answer:").append("\n");
            }
            return str.toString();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return "";
    }

    // print question answers
    public String printAnswers(int qid) throws SQLException {
        StringBuilder str = new StringBuilder();
        String query = "SELECT selection_question.is_correct, answer.answer_text, answer.aid, question.question_text, question.qid FROM selection_question JOIN answer ON selection_question.aid = answer.aid JOIN question on question.qid = selection_question.qid WHERE selection_question.qid = " + qid;
        ResultSet resultSet = stmt.executeQuery(query);
        str.append("Question ").append(resultSet.getInt("question.qid")).append(": ").append(resultSet.getString("question.content")).append("\n");
        while (resultSet.next()) {
            str.append(resultSet.getString("answer.aid")).append(") ").append(resultSet.getString("answer.answer_text")).append(": ").append(resultSet.getBoolean("selection_question.is_correct")).append("\n");
        }
        return str.toString();
    }

    // check if a question has answers
    public boolean hasAnswers(int qid) throws SQLException {
        String query = "SELECT aid FROM selection_question WHERE qid = " + qid;
        ResultSet resultSet = stmt.executeQuery(query);
        return resultSet.next();
    }

    // get difficulty as a string
    private String getDifficulty(int difficulty) {
        return switch (difficulty) {
            case 1 -> "easy";
            case 2 -> "medium";
            case 3 -> "hard";
            default -> "unknown"; // Handle unexpected values
        };
    }
}
