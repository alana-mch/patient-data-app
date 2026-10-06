# Patient Data App
A Java web application for viewing, searching, and managing patient records stored in a CSV file.
Built using Servlets, JSP, and a simple MVC structure.

## Demo Video
https://youtu.be/7hOt5UCSJMg

## Project Structure
- src/ — Java source code (servlets, model classes, JSPs)
- data/ — CSV patient dataset
- pom.xml — Maven build file
- README.md — Project documentation

## Features
- View all patient records
- Search across all fields
- Add, edit, and delete records
- View statistics 
- CSV-based data storage with validation
- Error handling for invalid input and data loading

## Statistics
The app provides several built‑in statistics:
- Number of patients in a location
- Gender distribution
- Patients in a specific location
- Deceased patients
- Oldest patient(s)
- Youngest patient(s)

## Requirements
- Java 8+
- Maven
- Apache Tomcat 9+
- CSV data file located in /data
- Runs on http://localhost:8080/

## Run using IntelliJ 
1. Open the project in IntelliJ.
2. Use the preconfigured Tomcat run configuration.
3. Click the Run button.
4. Access the app at:
   http://localhost:8080/patientdata

## Purpose
UCL BSc Computer Science Coursework
Module Code: COMP0004

