# 💊 MedTrack — Android Medication Management App

MedTrack is an Android medication management application built with **Kotlin and Jetpack Compose**. It helps patients manage medications, track symptoms, and access AI-assisted health information through an intuitive mobile interface.

The application follows the **Model–View–ViewModel (MVVM)** architecture and integrates **Room Database**, **OpenFDA**, and **Google Gemini AI**.

## ✨ Key Features

### 🔐 Patient Authentication
- Patient account claiming and login using Room-backed validation
- Persistent login sessions using SharedPreferences
- Logout functionality with navigation back-stack clearing

### 💊 Medication Management
- View and manage medications associated with individual patients
- Add new medications with dosage and frequency information
- Track daily medication intake with persistent taken-status toggles
- Automatically reset daily taken status based on the current date

### 📋 Symptom Tracking
- View patient-specific symptom history
- Display symptom categories, severity, notes, and timestamps
- Retrieve and organise symptom records from Room Database

### 🔎 Drug Information — OpenFDA
- Search medication information using the OpenFDA API
- Retrieve drug purposes, dosage information, and warnings
- Handle unavailable results and network failures

### 🤖 AI-Powered MedCoach
- Generate personalised medication tips using Google Gemini AI
- Incorporate patient medication details and symptom history into AI prompts
- Save generated tips to Room Database
- View previously generated tips through a history dialog

### ⚠️ AI-Assisted Drug Interaction Warnings
- Compare newly added medications against a patient's existing medication list
- Use Gemini AI to identify potential drug interactions
- Display warning dialogs before medication records are saved
- Allow patients to cancel or proceed after reviewing a warning

### 📊 Clinician Dashboard
- Access a clinician-facing dashboard
- Display aggregated patient and medication statistics
- Identify common symptoms and average symptom severity
- Generate AI-assisted insights based on database statistics

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Kotlin | Android application development |
| Jetpack Compose | Declarative user interface |
| MVVM | Separation of UI, application logic and data access |
| Room Database | Persistent local storage |
| Kotlin Coroutines & StateFlow | Asynchronous operations and reactive UI |
| Retrofit | OpenFDA REST API integration |
| OpenFDA API | Medication information |
| Google Gemini API | AI-generated tips, insights and interaction warnings |
| SharedPreferences | Session persistence and database seeding state |
| Gradle | Build configuration and dependency management |

## 🏗️ Architecture

MedTrack follows the MVVM architectural pattern to separate presentation, business logic and data access.

```text
Jetpack Compose UI
        |
        v
     ViewModel
        |
        v
    Repository
        |
        +----------------------+
        |                      |
        v                      v
    Room DAO             Network Service
        |                      |
        v                      v
  Room Database        OpenFDA / Gemini API
```

**Architecture components:**

- **Presentation:** Jetpack Compose screens and ViewModels
- **Data:** Room entities, DAOs and repositories
- **Network:** Retrofit/OpenFDA and Gemini API integration
- **Utilities:** Authentication/session management and CSV database seeding

## 📱 Screenshots

*Add screenshots of the application here.*

Recommended screenshots:
- Home dashboard and medication list
- Medication management and daily tracking
- MedCoach drug information and AI tips
- Drug interaction warning dialog
- Clinician dashboard

## 🚀 Getting Started

### Prerequisites

- Android Studio
- Android SDK
- Android emulator or physical Android device
- Google Gemini API key for AI features

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/YOUR_USERNAME/MedTrack-Android.git
   ```

2. Open the project in Android Studio.

3. Create or update the `local.properties` file in the project root.

4. Add your Gemini API key:

   ```properties
   MEDTRACK_GEMINI_API_KEY=YOUR_GEMINI_API_KEY
   ```

5. Sync Gradle and run the application on an Android emulator or device.

**Security:** API keys are excluded from version control. Never commit actual credentials.

## 🗃️ Database Design

MedTrack uses Room Database to store application data, including:

- Patient records
- Medication records
- Symptom history
- Daily medication taken status
- Generated MedCoach tips

Patient-specific records are associated with patients through database relationships.

Initial data is loaded from CSV assets using a one-time database seeding process.

## 💡 Technical Highlights

- Implemented layered MVVM architecture with repositories and Room DAOs
- Integrated external APIs and asynchronous operations using Kotlin coroutines
- Developed patient-specific AI prompts using medication and symptom data
- Persisted medication tracking and AI-generated tip history locally
- Implemented an AI-assisted drug interaction warning workflow
- Built a clinician dashboard combining database aggregation with generative AI

## ⚕️ Disclaimer

MedTrack is an educational software project and is not intended for clinical use. AI-generated medication tips and interaction warnings may be inaccurate or incomplete and should not replace advice from qualified healthcare professionals.

## 👩‍💻 Project Information

**Project:** MedTrack — Android Medication Management App  
**Type:** University Android Development Project  
**Development:** Kotlin, Jetpack Compose, MVVM, Room, Retrofit, Gemini AI

Developed as part of a university assignment to demonstrate Android application development, database integration, architectural design and AI-powered functionality.
