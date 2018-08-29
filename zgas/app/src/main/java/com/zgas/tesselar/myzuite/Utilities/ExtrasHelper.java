package com.zgas.tesselar.myzuite.Utilities;

/**
 * This class contains all the strings that are used in the application - for mapping and
 * references - so we only have the reference to this class.
 *
 * @author jarvizu on 19/10/2017.
 * @version 2018.0.9
 */

public class ExtrasHelper {

    public static final String CALL = "tel:12345678910";
    public static final String MY_ZUITE = "myZuite";

    //Login Object
    public static final String LOGIN_JSON_OBJECT_TOKEN = "access_token";
    public static final String LOGIN_JSON_OBJECT_ID = "id";
    public static final String LOGIN_JSON_OBJECT_INSTANCE = "instance_url";
    public static final String LOGIN_JSON_OBJECT_TOKEN_TYPE = "token_type";
    public static final String LOGIN_JSON_OBJECT_ISSUED_AT = "issued_at";
    public static final String LOGIN_JSON_OBJECT_SIGNATURE = "signature";
    public static final String LOGIN_JSON_OBJECT_EMAIL = "email";
    public static final String LOGIN_JSON_OBJECT_PASSWORD = "password";
    public static final String EMAIL_TAG = "email";
    public static final String ADMIN_TOKEN = "access_token";

    //OrdersObject
    public static final String ORDER_JSON_OBJECT_ID = "Id";
    /*Hora en la que se asigna el pedido en SF*/
    public static final String ORDER_JSON_OBJECT_TIME_ASSIGNMENT = "DateTimeAssignment";
    public static final String ORDER_JSON_OBJECT_TIME_SEEN = "SeenDate";
    public static final String ORDER_JSON_OBJECT_TIME_ARRIVAL = "CaseTimeArrival";
    /*Hora en que se programa el pedido. Si el cliente no especifica una hora, es al mismo tiempo que DateTimeAssignment*/
    public static final String ORDER_JSON_OBJECT_TIME_SCHEDULED = "ScheduledDate";
    public static final String ORDER_JSON_OBJECT_STATUS = "Status";
    public static final String ORDER_JSON_OBJECT_STATUS_UPDATE = "StatusUpdate";
    public static final String ORDER_JSON_OBJECT_FAILURE_REASON = "cancelationReason";
    public static final String ORDER_JSON_OBJECT_STATUS_CODE = "StatusCode";
    public static final String ORDER_JSON_OBJECT_PRIORITY = "Priority";
    public static final String ORDER_JSON_OBJECT_USER_NAME = "AccountName";
    public static final String ORDER_JSON_OBJECT_NOTICE = "Notice";
    public static final String ORDER_JSON_TIME_FINISHED = "deliveredDT";
    public static final String ORDER_JSON_TREATMENT = "Treatment";
    public static final String ORDER_JSON_QUANTIFY = "Quantify";
    public static final String ORDER_JSON_NAME = "Name";
    public static final String ORDER_JSON_OPERATOR_NAME = "OperatorName";

    /*Tipo de servicio: estacionario, cilindro...*/
    public static final String ORDER_JSON_OBJECT_SERVICE_TYPE = "ServiceType";
    public static final String ORDER_JSON_OBJECT_ADDRESS = "Address";
    public static final String ORDER_JSON_OBJECT_TYPE = "RecordTypeName";
    public static final String ORDER_JSON_OBJECT_ACCOUNT_NAME = "AccountName";
    public static final String ORDER_JSON_OBJECT_CONTACT_NAME = "ContactName";
    public static final String ORDER_JSON_OBJECT_PAYMENT_METHOD = "PaymentMethod";
    public static final String ORDER_JSON_OBJECT_RECORD_TYPE = "RecordTypeName";
    /*Finalizar pedidos:*/
    public static final String ORDER_JSON_OBJECT_TICKET = "folio";
    public static final String ORDER_JSON_OBJECT_TICKET_FOLIO = "folio";
    public static final String ORDER_JSON_OBJECT_QUANTITY = "quantity";
    public static final String ORDER_JSON_OBJECT_CYLINDER_10 = "cylinder10";
    public static final String ORDER_JSON_OBJECT_CYLINDER_20 = "cylinder20";
    public static final String ORDER_JSON_OBJECT_CYLINDER_30 = "cylinder30";
    public static final String ORDER_JSON_OBJECT_CYLINDER_45 = "cylinder45";
    /*Extra order*/
    public static final String ORDER_JSON_EXTRA_ORDER_NAME = "name";
    public static final String ORDER_JSON_EXTRA_ORDER_PHONE = "phone";
    public static final String ORDER_JSON_EXTRA_ORDER_ADDRESS = "address";

    //UserObject
    public static final String EXTRA_USER_ID = "Id";
    public static final String EXTRA_USER_NAME = "Name";
    public static final String EXTRA_USER_EMAIL = "Email";
    public static final String EXTRA_USER_TYPE = "Position";
    public static final String EXTRA_USER_STATUS = "Status";
    public static final String EXTRA_USER_ROUTE = "Route";
    public static final String EXTRA_USER_ZONE = "Zone";

    //LeakObject
    public static final String LEAK_JSON_OBJECT_ID = "Id";
    public static final String LEAK_JSON_OBJECT_WHO_REPORTS = "WhoReports";
    public static final String LEAK_JSON_OBJECT_SERVICE_TYPE = "RecordTypeName";
    public static final String LEAK_JSON_OBJECT_SUBJECT = "Subject";
    public static final String LEAK_JSON_OBJECT_STATUS = "Status";
    public static final String LEAK_JSON_OBJECT_PRIORITY = "Priority";
    public static final String LEAK_JSON_OBJECT_OPERATOR = "Operator";
    public static final String LEAK_JSON_OBJECT_SALES_NOTE = "FolioSalesNote";
    public static final String LEAK_JSON_OBJECT_DATE_TECHNICIAN = "DateTimeTechnician";
    public static final String LEAK_JSON_OBJECT_DATE_SCHEDULED = "DateTimeScheduled";
    public static final String LEAK_JSON_OBJECT_DATE_END = "DateTimeEnd";
    public static final String LEAK_JSON_OBJECT_SEEN = "SeenDate";
    public static final String LEAK_JSON_OBJECT_DATE_DEPARTURE = "DateTimeDeparture";
    public static final String LEAK_JSON_OBJECT_CYLINDER_CAPACITY = "CylinderCapacity";
    public static final String LEAK_JSON_OBJECT_COLOR = "Color";
    public static final String LEAK_JSON_OBJECT_CHANNEL = "Chanel";
    public static final String LEAK_JSON_OBJECT_ADDRESS = "Address";
    public static final String LEAK_JSON_OBJECT_RESOLUTION_STATUS = "resolution";
    public static final String LEAJ_JSON_OBJECT_CHANNEL_STATUS = "channel";
    public static final String LEAK_JSON_OBJECT_STATUS_UPDATE = "StatusUpdate";
    public static final String LEAK_JSON_OBJECT_NAME= "Name";

    //Incidences
    public static final String INCIDENCE_JSON_OBJECT_ID = "Id";
    public static final String INCIDENCE_JSON_OBJECT_REASON = "reason";
    public static final String INCIDENCE_JSON_OBJECT_TIME = "incidenceDT";

    //Reviews
    public static final String REVIEW_JSON_OBJECT_ORDER_ID = "orderId";
    public static final String REVIEW_JSON_OBJECT_OPERATOR_ID = "operatorId";
    public static final String REVIEW_JSON_OBJECT_REVIEW = "reasonReview";
}
