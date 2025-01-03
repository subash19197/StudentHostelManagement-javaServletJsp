<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="student.FeeManagement" %>
<%@ page import="student.Fee" %>
<%@ page import="studentdb.DatabaseConnection1" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Fee Management</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #e3f2fd;
        }
        .container {
            max-width: 800px;
            margin: 20px auto;
            padding: 20px;
            background: #fff;
            box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.2);
        }
        h2 {
            text-align: center;
        }
        form, table {
            margin-top: 20px;
            width: 100%;
        }
        form label, input, select {
            display: block;
            width: 100%;
            margin-bottom: 10px;
        }
        input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 10px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #45a049;
        }
        table {
            border-collapse: collapse;
            width: 100%;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f4f4f4;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Fee Management</h2>
        <form action="FeeManagement" method="post">
            <input type="hidden" name="action" value="add">
            <label>Student ID:</label>
            <input type="number" name="student_id" required>
            <label>Amount:</label>
            <input type="number" name="amount" step="0.01" required>
            <label>Due Date:</label>
            <input type="date" name="due_date" required>
            <label>Status:</label>
            <select name="status" required>
                <option value="Paid">Paid</option>
                <option value="Unpaid">Unpaid</option>
            </select>
            <input type="submit" value="Add Fee">
        </form>
        <h3>Fee Records</h3>
        <table>
            <thead>
                <tr>
                    <th>Fee ID</th>
                    <th>Student ID</th>
                    <th>Amount</th>
                    <th>Due Date</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<Fee> fees = (List<Fee>) request.getAttribute("fees");
                    if (fees != null && !fees.isEmpty()) {
                        for (Fee fee : fees) {
                %>
                <tr>
                    <td><%= fee.getFeeId() %></td>
                    <td><%= fee.getStudentId() %></td>
                    <td><%= fee.getAmount() %></td>
                    <td><%= fee.getDueDate() %></td>
                    <td><%= fee.getStatus() %></td>
                    <td>
                        <form action="FeeManagement" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="edit">
                            <input type="hidden" name="fee_id" value="<%= fee.getFeeId() %>">
                            <input type="submit" value="Edit">
                        </form>
                        <form action="FeeManagement" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="fee_id" value="<%= fee.getFeeId() %>">
                            <input type="submit" value="Delete" onclick="return confirm('Are you sure?');">
                        </form>
                    </td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr>
                    <td colspan="6">No fee records found.</td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
</body>
</html>
