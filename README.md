# 📚 PahanaEdu-WebManagement

Pahana Edu is a leading bookshop in Colombo City, serving hundreds of customers each month. Currently, customer account details are maintained manually, and the company requires a computerized online (web-based) system to manage billing information efficiently.  
For this purpose, I created a **Java EE-based web application** designed to manage books, customers, users, and billing processes in a bookshop environment.

## 🚀 Features

- 👥 **User Management Module** – Full CRUD for users with role-based access control.  
- 🛍️ **Product Management Module** – CRUD for books with category tagging and image handling.  
- 👨‍💼 **Customer Management Module** – CRUD for customer profiles with unique account numbers and profile images.  
- 🔐 **Authentication & Security Module** – Hashed login credentials with OTP-based password recovery.  
- 🧾 **Billing Engine** – Invoice generation with dynamic discounts based on payment rules.  
- 📊 **Sales Analytics & History Module** – Filter and query billing records by relevant parameters.  
- 🖨️ **PDF Invoice Generation Module** – Produces downloadable, formatted receipts for each transaction.  
- 📷 **Media Management Layer** – Handles image uploads and retrieval for users, customers, and products.  
- 📦 **Reporting Module** – Aggregates metrics and visual summaries, including total revenue, total customers etc..

## 🎬 Demonstration Video

A demonstration of the **PahanaEdu Billing System** in action can be viewed here:  
[Watch Demo Video](https://drive.google.com/file/d/1QW5H1EuLhr8QPEoRR1J825ZYBjIi51l/view?usp=sharing)

## 📄 Project Report

The full system report detailing the design, architecture, and implementation of **PahanaEdu Billing System** can be accessed here:  
[View System Report](https://docs.google.com/document/d/1FRPoZSXxv0QVIIY3TO98PKzVT5XMOInB/edit?usp=sharing&ouid=108374880673706677958&rtpof=true&sd=true)

## 🛠️ Technologies Used

- Frontend: HTML, CSS, JavaScript, jQuery, SweetAlert
- Backend: Java Servlets, JSP, JDBC, DAO Pattern, MVC
- Database: MySQL
- Testing: JUnit 5
- Security: jBCrypt, JavaMail
- Server: Apache Tomcat 9
- Maven
- Git + GitHub for version control

## 📁 Folder Structure

PahanaEdu-WebManagement/            
├── src/            
│   ├── main/            
│   │   ├── java/com/pahanaedu/            
│   │   │   ├── controller/   
│   │   │   ├── dao/            
│   │   │   ├── model/          
│   │   │   └── util/           
│   │   └── webapp/
│   │       ├── css/            
│   │       ├── js/             
│   │       ├── fonts/          
│   │       ├── META-INF/       
│   │       ├── WEB-INF/        
│   │       └── *.jsp           
│   │
│   └── test/java/com/pahanaedu/test/   
│
├── target/                     
├── pom.xml                    
├── README.md                   


## 💻 Setup Instructions

1. Clone the repo:
   ```bash
   git clone https://github.com/yourusername/PahanaEdu-WebManagement.git
   
 2. Import as Maven project in your IDE (Eclipse/IntelliJ).

 3. Configure Tomcat server and MySQL DB.

 4. Update DB credentials in DBUtil.java.

 5. Run the application on http://localhost:8090/PahanaEdu-WebManagement

🙋‍♂️ Author - PPD Fernando
pasindudiloshan — GitHub
pasindudiloshan@gmail.com - Email

   
