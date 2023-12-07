package twitterapp.model;

import jdk.swing.interop.SwingInterOpUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.UUID;

public class User extends Subject implements Composite, Observer {

    private String userName;
    private String userID;
    private ArrayList<String> followingList = new ArrayList<>();
    private DefaultListModel userListModel = new DefaultListModel();
    private DefaultListModel<String> messageListModel = new DefaultListModel();
    private long creationTime; // create time 
    private long lastUpdateTime;

    // the message count
    private ArrayList<String> messageArrayList = new ArrayList<>();

    // JTextArea where the newsFeed will be displayed
    private JTextArea newsFeedTextField = new JTextArea();


    public JTextArea getNewsFeedTextField() {
        return newsFeedTextField;
    }


    public User(String studentName) {
        this.userName = studentName;
        this.userID = UUID.randomUUID().toString();
        this.creationTime = System.currentTimeMillis(); // Initialize creationTime

    }


    public ArrayList<String> getFollowingList() {
        return this.followingList;
    }
    public String getUserName() {
        return this.userName;
    }


    public DefaultListModel getUserListModel() {
        return userListModel;
    }

    public DefaultListModel getMessageListModel() {
        return messageListModel;
    }

    public String getID() {
        return userID;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    // Visitor pattern method to accept a visitor
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);

    }

    // Implement observer pattern
    @Override
    public void update(Subject subject) {
        JTextArea subjectTextArea = this.getNewsFeedTextField();
        User user = (User) subject;
        subjectTextArea.removeAll();
        subjectTextArea.append(user.getNewsFeedTextField().getText());

    }

    public ArrayList<String> getMessageArrayList() {
        return messageArrayList;
    }
    
    

}