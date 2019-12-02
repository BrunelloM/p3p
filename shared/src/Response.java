package shared.protocol;

import shared.Email; 

import java.io.Serializable;

public class Response implements Serializable {

    public enum Type {
        DONE,
        NEXT,
        ERROR
    }

    private Type type;
    private String message;
    private Email payload;

    public Response(Type type, String message, Email payload) {
        this.type = type;
        this.message = message;
        this.payload = payload;
    }

    public Response() {
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Email getPayload() {
        return payload;
    }

    public void setPayload(Email payload) {
        this.payload = payload;
    }

    public void setCompletedState() {
        this.type = Type.DONE;
        this.message = "OK";
        this.payload = null;
    }

    public void setCompletedState(Email payload) {
        this.type = Type.DONE;
        this.message = "OK";
        this.payload = payload;
    }

    public void setErrorState(String message) {
        this.type = Type.ERROR;
        this.message = message;
        this.payload = null;
    }

    public void setNextState() {
        this.type = Type.NEXT;
        this.message = "OK";
        this.payload = null;
    }

    @Override
    public String toString() {
        return "Response {" +
                "type=" + type +
                ", message='" + message + '\'' +
                ", payload=" + payload +
                '}';
    }
}
