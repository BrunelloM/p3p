package io;

import java.util.List;
import shared.Email;
import shared.protocol.Response;

public interface DataSource {

    void send(Email email, DataSourceCallback<Response> callback);
    void delete(Email email, DataSourceCallback<Response> callback);
    void get(DataSourceCallback<List<Email>> callback);
    void star(Email email, DataSourceCallback<Response> callback);
    void tearDown();

    interface DataSourceCallback<T> {
        void call(T item);
    }

}
