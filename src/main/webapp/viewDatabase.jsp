<%@ page import="dhima.gerald.wump_final_project.servlets.ViewDatabaseServlet" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dhima.gerald.wump_final_project.model.Data" %>
<%@ page import="java.io.File" %>
<%@ page import="java.util.Base64" %><%--
  Created by IntelliJ IDEA.
  User: user
  Date: 4/27/2022
  Time: 9:51 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Database Info</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class = "bg-cyan-400">
<table class="w-full text-sm text-left text-black dark:text-black table-auto border-2">
    <tr class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-yellow-300">
        <td class="px-6 py-3 border-2">Experiment Id</td>
        <td class="px-6 py-3 border-2">User Id</td>
        <td class="px-6 py-3 border-2">Username</td>
        <td class="px-6 py-3 border-2">AccessLevel</td>
        <td class="px-6 py-3 border-2">Area of Plate</td>
        <td class="px-6 py-3 border-2">Objects Found</td>
        <td class="px-6 py-3 border-2">Longitude</td>
        <td class="px-6 py-3 border-2">Latitude</td>
        <td class="px-6 py-3 border-2">Temperature</td>
        <td class="px-6 py-3 border-2">Weather</td>
        <td class="px-6 py-3 border-2">Photo</td>
    </tr>
    <%
        Integer numberOfExperiments = (Integer) session.getAttribute("numberOfExperiments"); //Gets the number of experiments in the jsp.
        ArrayList<Data> experiments = (ArrayList<Data>) session.getAttribute("experiments"); //Gets the experiments
        for(int i = 0; i < numberOfExperiments; i++){ //Goes through every experiment in the arraylist creating a row for each
           // String filePath = "images/" + File.separator + experiments.get(i).getExperimentId() + ".jpg";
           // System.out.println(filePath);
            String encode = Base64.getEncoder().encodeToString(experiments.get(i).getByteImage()); //Create a string encoder for the iamge
    %>
    <tr class="bg-white border-b dark:bg-gray-600 dark:border-gray-700">
        <td class="px-6 py-4 font-medium text-gray-900 dark:text-white whitespace-nowrap border-2"><%=experiments.get(i).getExperimentId()%></td>
        <td class="px-6 py-4 border-2"><%=experiments.get(i).getUserId()%></td>
        <td class="px-6 py-4 border-2"><%=experiments.get(i).getUsername()%></td>
        <td class="px-6 py-4 border-2"><%=experiments.get(i).getAccessLevel()%></td>
        <td class="px-6 py-4 border-2"><%=experiments.get(i).getAreaOfPlate()%></td>
        <td class="px-6 py-4 border-2"><%=experiments.get(i).getObjectsFound()%></td>
        <td class="px-6 py-4 border-2"><%=experiments.get(i).getLongitude()%></td>
        <td class="px-6 py-4 border-2"><%=experiments.get(i).getLatitude()%></td>
        <td class="px-6 py-4 border-2"><%=experiments.get(i).getTemperature()%></td>
        <td class="px-6 py-4 border-2"><%=experiments.get(i).getWeather()%></td>
        <td class="px-6 py-4 border-2"><img src = "data:image/jpeg;base64,<%=encode%>" width="100" height="100"></td>
    </tr>
    <%
        }
    %>
</table>
<div class="flex mx-auto my-5 w-auto max-2-2xl rounded-3xl bg-white py-5 text-center shadow-xl">
    <div class="float-left text-l font-semibold my-auto">
        <form method="post" action="ViewDatabaseServlet">
            <label for = "delete-experiment">Delete experiment (ID): </label>
            <input id = "delete-experiment" type="number" step="1" min="0" name="deleteExperimentId" required class = "border-2 rounded-lg border-blue p-1 placeholder-blue-800 focus:outline-none focus:ring-2">
            <br>
            <button type = "submit" name = "button" value = "deleteButton" class="my-5 py-1 mr-7 rounded-lg bg-blue-300 px-4 text-l font-semibold hover:bg-blue-800 hover:text-white">Delete</button>
        </form>

    </div>
    <div class="float-right text-l font-semibold mx-auto text-left">
        <form method="post" action="ViewDatabaseServlet">
            <label for = "update-experiment">Experiment ID: </label>
            <input id = "update-experiment" type="number" step="1" min="0" name="updateExperimentId" required class = "border-2 rounded-lg border-blue p-1 placeholder-blue-800 focus:outline-none focus:ring-2">
            <br><br>
            <label for = "update-field">Update Field: </label>
            <select id = "update-field" name = "updateField" class = "border-2 rounded-lg border-blue p-1 placeholder-blue-800 focus:outline-none focus:ring-2">
                <option value="areaOfPlate">Area of Plate</option>
                <option value="objectsFound">Objects Found</option>
                <option value="longitude">Longitude</option>
                <option value="latitude">Latitude</option>
                <option value="temperature">Temperature</option>
                <option value="weather">Weather</option>
            </select>
            <br><br>
            <label for = "new-update-value">New Value:</label>
            <input type="text" name = "updatedValue" id = "new-update-value" required class = "border-2 rounded-lg border-blue p-1 placeholder-blue-800 focus:outline-none focus:ring-2">
            <br>
            <button type="submit" name = "button" value="updateButton" class="my-5 py-1 mr-7 rounded-lg bg-blue-300 px-4 text-l font-semibold hover:bg-blue-800 hover:text-white">Update</button>
        </form>
    </div>
</div>
<h3 class = "my-2 mx-auto w-80 rounded-lg bg-red-400 text-xl font-semibold text-black">${errorMessage1}</h3>
<button type="button" onclick="location='/ViewDatabaseServlet'" class="my-5 py-1 mr-7 rounded-lg bg-orange-300 px-4 text-l font-semibold hover:bg-white-800 hover:text-white">Refresh</button>
<button type = "button" onclick="location='/DataInsertionServlet'" class="my-5 py-1 mr-7 rounded-lg bg-orange-300 px-4 text-l font-semibold hover:bg-white-800 hover:text-white">Go Back To Data Insertion</button>
<button type="button" onclick="location='/'" class="my-5 py-1 mr-7 rounded-lg bg-orange-300 px-4 text-l font-semibold hover:bg-white-800 hover:text-white">Log Out.</button>

</body>
</html>
