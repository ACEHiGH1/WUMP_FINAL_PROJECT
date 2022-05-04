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

//@WebServlet("/CreateAccountServlet")

/**
 *  This servlet handles clients' actions in the create account page.
 *  Checks whether the client has provided input, sends the entered input to the server and takes
 *  the appropriate actions based on the account creation status.
 *  Redirects to SuccessfulRegistrationServlet if account was created successfully
 *  and LoginServlet if client wants to turn back to the login page.
 */
@WebServlet(name = "CreateAccountServlet", value = "/CreateAccountServlet")
public class CreateAccountServlet extends HttpServlet implements AccountCodes {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/AccountSignUp.jsp").forward(request,response); //redirects the get requests to the jsp page.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //The post request is  activated after the user submits the form.
        HttpSession session = request.getSession(); //creates a session for this client.

        Account account = null;

        //Saves all the requests parameters entered by the user into fields.
        String firstName = request.getParameter("accountSignupFirstName");
        String lastName = request.getParameter("accountSignupLastName");
        String institution = request.getParameter("accountSignupInstitution");
        String username = request.getParameter("accountSignupUsername");
        String password1 = request.getParameter("accountSignupPassword1");
        String password2 = request.getParameter("accountSignupPassword2");
        String accessLevel = request.getParameter("accountSignupAccessLevel");
        String errorMessage = "";

        //Checks whether the user has left any of the input fields empty.
        if(firstName.isEmpty() || lastName.isEmpty() || institution.isEmpty() || username.isEmpty() || password1.isEmpty() || password2.isEmpty() || accessLevel.isEmpty() ||
              firstName == null || lastName == null || institution == null || username == null || password1 == null || accessLevel == null  || password2 == null){
           //If yes, sets an attribute for the session named error message which is shown to the client dynamically when something goes wrong.
            errorMessage = "Please fill in all the blank fields.";
            session.setAttribute("error",errorMessage);

            getServletContext().getRequestDispatcher("/AccountSignUp.jsp").forward(request,response);
            //Forwards the post request again to the AccountSignUp jsp page.
        }else if(!password1.equals(password2)){ //If all the fields are filled, but the two passwords are not the same
            errorMessage = "The two passwords do not match."; //Set the errorMessage attribute appropriately.
            session.setAttribute("error",errorMessage);

            getServletContext().getRequestDispatcher("/AccountSignUp.jsp").forward(request,response);
            //Forward the post request again to the AccountSignUp jsp page.
        }
        else{ //In this case the user has filled the fields correctly.
            Socket socket = new Socket("192.168.0.29", 7777); //connects with the server at that IP and in the specified port.
            ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream()); //Creates an output stream for objects in order to send info as packets to the server.
            DataInputStream fromServer = new DataInputStream(socket.getInputStream()); //Creates Data input stream to get the output codes from the server.
            DataOutputStream codeToServer = new DataOutputStream(socket.getOutputStream()); //Creates a Data output stream to send codes to the server.

            codeToServer.writeInt(SENDING_ACCOUNT_OBJECT); //Notify the server we are sending an account object.
            switch (accessLevel) { //Check whether the user has applied for a citizen scientist or professional.
                case "citizenScientist":
                    //Creates an account model for the citizenScientist.
                    account = new Account(firstName, lastName, institution, username, password1, accessLevel, "Approved",SIGNING_UP);
                    break;
                case "professionalScientist":
                    //Creates an account model for the professional Scientist.
                    account = new Account(firstName, lastName, institution, username, password1, accessLevel, "Pending",SIGNING_UP);
                    break;
            }

            toServer.writeObject(account); //Send the model to the server.
            Integer codeReceived = fromServer.readInt(); // Get the code sent by the server after processing the object.

            if(codeReceived.equals(ALREADY_USED_USERNAME)){ //Checks whether the code signals that the username is already used.
                errorMessage = "Username is already used, please choose another one."; //Sets the attribute appropriately.
                session.setAttribute("error",errorMessage);

                getServletContext().getRequestDispatcher("/AccountSignUp.jsp").forward(request,response);
                //Forwards the request to the registration form page.
            }else if(codeReceived.equals(SUCCESSFULLY_CREATED_CITIZEN_SCIENTIST_ACCOUNT)){ //if the client has successfully created a citizen scientist account
                //Create a new attribute named accountType for the session which indicates that the user is a citizenscientist.
                session.setAttribute("accountType", "citizenScientist");

            }else if(codeReceived.equals(SUCCESSFULLY_APPLIED_PROFESSIONAL_SCIENTIST_ACCOUNT)){ //checks if the user has successfully applied to be a professional scientist.
                //Create a new attribute named accountType for the session which indicates that the user has applied for a professional scientist account.
                session.setAttribute("accountType", "professionalScientist");
            }
            getServletContext().getRequestDispatcher("/SuccessfulRegistrationServlet").forward(request,response);
            //Forwards the requests to the page that confirms the signup.
        }
    }
}
