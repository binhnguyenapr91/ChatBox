package server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JTextArea;

public class ServerGui {

	private JFrame frame;
	public JTextField txtDatabasePath;
	public JTextArea txtServerLog = new JTextArea();
	private SocketServer server;
	public JFileChooser fileChooser;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGui window = new ServerGui();
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ServerGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 652, 387);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel pnlCenter = new JPanel();
		frame.getContentPane().add(pnlCenter, BorderLayout.CENTER);
		
		JLabel lblDatabase = new JLabel("Database");
		pnlCenter.add(lblDatabase);
		
		txtDatabasePath = new JTextField();
		txtDatabasePath.setFont(new Font("Tahoma", Font.PLAIN, 12));
		pnlCenter.add(txtDatabasePath);
		txtDatabasePath.setColumns(30);
		
		JButton btnStartServer = new JButton("Start Server");
		btnStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connectButtonActionPerformed(e);
			}
		});
		
		JButton btnLoadDataFile = new JButton("Load Database");
		btnLoadDataFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION) {
				    File selectedFile = fileChooser.getSelectedFile();
				    txtDatabasePath.setText(selectedFile.getPath());
				}							
			}
		});
		pnlCenter.add(btnLoadDataFile);
		pnlCenter.add(btnStartServer);
		
		JPanel pnlTop = new JPanel();
		frame.getContentPane().add(pnlTop, BorderLayout.NORTH);
		
		JLabel lblServerControlPanel = new JLabel("SERVER CONTROL PANEL");
		lblServerControlPanel.setFont(new Font("Tahoma", Font.BOLD, 16));
		pnlTop.add(lblServerControlPanel);
		
		JPanel pnlBottom = new JPanel();
		frame.getContentPane().add(pnlBottom, BorderLayout.SOUTH);
		
		txtServerLog.setColumns(50);
		txtServerLog.setRows(15);
		pnlBottom.add(txtServerLog);
		
		frame.setLocationRelativeTo(null);
	}

	protected void connectButtonActionPerformed(ActionEvent e) {
		server = new SocketServer(this);	
	}

	public void retryStart(int port) {
		if(server != null){ server.stop(); }
        server = new SocketServer(this, port);
	}
}
