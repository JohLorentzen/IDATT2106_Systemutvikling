# Sparesti - Frontend

Sparesti is a web application that tries to make saving money fun. Users can create a goal, set a target amount, and track their progress.
The app will give the user varied challenges, curated to their spending habits, to help them save money.
Users also have the ability to set up personalized budgets and track their spending.

## Description

This is the frontend of the "Sparesti" web application, which is built using Vue.js and Vite.js.
The frontend is responsible for handling the user interface of the application, as well as the communication with the backend.
Using the frontend, users can navigate through the application, create goals, set target amounts, track their progress, and complete challenges.
Axios is used to communicate with the backend through simple RESTful API calls.


## Getting Started

### Dependencies

* Node.js (20.11.1 or newer)
* Vite.js (^5.2.8)
* Vue.js (^3.4.21)
* TypeScript (^5.4.5)
* Docker (4.28.0 or newer)


### Installing

1. Download and run the newest version of [Docker](https://www.docker.com/products/docker-desktop)

2. Clone the repo from the project [repository](https://gitlab.stud.idi.ntnu.no/idatt2106-v24-9/idatt2106-v24-9-frontend.git):
```
git clone https://gitlab.stud.idi.ntnu.no/idatt2106-v24-9/idatt2106-v24-9-frontend.git
```
3. Navigate to the root directory of the project:
```sh
cd idatt2106-v24-9-frontend
```
4. In the root directory of the project, run the following command to build the project:
```sh
docker build -t sparesti-frontend .
```


### Executing program

While running Docker, run the following command in the root directory to start the application:
```sh
docker run -p 5173:5173 sparesti-frontend
```
The application should now be running on [localhost:5173](http://localhost:5173/).


#### The application can also be accessed by being connected to NTNU's 'eduroam' on the following address: [http://129.241.98.8:5173](http://129.241.98.8:5173/)


## Additional Commands


### Test users

The program contains 5 test users for testing purposes, each containing different test data.


| Username   | Password  |
|------------|-----------|
| testuser1  | password  |
| testuser2  | password  |
| testuser3  | password  |
| testuser4  | password  |
| testuser5  | password  |


### Run Unit Tests with [Vitest](https://vitest.dev/)

```sh
npm run test:unit
```

### Run Unit Tests with Coverage

```sh
npm run test:unit:coverage
```


### Run End-to-End Tests with [Cypress](https://www.cypress.io/)

```sh
npm run test:e2e:dev
```

This runs the end-to-end tests against the Vite development server.
It is much faster than the production build.

But it's still recommended to test the production build with `test:e2e` before deploying (e.g. in CI environments):

```sh
npm run build
npm run test:e2e
```

### Lint with [ESLint](https://eslint.org/)

```sh
npm run lint
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

### Libraries used:
* [FontAwesome](https://www.fontawesome.com/)
* [TailWind CSS](https://www.tailwindcss.com/) - Version ^3.4.3
* [PrimeVue Toast](https://primevue.org/toast/) - Version ^3.52.0
* [Pinia](https://pinia.vuejs.org/) - Version ^2.1.7


### Special thanks to:

* Surya Bahadur Kathayat, our project supervisor - for technical guidance and support
* Grethe Sandstrak, our course manager - for project guidance and support
* Magnus Lutro Allison, our teaching assistant - for general guidance and support
* Tuva Nordbø and Hedda Hatling Schønning, our Product owners - for excellent teamwork and communication




