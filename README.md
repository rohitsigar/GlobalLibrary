

<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://github.com/rohitsigar/GlobalLibrary">
    <img src="https://github.com/rohitsigar/GlobalLibrary/blob/main/app/src/main/res/mipmap-hdpi/app_logo.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">Global Library</h3>

  <p align="center">
    An online platform for coaching Institute 
    <br />
    <a href="https://github.com/rohitsigar/GlobalLibrary"><strong>Explore the docs »</strong></a>
    <br />
    <br />

   
  </p>
</p>



<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary><h2 style="display: inline-block">Table of Contents</h2></summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
       </li>
       <li><a href="#project-setup">Project Setup</a>
        </li>
        <li>
      <a href="#stacks-libraries-and-api-used">Stacks Libraries and API Used</a>
      </li>
        <li><a href="#features">Features</a>
         <ul>
        <li><a href="#attendance-using-location-tracking">Attendance using location Tracking</a></li>
        <li><a href="#authentication-using-otp">Authentication using OTP</a></li>
        <li><a href="#fee-handling-with-online-payment-method">Fee Handling with online payment Method</a></li>
        <li><a href="#quiz-platform">Quiz Platform</a></li>
         <li><a href="#projection-of-interesting-facts">Projection of Interesting Facts</a></li>
          <li><a href="#other-features">Other Features</a></li>
      </ul>
        </li>
    <li><a href="#glimpses-of-the-project">Glimpses of the project</a></li>
    </li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

# About The Project
 <a href="https://github.com/rohitsigar/GlobalLibrary">
    <img src="https://github.com/rohitsigar/GlobalLibrary/blob/main/app/src/main/res/drawable/flash_screen.png" alt="Logo" width="40" height="70">
  </a>
  
  
**This Platform provides lots of features that can be used by students and The Institute can capture its student’s activities.**

# Project Setup

* Clone the repo.
```sh
  git clone https://github.com/rohitsigar/GlobalLibrary.git 
  ```
* Create a new project in Google Firestore .
```sh
  * Visit https://console.firebase.google.com/ and add Project 
  * Connnect Google firestore Project with this Project.
  * Add SHA -1 from android studio( Gradle/projectName/Tasks/Android/signingReport)
    at SHA-1 certificate fingerprints colunm in Project Settings of the firestore project.
  ```

* Create an Account on https://razorpay.com/ and generate a testing key and add-in PaymentActivity.java file.
  
  ### :blush: Project is ready !!
  
  # Stacks, Libraries, and API Used.
  
  * Android Studio
  * Java
  * Google Firebase Authentication
  * Google FireBase FireStore
  * Google firebase storage
  * Razorpay 
  * Open Trivia DataBase
  * NumbersApi 
  * Valley Library
  * Google Maps
  * Material Ui
  
  # Features
  
  ### Attendance using location Tracking
    
* The Institute will mark the location of the institute using mark location in settings and give the radius of the institute.
* Using the above two values and the location of the student we can check whether a student is present in the Institute or not.
* Using Google play service location API we get the location.
* Calculate the distance between both the locations and if the distance is less the radius of the Institute and Student is
  present in the Institute
  
  
  There is a little inaccuracy in getting location 
    :no_mouth:
    
<img src="https://github.com/rohitsigar/GlobalLibrary/blob/main/app/src/main/res/drawable/attandance_branch.gif" width="200" height="320" /> <img src="https://github.com/rohitsigar/GlobalLibrary/blob/main/app/src/main/res/drawable/mark_attandance.gif" width="200" height="320" /> <img src="https://github.com/rohitsigar/GlobalLibrary/blob/main/app/src/main/res/drawable/show_attandance.gif" width="200" height="320"  />

  ### Authentication using OTP 
* Using firebase Authentication method generating OTP

  ### Fee Handling with online payment Method
* Using Razorpay platform accepting payments online 
* All payment method is available (Phone Pay, G Pay, All Types Of Cards, Net banking, Paytm)

<img src="https://github.com/rohitsigar/GlobalLibrary/blob/main/app/src/main/res/drawable/payment_a.jpeg" width="200" height="320" /> <img src="https://github.com/rohitsigar/GlobalLibrary/blob/main/app/src/main/res/drawable/payment_b.jpeg" width="200" height="320" /> 


  ### Quiz Platform 
* Using Volley Library and Open Trivia DataBase Api Implemented a Quiz Platform.
* 24 types of categories are available for a quiz.
* Manually chooses the difficulty level.

<img src="https://github.com/rohitsigar/GlobalLibrary/blob/main/app/src/main/res/drawable/quiz_given.gif" width="200" height="320" /> <img src="https://github.com/rohitsigar/GlobalLibrary/blob/main/app/src/main/res/drawable/quiz_report.gif" width="200" height="320" /> <img src="https://github.com/rohitsigar/GlobalLibrary/blob/main/app/src/main/res/drawable/catigory_quiz.gif" width="200" height="320" />

  ### Projection of Interesting Facts
* Using Volley Library and NumbersApi Implemented Interesting Facts Features

<img src="https://github.com/rohitsigar/GlobalLibrary/blob/main/app/src/main/res/drawable/facts.gif" width="200" height="320" />

  ### Other Features
* Performance Report of students
* Notification feature
* Can Manually change the fee of Particular Student
* Delete Any Account (Institute access)
* Profile 
* Login, Registration, forget Password 

# Glimpses of the project

<img src="https://github.com/rohitsigar/GlobalLibrary/blob/main/app/src/main/res/drawable/auth_page.gif" width="200" height="320" />                          <img src="https://github.com/rohitsigar/GlobalLibrary/blob/main/app/src/main/res/drawable/delete_account.gif" width="200" height="320" /> <img src="https://github.com/rohitsigar/GlobalLibrary/blob/main/app/src/main/res/drawable/fee.gif" width="200" height="320" />


<img src="https://github.com/rohitsigar/GlobalLibrary/blob/main/app/src/main/res/drawable/profile.gif" width="200" height="320" />                            <img src="https://github.com/rohitsigar/GlobalLibrary/blob/main/app/src/main/res/drawable/notigication.gif" width="200" height="320" /> 






# Contact

  ### Mail me for any assistance [rohitsigar05@gmail.com](rohitsigar05@gmail.com)
  ### Linked In [linkedin.com/in/rohit-sigar-056a19193](linkedin.com/in/rohit-sigar-056a19193)
  

      
  



