package twitterapp.model;

public class UserCount implements Visitor {
    // Get the count of visitors
    private int userCount = 0;

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    // Iterate the count by 1 when there's a visitor
    public void visit(User user) {
        setUserCount(userCount + 1);
    }

    // If it's a group value don't change
    @Override
    public void visit(Group group) {
       setUserCount(userCount);
    }
}