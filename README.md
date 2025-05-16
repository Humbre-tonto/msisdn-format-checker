

---

# **MSISDN Format Checker**

## **Description**
A small microservice that leverages the [Google libphonenumber library](https://github.com/google/libphonenumber) to validate and format phone numbers through a simple API.

## **Features**
- Validate phone numbers for correctness.
- Format phone numbers into standard formats.
- Lightweight microservice for easy integration into other applications.

## **Table of Contents**
- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Development](#development)
- [Docker Usage](#docker-usage)
- [Contributing](#contributing)
- [License](#license)

---

## **Installation**

### **Prerequisites**
- Java (version 21).
- Docker (optional, for containerized usage).

### **Steps**
1. Clone the repository:
   ```bash
   git clone https://github.com/Humbre-tonto/msisdn-format-checker.git
   cd msisdn-format-checker
   ```

2. Build the project:
   ```bash
   mvn clean package
   ```

3. Run the application on :
   ```bash
    mvn spring-boot:run
   ```

---

## **Usage**

Once the service is running, you can use tools like `curl` or Postman to interact with the API.

### **Example Request:**
```bash
curl -X POST http://localhost:8080/api/msisdn/validate \
    -H "Content-Type: application/json" \
    -d '{"msisdn": "+1234567890", "countryCode": "US"}'
```

### **Example Response:**
```json
{
  "isValid": true,
  "message": "Valid MSISDN format for country US"
}
```

---

## **API Endpoints**

### **POST /validate**
- **Description:** Validates and formats a phone number.
- **Request Body:**
  ```json
  {
    "msisdn": "<string>",
    "countryCode": "<string>"
  }
  ```
- **Response:**
  ```json
  {
    "isValid": "<boolean>",
    "message": "<string>"
  }
  ```

---

## **Development**

### **Code Overview**
The application is written in Java and uses the following:
- **Library:** [Google libphonenumber](https://github.com/google/libphonenumber).
- **Build Tool:** Maven.

### **Building and Testing**
Run the following to build and test the project:
```bash
mvn clean package
mvn spring-boot:run
```

---

## **Docker Usage**

### **Building the Docker Image**
Build the Docker image:
```bash
docker build -t humbre-tonto/msisdn-format-checker .
```

### **Running the Docker Container**
Run the container:
```bash
docker run -p 8080:8080 humbre-tonto/msisdn-format-checker
```

The application will be available at `http://localhost:8080`.

---

## **Contributing**

Contributions are welcome! To contribute:
1. Fork the repository.
2. Create a new branch:
   ```bash
   git checkout -b feature-branch-name
   ```
3. Commit your changes and push the branch:
   ```bash
   git push origin feature-branch-name
   ```
4. Open a Pull Request.

---

## **License**

This project is licensed under the [MIT License](LICENSE).

---
