package model;

public class Messages {

    public static final String SERVED_REQUEST = "Request %s from %s has been successfully served.";
    public static final String ERROR_MSG = "Error: %s";
    public static final String INIT_STORAGE= "Initializing server storage...";
    public static final String INIT_USERS_TABLE = "Initializing users table...";
    public static final String SERVER_STARTED = "Core thread started: Listening for incoming connections on port %d.";
    public static final String SERVER_STOPPED = "Server stopped.";
    public static final String SERVER_STOPPING = "Stopping server...";
    public static final String RECEIVED_REQUEST = "Received %s request from %s";

    public static final String ERR_IDENTITY_NOT_FOUND = "Unable to retrieve the given user, Wrong identity";
    public static final String ERR_RECIPIENT_NOT_FOUND = "Unable to retrieve some recipients, Wrong address";
    public static final String ERR_MALFORMED_PARAM = "Unable to retrieve sender from email parameter, Malformed email";
    public static final String ERR_INTERNAL = "Unable to process the request, Internal error";

}