import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserView 
{

    int holder1, holder2, holder3;
    ArrayList<String> following = new ArrayList<String>();

    public UserView() 
	{
        // public Constructor because there must be a separate
    }

    // This is the method called when the button is clicked on. This method
    // will check if the selectedNode is a user and allow different functionalities
    // through the follow button, post button, and refresh button
    public void display(DefaultMutableTreeNode selectedNode) 
	{
        JFrame frame = new JFrame("User View for User ID: " + selectedNode.toString());
		
		ArrayList<Long> tempTime = Twitter.pointer.getUserTime();
        ArrayList<String> tempUsers = Twitter.pointer.getUsers();
        ArrayList<Long> tempUpdatedTime = Twitter.pointer.getUpdatedTime();

        long milliTime = tempTime.get(tempUsers.indexOf(selectedNode.toString()));
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyy HH:mm");
        Date resultDate = new Date(milliTime);
        JLabel time = new JLabel("Creation Time: " + sdf.format(resultDate));

        JButton followButton = new JButton("Follow User");
        JButton postButton = new JButton("Post Tweet");
        JButton refreshButton = new JButton("Refresh Feed");
        JLabel user = new JLabel("User ID: ");
        JLabel tweet = new JLabel("Tweet: ");
        JLabel followList = new JLabel("Following: ");
        JTextField userID = new JTextField(20);
        JTextArea message = new JTextArea(5, 40);
        message.setLineWrap(true);
        message.setWrapStyleWord(true);

        ArrayList<DefaultListModel> models = Twitter.pointer.getModels();
        ArrayList<User> tempUser = Twitter.pointer.getUsersSubject();
        ArrayList<Follower> tempFollower = Twitter.pointer.getFollower();

        ArrayList<String> temp = Twitter.pointer.getUsers();
        holder2 = temp.indexOf(selectedNode.toString());

        DefaultListModel model = models.get(holder2);

        JList followingList = new JList(model);

        DefaultListModel model2 = new DefaultListModel();
        Follower tf = tempFollower.get(holder2);
        ArrayList<String> tweets = tf.getTweets();

        for (int i = 0; i < tweets.size(); i++) 
		{
            if (!model2.contains(tweets.get(i))) 
			{
                model2.addElement(tweets.get(i));
            }
        }
        ArrayList<Long> times = tf.getUpdatedTimes();

        JLabel lastUpdated = new JLabel();

        if (times.size() > 0) 
		{
            long currentTime = times.get(times.size() - 1);
            Date update = new Date(currentTime);
            lastUpdated.setText("Last Updated: " + sdf.format(update));
        } 
		else 
		{
            lastUpdated.setText("Last Updated: no update yet.");
        }		
        JList feed = new JList(model2);

        JScrollPane scrollpane = new JScrollPane(feed);

        // When the follow button is pressed on, this method will first check if
        // the user ID is of an existing user, that the user is not already following
        // said user, and that the user is not trying to follow themselves.
        followButton.addActionListener(new ActionListener() 
		{
            public void actionPerformed(ActionEvent e) 
			{
                if (Twitter.pointer.getUsers().contains(userID.getText()) && !following.contains(userID.getText())
                        && !selectedNode.toString().equals(userID.getText())) 
				{

                    holder1 = temp.indexOf(userID.getText());

                    tempUser.get(holder1).registerObserver(tempFollower.get(holder2));
                    following.add(userID.getText());

                    model.addElement(userID.getText());

                    Twitter.pointer.setUsersSubject(tempUser);
                    Twitter.pointer.setFollower(tempFollower);

                } 
				else if (selectedNode.toString().equals(userID.getText())) 
				{
                    JFrame frame2 = new JFrame("Error");
                    JLabel label = new JLabel("You may not follow yourself.");

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
                    JFrame frame2 = new JFrame("Error");
                    JLabel label = new JLabel("This user ID does not exist or you are already following them.");

                    JPanel panel = new JPanel(new BorderLayout(5, 5));
                    BoxLayout box = new BoxLayout(panel, BoxLayout.X_AXIS);

                    panel.setLayout(box);
                    panel.add(label);
                    panel.setAlignmentX(Component.CENTER_ALIGNMENT);

                    frame2.add(panel);
                    frame2.setSize(400, 100);
                    frame2.setVisible(true);
                    frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                }
                userID.setText("");
            }
        });

        refreshButton.addActionListener(new ActionListener() 
		{
            public void actionPerformed(ActionEvent e) 
			{
                for (int i = 0; i < tweets.size(); i++) 
				{
                    if (!model2.contains(tweets.get(i))) 
					{
                        model2.addElement(tweets.get(i));
                    }
                }
				if (times.size() > 0) 
				{
                    long currentTime = times.get(times.size() - 1);
                    Date update = new Date(currentTime);
                    lastUpdated.setText("Last Updated: " + sdf.format(update));
                } 
				else 
				{
                    lastUpdated.setText("Last Updated: no update yet.");
                }
            }
        });

        // When this button is pressed on, it will first check if the text area
        // is empty. If not, then the method will set the tweet to the user's
        // corresponding User object's setTweet method, while also adding the tweet
        // to the user's own news feed.
        postButton.addActionListener(new ActionListener() 
		{
            public void actionPerformed(ActionEvent e) 
			{
                if (message.getText().isEmpty()) 
				{
                    JFrame frame2 = new JFrame("Error");
                    JLabel label = new JLabel("Please type a tweet first.");

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
                    ArrayList<User> tempUser = Twitter.pointer.getUsersSubject();
					ArrayList<Long> tempTime = Twitter.pointer.getUpdatedTime();

					long milliseconds = System.currentTimeMillis();
                    tempTime.set(holder2, milliseconds);
                    Twitter.pointer.setUpdatedTime(tempTime);
                    User currentUser = tempUser.get(holder2);
                    currentUser.setTweet(selectedNode.toString() + ": " + message.getText(), milliseconds);
                    model2.addElement(selectedNode.toString() + ": " + message.getText());
                    ArrayList<String> tempMessages = Twitter.pointer.getMessages();
                    tempMessages.add(selectedNode.toString() + ": " + message.getText());
                    Twitter.pointer.setMessages(tempMessages);
                    message.setText("");

                }
            }
        });

        JPanel panel1 = new JPanel(new BorderLayout(5, 5));
        JPanel panel2 = new JPanel(new BorderLayout(5, 5));
        JPanel panel3 = new JPanel(new BorderLayout(5, 5));

        BoxLayout box1 = new BoxLayout(panel1, BoxLayout.X_AXIS);
        BoxLayout box2 = new BoxLayout(panel2, BoxLayout.X_AXIS);
        BoxLayout box3 = new BoxLayout(panel3, BoxLayout.Y_AXIS);

        panel1.setLayout(box1);
        panel2.setLayout(box2);
        panel3.setLayout(box3);

        panel1.setBorder(new EmptyBorder(new Insets(20, 50, 50, 20)));
        panel2.setBorder(new EmptyBorder(new Insets(20, 50, 50, 20)));
        panel3.setBorder(new EmptyBorder(new Insets(20, 50, 50, 20)));

        panel1.add(user);
        panel1.add(userID);
        panel1.add(Box.createGlue());
        panel1.add(followButton);
        panel1.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel2.add(tweet);
        panel2.add(message);
        panel2.add(Box.createGlue());
        panel2.add(postButton);
        panel2.setAlignmentX(Component.CENTER_ALIGNMENT);

		panel3.add(time);
        panel3.add(panel1);
        panel3.add(followList);
        panel3.add(followingList);
        panel3.add(panel2);
        panel3.add(scrollpane);
        panel3.add(refreshButton);
		panel3.add(lastUpdated);
        panel3.setAlignmentY(Component.CENTER_ALIGNMENT);
        panel3.setAlignmentX(Component.CENTER_ALIGNMENT);

        frame.add(panel3);
        frame.setSize(700, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}