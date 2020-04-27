package client;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.UIManager;
import message.Message;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class ClientGui extends javax.swing.JFrame{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JFrame frmClient;
	public JTextField txtHostname;
	public JTextField txtPort;
	public JTextField txtMessage;
	public JTextField txtUsername;
	public JTextArea textArea = new JTextArea();
	public String serverAddr, username, password;
	public int port;
	public SocketClient client;
	public Thread clientThread;
	public DefaultListModel model;
	public JList list = new JList();
	private JPasswordField txtPassword;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGui window = new ClientGui();
					window.frmClient.setVisible(true);
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientGui() {
		initialize();
		this.setTitle(Config.CLIENT_FRAME_TITLE);
		model.addElement(Config.ALL_CLIENTS);
		list.setSelectedIndex(0);
		this.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				client.send(new Message(Config.TOKEN_STRING_MESSAGE, username, Config.MESSAGE_OUT, Config.RECIPIENT_SERVER)); 
				clientThread.stop();
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmClient = new JFrame();
		frmClient.setTitle(Config.CLIENT_FRAME_TITLE);
		frmClient.setBounds(100, 100, 927, 398);
		frmClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel pnlTop = new JPanel();
		frmClient.getContentPane().add(pnlTop, BorderLayout.NORTH);

		JLabel lblHostname = new JLabel("Hostname");
		pnlTop.add(lblHostname);

		txtHostname = new JTextField();
		pnlTop.add(txtHostname);
		txtHostname.setColumns(10);

		JLabel lblPort = new JLabel("Port");
		pnlTop.add(lblPort);

		txtPort = new JTextField();
		pnlTop.add(txtPort);
		txtPort.setColumns(10);

		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				connectAction();
			}
		});
		pnlTop.add(btnConnect);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logoutAction();
			}
		});
		pnlTop.add(btnLogout);

		JPanel pnlBottom = new JPanel();
		frmClient.getContentPane().add(pnlBottom, BorderLayout.SOUTH);

		JLabel lblMessage = new JLabel("Message");
		pnlBottom.add(lblMessage);

		txtMessage = new JTextField();
		pnlBottom.add(txtMessage);
		txtMessage.setColumns(30);

		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendAction();
			}
		});
		pnlBottom.add(btnSend);

		JPanel pnlRight = new JPanel();
		frmClient.getContentPane().add(pnlRight, BorderLayout.EAST);
		
		list = new JList();
		list.setModel((model = new DefaultListModel()));
		pnlRight.add(list);

		JPanel pnlCenter = new JPanel();
		frmClient.getContentPane().add(pnlCenter, BorderLayout.CENTER);

		textArea.setRows(15);
		textArea.setColumns(50);
		pnlCenter.add(textArea);
		
		JPanel pnlLeft = new JPanel();
		frmClient.getContentPane().add(pnlLeft, BorderLayout.WEST);
				GridBagLayout gbl_pnlLeft = new GridBagLayout();
				gbl_pnlLeft.columnWidths = new int[]{48, 86, 0};
				gbl_pnlLeft.rowHeights = new int[]{20, 0, 0, 0, 0};
				gbl_pnlLeft.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
				gbl_pnlLeft.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
				pnlLeft.setLayout(gbl_pnlLeft);
						
								JLabel lblUsername = new JLabel("Username");
								GridBagConstraints gbc_lblUsername = new GridBagConstraints();
								gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
								gbc_lblUsername.gridx = 0;
								gbc_lblUsername.gridy = 0;
								pnlLeft.add(lblUsername, gbc_lblUsername);
						
								txtUsername = new JTextField();
								GridBagConstraints gbc_txtUsername = new GridBagConstraints();
								gbc_txtUsername.insets = new Insets(0, 0, 5, 0);
								gbc_txtUsername.anchor = GridBagConstraints.NORTHWEST;
								gbc_txtUsername.gridx = 1;
								gbc_txtUsername.gridy = 0;
								pnlLeft.add(txtUsername, gbc_txtUsername);
								txtUsername.setColumns(10);
								
								JLabel lblPassword = new JLabel("Password");
								GridBagConstraints gbc_lblPassword = new GridBagConstraints();
								gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
								gbc_lblPassword.gridx = 0;
								gbc_lblPassword.gridy = 1;
								pnlLeft.add(lblPassword, gbc_lblPassword);
								
								JButton btnLogin = new JButton("Login");
								btnLogin.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										loginAction();
									}
								});
								
								txtPassword = new JPasswordField();
								GridBagConstraints gbc_txtPassword = new GridBagConstraints();
								gbc_txtPassword.insets = new Insets(0, 0, 5, 0);
								gbc_txtPassword.fill = GridBagConstraints.HORIZONTAL;
								gbc_txtPassword.gridx = 1;
								gbc_txtPassword.gridy = 1;
								pnlLeft.add(txtPassword, gbc_txtPassword);
								GridBagConstraints gbc_btnLogin = new GridBagConstraints();
								gbc_btnLogin.insets = new Insets(0, 0, 5, 5);
								gbc_btnLogin.gridx = 0;
								gbc_btnLogin.gridy = 2;
								pnlLeft.add(btnLogin, gbc_btnLogin);
								
								JButton btnSignUp = new JButton("SignUp");
								btnSignUp.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										signupAction();
									}
								});
								GridBagConstraints gbc_btnSignUp = new GridBagConstraints();
								gbc_btnSignUp.insets = new Insets(0, 0, 5, 0);
								gbc_btnSignUp.gridx = 1;
								gbc_btnSignUp.gridy = 2;
								pnlLeft.add(btnSignUp, gbc_btnSignUp);

		frmClient.setLocationRelativeTo(null);
	}

	protected void logoutAction() {
		client.send(new Message(Config.TOKEN_STRING_MESSAGE, username, Config.MESSAGE_OUT, Config.RECIPIENT_SERVER));
		clientThread.stop();
		
	}

	protected void sendAction() {
		String msg = txtMessage.getText();
        String target = list.getSelectedValue().toString();
        
        if(!msg.isEmpty() && !target.isEmpty()){
            txtMessage.setText(Config.CLEAR_VALUE);
            client.send(new Message(Config.TOKEN_STRING_MESSAGE, username, msg, target));
        }
		
	}

	protected void signupAction() {
		username = txtUsername.getText();
        password = txtPassword.getText();
        
        if(!username.isEmpty() && !password.isEmpty()){
            client.send(new Message(Config.TOKEN_STRING_SIGNUP, username, password, Config.RECIPIENT_SERVER));
        }
		
	}

	protected void loginAction() {
		username = txtUsername.getText();
        password = txtPassword.getText();
        
        if(!username.isEmpty() && !password.isEmpty()){
            client.send(new Message(Config.TOKEN_STRING_LOGIN, username, password, Config.RECIPIENT_SERVER));
        }
		
	}

	protected void connectAction() {
			serverAddr = txtHostname.getText(); 
			port = Integer.parseInt(txtPort.getText());
			client = new SocketClient(this);
            clientThread = new Thread(client);
            clientThread.start();
            client.send(new Message(Config.TOKEN_STRING_TEST, Config.TEST_USER, Config.TEST_CONTENT, Config.RECIPIENT_SERVER));
	}
}
