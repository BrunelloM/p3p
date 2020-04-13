package model;

import java.util.List;

import fx.AlertManager;
import fx.MainApplication;
import io.DataSource;
import io.NetworkDataSource;
import javafx.collections.transformation.FilteredList;
import shared.protocol.Response;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import shared.Email;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 *
 * Model class of the MVC pattern.
 * It holds an observable (and filterable) dataset as well as the DataSource interface
 * on which it will interact in order to retrieve/modify/add data.
 */
public class MainModel {

    private ScheduledExecutorService refresherScheduler = Executors.newScheduledThreadPool(1);
    private static final int REFRESH_RATE_SECONDS = 5;

    private FilteredList<Email> emailList;
    private ObservableList<Email> dataset;
    private final DataSource dataSource;

    private final Predicate<Email> inboxFilter = email -> email.getType() == Email.Type.RECEIVED || email.getType() == Email.Type.SPECIAL;
    private final Predicate<Email> sentFilter = email -> email.getType() == Email.Type.SENT;
    private final Predicate<Email> specialFilter = email -> email.getType() == Email.Type.SPECIAL;
    private final Predicate<Email> trashFilter = email -> email.getType() == Email.Type.TRASH;

    public MainModel() {
        dataset = FXCollections.observableArrayList();
        emailList = new FilteredList<>(dataset);
        emailList.setPredicate(inboxFilter);
        dataSource = new NetworkDataSource();
    }

    /**
     * Bind the current dataset on the give ListView
     * @param listView ListView fx widget on which the dataset will be bind.
     */
    public void bind(ListView<Email> listView) {
        Objects.requireNonNull(listView);
        if(listView.getItems() != emailList) {
            listView.setItems(emailList);
            // Refresh data every REFRESH_RATE_SECONDS.
            refresherScheduler.scheduleAtFixedRate(this::refresh, 0, REFRESH_RATE_SECONDS, TimeUnit.SECONDS);
        }
    }

    /**
     * Refresh the dataset of the model.
     * When the data are ready the dataset is cleared and then new data are added to the dataset.
     * Since the DataSet is of an asynchronous type, the operations on it will requires a callback.
     * Platform.runLater tells the FXApplication to refresh data on the UI thread.
     */
    public void refresh() {
        DataSource.DataSourceCallback<List<Email>> callback = item -> {
            for(Email email : item) {
                if(!dataset.contains(email)) {
                    Platform.runLater(() -> dataset.add(email));
                }
            }
        };
        dataSource.get(callback);
    }

    /**
     * Delete a specific Email from the DataSource.
     * @param email Email to be deleted from the DataSource
     */
    public void delete(Email email) {
        DataSource.DataSourceCallback<Response> callback = response -> {
           if(response.getType() == Response.Type.DONE) {
               Platform.runLater(() -> dataset.remove(email));
           } else {
               AlertManager.showError(response.getMessage());
           }
        };
        dataSource.delete(email, callback);
    }

    /**
     * Star a specific Email on the DataSource.
     * @param email Email to be starred on the DataSource.
     */
    public void star(Email email) {
        DataSource.DataSourceCallback<Response> callback = response -> {
            if(response.getType() == Response.Type.DONE) {
                Platform.runLater(() -> email.setType(Email.Type.SPECIAL));
            } else {
                AlertManager.showError(response.getMessage());
            }
        };
        dataSource.star(email, callback);
    }

    public void send(Email email) {
        DataSource.DataSourceCallback<Response> callback = response -> {
            if(response.getType() == Response.Type.ERROR) {
                AlertManager.showError(response.getMessage());
            } else {
                MainApplication.switchToMain();
            }
        };
        dataSource.send(email, callback);
    }

    /**
     * Filter the dataset on the given Email type.
     * @param emailType The type of Email on which the dataset should be filtered
     */
    public void filter(Email.Type emailType) {
        switch(emailType) {
            case RECEIVED:
                emailList.setPredicate(inboxFilter);
            break;

            case SENT:
                emailList.setPredicate(sentFilter);
            break;

            case SPECIAL:
                emailList.setPredicate(specialFilter);
            break;

            case TRASH:
                emailList.setPredicate(trashFilter);
            break;

            default:
                throw new IllegalArgumentException("Invalid argument on filter");
        }
    }

    /**
     * Close the refresher thread
     */
    public void tearDown() {
        refresherScheduler.shutdown();
        dataSource.tearDown();
    }

}
