package twitterapp.model;

import javax.swing.*;

public class TotalMessage implements Visitor{

    // Stores the count of messages by the end of the call
    private int messageCount = 0;

    public void visit(User user) {
        DefaultListModel messages = user.getMessageListModel();
        setMessageCount(getMessageCount() + messages.getSize());
    }

    // Don't change count if visit a group
    @Override
    public void visit(Group group) {
        setMessageCount(getMessageCount());

    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }
}