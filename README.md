# COMP2000-Software-Engineering-2-Assessment-1: Android Login/Registration Flow Demo

## Project Overview

This project is an Android application developed for the COMP2000 Software Engineering course.  
It fully implements a modern user login and registration workflow, demonstrating key Android development concepts and best practices.

The app features a splash screen, user input validation, persistent storage (Remember Me function), smooth UI animations, and simulated asynchronous operations.

## Key Features

* **Splash Screen**:  
  Uses the `SplashScreen` API introduced in Android 12 to provide a seamless startup experience while initial data loads in the background.

* **Login Functionality**:
  * Supports login via email and password.  
  * Includes a “Remember Me” option using `SharedPreferences` to locally store the user’s email.  
  * Differentiates between admin (`admin/admin123`) and regular registered users.  
  * Dynamically enables or disables the login button based on input validity.  
  * Displays clear error messages for invalid credentials.  
  * Provides clickable text links to the “Forgot Password” and “Register” pages.

* **Registration Functionality**:
  * Offers a registration interface for new users.  
  * Performs real-time validation for email format, password length, and password confirmation matching.  
  * **Password Strength Indicator**: Dynamically displays “Weak,” “Medium,” or “Strong” based on password complexity (length, inclusion of numbers, uppercase letters, and special characters).  
  * Smooth slide-in and slide-out animations enhance the overall user experience.

* **Admin Dashboard**:
  * Provides a dedicated `StaffDashboardActivity` page for administrators (currently a placeholder).  
  * A user dashboard for regular users upon successful login is planned (to be implemented).

* **Enhanced User Experience**:
  * Applies `ObjectAnimator` to achieve smooth UI element animations (such as fade-in and translation of the login card).  
  * Uses `Handler.postDelayed` to simulate network latency, preventing the main thread from blocking during login or registration processes, and displays loading indicators accordingly.

## How to Run

1. Clone this project to your local machine.  
2. Open the project in Android Studio.  
3. Wait for Gradle to complete project synchronization and dependency downloads.  
4. Select the `app` module and run it on an emulator or physical device.

### Test Accounts

* **Admin Account**  
  * **Username**: `admin`  
  * **Password**: `admin123`  

* **Regular User (Mock)**  
  * **Username**: `test@example.com`  
  * **Password**: (any password)

## Tech Stack and Core Components

* **Programming Language**: Java  
* **UI**: XML Layouts  
* **Core Android Components**:
  * `AppCompatActivity`  
  * `androidx.core.splashscreen.SplashScreen`  
  * `SharedPreferences`  
  * `CardView`, `TextInputEditText`, `TextInputLayout` (Material Design Components)  
  * `ObjectAnimator` and `AnimatorSet` (Property Animations)  
  * `Handler` and `Looper` (for delayed tasks and thread communication)  
  * `TextWatcher` (for real-time input monitoring)
