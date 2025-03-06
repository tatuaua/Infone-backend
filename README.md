# Infone - backend

Infone is a simple application that sends small data points to the user everyday in the form of a notification.
A user can choose which data points they want to receive and at what time they want to receive them.
This is the backend part of the application which only fetches + stores data points and responds to requests.

## Structure

The backend is a simple Spring Boot application that uses a PostgreSQL database to store the data points.
Currently, the application runs locally with a database running in a Docker container.
New data points can be easily added by defining a @Component that implements DataPointFetcher.

## Roadmap

- [x] Create Spring Boot application
- [x] Create PostgreSQL database
- [x] Create Docker Compose file
- [x] Create logic to fetch and store data points
- [ ] Maybe use a non-relational database and add flexibility to data points

## See also
Android app: https://github.com/tatuaua/infone-app