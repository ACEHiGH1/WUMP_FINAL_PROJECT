package dhima.gerald.wump_final_project.servlets;

import dhima.gerald.wump_final_project.model.Account;
import dhima.gerald.wump_final_project.model.AccountCodes;
import dhima.gerald.wump_final_project.model.Data;


import javax.accessibility.Accessible;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.rowset.serial.SerialBlob;
import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Objects;

/**
 *  This servlet handles clients' actions in the data insertion page.
 *  Checks the clients' verification status to allow them to view the database or not,
 *  Checks whether the person has provided the experiment's input correctly,
 *  Sends the entered experiment's data to the server
 *  Takes the appropriate actions based on the insertion status.
 *  Redirects to LoginServlet if user wants to Sign out
 *  Redirects to ViewDatabaseServlet if user is a Professional Scientist and wants to view the database
 */
@WebServlet(name = "DataInsertionServlet", value = "/DataInsertionServlet")
@MultipartConfig()
public class DataInsertionServlet extends HttpServlet implements AccountCodes {
    String accountLoginUsername;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Handles the get requests of DataInsertion page.
        HttpSession session = request.getSession(); //Gets the session of the client
        session.setAttribute("errorMessage", ""); //Resets the errorMessage attribute.

        getServletContext().getRequestDispatcher("/dataInsertionPage.jsp").forward(request,response);
        //Forwards the request to the correct page.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handles the post requests of the page (After user logs in, or after the client submits the form)
        HttpSession session = request.getSession(); //Gets the session for that client
        session.setAttribute("errorMessage", ""); //Resets the errorMessage attribute
        String submittedForm = request.getParameter("submittedForm"); //Checks whether the user has previously submitted a form or not.

        String accountVerificationStatus = (String) session.getAttribute("accountVerificationStatus"); //Gets the client's verification status.
        String accountAccessLevelOutput = "";


        if(submittedForm == null) { //The client has not previously submitted a experiment
            //Considers all the possible verification statuses for the client and sets the attribute appropriately.
            if(Objects.equals(accountVerificationStatus, "Pending")){
                accountAccessLevelOutput = "Your application for a professional scientist is currently under review. Currently in Citizen Scientist view.";
            }else if(Objects.equals(accountVerificationStatus, "Declined")){
                accountAccessLevelOutput = "Your application for a professional scientist was declined. You have only citizen scientist privileges.";

            }else if(Objects.equals(accountVerificationStatus, "Approved")){
                accountAccessLevelOutput = "Your application for a professional scientist was approved. You have full privileges now!";
                session.setAttribute("isProfessionalScientist","true");
            }
                //session.setAttribute("accountLoginUsername",accountLoginUsername);
                session.setAttribute("accountAccessLevelOutput", accountAccessLevelOutput);

            //Forwards the request in the same page.
            getServletContext().getRequestDispatcher("/dataInsertionPage.jsp").forward(request, response);
        }

        if(Objects.equals(submittedForm, "FormSubmitted")) { //If the user has tried to submit a form at least once.

            //Gets all the input given by the client.
            String areaOfPlate = request.getParameter("dataInsertAreaOfPlate");
            String objectsFound = request.getParameter("dataInsertObjectsFound");
            String longitude = request.getParameter("dataInsertLongitude");
            String latitude = request.getParameter("dataInsertLatitude");
            String temperature = request.getParameter("dataInsertTemperature");
            String weather = request.getParameter("dataInsertWeather");
            Part filePart = request.getPart("dataInsertImage");


            String errorMessage = "";

            //Checks whether any input is left empty and prints sets the errorMessage appropriately in that case.
            if (areaOfPlate.isEmpty() || objectsFound.isEmpty() || longitude.isEmpty() || latitude.isEmpty() || temperature.isEmpty() || weather.isEmpty()) {
                errorMessage = "Please fill in all the fields and upload a photo.";
                session.setAttribute("errorMessage", errorMessage);

                getServletContext().getRequestDispatcher("/dataInsertionPage.jsp").forward(request, response);
            }else if(!Objects.equals(filePart.getContentType(), "image/jpeg")) { //Checks if the image provided is a jpeg image.
                //If not, set the errorMessage attribute correctly.
                errorMessage = "Please provide an jpg image.";
                session.setAttribute("errorMessage", errorMessage);

                getServletContext().getRequestDispatcher("/dataInsertionPage.jsp").forward(request,response);
            }else{ //In this case the form has been filled in correctly.

                    Socket socket = new Socket("192.168.0.29", 7777); //connects with the server at that IP and in the specified port.
                    ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream()); //Creates an output stream for objects in order to send info as packets to the server.
                    DataInputStream fromServer = new DataInputStream(socket.getInputStream()); //Creates Data input stream to get the output codes from the server.
                    DataOutputStream codeToServer = new DataOutputStream(socket.getOutputStream()); // Creates a data output stream to send code to the server.

                    //Converts the file into an array byte.
                    InputStream inputStream = filePart.getInputStream();
                    byte[] byteArray = inputStream.readAllBytes();


                    accountLoginUsername = (String) session.getAttribute("accountLoginUsername"); //gets the username of the client by the session attribute
                    codeToServer.writeInt(SENDING_EXPERIMENT_DATA_OBJECT); //notify the server that we are sending an experiment

                    //creates a model for the experiment
                    Data experimentData = new Data(accountLoginUsername, Double.parseDouble(areaOfPlate), objectsFound, Double.parseDouble(longitude), Double.parseDouble(latitude), Double.parseDouble(temperature), weather,byteArray);

                    toServer.writeObject(experimentData); //sends the model to the server.

                    int code = fromServer.readInt(); //Reads the response from the server.


                    //Handles the response.
                    if (code == SUCCESSFULLY_ENTERED_DATA) {
                        errorMessage = "Data submitted and saved in database successfully.";
                        session.setAttribute("errorMessage", errorMessage);

                        getServletContext().getRequestDispatcher("/dataInsertionPage.jsp").forward(request, response);
                    }else{
                        errorMessage = "Unable to submit the data.";
                        session.setAttribute("errorMessage", errorMessage);

                        getServletContext().getRequestDispatcher("/dataInsertionPage.jsp").forward(request, response);
                    }
                }
            }
        }

    }
