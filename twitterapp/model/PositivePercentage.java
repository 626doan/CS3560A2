package twitterapp.model;

import java.util.ArrayList;

public class PositivePercentage implements Visitor{

    private double positiveWordFound = 0;
    private String positiveWord = "awesome";

    @Override
    public void visit(User user) {

        ArrayList<String> messages = user.getMessageArrayList();
        for(int i = 0; i < messages.size(); i++) {
            if(messages.get(i).contains(positiveWord)) {
                positiveWordFound++;
            }
        }
    }
    @Override
    public void visit(Group group) {
        // If a group is visited, don't change the value
        PositiveWordOccurence(getPositiveWord());

    }

    public double getPositiveWord() {
        return positiveWordFound;
    }

    public void PositiveWordOccurence(double positivePercentage) {
        this.positiveWordFound = positivePercentage;
    }




}