package twitterapp.model;

public interface Observer {

    // update method that will be used to apply the observer pattern
    public abstract void update(Subject subject);

}
