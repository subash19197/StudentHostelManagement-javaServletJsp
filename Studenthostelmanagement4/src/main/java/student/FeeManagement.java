package student;

import studentdb.DatabaseConnection1;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
@WebServlet("/FeeManagement")
public class FeeManagement extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try (Connection connection = DatabaseConnection1.getConnection()) {
            if ("add".equals(action)) {
                addFee(request, connection);
            } else if ("edit".equals(action)) {
                editFee(request, connection);
            } else if ("delete".equals(action)) {
                deleteFee(request, connection);
            }
            fetchFees(request, connection);
            request.getRequestDispatcher("/fee.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = DatabaseConnection1.getConnection()) {
            fetchFees(request, connection);
            request.getRequestDispatcher("/fee.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }

    private void addFee(HttpServletRequest request, Connection connection) throws SQLException {
        String sql = "INSERT INTO fees (student_id, amount, due_date, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(request.getParameter("student_id")));
            stmt.setDouble(2, Double.parseDouble(request.getParameter("amount")));
            stmt.setString(3, request.getParameter("due_date"));
            stmt.setString(4, request.getParameter("status"));
            stmt.executeUpdate();
        }
    }

    private void editFee(HttpServletRequest request, Connection connection) throws SQLException {
        String sql = "UPDATE fees SET student_id = ?, amount = ?, due_date = ?, status = ? WHERE fee_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(request.getParameter("student_id")));
            stmt.setDouble(2, Double.parseDouble(request.getParameter("amount")));
            stmt.setString(3, request.getParameter("due_date"));
            stmt.setString(4, request.getParameter("status"));
            stmt.setInt(5, Integer.parseInt(request.getParameter("fee_id")));
            stmt.executeUpdate();
        }
    }

    private void deleteFee(HttpServletRequest request, Connection connection) throws SQLException {
        String sql = "DELETE FROM fees WHERE fee_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(request.getParameter("fee_id")));
            stmt.executeUpdate();
        }
    }

    private void fetchFees(HttpServletRequest request, Connection connection) throws SQLException {
        String sql = "SELECT * FROM fees";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<Fee> fees = new ArrayList<>();
            while (rs.next()) {
                Fee fee = new Fee();
                fee.setFeeId(rs.getInt("fee_id"));
                fee.setStudentId(rs.getInt("student_id"));
                fee.setAmount(rs.getDouble("amount"));
                fee.setDueDate(rs.getString("due_date"));
                fee.setStatus(rs.getString("status"));
                fees.add(fee);
            }
            request.setAttribute("fees", fees);
        }
    }
}
