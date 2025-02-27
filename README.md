# Suitcase App

## Overview
Suitcase App is a mobile application developed using Android Studio, designed to help users manage their travel packing lists efficiently. The app allows users to create, edit, and organize packing lists for different trips.

## Features
- User authentication and profile management
- Create and manage multiple packing lists
- Add, edit, and delete items in packing lists
- Categorize items (clothing, electronics, toiletries, etc.)
- Checklist functionality to mark items as packed


## Technology Stack
- Android Studio (Java)
- Firebase Authentication for user management
- Firebase Firestore for cloud storage
- Material Design Components

## Installation
1. Clone the repository:
   ```bash
   git clone git@github.com:dipson77/Suitcase.git
   cd Suitcase
   ```
2. Open the project in Android Studio.
3. Sync Gradle files.
4. Configure Firebase:
   - Add `google-services.json` to the `app` directory.
   - Enable Email/Password authentication in Firebase Console.
   - Set up Firestore database.
5. Build and run the app on an emulator or physical device.

## Configuration
- Update `build.gradle` with necessary dependencies.
- Set API keys and other configuration in `res/values/strings.xml`.

## Usage
1. Launch the app.
2. Sign in or create an account.
3. Create a new packing list by entering the trip name and dates.
4. Add items to the list and categorize them.
5. Check off items as they are packed.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Contact
For any inquiries, please contact [dipson769@gmail.com].

