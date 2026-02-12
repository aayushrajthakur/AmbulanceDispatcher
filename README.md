# 🚑 Ambulance Dispatcher (Android - Java)

An Android application that allows users to quickly input emergency details and send them to a centralized **Admin Console (Server)** for ambulance dispatch management.

---

## 📌 Overview
The Ambulance Dispatcher app is designed to simplify emergency reporting.  
Users can enter critical information (patient details, location, and emergency type), which is then transmitted to the **Admin Console** where dispatchers can manage ambulance assignments.

---

## ⚙️ Features
- Simple and fast input form.
- Collects essential emergency details:
  - Patient name
  - Contact number
  - Location
  - Emergency type
- Sends data to the server (Admin Console).
- Lightweight and responsive Android UI.

---

## 🛠️ Tech Stack
- **Frontend (Mobile App):**
  - Android (Java)
  - XML layouts for UI
- **Backend (Admin Console):**
  - Node.js / Java / Python (depending on implementation)
  - Receives and logs requests
  - Displays incoming requests for dispatcher action
- **Communication:**
  - REST API / Firebase Realtime Database / Socket (based on chosen implementation)

---

## 🚀 Getting Started

### Prerequisites
- Android Studio installed
- Java SDK
- Server (Admin Console) running and accessible

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/ambulance-dispatcher-android.git
Open the project in Android Studio.

Configure server URL in Constants.java (or relevant config file).

Build and run the app on an emulator or physical device.

📡 Workflow
User Input (Android App):  
The app collects emergency details.

Data Transmission:  
Information is sent to the server via API call or Firebase.

Admin Console (Server):  
The server logs and displays requests for dispatcher review.

Dispatcher Action:  
Admin assigns an ambulance based on the request details.

📂 Project Structure
Code
ambulance-dispatcher-android/
│── app/src/main/java/com/example/dispatcher/
│   ├── MainActivity.java        # Input form logic
│   ├── ApiClient.java           # Handles server communication
│   └── models/                  # Data models (Request, Patient, etc.)
│── app/src/main/res/layout/
│   └── activity_main.xml        # Input form UI
│── server/                      # Admin Console backend (optional repo)
│── README.md                    # Documentation
