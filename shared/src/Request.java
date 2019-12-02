package shared.protocol;

import shared.Email;

import java.io.Serializable;

public class Request implements Serializable {

    public enum Type {
        GET,
        SEND,
        STAR,
        DELETE
    }

    private String identity;
    private Type type;
    private Email emailParam;           // If the request type is just a data request, then ignore this field

    public Request(String identity, Type type, Email emailParam) {
        this.identity = identity;
        this.type = type;
        this.emailParam = emailParam;
    }

    public Request(String identity, Type type) {
        this.identity = identity;
        this.type = type;
        this.emailParam = null;
    }

    public String getIdentity() {
        return identity;
    }

    public Type getType() {
        return type;
    }

    public Email getEmailParam() {
        return emailParam;
    }

}
