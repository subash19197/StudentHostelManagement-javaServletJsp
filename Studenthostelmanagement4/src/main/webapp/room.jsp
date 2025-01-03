<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="student.RoomManagement" %>
<%@ page import="student.Room" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Room Management</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #0acd93;
        }
        .container {
            width: 80%;
            margin: 0 auto;
            padding: 20px;
            background-color: white;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
        }
        h2 {
            text-align: center;
            color: #333;
        }
        .form-container {
            margin-bottom: 30px;
            padding: 20px;
            background-color: #f9f9f9;
            border-radius: 8px;
            box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
        }
        form {
            display: flex;
            flex-direction: column;
            gap: 15px;
        }
        label {
            font-size: 14px;
            font-weight: bold;
        }
        input[type="text"], input[type="number"] {
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 14px;
        }
        input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 10px;
            cursor: pointer;
            font-size: 16px;
        }
        input[type="submit"]:hover {
            background-color: #45a049;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        table, th, td {
            border: 1px solid #ddd;
        }
        th, td {
            padding: 12px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        .actions form {
            display: inline-block;
            margin-right: 10px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Room Management</h2>

        
        <div class="form-container">
            <form action="RoomManagement" method="post">
                <input type="hidden" name="action" value="add">
                <h3>Add New Room</h3>

                <label for="room_number">Room Number:</label>
                <input type="text" id="room_number" name="room_number" required>

                <label for="room_type">Room Type:</label>
                <input type="text" id="room_type" name="room_type" required>

                <label for="availability">Availability:</label>
                <input type="text" id="availability" name="availability" required>

                <label for="rent">Rent:</label>
                <input type="number" id="rent" name="rent" required>

                <input type="submit" value="Add Room">
            </form>
        </div>

       
        <h3>List of Rooms</h3>
        <table>
            <tr>
                <th>Room ID</th>
                <th>Room Number</th>
                <th>Room Type</th>
                <th>Availability</th>
                <th>Rent</th>
                <th>Actions</th>
            </tr>

            <% 
            List<Room> rooms = (List<Room>) request.getAttribute("rooms");
           
             if (rooms != null) {
                 for (Room room : rooms) {
            %>
                <tr>
                    <td><%= room.getId() %></td>
                    <td><%= room.getRoomNumber() %></td>
                    <td><%= room.getRoomType() %></td>
                    <td><%= room.getAvailability() %></td>
                    <td><%= room.getRent() %></td>
                    <td class="actions">
                        <form action="RoomManagement" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="edit">
                            <input type="hidden" name="room_id" value="<%= room.getId() %>">
                            <input type="submit" value="Edit">
                        </form>
                        <form action="RoomManagement" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="room_id" value="<%= room.getId() %>">
                            <input type="submit" value="Delete" onclick="return confirm('Are you sure?');">
                        </form>
                    </td>
                </tr>
            <%  
                    }
                }
            %>
        </table>
    </div>
</body>
</html>
    