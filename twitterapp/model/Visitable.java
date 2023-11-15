package twitterapp.model;

public interface Visitable {
    public void accept(Visitor visitor);
}