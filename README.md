<h1 align="center">BookShop</h1>
<h2 align="center">module 5 Home work</h2>
<p align="center">
  
<img src="https://img.shields.io/badge/Java-8-red.svg">

<img src="https://img.shields.io/badge/spring__webmvc-5.2.8-brightgreen.svg" >

<img src="https://img.shields.io/badge/Thymeleaf-3.x-blue.svg">

<img src="https://img.shields.io/badge/H2-1.4.x-9cf.svg">

<img src="https://img.shields.io/badge/spring--jdbc-5.2.8-lightgrey.svg">

</p>

## Description
<div>
<h4>In module 6 were done:</h4>
<ul>
<li>Enable pagination for "New" and "Popular" feeds on the main page 🦾</li>
<li>Implement a list of books:</li>
<li>new 🦾🦾</li>
<li>by popular 🦾🦾</li>
<li>by tags 🦾🦾🦾</li>
<li>by genre 🦾🦾🦾</li>
<li>Implement author page</li>
</ul>
</div>

## How it works
if you follow the link
http://localhost:8080/bookshop/main
you can see the main page of the web app, then you can click on the any tab
and you will see the corresponding pages with all needed information, it's not a static content, 
this is the content that is returned by the controller.
There are no boreplote code (create, open/close connection to the database) in the project, because of using jpa (hibernate realization),
now it's all happening under the hood and it did not affect the application in any way.
## Project setup
<h4>To build the app you should copy this links into your .bat file and run it</h4>
<p>replace JAVA_HOME with your path</p>
@echo build maven project <br>
set JAVA_HOME=C:\Program Files\Java\jdk-8.0 <br>
call mvn clean -Dmaven.test.skip package <br>
pause
