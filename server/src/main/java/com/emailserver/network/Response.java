package com.emailserver.network;

import com.emailserver.beans.Email;

import java.io.Serializable;

public class Response implements Serializable {

    ResponseType type;
    String message;
    Email payload;

    public Response(ResponseType type, String message, Email payload) {
        this.type = type;
        this.message = message;
        this.payload = payload;
    }

    public Response() {
    }

    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
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
        type = ResponseType.DONE;
        message = "OK";
        payload = null;
    }

    public void setErrorState(String message) {
        type = ResponseType.ERROR;
        this.message = message;
        payload = null;
    }

    @Override
    public String toString() {
        return "Response{" +
                "type=" + type +
                ", message='" + message + '\'' +
                ", payload=" + payload +
                '}';
    }
}