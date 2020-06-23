/*
Author: Josh Holt
Temperatura Backend
Versions: Spring Boot 2.3, Java 11.

Purpose of Class: 
This is the enum for the various roles users can have. They can have ADMIN, VIEWER, or both. 

*/
package edge.temperatura.temperatura.models;


public enum UserRole {
    ADMIN,
    VIEWER
}