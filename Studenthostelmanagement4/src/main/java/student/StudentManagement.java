package student;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
@WebServlet("/StudentManagement")
public class StudentManagement extends HttpServlet {
    private static final String ACTION_ADD = "add";
    private static final String ACTION_EDIT = "edit";
    private static final String ACTION_DELETE = "delete";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try (Connection connection = DatabaseConnection.getConnection()) {
            switch (action) {
                case ACTION_ADD:
                    addStudent(request, response, connection);
                    break;
                case ACTION_EDIT:
                    editStudent(request, response, connection);
                    break;
                case ACTION_DELETE:
                    deleteStudent(request, response, connection);
                    break;
                default:
                    response.sendRedirect("StudentManagement.jsp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error performing operation: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            fetchAndDisplayStudents(request, response, connection);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading students: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void addStudent(HttpServletRequest request, HttpServletResponse response, Connection connection) throws SQLException, ServletException, IOException {
        String sql = "INSERT INTO students (first_name, last_name, email, phone, address) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, request.getParameter("first_name"));
            stmt.setString(2, request.getParameter("last_name"));
            stmt.setString(3, request.getParameter("email"));
            stmt.setString(4, request.getParameter("phone"));
            stmt.setString(5, request.getParameter("address"));
            stmt.executeUpdate();
            fetchAndDisplayStudents(request, response, connection);
        }
    }

    private void editStudent(HttpServletRequest request, HttpServletResponse response, Connection connection) throws SQLException, ServletException, IOException {
        String sql = "UPDATE students SET first_name = ?, last_name = ?, email = ?, phone = ?, address = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, request.getParameter("first_name"));
            stmt.setString(2, request.getParameter("last_name"));
            stmt.setString(3, request.getParameter("email"));
            stmt.setString(4, request.getParameter("phone"));
            stmt.setString(5, request.getParameter("address"));
            stmt.setInt(6, Integer.parseInt(request.getParameter("id")));
            stmt.executeUpdate();
            fetchAndDisplayStudents(request, response, connection);
        }
    }

    private void deleteStudent(HttpServletRequest request, HttpServletResponse response, Connection connection) throws SQLException, ServletException, IOException {
        String sql = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(request.getParameter("id")));
            stmt.executeUpdate();
            fetchAndDisplayStudents(request, response, connection);
        }
    }

    private void fetchAndDisplayStudents(HttpServletRequest request, HttpServletResponse response, Connection connection) throws SQLException, ServletException, IOException {
        String sql = "SELECT * FROM students";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            List<Student> students = new ArrayList<>();
            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setFirstName(rs.getString("first_name"));
                student.setLastName(rs.getString("last_name"));
                student.setEmail(rs.getString("email"));
                student.setPhone(rs.getString("phone"));
                student.setAddress(rs.getString("address"));
                students.add(student);
            }
            request.setAttribute("students", students);
            request.getRequestDispatcher("/StudentManagement.jsp").forward(request, response);
        }
    }
}
