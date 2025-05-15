# MSISDN Format Checker Service

A Spring Boot microservice for validating MSISDN (phone number) formats with country-specific validation.

## Prerequisites
- Java 21 JDK
- Maven 3.6+
- Git (optional)

## Building the Service
1. Clone the repository:
```bash
git clone https://github.com/your-repo/msisdn-format-checker.git
cd msisdn-format-checker

##build the project
mvn clean package
##run the project
mvn spring-boot:run

##Prodcution-mode
java -jar target/msisdn-format-checker-0.0.1-SNAPSHOT.jar

###Other deployment Options
- Docker
docker build -t msisdn-validator .
docker run -p 8080:8080 msisdn-validator
- Systemd Service (Linux)
[Unit]
Description=MSISDN Validator Service
After=syslog.target

[Service]
User=appuser
ExecStart=/usr/bin/java -jar /opt/msisdn-validator/msisdn-format-checker-0.0.1-SNAPSHOT.jar
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target

enable and start service:
sudo systemctl enable msisdn-validator
sudo systemctl start msisdn-validator

## API Documentation
After starting the service, access Swagger UI at: http://localhost:8080/swagger-ui.html

## Configuration
Default port: 8080 (can be changed in application.properties )

## Postman Collection

You can test the API using this sample Postman collection:

1. Import the following JSON into Postman:
```json
{
  "info": {
    "_postman_id": "your-collection-id",
    "name": "MSISDN Validator",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Validate MSISDN",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"msisdn\": \"+15551234567\",\n  \"countryCode\": \"US\"\n}"
        },
        "url": {
          "raw": "http://localhost:8080/api/msisdn/validate",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api","msisdn","validate"]
        }
      }
    }
  ]
}
```