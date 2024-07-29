import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AnswerPool implements Serializable {

    // variables
    private int sid;
    private Connection con;
    private static Statement stmt;

    // constructor
    public AnswerPool(Connection con) throws SQLException {
        this.con = con;
        stmt = this.con.createStatement();
    }

    // usual get/set commands
    public int getAnswersLen() throws SQLException {
        String query = "SELECT COUNT(*) as total FROM answer WHERE sid = " + sid;
        ResultSet resultSet = stmt.executeQuery(query);
        return resultSet.next() ? resultSet.getInt("total") : 0;
    }

    public void setSID(int sid) {
        this.sid = sid;
    }

    // has checks
    public Boolean hasAnswers() throws SQLException {
        String query = "SELECT COUNT(*) as total FROM answer WHERE sid = " + sid;
        ResultSet resultSet = stmt.executeQuery(query);
        int total = resultSet.next() ? resultSet.getInt("total") : 0;
        return total > 0;
    }

    // adding answer
    public void addAnswer(String content) throws SQLException {
        String query = "INSERT INTO answer(sid, answer_text) VALUES (" + sid + ", '" + content + "')";
        stmt.executeUpdate(query);
    }

    // removing answer
    public void deleteAnswer(int aid) throws SQLException {
        String query = "SELECT COUNT(*) as total FROM open_question WHERE aid = " + aid;
        ResultSet resultSet = stmt.executeQuery(query);
        int total1 = resultSet.next() ? resultSet.getInt("total") : 0;
        query = "SELECT COUNT(*) as total FROM selection_question WHERE aid = " + aid;
        resultSet = stmt.executeQuery(query);
        int total2 = resultSet.next() ? resultSet.getInt("total") : 0;
        if (total1 > 0 || total2 > 0) {
            System.out.println("Can't remove answer as it's a part of a question");
            return;
        }
        query = "DELETE FROM answer WHERE aid = " + aid;
        stmt.executeUpdate(query);
    }

    // changing answer content
    public void changeAnswerContent(int aid, String content) throws SQLException {
        String query = "UPDATE answer SET answer_text = '" + content + "' WHERE aid = " + aid + "AND sid = " + sid;
        stmt.executeUpdate(query);
    }

    // data to string
    public String toString() {
        StringBuilder str = new StringBuilder();
        String query = "SELECT aid, answer_text FROM answer WHERE sid = " + sid;
        try {
            ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                str.append("Answer ").append(resultSet.getInt("aid")).append(": ").append(resultSet.getString("answer_text")).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return str.toString();
    }
}
