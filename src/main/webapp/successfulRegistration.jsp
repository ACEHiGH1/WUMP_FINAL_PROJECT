<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 4/20/2022
  Time: 12:15 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Successful Registration</title>

    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-orange-300">
<div>
    <div class="mx-auto my-60 w-3/4 max-w-2xl rounded-lg bg-white py-5 text-center shadow-lg">
        <h3 class="mx-10 flex text-xl font-medium">You have been able to create an account succesfully.</h3>
        <h4 class="mx-auto text-xl font-bold text-red-600">${output}</h4>
        <br />
        <button type="button" onclick="location='/'" class="rounded-xl bg-blue-300 py-1 px-3 font-semibold hover:bg-blue-800 hover:text-white">Back To Log In</button>
    </div>
</div>
</body>
</html>
