package com.zgas.tesselar.myzuite.Model;

/**
 * Login model
 *
 * @author jarvizu on 28/08/2017
 * @version 2018.0.9
 */
public class Login {
    private String loginId;
    private String loginAccessToken;
    private String loginInstanceUrl;
    private String loginTokenType;
    private String loginIssuedAt;
    private String loginSignature;
    private String loginEmail;

    public Login() {
    }

    public Login(String loginId, String loginAccessToken, String loginInstanceUrl, String loginTokenType, String loginIssuedAt, String loginSignature, String loginEmail) {
        this.loginId = loginId;
        this.loginAccessToken = loginAccessToken;
        this.loginInstanceUrl = loginInstanceUrl;
        this.loginTokenType = loginTokenType;
        this.loginIssuedAt = loginIssuedAt;
        this.loginSignature = loginSignature;
        this.loginEmail = loginEmail;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginAccessToken() {
        return loginAccessToken;
    }

    public void setLoginAccessToken(String loginAccessToken) {
        this.loginAccessToken = loginAccessToken;
    }

    public String getLoginInstanceUrl() {
        return loginInstanceUrl;
    }

    public void setLoginInstanceUrl(String loginInstanceUrl) {
        this.loginInstanceUrl = loginInstanceUrl;
    }

    public String getLoginTokenType() {
        return loginTokenType;
    }

    public void setLoginTokenType(String loginTokenType) {
        this.loginTokenType = loginTokenType;
    }

    public String getLoginIssuedAt() {
        return loginIssuedAt;
    }

    public void setLoginIssuedAt(String loginIssuedAt) {
        this.loginIssuedAt = loginIssuedAt;
    }

    public String getLoginSignature() {
        return loginSignature;
    }

    public void setLoginSignature(String loginSignature) {
        this.loginSignature = loginSignature;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public void setLoginEmail(String loginEmail) {
        this.loginEmail = loginEmail;
    }

}

