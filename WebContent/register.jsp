<!DOCTYPE html>
<html>

<head>
    <meta charset="ISO-8859-1">
    <title>Insert title here</title>
</head>

<body>

    <h1 align=center style="background-color:DodgerBlue"> Create User Page</h1>
    <form action="createuser" method="post">
        <div class="container">

            <br>
            <label><b>UserName:</b></label>
            &nbsp;&nbsp;
            <input type="text" placeholder="Enter username" name="username" maxlength="20" required>
            </br>

            <br>
            <label><b>Email:</b></label>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="text" placeholder="Enter Email" name="email" maxlength="40" required>
            </br>

            <br>
            <label><b>Password:</b></label>
            &nbsp;&nbsp;&nbsp;
            <input type="password" placeholder="Enter Password" name="psw" maxlength="40" required>
            </br>

            <br>
            <label><b>Repeat Password:</b></label>
            <input type="password" placeholder="Repeat Password" name="psw-repeat" maxlength="40" required>
            </br>
            <p>By creating an account you agree to our <a href="#">Terms & Privacy</a>.</p>

            <div class="clearfix">
                <button type="button" class="cancelbtn">Cancel</button>
                <button type="submit" class="signupbtn">Sign Up</button>
                ${message}
            </div>
        </div>
    </form>
    <form>
<input type="hidden" name="userval" value="0">
</form>
</body>

</html>