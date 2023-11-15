package twitterapp.model;

public interface Visitor {
    void visit(User user);
    void visit(Group group);
}
