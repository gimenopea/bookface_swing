package com.gwu;

import java.awt.BorderLayout;
import java.sql.*;
import java.util.ArrayList;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SpringLayout;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JEditorPane;

public class BookfaceGUI extends JFrame {
	static Connection conn = null;

	private JPanel contentPane;
	private JTextField textFieldUsername;
	private JTextField textFieldPost;
	private JButton btnPost;
	private JEditorPane dtrpnPosts;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		connect();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {				
				try {
					BookfaceGUI frame = new BookfaceGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}

	
	public BookfaceGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 602, 462);
		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		JButton btnReload = new JButton("Reload");
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnReload, 7, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, btnReload, 257, SpringLayout.WEST, contentPane);
		btnReload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = query();			
				System.out.println(message);		
				dtrpnPosts.setText(message);
			}
		});
		contentPane.add(btnReload);
		
		textFieldUsername = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.WEST, textFieldUsername, 10, SpringLayout.WEST, contentPane);
		textFieldUsername.setText("Username");
		contentPane.add(textFieldUsername);
		textFieldUsername.setColumns(10);
		
		textFieldPost = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, textFieldUsername, 0, SpringLayout.NORTH, textFieldPost);
		sl_contentPane.putConstraint(SpringLayout.EAST, textFieldUsername, -6, SpringLayout.WEST, textFieldPost);
		sl_contentPane.putConstraint(SpringLayout.WEST, textFieldPost, 136, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, textFieldPost, -10, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, textFieldPost, -71, SpringLayout.EAST, contentPane);
		textFieldPost.setText("Enter Message Here");
		textFieldPost.setToolTipText("Enter Message Here");
		contentPane.add(textFieldPost);
		textFieldPost.setColumns(10);
		
		btnPost = new JButton("Post");
		btnPost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insertComment(textFieldUsername.getText(),textFieldPost.getText());
				
				
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnPost, -1, SpringLayout.NORTH, textFieldPost);
		sl_contentPane.putConstraint(SpringLayout.WEST, btnPost, 6, SpringLayout.EAST, textFieldPost);
		contentPane.add(btnPost);
		
		dtrpnPosts = new JEditorPane("text/html", "Posts");
		dtrpnPosts.setForeground(Color.GRAY);
		dtrpnPosts.setBackground(Color.WHITE);
		sl_contentPane.putConstraint(SpringLayout.NORTH, dtrpnPosts, 6, SpringLayout.SOUTH, btnReload);
		sl_contentPane.putConstraint(SpringLayout.WEST, dtrpnPosts, 0, SpringLayout.WEST, textFieldUsername);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, dtrpnPosts, -6, SpringLayout.NORTH, textFieldUsername);
		sl_contentPane.putConstraint(SpringLayout.EAST, dtrpnPosts, 0, SpringLayout.EAST, btnPost);
		contentPane.add(dtrpnPosts);
	}
	
	
	
	
	public static void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/bookface?" +
            	                                   "user=<>&password=<>");            
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }catch (Exception ex) {
            // handle the error
        }
	}
	
	
	public static String query() {
		Statement stmt = null;
		ResultSet rs = null;		
		String resultString = "";
		
		try {
		    stmt = conn.createStatement();
		    rs = stmt.executeQuery("SELECT * FROM wall");
		    while (rs.next()) {
		    	String username = rs.getString(1);
		    	String msg = rs.getString(2);
		    	String timestamp = rs.getString(3);
		    	
		    	System.out.println(username + " : " + msg);
		    	
		    	resultString += "<em>" + timestamp + "</em>" + " " + "<b>" + username + "</b>" + " Says: " + msg + "<br />";
		    }
		   

		    // Now do something with the ResultSet ....
		}
		catch (SQLException ex){
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		finally {
		    // it is a good idea to release
		    // resources in a finally{} block
		    // in reverse-order of their creation
		    // if they are no-longer needed

		    if (rs != null) {
		        try {
		            rs.close();
		        } catch (SQLException sqlEx) { } // ignore

		        rs = null;
		    }

		    if (stmt != null) {
		        try {
		            stmt.close();
		        } catch (SQLException sqlEx) { } // ignore

		        stmt = null;
		    }
		}
		return resultString;
	}
	
	
	public static void insertComment(String username, String comment) {
		
		ResultSet rs = null;
		String insertString = "INSERT INTO WALL (username, comment, createdon) VALUES (?,?, CURRENT_TIMESTAMP)";
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(insertString);
		    stmt.setString(1,username);
		    stmt.setString(2, comment);
		    
		    stmt.executeUpdate();

		    // Now do something with the ResultSet ....
		    System.out.println(username + ": " + comment);
		}
		catch (SQLException ex){
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		finally {
		    // it is a good idea to release
		    // resources in a finally{} block
		    // in reverse-order of their creation
		    // if they are no-longer needed

		    if (rs != null) {
		        try {
		            rs.close();
		        } catch (SQLException sqlEx) { } // ignore

		        rs = null;
		    }

	
		}
	}
}
