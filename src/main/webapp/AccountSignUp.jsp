<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sign Up</title>

    <script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-orange-300">

<div>
    <div class="mx-auto my-5 w-8/12 max-w-2xl rounded-3xl bg-white py-5 text-center shadow-xl">
        <h1 class="mx-6 border-b pb-2 text-4xl font-semibold">Account Sign Up Form</h1>
        <form action="CreateAccountServlet" method="post">
            <div class="my-5 mx-8 grid-cols-2 gap-4">
                <label class="flex items-center text-xl" for="account-signup-firstName">First Name:</label>
                <input id="account-signup-firstName" type="text" name="accountSignupFirstName" class="w-full appearance-none rounded-lg border-2 border-blue-700 p-1 placeholder-blue-800 focus:outline-none focus:ring-2" />
                <br />

                <label for="account-signup-lastName" class="flex items-center text-xl">Last Name:</label>
                <input id="account-signup-lastName" type="text" name="accountSignupLastName" class="w-full appearance-none rounded-lg border-2 border-blue-700 p-1 placeholder-blue-800 focus:outline-none focus:ring-2" />
                <br />

                <label for="account-signup-institution" class="flex items-center text-xl">Institution:</label>
                <input id="account-signup-institution" type="text" name="accountSignupInstitution" class="w-full appearance-none rounded-lg border-2 border-blue-700 p-1 placeholder-blue-800 focus:outline-none focus:ring-2" />
                <br />

                <label for="account-signup-username" class="flex items-center text-xl">Username:</label>
                <input id="account-signup-username" type="text" name="accountSignupUsername" class="w-full appearance-none rounded-lg border-2 border-blue-700 p-1 placeholder-blue-800 focus:outline-none focus:ring-2" />
                <br />

                <label for="account-signup-password1" class="flex items-center text-xl">Password: </label>
                <input id="account-signup-password1" type="password" name="accountSignupPassword1" class="w-full appearance-none rounded-lg border-2 border-blue-700 p-1 placeholder-blue-800 focus:outline-none focus:ring-2" />
                <br />

                <label for="account-signup-password2" class="flex items-center text-xl">Repeat Password:</label>
                <input id="account-signup-password2" type="password" name="accountSignupPassword2" class="w-full appearance-none rounded-lg border-2 border-blue-700 p-1 placeholder-blue-800 focus:outline-none focus:ring-2" />
                <br />

                <label class="flex items-center text-xl">Access Level: </label>
                <input id="account-signup-accesslevelCitizenScientist" type="radio" value="citizenScientist" name="accountSignupAccessLevel" checked />
                <label for="account-signup-accesslevelCitizenScientist" class="mr-6">Citizen Scientist</label>
                <input id="account-signup-accesslevelProfessionalScientist" type="radio" value="professionalScientist" name="accountSignupAccessLevel" />
                <label for="account-signup-accesslevelProfessionalScientist"> Professional Scientist</label>
            </div>
            <br />
            <button type="submit" class="py-1 mr-7 rounded-lg bg-blue-300 px-4 text-xl font-semibold hover:bg-blue-800 hover:text-white">Create Account</button>
            <button type="button" onclick="location='/'" class="py-1 rounded-lg bg-blue-300 px-4 text-xl font-semibold hover:bg-blue-800 hover:text-white">Back to Log In</button>

            <h4 class="my-2 mx-auto w-80 rounded-lg bg-red-400 text-xl font-semibold text-black">${error}</h4>
            <br />
            <br />

            <label class="text-center font-bold">Disclaimer: If you want to open a Professional Scientist account, you will have to wait for server approval before entering it.</label>
        </form>
    </div>
</div>


</body>
</html>
