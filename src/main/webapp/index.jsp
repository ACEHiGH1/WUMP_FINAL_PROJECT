<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>W.U.M.P</title>

    <script src="https://cdn.tailwindcss.com"></script>

</head>
<body class="bg-blue-300">
<div>
    <br />
    <div class="shadow-xl mx-auto my-5 w-1/2 max-w-2xl rounded-3xl bg-white py-5 text-center">
        <h1 class="mx-6 border-b pb-2 text-4xl font-semibold">Welcome!</h1>
        <%-- The login form --%>
        <form action="" method="post">
            <div class="grid-cols2 my-5 mx-8 grid gap-4">
                <label for="account-login-username" class="flex items-center text-xl">Username:</label>
                <input id="account-login-username" type="text" name="accountLoginUsername" class="w-full appearance-none rounded-lg border-2 border-blue-700 p-1 placeholder-blue-800 focus:outline-none focus:ring-2" />
                <br />

                <label for="account-login-password" class="flex items-center text-xl">Password: </label>
                <input id="account-login-password" type="password" name="accountLoginPassword" class="w-full appearance-none rounded-lg border-2 border-blue-700 p-1 placeholder-blue-800 focus:outline-none focus:ring-2" />
                <br />
            </div>
            <button type="submit" class="py=1 rounded-lg bg-blue-300 px-4 text-xl font-semibold hover:bg-blue-800 hover:text-white">Log In</button>
            <button type="button" onclick="location='CreateAccountServlet'" class="py=1 rounded-lg bg-blue-300 px-4 text-xl font-semibold hover:bg-blue-800 hover:text-white">Create Account</button>
        </form>
        <h4 class="my-2 mx-auto w-80 rounded-lg bg-red-400 text-xl font-semibold text-black">${error}</h4>
    </div>
</div>
</body>
</html>