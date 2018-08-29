package com.zgas.tesselar.myzuite.Utilities;

/**
 * Class that contains all the web service URLs.
 *
 * @author jarvizu on 25/09/2017.
 * @version 2018.0.9
 */

public class UrlHelper {

    public static final String LOGIN_URL = "https://login.salesforce.com/services/oauth2/token?grant_type=%1$s&client_id=%2$s&client_secret=%3$s&username=%4$s&password=%5$s";
    public static final String GET_USER_DATA_URL = "https://zetagasureste.my.salesforce.com/services/apexrest/mobile/users?username=%1$s";
    public static final String GET_CASES_URL = "https://zetagasureste.my.salesforce.com/services/apexrest/mobile/orders?operatorId=%1$s";
    public static final String GET_CASES_SUPERVISOR_URL = "https://zetagasureste.my.salesforce.com/services/apexrest/mobile/orders_supervisor?IdSupervisor=%1$s";
    public static final String GET_LEAKS_URL = "https://zetagasureste.my.salesforce.com/services/apexrest/mobile/leaks?operatorId=%1$s ";
    public static final String PUT_ORDER_STATUS_URL = "https://zetagasureste.my.salesforce.com/services/apexrest/mobile/case_update?Id=%1$s";
    public static final String PUT_LEAK_STATUS_URL = "https://zetagasureste.my.salesforce.com/services/apexrest/mobile/closed_leak?Id=%1$s";
    public static final String PUT_INCIDENCE_URL = "https://zetagasureste.my.salesforce.com/services/apexrest/mobile/report_incidence?operatorId=1$s";
    public static final String PUT_EXTRA_ORDER = "https://zetagasureste.my.salesforce.com/services/apexrest/mobile/extra_order?operatorId=1$s ";
    public static final String PUT_REVIEW_URL = "https://zetagasureste.my.salesforce.com/services/apexrest/mobile/order_review?orderId=1$s";
    public static final String PUT_TIME_SEEN_URL = "https://zetagasureste.my.salesforce.com/services/apexrest/mobile/display_date";

    public static final String ADMIN_EMAIL = "gener.ventura@grupozeta.biz";
    public static final String ADMIN_PASS = "Tess2018";
    public static final String GRANT_TYPE = "password";
    public static final String CLIENT_ID = "3MVG9zlTNB8o8BA0zh0aLo6sw8RP2vqWBObNH6eKsED5xgpV7atzaloRhV9S7ErS7TZFbxTsYs46RLb9POGMV";
    public static final String CLIENT_SECRET = "4341326727909512102";
    public static final String AUTH_KEY = "Bearer ";
}
