Blog Application
Introduction
The Blog Application is a platform that allows users to create, edit, and manage their blog posts. Users can interact with posts through features such as commenting, liking, and following other users. The project is built using Java and Spring Boot, with Kafka integration for handling asynchronous events and strong security powered by Spring Security.

Key Features
Post Management: Users can create, edit, delete, and view blog posts. Image upload is supported for posts.
Commenting: Users can add, edit, and delete comments on posts. New comment events are sent through Kafka.
Notifications: Send real-time notifications to users about new activities via WebSocket.
User Following: Users can follow each other and get updates when followed users post new content.
Role and Permission Management: Manage user roles and corresponding permissions to control access and security within the application.
Post Interactions: Users can like or unlike posts, with interaction history stored.
Email Notifications: Integrated with the Brevo (Sendinblue) API to send email notifications for account and post-related events.
Technologies Used
Programming Language: Java 17
Framework: Spring Boot
Database: JPA/Hibernate (PostgreSQL, MySQL)
Security: Spring Security
Event and Notification Management: Kafka, WebSocket
Email Integration: Brevo (Sendinblue) API
Testing: JUnit, Mockito
