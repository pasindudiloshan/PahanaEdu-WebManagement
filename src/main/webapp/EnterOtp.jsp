<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>

<link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">

<!-- SweetAlert -->
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

<style type="text/css">
.form-gap {
    padding-top: 70px;
}
</style>
</head>

<body>
<div class="form-gap"></div>
<div class="container">
    <div class="row">
        <div class="col-md-4 col-md-offset-4">
            <div class="panel panel-default">
                <div class="panel-body">
                    <div class="text-center">
                        <h3><i class="fa fa-lock fa-4x"></i></h3>
                        <h2 class="text-center">Enter OTP</h2>

                        <!-- Hidden status for SweetAlert -->
                        <input type="hidden" id="status" 
                               value="<%= request.getAttribute("status") != null ? request.getAttribute("status") : "" %>"/>
                        <input type="hidden" id="message" 
                               value="<%= request.getAttribute("message") != null ? request.getAttribute("message") : "" %>"/>

                        <div class="panel-body">

                            <form id="register-form" action="ValidateOtp" role="form" autocomplete="off"
                                  class="form" method="post">

                                <div class="form-group">
                                    <div class="input-group">
                                        <span class="input-group-addon"><i class="glyphicon glyphicon-envelope color-blue"></i></span>
                                        <input id="otp" name="otp" placeholder="Enter OTP" class="form-control" type="text" required="required">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <input name="recover-submit" class="btn btn-lg btn-primary btn-block" value="Reset Password" type="submit">
                                </div>

                                <input type="hidden" class="hide" name="token" id="token" value="">
                            </form>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- SweetAlert Script -->
<script type="text/javascript">
$(document).ready(function() {
    const status = document.getElementById("status").value;
    const message = document.getElementById("message").value;

    if(status === "error") {
        swal("Oops!", message, "error");
    }
});
</script>

</body>
</html>
