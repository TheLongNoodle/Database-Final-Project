import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TeacherPool {
    private static Statement stmt;

    // constructor
    public TeacherPool(Connection con) throws SQLException {
        stmt = con.createStatement();
    }

    // usual get/set commands
    public int getTeachersLen() throws SQLException {
        String query = "SELECT COUNT(*) as total FROM teacher";
        ResultSet resultSet = stmt.executeQuery(query);
        return resultSet.next() ? resultSet.getInt("total") : 0;
    }

    // adding subject
    public int addTeacher(String name, String address, int exp) throws SQLException {
        String query = "INSERT INTO teacher (name, address, years_of_exp) VALUES ('" + name + "', '" + address + "', " + exp + ")";
        stmt.executeUpdate(query);
        query = "SELECT tid FROM teacher WHERE name = '" + name + "' AND years_of_exp = '" + exp + "' AND address = '" + address + "'";
        ResultSet resultSet = stmt.executeQuery(query);
        return resultSet.next() ? resultSet.getInt("tid") : 0;
    }

    // data to string (only names)
    @Override
    public String toString() {

        StringBuilder str = new StringBuilder();
        String query = "SELECT * FROM teacher";
        ResultSet resultSet;
        try {
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                str.append(resultSet.getInt("tid")).append(") ").append(resultSet.getString("name")).append(", Lives in ").append(resultSet.getString("address")).append(", Has ").append(resultSet.getInt("years_of_exp")).append(" Years of experience\n");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return str.toString();
    }
}
