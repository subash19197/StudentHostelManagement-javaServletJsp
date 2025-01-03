package student;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/RoomManagement")
public class RoomManagement extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/room";
        String userName = "root";
        String password = "9342864391";
        return DriverManager.getConnection(url, userName, password);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Room> rooms = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String sql = "SELECT * FROM rooms";
            try (PreparedStatement stmt = connection.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Room room = new Room();
                    room.setId(rs.getInt("room_id"));
                    room.setRoomNumber(rs.getString("room_number"));
                    room.setRoomType(rs.getString("room_type"));
                    room.setAvailability(rs.getString("availability"));
                    room.setRent(rs.getDouble("rent"));
                    rooms.add(room);
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Error fetching rooms", e);
        }
        request.setAttribute("rooms", rooms);
        RequestDispatcher dispatcher = request.getRequestDispatcher("room.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            addRoom(request, response);
        } else if ("edit".equals(action)) {
            editRoom(request, response);
        } else if ("delete".equals(action)) {
            deleteRoom(request, response);
        } else {
            response.sendRedirect("room.jsp");
        }
    }

    private void addRoom(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String roomNumber = request.getParameter("room_number");
        String roomType = request.getParameter("room_type");
        String availability = request.getParameter("availability");
        String rent = request.getParameter("rent");

        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO rooms (room_number, room_type, availability, rent) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, roomNumber);
                stmt.setString(2, roomType);
                stmt.setString(3, availability);
                stmt.setDouble(4, Double.parseDouble(rent));
                stmt.executeUpdate();
            }
            response.sendRedirect("RoomManagement"); 
        } catch (SQLException e) {
            throw new ServletException("Error adding room", e);
        }
    }

    private void editRoom(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int roomId = Integer.parseInt(request.getParameter("room_id"));
        String roomNumber = request.getParameter("room_number");
        String roomType = request.getParameter("room_type");
        String availability = request.getParameter("availability");
        String rent = request.getParameter("rent");

        try (Connection connection = getConnection()) {
            String sql = "UPDATE rooms SET room_number = ?, room_type = ?, availability = ?, rent = ? WHERE room_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, roomNumber);
                stmt.setString(2, roomType);
                stmt.setString(3, availability);
                stmt.setDouble(4, Double.parseDouble(rent));
                stmt.setInt(5, roomId);
                stmt.executeUpdate();
            }
            response.sendRedirect("RoomManagement"); 
        } catch (SQLException e) {
            throw new ServletException("Error editing room", e);
        }
    }

    private void deleteRoom(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int roomId = Integer.parseInt(request.getParameter("room_id"));

        try (Connection connection = getConnection()) {
            String sql = "DELETE FROM rooms WHERE room_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, roomId);
                stmt.executeUpdate();
            }
            response.sendRedirect("RoomManagement"); 
        } catch (SQLException e) {
            throw new ServletException("Error deleting room", e);
        }
    }
}
