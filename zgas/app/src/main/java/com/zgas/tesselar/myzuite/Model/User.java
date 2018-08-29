package com.zgas.tesselar.myzuite.Model;

/**
 * User model
 *
 * @author jarvizu on 28/08/2017
 * @version 2018.0.9
 */

public class User {
    private String userId;
    private String userType;
    private String userName;
    private String userEmail;
    private String userRoute;
    private String userZone;
    private String userStatus;

    public User(String userId, String userType, String userName, String userEmail, String userRoute, String userZone, String userStatus) {
        this.userId = userId;
        this.userType = userType;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userRoute = userRoute;
        this.userZone = userZone;
        this.userStatus = userStatus;
    }

    /*public enum userStatus {
        ACTIVE("Activo"),
        NOTACTIVE("No activo");

        private final String name;

        private userStatus(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }*/

    /*public enum userType {
        OPERATOR("Operador"),
        SERVICE("Servicio Medido"),
        SUPERVISOR("Supervisor"),
        LEAKAGE("Técnico de Fugas");

        private final String name;

        private userType(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }*/

    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserRoute() {
        return userRoute;
    }

    public void setUserRoute(String userRoute) {
        this.userRoute = userRoute;
    }

    public String getUserZone() {
        return userZone;
    }

    public void setUserZone(String userZone) {
        this.userZone = userZone;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
}
