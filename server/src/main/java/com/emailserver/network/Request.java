package com.emailserver.network;

import com.emailserver.beans.Email;
import com.emailserver.beans.User;

import java.io.Serializable;

public class Request implements Serializable {

    private User identity;
    private RequestType type;
    private Email emailParam;           // If the request type is just a data request, then ignore this field

    public Request(User identity, RequestType type, Email emailParam) {
        this.identity = identity;
        this.type = type;
        this.emailParam = emailParam;
    }

    public Request(User identity, RequestType type) {
        this.identity = identity;
        this.type = type;
        this.emailParam = null;
    }

    public User getIdentity() {
        return identity;
    }

    public RequestType getType() {
        return type;
    }

    public Email getEmailParam() {
        return emailParam;
    }
}
