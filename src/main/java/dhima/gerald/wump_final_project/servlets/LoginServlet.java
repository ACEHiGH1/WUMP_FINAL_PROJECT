package dhima.gerald.wump_final_project.servlets;

import dhima.gerald.wump_final_project.model.Account;
import dhima.gerald.wump_final_project.model.AccountCodes;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

/**
 * This servlet handles clients' actions in the login page.
 * Checks whether the client has provided input, sends the entered input to the server and takes
 * the appropriate actions based on the status.
 * Redirects to DataInsertionServlet if login is successful and to CreateAccountServlet if client wants to create an account.
 */
@WebServlet(name = "/LoginServlet", value = {""})
public class LoginServlet extends HttpServlet implements AccountCodes {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Handles the get requests of this page (Client entering for the first time, or client logging out)
        HttpSession session = request.getSession(); //Gets the session of the client if he has logged out of his account.
        session.invalidate(); //Closes that session.

        getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
        //Forwards the request to the page.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Handles the post requests of the page (When a user hits the submit button and is redirected in this page again).
        Account account;

        //Save the inputs entered by the user in the login form.
        String username = request.getParameter("accountLoginUsername");
        String password = request.getParameter("accountLoginPassword");
        String errorMessage = "";


        if (username.isEmpty() || password.isEmpty()) { //Checks whether the user left any of the fields empty.
            errorMessage = "Please fill in all blank fields.";

            request.setAttribute("error", errorMessage); //Sets a request attribute accordingly.
            getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
            //Forwards the request in the same page.
        } else { //The user has provided the input correctly.
            account = new Account(username, password, LOGGING_IN); //Create a model Account accordingly with the info provided.

            Socket socket = new Socket("192.168.0.29", 7777); //connects with the server at that IP and in the specified port.
            ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream()); //Creates an output stream for objects in order to send info as packets to the server.
            DataInputStream fromServer = new DataInputStream(socket.getInputStream()); //Creates Data input stream to get the output codes from the server.
            DataOutputStream codeToServer = new DataOutputStream(socket.getOutputStream()); //Create a Data outputstream to send codes to the server.

            codeToServer.writeInt(SENDING_ACCOUNT_OBJECT); //Notify the server that we are sending an account object.
            toServer.writeObject(account); //Send the account
            Integer loginCode = fromServer.readInt(); //Read the response given from the server.


            if (loginCode.equals(INVALID_ACCOUNT_CREDENTIALS)) { //If code signals for invalid credentials
                errorMessage = "Incorrect username/password.";

                request.setAttribute("error",errorMessage); //Sets the error attribute
                getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
                //Redirects the request in the same page.
            } else { //If the credentials are correct.
                HttpSession session = request.getSession(); //Create a session for the logged in client.

                //Check the verification status of the account and sets an attribute in the session regarding it.
                if (loginCode.equals(PENDING_PROFESSIONAL_SCIENTIST_REVIEW)) {
                    session.setAttribute("accountVerificationStatus", "Pending");
                } else if (loginCode.equals(DECLINED_PROFESSIONAL_SCIENTIST)) {
                    session.setAttribute("accountVerificationStatus", "Declined");
                } else if (loginCode.equals(APPROVED_PROFESSIONAL_SCIENTIST)) {
                    session.setAttribute("accountVerificationStatus", "Approved");
                }
                session.setAttribute("accountLoginUsername",username);

                getServletContext().getRequestDispatcher("/DataInsertionServlet").forward(request, response);
                //Forward the request to the form where the user inserts experiments.
            }
        }
    }
}

