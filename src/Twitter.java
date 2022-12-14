import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.*;
import java.util.*;
import java.awt.*;
import javax.swing.tree.*;

public class Twitter 
{

    private static DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
    private static JTree tree = new JTree(root);
    DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
    private ArrayList<String> users = new ArrayList<String>();
    private ArrayList<String> groups = new ArrayList<String>();
	private ArrayList<Long> userTime = new ArrayList<Long>();
    private ArrayList<Long> updatedTime = new ArrayList<Long>();

    private ArrayList<Long> groupTime = new ArrayList<Long>();
    private ArrayList<String> messages = new ArrayList<String>();

    private ArrayList<DefaultMutableTreeNode> groupsNode = new ArrayList<DefaultMutableTreeNode>();
    DefaultMutableTreeNode selectedNode;
    private ArrayList<User> usersSubject = new ArrayList<User>();
    private ArrayList<Follower> follower = new ArrayList<Follower>();
    private ArrayList<DefaultListModel> models = new ArrayList<DefaultListModel>();

    private int userCounter = 0;

    // Pointer instance so Twitter can only be run through this one instance
    public static Twitter pointer = new Twitter();

    // private Constructor for only one instance of Twitter
    private Twitter() 
	{
        
    }



    public void main() 
	{
        Twitter.pointer.addGroupNode(root);
        JFrame frame = new JFrame("Assignment 2 Admin Control Panel");
        JButton addUserButton = new JButton("Add User");
        JButton addGroupButton = new JButton("Add Group");
        JButton userViewButton = new JButton("Open User View");
        JButton userTotalButton = new JButton("Show User Total");
        JButton groupTotalButton = new JButton("Show Group Total");
        JButton messageTotalButton = new JButton("Show Messages Total");
        JButton positiveButton = new JButton("Show Positive Percentage");
		JButton validateButton = new JButton("Validate");
        JButton lastUpdated = new JButton("Last Updated User");
        JLabel userLabel = new JLabel("User ID: ");
        JLabel groupLabel = new JLabel("Group ID: ");

        addUserButton.setPreferredSize(new Dimension(185, 50));
        addGroupButton.setPreferredSize(new Dimension(185, 50));
        userViewButton.setPreferredSize(new Dimension(185, 50));
        userTotalButton.setPreferredSize(new Dimension(185, 50));
        groupTotalButton.setPreferredSize(new Dimension(185, 50));
        messageTotalButton.setPreferredSize(new Dimension(185, 50));
        positiveButton.setPreferredSize(new Dimension(185, 50));

        userViewButton.addActionListener(new ActionListener() 
		{
            public void actionPerformed(ActionEvent e) 
			{
                if (tree.getLastSelectedPathComponent() == null || !users.contains(selectedNode.toString())) 
				{
                    JFrame frame2 = new JFrame("Error");
                    JLabel label = new JLabel("Please select a user.");

                    JPanel panel = new JPanel(new BorderLayout(5, 5));
                    BoxLayout box = new BoxLayout(panel, BoxLayout.X_AXIS);

                    panel.setLayout(box);
                    panel.add(label);
                    panel.setAlignmentX(Component.CENTER_ALIGNMENT);

                    frame2.add(panel);
                    frame2.setSize(300, 100);
                    frame2.setVisible(true);
                    frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                } 
				else 
				{
                    UserView uv = new UserView();
                    uv.display(selectedNode);
                }

            }
        });

        JTextField userID = new JTextField();
        userID.setPreferredSize(new Dimension(40, 40));

        // calls AddUser's add method through already made instance pointer when Add
    	// User Button is pressed on, sending the text in the userID textfield with it to be validated and then added to the tree
        addUserButton.addActionListener(new ActionListener() 
		{
            public void actionPerformed(ActionEvent e) 
			{
                if (tree.getLastSelectedPathComponent() == null) 
				{
                    AddUser.pointer.add(userID.getText());
                    userID.setText("");
                } 
				else if (groupsNode.contains(selectedNode)) 
				{
                    AddUser.pointer.addUnder(selectedNode, userID.getText());
                    userID.setText("");

                } 
				else 
				{
                    JFrame frame2 = new JFrame("Error");
                    JLabel label = new JLabel("You have selected a user, not a group.");

                    JPanel panel = new JPanel(new BorderLayout(5, 5));
                    BoxLayout box = new BoxLayout(panel, BoxLayout.X_AXIS);

                    panel.setLayout(box);
                    panel.add(label);
                    panel.setAlignmentX(Component.CENTER_ALIGNMENT);

                    frame2.add(panel);
                    frame2.setSize(300, 100);
                    frame2.setVisible(true);
                    frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                }

            }
        });
        lastUpdated.addActionListener(new ActionListener() 
		{
            public void actionPerformed(ActionEvent e) 
			{
                ArrayList<Long> tempTime = Twitter.pointer.getUpdatedTime();
                ArrayList<String> tempUser = Twitter.pointer.getUsers();
                if (tempTime.size() > 0) 
				{
                    Long maxVal = Collections.max(tempTime);
                    Integer maxId = tempTime.indexOf(maxVal);

                    JFrame frame2 = new JFrame("Last Updated User");
                    JLabel label = new JLabel("The Last Updated User is: " + tempUser.get(maxId));

                    JPanel panel = new JPanel(new BorderLayout(5, 5));
                    BoxLayout box = new BoxLayout(panel, BoxLayout.X_AXIS);

                    panel.setLayout(box);
                    panel.add(label);
                    panel.setAlignmentX(Component.CENTER_ALIGNMENT);

                    frame2.add(panel);
                    frame2.setSize(300, 100);
                    frame2.setVisible(true);
                    frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                }
				else 
				{
                    JFrame frame2 = new JFrame("Last Updated User");
                    JLabel label = new JLabel("No messages have been updated.");

                    JPanel panel = new JPanel(new BorderLayout(5, 5));
                    BoxLayout box = new BoxLayout(panel, BoxLayout.X_AXIS);

                    panel.setLayout(box);
                    panel.add(label);
                    panel.setAlignmentX(Component.CENTER_ALIGNMENT);

                    frame2.add(panel);
                    frame2.setSize(300, 100);
                    frame2.setVisible(true);
                    frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                }

            }
        });

        validateButton.addActionListener(new ActionListener() 
		{
            public void actionPerformed(ActionEvent e) 
			{
                ArrayList<String> temp = new ArrayList<>();
                ArrayList<String> tempUsers = pointer.getUsers();
                ArrayList<String> tempGroups = pointer.getGroups();
                temp.addAll(tempUsers);
                temp.addAll(tempGroups);
                int a = 0;
                for (int i = 0; i < temp.size(); i++) 
				{
                    for (int j = i + 1; j < temp.size(); j++) 
					{
                        if (temp.get(i).equals(temp.get(j))) 
						{
                            a = 1;
                        }
                    }
                }
                for (int k = 0; k < temp.size(); k++) 
				{
                    if (temp.get(k).contains(" "))
                        a = 1;
                }
                if (a == 1) 
				{
                    JFrame frame2 = new JFrame("Invalid ID Data");
                    JLabel label = new JLabel("User/Group ID Data is invalid.");

                    JPanel panel = new JPanel(new BorderLayout(5, 5));
                    BoxLayout box = new BoxLayout(panel, BoxLayout.X_AXIS);

                    panel.setLayout(box);
                    panel.add(label);
                    panel.setAlignmentX(Component.CENTER_ALIGNMENT);

                    frame2.add(panel);
                    frame2.setSize(300, 100);
                    frame2.setVisible(true);
                    frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                } 
				else 
				{
                    JFrame frame2 = new JFrame("Valid ID Data");
                    JLabel label = new JLabel("User/Group ID Data is valid.");

                    JPanel panel = new JPanel(new BorderLayout(5, 5));
                    BoxLayout box = new BoxLayout(panel, BoxLayout.X_AXIS);

                    panel.setLayout(box);
                    panel.add(label);
                    panel.setAlignmentX(Component.CENTER_ALIGNMENT);

                    frame2.add(panel);
                    frame2.setSize(300, 100);
                    frame2.setVisible(true);
                    frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                }
            }
        });

        JTextField groupID = new JTextField();
        groupID.setPreferredSize(new Dimension(40, 40));

        // similar to the Add User button, this chunk of code will send the text in groupID to the AddGroup class's add method through its instance pointer and validate the id before adding it to the tree
        addGroupButton.addActionListener(new ActionListener() 
		{

            public void actionPerformed(ActionEvent e) 
			{
                if (tree.getLastSelectedPathComponent() == null) 
				{
                    AddGroup.pointer.add(groupID.getText());
                    groupID.setText("");
                } 
				else if (groupsNode.contains(selectedNode)) 
				{
                    AddGroup.pointer.addUnder(selectedNode, groupID.getText());
                    groupID.setText("");
                } 
				else 
				{
                    JFrame frame2 = new JFrame("Error");
                    JLabel label = new JLabel("You have selected a user, not a group.");

                    JPanel panel = new JPanel(new BorderLayout(5, 5));
                    BoxLayout box = new BoxLayout(panel, BoxLayout.X_AXIS);

                    panel.setLayout(box);
                    panel.add(label);
                    panel.setAlignmentX(Component.CENTER_ALIGNMENT);

                    frame2.add(panel);
                    frame2.setSize(300, 100);
                    frame2.setVisible(true);
                    frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                }

            }
        });

        // This listener added to the tree will save the current selected component
        // of the tree into selectedNode to couple it with any other actions
        // such as clicking on corresponding buttons that may manipulate data
        tree.addTreeSelectionListener(new TreeSelectionListener() 
		{
            @Override
            public void valueChanged(TreeSelectionEvent e) 
			{
                selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

            }
        });

        // Displays the total amount of users by using the visitor pattern set up with
        // the UserTotal Class and corresponding Visitor interface
        userTotalButton.addActionListener(new ActionListener() 
		{
            public void actionPerformed(ActionEvent e) 
			{
                UserTotal ut = new UserTotal();
                Visitor userCheck = new CalculateVisitor();
                ut.accept(userCheck);

            }
        });

        // Displays the total amount of groups by using the visitor pattern set up with
        // the GroupTotal Class and corresponding Visitor interface
        groupTotalButton.addActionListener(new ActionListener() 
		{
            public void actionPerformed(ActionEvent e) 
			{
                GroupTotal gt = new GroupTotal();
                Visitor userCheck = new CalculateVisitor();
                gt.accept(userCheck);

            }
        });

        // Displays the total amount of messages by using the visitor pattern set up
        // with
        // the MessageTotal Class and corresponding Visitor interface and
        // CalculateVisitor class
        messageTotalButton.addActionListener(new ActionListener() 
		{
            public void actionPerformed(ActionEvent e) 
			{
                MessageTotal mt = new MessageTotal();
                Visitor userCheck = new CalculateVisitor();
                mt.accept(userCheck);

            }
        });

        // Displays the percentage of positive messages (messages that include words:
        // good,
        // great, awesome, cool) by using the visitor pattern set up with
        // the PositiveMessages Class and corresponding Visitor interface and
        // CalculateVisitor class
        positiveButton.addActionListener(new ActionListener() 
		{
            public void actionPerformed(ActionEvent e) 
			{
                PositiveMessages pm = new PositiveMessages();
                Visitor userCheck = new CalculateVisitor();
                pm.accept(userCheck);

            }
        });

        JPanel panel1 = new JPanel(new BorderLayout(5, 5));
        JPanel panel2 = new JPanel(new BorderLayout(5, 5));
        JPanel panel3 = new JPanel(new BorderLayout(5, 5));
        JPanel panel4 = new JPanel(new BorderLayout(5, 5));
        JPanel panel5 = new JPanel(new BorderLayout(5, 5));
        JPanel panel6 = new JPanel(new BorderLayout(5, 5));
        JPanel panel7 = new JPanel(new BorderLayout(5, 5));
		JPanel panel8 = new JPanel(new BorderLayout(5, 5));

        BoxLayout box1 = new BoxLayout(panel1, BoxLayout.X_AXIS);
        BoxLayout box2 = new BoxLayout(panel2, BoxLayout.X_AXIS);
        BoxLayout box3 = new BoxLayout(panel3, BoxLayout.X_AXIS);
        BoxLayout box4 = new BoxLayout(panel4, BoxLayout.X_AXIS);
        BoxLayout box5 = new BoxLayout(panel5, BoxLayout.Y_AXIS);
        BoxLayout box6 = new BoxLayout(panel6, BoxLayout.X_AXIS);
        BoxLayout box7 = new BoxLayout(panel7, BoxLayout.X_AXIS);
		BoxLayout box8 = new BoxLayout(panel8, BoxLayout.X_AXIS);

        panel1.setLayout(box1);
        panel2.setLayout(box2);
        panel3.setLayout(box3);
        panel4.setLayout(box4);
        panel5.setLayout(box5);
        panel6.setLayout(box6);
        panel7.setLayout(box7);
		panel8.setLayout(box8);

        panel1.setBorder(new EmptyBorder(new Insets(20, 50, 50, 20)));
        panel2.setBorder(new EmptyBorder(new Insets(20, 50, 50, 20)));
        panel3.setBorder(new EmptyBorder(new Insets(20, 50, 50, 20)));
        panel4.setBorder(new EmptyBorder(new Insets(20, 50, 50, 20)));
        panel5.setBorder(new EmptyBorder(new Insets(20, 50, 50, 20)));
        panel6.setBorder(new EmptyBorder(new Insets(20, 50, 50, 20)));
        panel7.setBorder(new EmptyBorder(new Insets(20, 50, 50, 20)));
		panel8.setBorder(new EmptyBorder(new Insets(20, 50, 50, 20)));

        panel1.add(userLabel);
        panel1.add(userID);
        panel1.add(Box.createGlue());
        panel1.add(addUserButton);
        panel1.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel2.add(groupLabel);
        panel2.add(groupID);
        panel2.add(Box.createGlue());
        panel2.add(addGroupButton);
        panel2.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel3.add(userTotalButton);
        panel3.add(Box.createGlue());
        panel3.add(groupTotalButton);
        panel3.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel4.add(messageTotalButton);
        panel4.add(Box.createGlue());
        panel4.add(positiveButton);
        panel4.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel5.add(panel1);
        panel5.add(panel2);
        panel7.add(userViewButton);
        panel5.add(panel7);
        panel7.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel5.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel5.add(panel3);
        panel5.add(panel4);
		panel8.add(validateButton);
        panel8.add(Box.createGlue());
        panel8.add(lastUpdated);
        panel8.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel5.add(panel8);
        panel5.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel6.add(tree);
        panel6.add(panel5);
        panel6.setAlignmentX(Component.CENTER_ALIGNMENT);

        frame.add(panel6);

        frame.setSize(800, 700);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public ArrayList<String> getMessages() 
	{
        return this.messages;
    }

    public void setMessages(ArrayList<String> messages) 
	{
        this.messages = messages;
    }

    public ArrayList<DefaultListModel> getModels() 
	{
        return this.models;
    }

    public void setModels(ArrayList<DefaultListModel> models) 
	{
        this.models = models;
    }

    public void setUsersSubject(ArrayList<User> usersSubject) 
	{
        this.usersSubject = usersSubject;
    }

    public void setFollower(ArrayList<Follower> follower) 
	{
        this.follower = follower;
    }

    public ArrayList<Follower> getFollower() 
	{
        return this.follower;
    }

    public void addToFollower(Follower follower1) 
	{
        follower.add(follower1);
    }

    public DefaultTreeModel getModel() 
	{
        return this.model;
    }

    public void setModel(DefaultTreeModel model) 
	{
        this.model = model;
    }

    public ArrayList<DefaultMutableTreeNode> getGroupsNode() 
	{
        return this.groupsNode;
    }

    public void setGroupsNode(ArrayList<DefaultMutableTreeNode> groupsNode) 
	{
        this.groupsNode = groupsNode;
    }

    public DefaultMutableTreeNode getSelectedNode() 
	{
        return this.selectedNode;
    }

    public void setSelectedNode(DefaultMutableTreeNode selectedNode) 
	{
        this.selectedNode = selectedNode;
    }

    public ArrayList<User> getUsersSubject() 
	{
        return usersSubject;
    }

    public void addToUsersSubject(User user) 
	{
        usersSubject.add(user);
    }

    public ArrayList<String> getUsers() 
	{
        return users;
    }

    public void setUsers(ArrayList<String> newUsers) 
	{
        users = newUsers;
    }

    public ArrayList<String> getGroups() 
	{
        return groups;
    }

    public void setGroups(ArrayList<String> newGroups) 
	{
        groups = newGroups;
    }

    public int getUserCounter() 
	{
        return userCounter;
    }

    public void setUserCounter(int count) 
	{
        userCounter = count;
    }

    public void addToTree(DefaultMutableTreeNode node) 
	{
        root.add(node);
        model.reload();
    }

    public void addToSelectedGroup(DefaultMutableTreeNode node1, DefaultMutableTreeNode node2) 
	{
        node1.add(node2);
        model.reload();
    }

    public void addGroupNode(DefaultMutableTreeNode node) 
	{
        groupsNode.add(node);
    }
	public ArrayList<Long> getUserTime() 
	{
        return this.userTime;
    }

    public void setUserTime(ArrayList<Long> userTime) 
	{
        this.userTime = userTime;
    }

    public ArrayList<Long> getGroupTime() 
	{
        return this.groupTime;
    }

    public void setGroupTime(ArrayList<Long> groupTime) 
	{
        this.groupTime = groupTime;
    }

    public ArrayList<Long> getUpdatedTime()
	{
        return this.updatedTime;
    }

    public void setUpdatedTime(ArrayList<Long> updatedTime) 
	{
        this.updatedTime = updatedTime;
    }

}