package record_indexer.gui.views;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import record_indexer.shared.communication.*;

@SuppressWarnings("serial")
public class JLoginDialog extends JDialog{
	private JTextField usernameText;
	private JPasswordField passwordText;
	private JLabel titleLabel;
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JButton loginButton;
	private JButton quitButton;
	
	public JLoginDialog(final RecordIndexerFrame parent){
		super(parent, "Login", true);
		
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		this.setLayout(new GridBagLayout());
		
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.CENTER;
		gbc.ipadx = 100;
		gbc.insets = new Insets(5,10,5,10);
		
		titleLabel = new JLabel("Please Log-in to Continue");
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		panel.add(titleLabel, gbc);
		
		usernameLabel = new JLabel("Username: ");
		usernameLabel.setHorizontalAlignment(JLabel.CENTER);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		panel.add(usernameLabel, gbc);
		
		passwordLabel = new JLabel("Password: ");
		passwordLabel.setHorizontalAlignment(JLabel.CENTER);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		panel.add(passwordLabel, gbc);
		
		usernameText = new JTextField();
		usernameText.setHorizontalAlignment(JButton.LEFT);
		usernameText.setMaximumSize(new Dimension(80,20));
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		panel.add(usernameText, gbc);
		
		passwordText = new JPasswordField();
		passwordText.setHorizontalAlignment(JButton.LEFT);
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		panel.add(passwordText, gbc);
		
		loginButton = new JButton("Login");
		loginButton.setHorizontalAlignment(JButton.CENTER);
		loginButton.setMaximumSize(new Dimension(40,20));
		loginButton.setPreferredSize(new Dimension(40,30));
		loginButton.addActionListener(new ActionListener(){

			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.setUser(usernameText.getText());
				parent.setPassword(passwordText.getText());
				ValidateUserResult r = RecordIndexerFrame.getController().login();
				if(r.toString().equals("FAILED\n")){
					JOptionPane.showMessageDialog(parent, "Login Failed", "Login Error", JOptionPane.ERROR_MESSAGE);
				}else if(r.toString().equals("FALSE\n")){
					JOptionPane.showMessageDialog(parent, "Invalid Username or Password", "Login Error", JOptionPane.ERROR_MESSAGE);
				}else{
					String output = "<html><div align='center'>Login Successful!<br>Welcome back " + r.getUSER_FIRST_NAME() + " " + r.getUSER_LAST_NAME() + "<br>Number of indexed records: " + r.getNUM_RECORDS() + "</html>";
					JOptionPane.showMessageDialog(parent, output, "Success", JOptionPane.PLAIN_MESSAGE);
					RecordIndexerFrame.getController().load();
					dispose();
				}
			}
			
		});
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		panel.add(loginButton, gbc);
		
		quitButton = new JButton("Quit");
		quitButton.setHorizontalAlignment(JButton.CENTER);
		quitButton.setMaximumSize(new Dimension(40,20));
		quitButton.setPreferredSize(new Dimension(40,30));
		quitButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				RecordIndexerFrame.getController().quit();
			}
			
		});
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		panel.add(quitButton, gbc);
		
		this.add(panel);
		this.setUndecorated(true);
		pack();
		setMinimumSize(new Dimension(500, 250));
		setResizable(false);
		setLocationRelativeTo(parent);
	}	
}
