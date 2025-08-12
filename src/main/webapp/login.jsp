<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <meta http-equiv="X-UA-Compatible" content="ie=edge" />
  <title>Login - PahanaEdu</title>

  <!-- Font Icon -->
  <link rel="stylesheet" href="fonts/material-icon/css/material-design-iconic-font.min.css" />
  <link rel="icon" type="image/x-icon" href="images/favicon.png"> 
  <!-- Main CSS -->
  <link rel="stylesheet" href="css/login.css" />

  <!-- SweetAlert CSS -->
  <link rel="stylesheet" href="alert/dist/sweetalert.css" />
</head>
<body>

<input type="hidden" id="status" value="<%= request.getAttribute("status") != null ? request.getAttribute("status").toString() : "" %>" />

<div class="main">
  <!-- Sign in Form -->
  <section class="sign-in">
    <div class="container">
      <div class="signin-content">
        <div class="signin-image">
          <figure>
            <img src="images/loginlogo.png" alt="Sign in image" />
          </figure>
        </div>

        <div class="signin-form">
          <h2 class="form-title">Sign In</h2>
          <form method="post" action="Login" class="register-form" id="login-form">
            <div class="form-group">
              <label for="username">
                <i class="zmdi zmdi-account material-icons-name"></i>
              </label>
              <input
                type="text"
                name="username"
                id="username"
                placeholder="Your Email"
                required
                autocomplete="username"
              />
            </div>

            <div class="form-group">
              <label for="password">
                <i class="zmdi zmdi-lock"></i>
              </label>
              <div class="password-wrapper">
                <input
                  type="password"
                  name="password"
                  id="password"
                  placeholder="Password"
                  required
                  autocomplete="current-password"
                />
              </div>
            </div>

            <div class="form-group">
              <a href="forgotPassword.jsp">Forgot Password?</a>
            </div>

            <div class="form-group form-button">
              <input type="submit" name="signin" id="signin" class="form-submit" value="Log in" />
            </div>
          </form>

          <div class="social-login">
            <span class="social-label">Or login with</span>
            <ul class="socials">
              <li><a href="#"><i class="display-flex-center zmdi zmdi-facebook"></i></a></li>
              <li><a href="#"><i class="display-flex-center zmdi zmdi-twitter"></i></a></li>
              <li><a href="#"><i class="display-flex-center zmdi zmdi-google"></i></a></li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </section>
</div>

<!-- JS -->
<script src="vendor/jquery/jquery.min.js"></script>
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

<script type="text/javascript">
const status = document.getElementById("status").value;
if (status === "failed") {
  swal("Sorry", "Wrong Username or Password", "error");
}
</script>

</body>

</html>




