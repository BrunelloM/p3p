package controllers;

import model.MainModel;

/**
 * Every Controller of this project should implement this class in order to be bindable with a model class.
 * The ApplicationStarter will bind them with the same model.
 */
public abstract class BindableController {

    MainModel model;

    public void bindModel(MainModel model) {
        this.model = model;
    }

}
