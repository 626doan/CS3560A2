package twitterapp.model;

public class GroupCount implements Visitor {
    // Setting a count to store the total group count
    private int groupCount = 0;

    @Override
    // If it's a user keep valie
    public void visit(User user) {
        setGroupCount(groupCount);
    }

    @Override
    // If a group is passed, increment the value of groupCount by 1
    public void visit(Group group) {
        setGroupCount(groupCount + 1);
    }

    // Substract one for root group
    public int getGroupCount() {
        return groupCount - 1;
    }

    public void setGroupCount(int groupCount) {
        this.groupCount = groupCount;
    }
}
