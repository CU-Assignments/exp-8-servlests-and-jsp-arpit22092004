//JDBC Configuration (DBConnection.java)

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/company";
        String user = "root";
        String pass = "password";
        return DriverManager.getConnection(url, user, pass);
    }
}

//Servlet (EmployeeServlet.java)

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/EmployeeServlet")
public class EmployeeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String id = request.getParameter("id");
        String query = (id != null && !id.isEmpty()) 
            ? "SELECT * FROM employees WHERE id = ?" 
            : "SELECT * FROM employees";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            if (id != null && !id.isEmpty()) {
                stmt.setInt(1, Integer.parseInt(id));
            }

            ResultSet rs = stmt.executeQuery();
            out.println("<h2>Employee List</h2>");
            out.println("<table border='1'><tr><th>ID</th><th>Name</th><th>Department</th></tr>");
            
            while (rs.next()) {
                out.println("<tr><td>" + rs.getInt("id") + "</td><td>" + rs.getString("name") 
                    + "</td><td>" + rs.getString("department") + "</td></tr>");
            }
            out.println("</table>");
        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
        }
    }
}


//Employee Search Form (employee.html)

<!DOCTYPE html>
<html>
<head>
    <title>Employee Search</title>
</head>
<body>
    <form action="EmployeeServlet" method="get">
        Enter Employee ID: <input type="text" name="id">
        <input type="submit" value="Search">
    </form>
</body>
</html>
