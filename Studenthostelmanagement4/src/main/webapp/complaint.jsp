
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="student.ComplaintManagement" %>
<%@ page import="student.Complaint" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Complaint Management</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #ff4d4d;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 80%;
            margin: 20px auto;
            padding: 20px;
            background-color: white;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }
        h2, h3 {
            text-align: center;
            color: #333;
        }
        form {
            margin-bottom: 30px;
            padding: 15px;
            background-color: #fafafa;
            border: 1px solid #ddd;
            border-radius: 8px;
        }
        label {
            display: block;
            margin: 10px 0 5px;
            font-weight: bold;
            color: #555;
        }
        input[type="text"], textarea {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 14px;
        }
        button {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 10px 15px;
            font-size: 16px;
            cursor: pointer;
            border-radius: 4px;
        }
        button:hover {
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
        th {
            background-color: #f2f2f2;
            color: #333;
            padding: 12px;
            text-align: left;
        }
        td {
            padding: 10px;
            text-align: left;
            color: #555;
        }
        td form {
            display: inline-block;
            margin-right: 5px;
        }
        td button {
            padding: 5px 10px;
            font-size: 14px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Complaint Management</h2>

       
        <form action="ComplaintManagement" method="post">
            <input type="hidden" name="action" value="add">
            <label for="user_name">User Name:</label>
            <input type="text" id="user_name" name="user_name" required>

            <label for="complaint_text">Complaint:</label>
            <textarea id="complaint_text" name="complaint_text" rows="4" required></textarea>

            <label for="status">Status:</label>
            <input type="text" id="status" name="status" required>

            <button type="submit">Add Complaint</button>
        </form>

        
        <h3>List of Complaints</h3>
        <table>
            <tr>
                <th>ID</th>
                <th>User Name</th>
                <th>Complaint</th>
                <th>Status</th>
                <th>Created At</th>
                <th>Actions</th>
            </tr>
            <% 
                List<Complaint> complaints = (List<Complaint>) request.getAttribute("complaints");
                if (complaints != null) {
                    for (Complaint complaint : complaints) {
            %>
            <tr>
                <td><%= complaint.getComplaintId() %></td>
                <td><%= complaint.getUserName() %></td>
                <td><%= complaint.getComplaintText() %></td>
                <td><%= complaint.getStatus() %></td>
                <td><%= complaint.getCreatedAt() %></td>
                <td>
                    <form action="ComplaintManagement" method="post">
                        <input type="hidden" name="action" value="edit">
                        <input type="hidden" name="complaint_id" value="<%= complaint.getComplaintId() %>">
                        <button type="submit">Edit</button>
                    </form>
                    <form action="ComplaintManagement" method="post">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="complaint_id" value="<%= complaint.getComplaintId() %>">
                        <button type="submit" onclick="return confirm('Are you sure?');">Delete</button>
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
