package dhima.gerald.wump_final_project.servlets;

import dhima.gerald.wump_final_project.model.AccountCodes;
import dhima.gerald.wump_final_project.model.Data;
import dhima.gerald.wump_final_project.model.ModifyExperiment;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Objects;

/**
 * This servlet allows the client to view all the experiments' data by getting them from the server.
 * Handles clients' requests about modifying the database in two forms: Deleting an experiment, updating a value.
 * Redirects to LoginServlet if user wants to log out.
 * Redirects to DataInsertionServlet if user wants to insert an experiment.
 */
@WebServlet(name = "ViewDatabaseServlet", value = "/ViewDatabaseServlet")
public class ViewDatabaseServlet extends HttpServlet implements AccountCodes {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Handles the Get requests in the View Database page.
        HttpSession session = request.getSession(); //gets the session for the client.

        Socket socket = new Socket("192.168.0.29", 7777); //connects with the server at that IP and in the specified port.
        ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream()); //Creates an output stream for objects in order to send info as packets to the server.
        DataInputStream fromServer = new DataInputStream(socket.getInputStream()); //Creates Data input stream to get the output codes from the server.
        DataOutputStream codeToServer = new DataOutputStream(socket.getOutputStream());

        codeToServer.writeInt(GETTING_EXPERIMENTS_DATA); //Notify the server that the client is getting experiments.

        ObjectInputStream experimentsFromServer = new ObjectInputStream(socket.getInputStream()); //Open an object input stream to get the experiments
        try {
            ArrayList<Data> experiments = (ArrayList<Data>) experimentsFromServer.readObject(); //Get the arraylist sent from the server.
            int numberOfExperiments = experiments.size(); //Stores the number of experiments.

            //Sets the number of experiments and the experiments as attributes.
            session.setAttribute("numberOfExperiments",numberOfExperiments);
            session.setAttribute("experiments",experiments);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //Forward the request to the viewDatabase page.
        getServletContext().getRequestDispatcher("/viewDatabase.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Handles the post requests on the View Database page (When the user tries to modify the database)
        HttpSession session = request.getSession(); //Gets the session for the client
        session.setAttribute("errorMessage1", ""); //Resets the attribute

        Socket socket = new Socket("192.168.0.29", 7777); //connects with the server at that IP and in the specified port.
        ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream()); //Creates an output stream for objects in order to send info as packets to the server.
        DataInputStream fromServer = new DataInputStream(socket.getInputStream()); //Creates Data input stream to get the output codes from the server.
        DataOutputStream codeToServer = new DataOutputStream(socket.getOutputStream());

        String buttonUsed = request.getParameter("button"); //Gets the name of the button which was clicked.
        System.out.println(buttonUsed);
        if(Objects.equals(buttonUsed, "deleteButton")){ //If the delete button is pressed.
            codeToServer.writeInt(VIEWING_DATABASE_AFTER_DELETING_EXPERIMENT); //Notify the server.
            Integer experimentId = Integer.valueOf(request.getParameter("deleteExperimentId")); //Get the experiment ID enterd by the user.
            ModifyExperiment experiment = new ModifyExperiment(experimentId); //Create the model

            toServer.writeObject(experiment); //Send the model to the serve.r
        }else if(Objects.equals(buttonUsed, "updateButton")){ //If the update button was pressed.
            codeToServer.writeInt(VIEWING_DATABASE_AFTER_UPDATING_EXPERIMENT); //Notify the server.

            //Get all the values given by the user.
            Integer experimentId = Integer.valueOf(request.getParameter("updateExperimentId"));
            String updateField = request.getParameter("updateField");
            String updatedValue = request.getParameter("updatedValue");

            ModifyExperiment experiment = new ModifyExperiment(experimentId,updateField,updatedValue);
            //Create the model.

            toServer.writeObject(experiment); //Send the model to the server.
        }

        Integer requestStatus = fromServer.readInt(); //Read the server's response.

        //Based on the response set the attribute appropriately.
        if(requestStatus.equals(UNABLE_TO_MODIFY_DATABASE)){
            session.setAttribute("errorMessage1", "Unable to modify the database. Please provide correct input.");
        }else if(requestStatus.equals(SUCCESSFULLY_MODIFIED_DATABASE)){
            session.setAttribute("errorMessage1","Successfully modified database");
        }else if(requestStatus.equals(RECORD_DOES_NOT_EXIST)){
            session.setAttribute("errorMessage1","The record you were trying to modify does not exist.");
        }

        response.sendRedirect("ViewDatabaseServlet"); //Redirect the page to itself.
    }
}