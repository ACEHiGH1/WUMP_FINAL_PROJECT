package dhima.gerald.wump_final_project.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Objects;

/**
 * This servlet sets the proper output for the page which informs the client about their successful registration.
 */
@WebServlet(name = "SuccessfulRegistrationServlet", value = "/SuccessfulRegistrationServlet")
public class SuccessfulRegistrationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String output = "";
        HttpSession session = request.getSession(); //Gets the session created in the signup form for the client.
        if(Objects.equals(session.getAttribute("accountType"), "professionalScientist")){ //Sets the output if the client has applied to be a professional scientist.
            output = "You have to wait to be approved as a Professional Scientist before you can actually log in.";
        }

        request.setAttribute("output",output); //Sets the output attribute for the page.
        getServletContext().getRequestDispatcher("/successfulRegistration.jsp").forward(request,response);
        //Forwards the request to the page.
    }
}
