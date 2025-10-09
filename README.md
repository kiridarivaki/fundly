<h1 align="center">Fundly</h1>
<p align="center"><em>Making Budgeting Fun 💸</em></p>

<p align="center">
  <a href="#features">Features</a> •
  <a href="#tech-stack">Tech Stack</a> •
  <a href="#architecture">Architecture</a>
</p>

---

## About Fundly
**Fundly** is a modern budgeting app designed to make personal finance management simple, secure, and even a little fun.  
Track your expenses, share them with friends, set savings goals, manage loan payments and gain insights into your spending habits.

---

## Features
- 💰 **Expense Tracking** – Log and categorize your daily expenses easily.  
- 🎯 **Budget Goals** – Set savings goals and monitor your progress.  
- 🤝 **Shared Goals** – Contribute to shared goals with your friends and family.  
- 📊 **Expense Reports** – Visualize spending patterns over time.  
- 💸 **Manage Loans** – Manage your loan payments and receive payment reminders.  

---

## Tech Stack
- **Spring Boot** – Backend framework
- **Thymeleaf** – Frontend interface  
- **Liquibase** – ORM for database management  
- **Azure Key Vault** – Secure secrets management  
- **Spring Security** – Secure application access  

---

## Architecture
Fundly follows a clean **package-by-feature** structure to ensure a maintainable code structure.
```
Fundly
├── domain
│ ├── auth
│ └── expense
│     ├──adapter.web
│     │     ├── controller 
│     │     ├── dto
│     │     └── validation
│     └──core
│           ├── model 
│           ├── port
│           │     ├── in 
│           │     └── out
│           └── infrastructure
│                 ├── dto 
│                 ├── service 
│                 └── mapper        
├── goal
├── loan
├── user
├── common
│   ├── controller
│   ├── exception
│   ├── enums
│   └── model
└── infrastructure
    └──config 
```
