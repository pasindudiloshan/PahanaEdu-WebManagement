# 📚 PahanaEdu-WebManagement

A Java-based web application for managing books, customers, users, and billing processes in a bookshop environment.

## 🚀 Features

- 👥 User Management – Full CRUD operations (Create, Read, Update, Delete)
- 🛍️ Product (Book) Management – CRUD operations with category and image upload
- 👨‍💼 Customer Management – CRUD operations with profile picture and detailed address
- 🧾 Bill Creation – Includes dynamic discount logic based on payment method
- 📊 Billing History – Filter bills by account number and date
- Each bill can be viewed and downloaded as a PDF receipt
- 🔐 Secure Login System – Passwords stored using hashing, with OTP-based - password recovery
- 📷 Image Upload & Display – For Users, Customers, and Products
- 📦 Dashboard Overview – Statistics cards showing:
		-🪙 Total Revenue
		-📦 Total Orders
		-👥 Total Customers
		-📚 Total Products
		-👨‍💻 Total Users
		-🕒 Latest 5 Orders Displayed	

## 🛠️ Technologies Used

- Java (Servlets, JSP)
- MySQL
- HTML, CSS, JavaScript (jQuery, Select2)
- Apache Tomcat
- Maven
- Git + GitHub for version control


## 📁 Folder Structure
PahanaEdu-WebManagement/
├── src/
│ └── main/
│ └── java/com/pahanaedu/
│ ├── controller/
│ ├── dao/
│ ├── model/
│ └── util/
├── src/main/webapp/
│ ├── css/
│ ├── js/
│ ├── *.jsp
├── pom.xml
└── README.md

## 💻 Setup Instructions

1. Clone the repo:
   ```bash
   git clone https://github.com/yourusername/PahanaEdu-WebManagement.git
   
 2. Import as Maven project in your IDE (Eclipse/IntelliJ).

 3. Configure Tomcat server and MySQL DB.

 4. Update DB credentials in DBUtil.java.

 5. Run the application on http://localhost:8080/PahanaEdu-WebManagement

🙋‍♂️ Author - PPD Fernando
pasindudiloshan — GitHub
pasindudiloshan@gmail.com - Email

   
