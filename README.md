# product-api

## Pre-requsites
- Java 1.8
- Maven 3.6.2
- git
- Docker
  - If you want to use Mongo docker image, otherwise you have to install MongoDB with default configuration

## Execution Steps
- Run MongoDB
  - docker run -d -p 27017:27017 --name mongodb mongo
- Clone this repo to your work stattion, and go to the project directory product-api
- Package and start the application
  - ./mvnw package && java -jar target/product-api-0.0.1-SNAPSHOT.jar
- Test the application
  - Open your browser and enter http://localhost:8080/api/products/13860420
  - You should get a response similar to this
    {
      "id": 13860420,
      "name": "Final Destination 5 (dvd_video)",
      "current_price": {
      "currency_code": "USD",
      "value": 10.99
      }
    }
- Test Update functionality
  - Run the curl from a command prompt "curl -d '{"id": 13860421, "name": "Revolutionary Girl Utena: Apocalypse Saga Collection (DVD)","current_price": {"currency_code": "USD", "value": 54.99}}' -H "Content-Type: application/json" -X PUT http://localhost:8080/api/products/13860421"
  - Open your browser and enter http://localhost:8080/api/products/13860421
- Play more
  - Here are few more ids you can use to to update prices: 13860423, 13860424, 13860425, 13860427, 13860429
