# Conta Vinculada

**Conta Vinculada** is a specialized management system designed to oversee and secure labor rights for employees of third-party companies contracted by public government entities. Following Brazilian administrative regulations (such as **IN SEGES/ME nº 5/2017**), the system ensures that funds for labor charges—like vacations, 13th-month salary, and severance—are provisioned and only released upon proof of compliance.

## 🚀 Overview

The project facilitates the fiscal oversight of outsourced labor contracts. It acts as a bridge between the government (Contracting Party) and private companies (Contracted Party) to mitigate the risk of subsidiary liability by the public administration.

**Key Objectives:**

* **Provision Management:** Automate the calculation and retention of labor percentages (e.g., 8.33% for 13th salary).
* **Compliance Verification:** Validate that companies have fulfilled their obligations before funds are unlocked.
* **Risk Mitigation:** Protect employees' legal rights and prevent "double payment" by the government due to labor lawsuits.

## 🛠️ Tech Stack

### Backend

* **Framework:** Spring Boot (Java)
* **Security:** Spring Security (handling roles for Managers and Fiscal Officers)
* **Data:** Spring Data JPA / Hibernate
* **Database:** Relational (PostgreSQL/MySQL)

### Frontend

* **Framework:** Angular
* **Styling:** CSS/SCSS (Modular components)
* **State Management:** Services & RxJS Observables

## 📋 Key Features

* **Contract Dashboard:** Real-time view of active third-party contracts and their current provisioned balances.
* **Employee Ledger:** Detailed tracking of every employee linked to a specific contract.
* **Release Workflow:** A structured request-and-approval process for releasing funds (Férias, 13º, Rescisão).
* **Fiscal Reports:** Generation of transparency reports for public auditing.

## 🔧 Project Structure

The repository is organized into two main modules:

* `/backend`: The Spring Boot REST API.
* `/frontend`: The Angular single-page application (SPA).

## 🚀 Getting Started

### Backend Setup

1. Navigate to `/backend`.
2. Configure your database credentials in `src/main/resources/application.properties`.
3. Run the application:
```bash
./mvnw spring-boot:run

```



### Frontend Setup

1. Navigate to `/frontend`.
2. Install dependencies:
```bash
npm install

```


3. Start the development server:
```bash
ng serve

```

4. Access the app at `http://localhost:4200`.

## 📄 License

This project is open-source and available under the [MIT License](https://www.google.com/search?q=LICENSE).
