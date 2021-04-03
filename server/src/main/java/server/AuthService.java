package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;

public class AuthService {
    private static final Connection dbConn;
    private static final String JDBC_DRIVER = "org.sqlite.JDBC";
    private static final String DB_URL = "jdbc:sqlite:auth.db";
    private static final String sql = "SELECT * FROM users WHERE login = ? and password = ?";


    static
    {
        try
        {
            Class.forName(JDBC_DRIVER);
            dbConn = getConnection(DB_URL);
        }
        catch (ClassNotFoundException | SQLException e)
        {
            throw new RuntimeException(e);
        }
    }


    public boolean autorize(String login, String password)
            throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement(sql);
        ps.setString(1, login);
        ps.setString(2, password);

        ResultSet rs = ((PreparedStatement) ps).executeQuery();

        if (rs.next())
            return true;

        return false;
    }
}
