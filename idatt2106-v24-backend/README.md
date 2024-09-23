# Sparesti - Backend

Sparesti is a web application that tries to make saving money fun. Users can create a goal, set a target amount, and track their progress.
The app will give the user varied challenges, curated to their spending habits, to help them save money.
Users also have the ability to set up personalized budgets and track their spending.

## Description

This is the backend of the "Sparesti" web application which is built using Java and Spring Boot.
The backend is responsible for handling the business logic of the application, as well as the communication with the database.
The backend is built using a RESTful API, which allows the frontend to communicate with the backend.
The backend is responsible for handling the creation, updating, and deletion of goals, challenges, budgets, and transactions.
The backend is also responsible for handling the authentication and authorization of users.

## Getting Started

### Dependencies

* Java (version 21 or newer)
* Spring Boot (version 3.2.0 or newer)
* Maven (version 3.9.6 or newer)
* Docker (version 4.28.0 or newer)


### Installing

1. Download and run the newest version of [Docker](https://www.docker.com/products/docker-desktop)

2. Clone the repo from the project [repository](https://gitlab.stud.idi.ntnu.no/idatt2106-v24-9/idatt2106-v24-backend.git):
```sh
git clone https://gitlab.stud.idi.ntnu.no/idatt2106-v24-9/idatt2106-v24-backend.git
```
3. Navigate to the root directory of the project:
```sh
cd idatt2106-v24-backend
```
4. In the root directory of the project, run the following command to build the project:
```sh
docker build -t sparesti-backend .
```


### Executing program

While running Docker, run the following command in the root directory to start the application:
```sh
docker run -p 8080:8080 sparesti-backend
```
The server should now be running on [localhost:8080](http://localhost:8080/).


## Additional information

### Test users

The program contains 5 test users for testing purposes, each containing different test data.


| Username   | Password  |
|------------|-----------|
| testuser1  | password  |
| testuser2  | password  |
| testuser3  | password  |
| testuser4  | password  |
| testuser5  | password  |


### Program Documentation (JavaDoc)

Documentation for the source code can be found in *./javadoc/index.html*. The *index.html* must be opened in a browser.

### API Documentation - [Swagger](http://localhost:8080/swagger-ui/index.html)

The API documentation is available at the following address when the server is running:
```
http://localhost:8080/swagger-ui/index.html
```

## Authors

* Andrea Amundsen
* Sondre Fjellving Andersen
* Heine Mærde Brakstad
* Jens Sæther Jordal
* Johannes Lorentzen
* Mikkel Bentzrud Rasch
* Julia Vik Remøy
* Joachim Grimen Westgaard


## Acknowledgments

### Special thanks to:

* Surya Bahadur Kathayat, our project supervisor - for technical guidance and support
* Grethe Sandstrak, our course manager - for project guidance and support
* Magnus Lutro Allison, our teaching assistant - for general guidance and support
* Tuva Nordbø and Hedda Hatling Schønning, our Product owners - for excellent teamwork and communication
