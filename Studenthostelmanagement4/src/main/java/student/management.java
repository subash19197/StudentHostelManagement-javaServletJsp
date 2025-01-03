package student;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
@WebServlet("/management")
@SuppressWarnings("serial")
public class management extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      
        String action = request.getParameter("management");

        
        if (action != null) {
            switch (action) {
                case "container1":
                    response.sendRedirect("StudentManagement.jsp");
                    break;
                case "container2":
                    response.sendRedirect("room.jsp");
                    break;
                case "container3":
                    response.sendRedirect("fee.jsp");
                    break;
                case "container4":
                   response.sendRedirect("complaint.jsp");
                    break;
                default:
                   response.sendRedirect("login.html");
                    break;
            }
        } else {
            response.sendRedirect("login.html");
        }
    }
}
