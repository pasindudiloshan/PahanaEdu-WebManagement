<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Help Section - Employee Tutorial</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="icon" href="images/favicon.png" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" />
    <link rel="stylesheet" href="css/sidebar-header.css">
    <link rel="stylesheet" href="css/bill.css">
    <link rel="stylesheet" href="css/help.css" />

    <script src="https://code.jquery.com/jquery-3.6.0.min.js" defer></script>
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js" defer></script>
    <script src="js/bill.js" defer></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>

<body>

<div class="container">
    <%@ include file="sidebar.jsp" %>
    <%@ include file="header.jsp" %>

    <div class="main-content">
        <div class="page-title" style="display: flex; justify-content: space-between; align-items: center; padding: 10px 20px;">
    <div class="title">Help Section - Employee Tutorial</div>
    <a href="javascript:history.back()" class="back-btn" style="text-decoration: none; color: #333; font-weight: bold; display: flex; align-items: center;">
        <i class="fas fa-arrow-left" style="margin-right: 5px;"></i> Back
    </a>
</div>

        <!-- Image Slider -->
        <div class="slider-container">
            <div class="slider">
                <div class="slide active">
                    <img src="images/1.jpeg" alt="Help Step 1" />
                </div>
                <div class="slide">
                    <img src="images/2.jpeg" alt="Help Step 2" />
                </div>
                <div class="slide">
                    <img src="images/3.jpeg" alt="Help Step 3" />
                </div>
                <div class="slide">
                    <img src="images/4.jpeg" alt="Help Step 4" />
                </div>
                 <div class="slide">
                    <img src="images/5.jpg" alt="Help Step 5" />
                </div>
                 <div class="slide">
                    <img src="images/6.jpg" alt="Help Step 6" />
                </div>
            </div>

            <!-- Navigation arrows -->
            <a class="prev">&#10094;</a>
            <a class="next">&#10095;</a>

            <!-- Dots -->
<div class="dots-container">
    <span class="dot active"></span>
    <span class="dot"></span>
    <span class="dot"></span>
    <span class="dot"></span>
    <span class="dot"></span>
    <span class="dot"></span>
</div>
        </div>
        <!-- End Slider -->

    </div>
</div>

<script>
    let currentSlide = 0;
    const slides = document.querySelectorAll(".slide");
    const dots = document.querySelectorAll(".dot");

    function showSlide(index) {
        slides.forEach((slide, i) => {
            slide.classList.toggle("active", i === index);
            dots[i].classList.toggle("active", i === index);
        });
        currentSlide = index;
    }

    document.querySelector(".next").addEventListener("click", () => {
        let nextSlide = (currentSlide + 1) % slides.length;
        showSlide(nextSlide);
    });

    document.querySelector(".prev").addEventListener("click", () => {
        let prevSlide = (currentSlide - 1 + slides.length) % slides.length;
        showSlide(prevSlide);
    });

    dots.forEach((dot, i) => {
        dot.addEventListener("click", () => {
            showSlide(i);
        });
    });
</script>

</body>
</html>
