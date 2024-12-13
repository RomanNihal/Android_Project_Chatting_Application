# Android Chat Application

This repository contains an **Android Chat Application** developed for instant messaging. The app allows users to send and receive real-time text messages, emojis, and multimedia content within a group chat environment. The app is built using **Java** and utilizes Firebase for backend services, providing real-time communication features.

## Features

### 1. **User Authentication**
- **Sign Up and Sign In**:  
  Users can create a new account or sign in using their credentials. Authentication is handled using Firebase Authentication.

### 2. **Real-Time Messaging**
- **Text Messages**:  
  Users can send and receive text messages in real-time within the chat group.
  
- **Emojis**:  
  The app allows users to send emojis to make the conversations more expressive.

- **Multimedia Sharing**:  
  Users can share images, videos, and other multimedia files within the chat. All media is stored on Firebase Cloud Storage.

### 3. **Group Chat**
- Users can participate in group chats, sending messages, images, and emojis to other members in the group.

### 4. **User Interface**
- **Chat Interface**:  
  The app provides a simple and intuitive interface for viewing and sending messages, images, and emojis.

- **Message List**:  
  All messages are displayed in a scrollable chat list with a timestamp to indicate when each message was sent.

- **Profile Management**:  
  Users can update their profile details, including their username and profile picture.

### 5. **Real-Time Data Sync**
- The app uses Firebase Realtime Database to sync messages and media across devices in real-time.

### 6. **Notifications**
- Users receive push notifications for new messages, ensuring they are notified even when they are not actively using the app.

## Technologies Used

- **Language**: Java
- **Backend**: Firebase
  - **Firebase Authentication**: For user authentication.
  - **Firebase Realtime Database**: For real-time message storage and syncing.
  - **Firebase Cloud Storage**: For storing and retrieving multimedia files.
- **UI**: Android SDK, XML for layout design.
- **Push Notifications**: Firebase Cloud Messaging (FCM) for notifying users about new messages.
