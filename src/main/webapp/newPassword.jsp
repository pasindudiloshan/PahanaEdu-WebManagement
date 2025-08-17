<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<meta charset='utf-8'>
<meta name='viewport' content='width=device-width, initial-scale=1'>
<title>Reset Password</title>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-social/5.1.1/bootstrap-social.css">
<link href='https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css' rel='stylesheet'>
<link href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.0.3/css/font-awesome.css' rel='stylesheet'>
<script type='text/javascript' src='https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js'></script>
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

<style>
.placeicon { font-family: fontawesome; }
.custom-control-label::before { background-color: #dee2e6; border: #dee2e6; }
</style>
</head>
<body class='snippet-body bg-info' oncontextmenu='return false'>

<div class="container">
    <div class="row justify-content-center">
        <div class="col-12 col-md-9 col-lg-7 col-xl-6 mt-5">
            <div class="container bg-white rounded mt-2 mb-2 px-0">
                <div class="row justify-content-center align-items-center pt-3">
                    <h1><strong>Reset Password</strong></h1>
                </div>
                <div class="pt-3 pb-3">
                    <!-- Hidden fields for SweetAlert -->
                    <input type="hidden" id="status" value="<%= request.getAttribute("status") != null ? request.getAttribute("status") : "" %>"/>
                    <input type="hidden" id="message" value="<%= request.getAttribute("message") != null ? request.getAttribute("message") : "" %>"/>
                    
                    <form class="form-horizontal" action="newPassword" method="POST">
                        <div class="form-group row justify-content-center px-3">
                            <div class="col-9 px-0">
                                <input type="password" name="password" placeholder="&#xf084; &nbsp; New Password" class="form-control border-info placeicon" required>
                            </div>
                        </div>
                        <div class="form-group row justify-content-center px-3">
                            <div class="col-9 px-0">
                                <input type="password" name="confPassword" placeholder="&#xf084; &nbsp; Confirm New Password" class="form-control border-info placeicon" required>
                            </div>
                        </div>
                        <div class="form-group row justify-content-center">
                            <div class="col-3 px-3 mt-3">
                                <input type="submit" value="Reset" class="btn btn-block btn-info">
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
$(document).ready(function(){
    const status = document.getElementById("status").value;
    const message = document.getElementById("message").value;

    if(status === "error") {
        swal("Oops!", message, "error");
    } else if(status === "success") {
        swal("Success!", message, "success");
    }
});
</script>

<script src='https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.bundle.min.js'></script>
</body>
</html>
