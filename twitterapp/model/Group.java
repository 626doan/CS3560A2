package twitterapp.model;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.UUID;

public class Group extends DefaultMutableTreeNode implements Composite {
    // store the user and group nodes added to the group
    private ArrayList<Composite> compositeList = new ArrayList<>();
    private String groupID;
    private String groupName;
    private long creationTime; // create time attribute


    public Group(String groupName) {
        this.groupID = UUID.randomUUID().toString();
        this.groupName = groupName;
        this.creationTime = System.currentTimeMillis(); // Initialize creationTime

    }

    public ArrayList<Composite> getCompositeList() {
        return compositeList;
    }
    public void setList(ArrayList<Composite> compositeArrayList) {
        this.compositeList = compositeArrayList;
    }

    public String getID() {
        return groupID;
    }

    public long getCreationTime() {
        return creationTime;
    }

    // Visitor pattern accept method for visitors
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
        for(Composite C : compositeList) {
            C.accept(visitor);
        }

    }
}