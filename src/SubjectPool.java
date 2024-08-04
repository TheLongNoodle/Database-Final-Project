import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SubjectPool implements Serializable {

    private static Statement stmt;

    // constructor
    public SubjectPool(Connection con) throws SQLException {
        // variables
        stmt = con.createStatement();
    }

    // usual get/set commands
    public int getSubjectsLen(int tid) throws SQLException {
        String query = "SELECT COUNT(*) as total FROM subject_teacher WHERE tid=" + tid;
        ResultSet resultSet = stmt.executeQuery(query);
        return resultSet.next() ? resultSet.getInt("total") : 0;
    }

    public String getSubjectName(int sid) throws SQLException {
        String query = "SELECT name FROM subject WHERE sid = " + sid;
        ResultSet resultSet = stmt.executeQuery(query);
        return resultSet.next() ? resultSet.getString("name") : "";
    }

    public void setSubjectName(int sid, String name) throws SQLException {
        String query = "UPDATE subject SET name = '" + name + "' WHERE sid = " + sid;
        stmt.executeUpdate(query);
    }

    // adding subject
    public int addSubject(String name, int tid) throws SQLException {
        String query = "INSERT INTO subject (name) VALUES ('" + name + "')";
        stmt.executeUpdate(query);
        query = "SELECT sid FROM subject WHERE name = '" + name + "'";
        ResultSet resultSet = stmt.executeQuery(query);
        int sid = resultSet.next() ? resultSet.getInt("sid") : 0;
        query = "INSERT INTO subject_teacher (sid, tid) VALUES (" + sid + ", " + tid + ")";
        stmt.executeUpdate(query);
        return sid;
    }

    // data to string (only names)
    @Override
    public String toString() {
        String query = "SELECT * FROM subject";
        return toStringQuery(query);
    }

    public String toString(int tid) {
        String query = "SELECT * FROM subject JOIN subject_teacher ON subject.sid = subject_teacher.sid WHERE subject_teacher.tid = " + tid;
        return toStringQuery(query);
    }

    public String toStringQuery(String query){
        StringBuilder str = new StringBuilder();
        ResultSet resultSet;
        try {
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                str.append(resultSet.getInt("sid")).append(") ").append(resultSet.getString("name")).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return str.toString();
    }
}
