package student;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ComplaintManagement")
public class ComplaintManagement extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/complaint";
        String userName = "root";
        String password = "9342864391";
        return DriverManager.getConnection(url, userName, password);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Complaint> complaints = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String sql = "SELECT * FROM complaints";
            try (PreparedStatement stmt = connection.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Complaint complaint = new Complaint();
                    complaint.setComplaintId(rs.getInt("complaint_id"));
                    complaint.setUserName(rs.getString("user_name"));
                    complaint.setComplaintText(rs.getString("complaint_text"));
                    complaint.setStatus(rs.getString("status"));
                    complaint.setCreatedAt(rs.getString("created_at"));
                    complaints.add(complaint);
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Error fetching complaints", e);
        }
        request.setAttribute("complaints", complaints);
        RequestDispatcher dispatcher = request.getRequestDispatcher("complaint.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            addComplaint(request, response);
        } else if ("edit".equals(action)) {
            editComplaint(request, response);
        } else if ("delete".equals(action)) {
            deleteComplaint(request, response);
        } else {
            response.sendRedirect("ComplaintManagement");
        }
    }

    private void addComplaint(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("user_name");
        String complaintText = request.getParameter("complaint_text");
        String status = request.getParameter("status");

        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO complaints (user_name, complaint_text, status) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, userName);
                stmt.setString(2, complaintText);
                stmt.setString(3, status);
                stmt.executeUpdate();
            }
            response.sendRedirect("ComplaintManagement");
        } catch (SQLException e) {
            throw new ServletException("Error adding complaint", e);
        }
    }

    private void editComplaint(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int complaintId = Integer.parseInt(request.getParameter("complaint_id"));
        String userName = request.getParameter("user_name");
        String complaintText = request.getParameter("complaint_text");
        String status = request.getParameter("status");

        try (Connection connection = getConnection()) {
            String sql = "UPDATE complaints SET user_name = ?, complaint_text = ?, status = ? WHERE complaint_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, userName);
                stmt.setString(2, complaintText);
                stmt.setString(3, status);
                stmt.setInt(4, complaintId);
                stmt.executeUpdate();
            }
            response.sendRedirect("ComplaintManagement");
        } catch (SQLException e) {
            throw new ServletException("Error editing complaint", e);
        }
    }

    private void deleteComplaint(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int complaintId = Integer.parseInt(request.getParameter("complaint_id"));

        try (Connection connection = getConnection()) {
            String sql = "DELETE FROM complaints WHERE complaint_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, complaintId);
                stmt.executeUpdate();
            }
            response.sendRedirect("ComplaintManagement");
        } catch (SQLException e) {
            throw new ServletException("Error deleting complaint", e);
        }
    }
}
