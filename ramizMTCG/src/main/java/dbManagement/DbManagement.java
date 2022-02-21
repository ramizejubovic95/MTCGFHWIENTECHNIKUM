package dbManagement;

import com.sun.tools.jconsole.JConsoleContext;
import lombok.NoArgsConstructor;
import user.User;
import java.sql.*;

@NoArgsConstructor
public class DbManagement {
    private final String dbInfo = "jdbc:postgresql://localhost:5432/postgres";
    private final String dbName = "ramiz";
    private final String dbPassword = "password";
    private Connection c = null;

    public void open() throws SQLException
    {
        this.c = DriverManager.getConnection(this.dbInfo, this.dbName, this.dbPassword);
    }

    public void close() throws SQLException
    {
        this.c.close();
    }

    public boolean checkIfUserExist(User user) throws SQLException {
        String sql = "SELECT username FROM USERS WHERE username = ?";
        ResultSet result = null;
        this.open();

        try (PreparedStatement pstmt = this.c.prepareStatement(sql))
        {
            pstmt.setString(1, user.getUsername());
            result = pstmt.executeQuery();
            if (result.next())
            {
                this.close();
                return false;
            }
            else
            {
                this.close();
                return true;
            }
        }
        catch (SQLException throwables)
        {
            this.close();
            return false;
        }
    }

    public boolean addUser(User user) throws SQLException {
        if (!checkIfUserExist(user))
        {
            System.out.println("USER EXISTS\n");
            return false;
        }

        this.open();
        String SQL = "INSERT INTO USERS(username, password)" + "VALUES(?,?)";

        try (PreparedStatement pstmt = this.c.prepareStatement(SQL))
        {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, String.valueOf(user.getPassword().hashCode()));
            pstmt.executeUpdate();
        } catch (Exception ex) { return false; }
        this.close();

        System.out.println("User added to DB\n");
        return true;
    }

    public User passwordCheck(User user) throws SQLException {
        String sql = "SELECT * FROM USERS WHERE username = ? AND password = ?";
        ResultSet result = null;

        this.open();
        try (PreparedStatement pstmt = this.c.prepareStatement(sql))
        {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, String.valueOf(user.getPassword().hashCode()));
            result = pstmt.executeQuery();

            if (result.next())
            {
                User currentUser = new User(result.getString("username"), result.getString("password"));
                this.close();
                return currentUser;
            }
            throw new SQLException();
        }
        catch (SQLException throwables) { this.close(); System.out.println("RESUT WRONG: "); return null; }

    }

    public User login(User user) throws SQLException
    {
        User currentUser;
        if (checkIfUserExist(user)) return null;
        if ((currentUser = passwordCheck(user)) == null) return null;

        return currentUser;
    }
}
