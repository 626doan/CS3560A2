package twitterapp.gui;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import twitterapp.model.User;
import twitterapp.model.Group;
import twitterapp.model.Composite;
import twitterapp.model.Observer;
import twitterapp.model.Visitor;
import twitterapp.model.Subject;
import twitterapp.model.UserCount;
import twitterapp.model.GroupCount;
import twitterapp.model.TotalMessage;
import twitterapp.model.PositivePercentage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;






public class AdminPanel extends JPanel {
    private JTree tree;
    private JTextField userIDTextField, groupIDTextField;
    private JButton addUserButton, addGroupButton, openUserViewButton, showUserTotalButton;
    private JButton showGroupTotalButton, showMessagesTotalButton, showPositivePercentageButton;
    private ArrayList<User> userArrayList;
    private ArrayList<String> userNameArrayList;
    private JButton validateIDsButton, findLastUpdatedUserButton;  // New button for A3



    public AdminPanel() {
        super(new GridLayout(1, 0));
        userArrayList = new ArrayList<>(10);
        userNameArrayList = new ArrayList<>(10);

        Group top = new Group("root");
        DefaultTreeModel treeModel = new DefaultTreeModel(top);
        tree = new JTree(treeModel);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        JScrollPane treeView = new JScrollPane(tree);
        add(treeView);

        JPanel rightPanel = new JPanel(new GridLayout(3, 0, 0, 50));
        JPanel upperRightPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        JPanel middleRightPanel = new JPanel();
        JPanel lowerRightPanel = new JPanel(new GridLayout(2, 2, 10, 5));

        userIDTextField = new JTextField();
        addUserButton = new JButton("Add User");
        groupIDTextField = new JTextField();
        addGroupButton = new JButton("Add Group");
        openUserViewButton = new JButton("Open User View");
        showUserTotalButton = new JButton("Show User Total");
        showGroupTotalButton = new JButton("Show Group Total");
        showMessagesTotalButton = new JButton("Show Messages Total");
        showPositivePercentageButton = new JButton("Show Positive Percentage Total");
        validateIDsButton = new JButton("Validate ID");
        findLastUpdatedUserButton = new JButton("Find Last Updated User");


        JScrollPane buttonView = new JScrollPane(rightPanel);
        upperRightPanel.add(userIDTextField);
        upperRightPanel.add(addUserButton);
        upperRightPanel.add(groupIDTextField);
        upperRightPanel.add(addGroupButton);
        middleRightPanel.add(openUserViewButton);
        lowerRightPanel.add(showUserTotalButton);
        lowerRightPanel.add(showGroupTotalButton);
        lowerRightPanel.add(showMessagesTotalButton);
        lowerRightPanel.add(showPositivePercentageButton);
        lowerRightPanel.add(validateIDsButton);
        lowerRightPanel.add(findLastUpdatedUserButton);


        rightPanel.add(upperRightPanel);
        rightPanel.add(middleRightPanel);
        rightPanel.add(lowerRightPanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(treeView);
        splitPane.setRightComponent(buttonView);

        Dimension minimumSize = new Dimension(100, 100);
        Dimension maximumSize = new Dimension(500, 500);
        buttonView.setMinimumSize(minimumSize);
        buttonView.setMaximumSize(maximumSize);
        treeView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(200);
        splitPane.setPreferredSize(new Dimension(750, 300));

        add(splitPane);

        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createUser(top, userIDTextField.getText());
                treeModel.reload();
            }
        });

        addGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createGroup(top, groupIDTextField.getText());
                treeModel.reload();
            }
        });

        openUserViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode selectedUser = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (selectedUser != null && !selectedUser.getAllowsChildren()) {
                    createAndShowUserGUI(selectedUser);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Error: A group was selected",
                            "Error ", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        showUserTotalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserCount userVisitor = new UserCount();
                top.accept(userVisitor);
                JOptionPane.showMessageDialog(null,
                        "User count: " + userVisitor.getUserCount(),
                        "User Count", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        showGroupTotalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GroupCount groupVisitor = new GroupCount();
                top.accept(groupVisitor);
                JOptionPane.showMessageDialog(null,
                        "Group count: " + groupVisitor.getGroupCount(),
                        "Group Count", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        showMessagesTotalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TotalMessage messageVisitor = new TotalMessage();
                top.accept(messageVisitor);
                int messageCount = messageVisitor.getMessageCount();
                JOptionPane.showMessageDialog(null,
                        "Message count: " + messageCount,
                        "Message Count", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        showPositivePercentageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TotalMessage messageVisitor = new TotalMessage();
                top.accept(messageVisitor);
                int messageCount = messageVisitor.getMessageCount();

                PositivePercentage positivePercentageVisitor = new PositivePercentage();
                top.accept(positivePercentageVisitor);
                double positivePercentage = positivePercentageVisitor.getPositiveWord();

                positivePercentage = calculatePositivePercentage(positivePercentage, messageCount);

                JOptionPane.showMessageDialog(null,
                        "Positive messages percentage: %" + positivePercentage,
                        "Positive messages percentage", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        validateIDsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validateIDs();
            }
        });

        findLastUpdatedUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findLastUpdatedUser();
            }
        });
    }

    private void createUser(DefaultMutableTreeNode top, String userName) {
        User newUserNode = new User(userName);
        userNameArrayList.add(userName);
        userArrayList.add(newUserNode);

        Group group = (Group) top;
        DefaultMutableTreeNode user;
        DefaultMutableTreeNode parent;
        TreePath parentPath = tree.getSelectionPath();

        user = new DefaultMutableTreeNode(userName, false);
        if (parentPath == null) {
            parent = top;
        } else {
            parent = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        }

        parent.add(user);
        ArrayList<Composite> topCompositeList = group.getCompositeList();
        topCompositeList.add((Composite) newUserNode);
    }

    private void createGroup(DefaultMutableTreeNode top, String groupName) {
        DefaultMutableTreeNode group;
        DefaultMutableTreeNode parent;
        TreePath parentPath = tree.getSelectionPath();
        group = new DefaultMutableTreeNode(groupName, true);
        if (parentPath == null) {
            parent = top;
        } else {
            parent = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        }

        Group newGroup = new Group(groupName);
        ArrayList<Composite> newList = new ArrayList<>();
        newGroup.setList(newList);
        parent.add(group);

        Group test = (Group) top;
        ArrayList<Composite> topCompositeList = test.getCompositeList();
        topCompositeList.add((Composite) newGroup);
    }

    void createAndShowUserGUI(DefaultMutableTreeNode user) {
        JFrame userViewFrame = new JFrame(user.toString());
        String userFrameName = user.toString();
        int currentUserIndex = userNameArrayList.indexOf(userFrameName);
        User currentUser = userArrayList.get(currentUserIndex);

        JPanel rightPanel = new JPanel(new GridLayout(4, 0, 0, 50));
        JPanel firstUserPanel = new JPanel(new GridLayout(1, 2, 10, 5));
        JPanel secondUserPanel = new JPanel();
        JPanel thirdUserPanel = new JPanel(new GridLayout(1, 2, 10, 5));
        JPanel fourthUserPanel = new JPanel();

        DefaultListModel listModel = currentUser.getUserListModel();

        JTextField userViewUserIDTextField = new JTextField();
        JButton followUserButton = new JButton("Follow User");
        firstUserPanel.add(userViewUserIDTextField);
        firstUserPanel.add(followUserButton);

        JList<String> followingListDisplay = new JList<>(listModel);
        JScrollPane listPane = new JScrollPane(followingListDisplay);
        secondUserPanel.add(listPane);
        listPane.setPreferredSize(new Dimension(500, 300));

        DefaultListModel messageModel = currentUser.getMessageListModel();

        JTextField tweetMessageTextField = new JTextField("");
        JButton postTweetButton = new JButton("Post Tweet");
        thirdUserPanel.add(tweetMessageTextField);
        thirdUserPanel.add(postTweetButton);

        JTextArea currentUserNewsFeedTextField = currentUser.getNewsFeedTextField();

        JScrollPane messagePane = new JScrollPane(currentUserNewsFeedTextField);
        fourthUserPanel.add(messagePane);
        messagePane.setPreferredSize(new Dimension(500, 100));

        rightPanel.add(firstUserPanel);
        rightPanel.add(secondUserPanel);
        rightPanel.add(thirdUserPanel);
        rightPanel.add(fourthUserPanel);
        rightPanel.setPreferredSize(new Dimension(500, 500));

        userViewFrame.add(rightPanel);
        userViewFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        userViewFrame.pack();
        userViewFrame.setVisible(true);

        followUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> currentUserFollowingList = currentUser.getFollowingList();
                String textFieldInput = userViewUserIDTextField.getText();
                int userToFollowIndex = userNameArrayList.indexOf(textFieldInput);
                User userToFollow = userArrayList.get(userToFollowIndex);

                if (userNameArrayList.contains(textFieldInput)
                        && userNameArrayList.contains(userFrameName)) {
                    currentUserFollowingList.add(userToFollow.getUserName());
                    listModel.clear();
                    listModel.addElement(currentUserFollowingList);
                    userToFollow.attach(currentUser);
                }
            }
        });

        postTweetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode selectedUserNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                String textFieldInput = getFormattedDate() + " " + currentUser.getUserName() + ": " + tweetMessageTextField.getText();
                currentUserNewsFeedTextField.setText(currentUserNewsFeedTextField.getText() + "\n" + textFieldInput);
                messageModel.addElement(currentUserNewsFeedTextField);
                currentUser.notifyObservers();
                currentUser.getMessageArrayList().add(tweetMessageTextField.getText());

                if (selectedUserNode != null && !selectedUserNode.getAllowsChildren()) {
                    User selectedUser = (User) selectedUserNode.getUserObject();
                    selectedUser.setLastUpdateTime(System.currentTimeMillis());
                }
                
            }
        });
    }

    private void validateIDs() {
        Set<String> usedIDs = new HashSet<>();
        boolean allValid = true;

        // Traverse the tree and validate user and group IDs
        for (int i = 0; i < tree.getModel().getChildCount(tree.getModel().getRoot()); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getModel().getChild(tree.getModel().getRoot(), i);

            if (node.getAllowsChildren()) {
                Group group = (Group) node.getUserObject();
                String groupID = group.getID();
                if (!isValidID(groupID, usedIDs)) {
                    allValid = false;
                    break;
                }
            } else {
                User user = (User) node.getUserObject();
                String userID = user.getID();
                if (!isValidID(userID, usedIDs)) {
                    allValid = false;
                    break;
                }
            }
        }

        // Display validation result
        if (allValid) {
            JOptionPane.showMessageDialog(null, "All IDs are valid.", "ID Validation", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid IDs found. Please ensure all IDs are unique and do not contain spaces.",
                    "ID Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidID(String id, Set<String> usedIDs) {
        // Check if the ID is not empty and does not contain spaces
        if (id == null || id.trim().isEmpty() || id.contains(" ")) {
            return false;
        }
    
        // Check if the ID is unique
        if (usedIDs.contains(id)) {
            return false;
        }
    
        // Add the ID to the set of used IDs
        usedIDs.add(id);
        return true;
    }
    


    public String getFormattedDate() {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return date.format(dateFormat);
    }

    private double calculatePositivePercentage(double positiveWordOccurrence, int getMessageCount) {
        positiveWordOccurrence = positiveWordOccurrence / (double) getMessageCount;
        positiveWordOccurrence *= 100;
        return positiveWordOccurrence;
    }

    void createAndShowGUI() {
        JFrame frame = new JFrame("Mini-Twitter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new AdminPanel());
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminPanel().createAndShowGUI());
    }

    private void findLastUpdatedUser() {
        User lastUpdatedUser = findLastUpdatedUserRecursively((DefaultMutableTreeNode) tree.getModel().getRoot());
        if (lastUpdatedUser != null) {
            JOptionPane.showMessageDialog(null, "Last updated user: " + lastUpdatedUser.getUserName(),
                    "Last Updated User", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No user found.", "Last Updated User", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private User findLastUpdatedUserRecursively(DefaultMutableTreeNode node) {
        User lastUpdatedUser = null;
        long maxUpdateTime = Long.MIN_VALUE;
    
        for (int i = 0; i < tree.getModel().getChildCount(node); i++) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) tree.getModel().getChild(node, i);
    
            if (childNode.getAllowsChildren()) {
                // It's a Group node, recursively search within the group
                User userInGroup = findLastUpdatedUserRecursively(childNode);
                if (userInGroup != null && userInGroup.getLastUpdateTime() > maxUpdateTime) {
                    lastUpdatedUser = userInGroup;
                    maxUpdateTime = userInGroup.getLastUpdateTime();
                }
            } else {
                // It's a User node
                User user = (User) childNode.getUserObject();
                if (user.getLastUpdateTime() > maxUpdateTime) {
                    lastUpdatedUser = user;
                    maxUpdateTime = user.getLastUpdateTime();
                }
            }
        }
    
        return lastUpdatedUser;
    }

    
}
