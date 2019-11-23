package shared; 

import java.io.Serializable;

public class Request implements Serializable {

    private String identity;
    private RequestType type;
    private Email emailParam;           // If the request type is just a data request, then ignore this field

    public Request(String identity, RequestType type, Email emailParam) {
        this.identity = identity;
        this.type = type;
        this.emailParam = emailParam;
    }

    public Request(String identity, RequestType type) {
        this.identity = identity;
        this.type = type;
        this.emailParam = null;
    }

    public String getIdentity() {
        return identity;
    }

    public RequestType getType() {
        return type;
    }

    public Email getEmailParam() {
        return emailParam;
    }

}
