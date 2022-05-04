<%@ page import="java.util.Objects" %>
<%@ page import="java.util.HashMap" %><%--
  Created by IntelliJ IDEA.
  User: user
  Date: 4/21/2022
  Time: 11:02 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Insert Data</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class = "bg-blue-300">
<%
    String isProfessionalScientist = (String) session.getAttribute("isProfessionalScientist");
    System.out.println(isProfessionalScientist);
%>
<div class = "mx-auto my-5 w-8/12 max-2-2xl rounded-3xl bg-white py-5 text-center shadow-xl">
    <h1 class = "mx-6 border-b pb-2 text-4xl font-semibold"><%="Welcome " + session.getAttribute("accountLoginUsername") +"!" %></h1>
    <h4 class = "font-bold text-xl text-red-600">${accountAccessLevelOutput}</h4>
    <p class = "font-semibold text-lg text-black">W.U.M.P Data Insertion Form</p>
    <form action = "DataInsertionServlet" method = "post" enctype="multipart/form-data">
        <div class = "my-6 mx-8 grid-cols-2 gap-4">
            <label for = "data-insert-areaOfPlate">Area of Plate (cm2):</label>
            <input id = "data-insert-areaOfPlate" type = "number" min = "0" step = "0.1" name = "dataInsertAreaOfPlate" class = "border-2 rounded-lg border-blue p-1 placeholder-blue-800 focus:outline-none focus:ring-2"> <br>
            <br>
            <label for = "data-insert-objectsFound">Objects Found:</label>
            <input id = "data-insert-objectsFound" type = "text" name = "dataInsertObjectsFound" class = "border-2 rounded-lg border-blue p-1 placeholder-blue-800 focus:outline-none focus:ring-2"> <br>
            <br>
            <label for = "data-insert-longitude">Longitude:</label>
            <input id = "data-insert-longitude" type = "number" name = "dataInsertLongitude" step="any" class = "border-2 rounded-lg border-blue p-1 placeholder-blue-800 focus:outline-none focus:ring-2"> <br>
            <br>
            <label for = "data-insert-latitude">Latitude:</label>
            <input id = "data-insert-latitude" type = "number" name = "dataInsertLatitude" step="any" class = "border-2 rounded-lg border-blue p-1 placeholder-blue-800 focus:outline-none focus:ring-2"> <br>
            <br>
            <label for = "data-insert-temperature">Temperature (in C):</label>
            <input id = "data-insert-temperature" type = "number" name = "dataInsertTemperature" step="any" class = "border-2 rounded-lg border-blue p-1 placeholder-blue-800 focus:outline-none focus:ring-2"> <br>
            <Br>
            <label for = "data-insert-weather" >Weather: </label>
            <select id = "data-insert-weather" name = "dataInsertWeather" class = "border-2 rounded-lg border-blue p-1 placeholder-blue-800 focus:outline-none focus:ring-2">
                <option value = "Sunny and Clear">Sunny&Clear</option>
                <option value = "Cloudy">Cloudy</option>
                <option value = "Rainy">Rainy</option>
                <option value = "Partially Cloudy">Partially Cloudy</option>
                <option value = "Snowy">Snowy</option>
                <option value = "Rainy and Windy">Rainy&Windy</option>
                <option value = "Cloudy and Windy">Cloudy&Windy</option>
            </select><br>
            <br>
            <label for = "data-insert-image">Experiment Photo:</label>
            <input type = "file" id = "data-insert-image" name = "dataInsertImage" class = "border-2 rounded-lg border-blue p-1 placeholder-blue-800 focus:outline-none focus:ring-2"><br>
            <input type = "hidden" name = "submittedForm" value = "FormSubmitted">
            <%--<input type = "hidden" name = "accountLoginUsername" value = ${accountLoginUsername}> --%>
            <%--<input type = "hidden" name = "isProfessionalScientist" value = ${isProfessionalScientist != "true" ? "false" : "true"}>--%>
            <br><br>
        </div>
        <button type = "submit" value="Submit"class="py-1 mr-7 rounded-lg bg-blue-300 px-4 text-xl font-semibold hover:bg-blue-800 hover:text-white">Submit</button>
        <button type = "button" onclick="location='/'"class="py-1 mr-7 rounded-lg bg-red-300 px-4 text-xl font-semibold hover:bg-red-800 hover:text-white">Log Out</button>
        <button type = "button" onclick="location ='/ViewDatabaseServlet'" ${ isProfessionalScientist != "true" ? 'disabled="disabled"' : ''} class="py-1 mr-7 rounded-lg bg-orange-300 px-4 text-xl font-semibold hover:bg-orange-800 hover:text-white">View Database</button>
    </form>

    <h5 class = "my-2 mx-auto w-80 rounded-lg bg-red-400 text-xl font-semibold text-black">${errorMessage}</h5>
</div>
</body>

</html>
