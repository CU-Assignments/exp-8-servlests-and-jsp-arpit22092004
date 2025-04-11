//JSP Page (student_portal.jsp)

<%@ page import="java.sql.*" %>
<html>
<head>
    <title>Student Attendance</title>
</head>
<body>
    <form action="AttendanceServlet" method="post">
        Student Name: <input type="text" name="name" required><br>
        Roll No: <input type="text" name="roll" required><br>
        Attendance: <select name="status">
            <option value="Present">Present</option>
            <option value="Absent">Absent</option>
        </select><br>
        <input type="submit" value="Submit">
    </form>

    <h2>Attendance Records</h2>
    <table border="1">
        <tr><th>Name</th><th>Roll No</th><th>Status</th></tr>
        <%
            try (Connection conn = DBConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM attendance")) {
                
                while (rs.next()) {
                    out.println("<tr><td>" + rs.getString("name") + "</td><td>" + rs.getString("roll") 
                        + "</td><td>" + rs.getString("status") + "</td></tr>");
                }
            } catch (Exception e) {
                out.println("<p>Error: " + e.getMessage() + "</p>");
            }
        %>
    </table>
</body>
</html>
          
//Servlet to Handle Attendance (AttendanceServlet.java)

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AttendanceServlet")
public class AttendanceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String roll = request.getParameter("roll");
        String status = request.getParameter("status");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO attendance (name, roll, status) VALUES (?, ?, ?)")) {
            stmt.setString(1, name);
            stmt.setString(2, roll);
            stmt.setString(3, status);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        response.sendRedirect("student_portal.jsp");
    }
}

//Database Table Structure


CREATE TABLE employees (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50),
    department VARCHAR(50)
);

CREATE TABLE attendance (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50),
    roll VARCHAR(20),
    status VARCHAR(10)
);
