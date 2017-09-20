
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Server extends JFrame implements Serializable, Runnable {

	private static final long serialVersionUID = 1L;
	static Map<String, User> usersMap;
	static File fileName;
	static String file = System.getProperty("user.dir").concat("\\dataFile.ser");
	static List<String> serverTemp;
	static int seq;
	// static List<Socket> clients;
	private User user;
	private ServerSocket serverSock;
	private JLabel jLabelPassword;
	private JFrame jFrame;
	private JPanel jLoginPanel;
	private JButton jLoginB;
	// private JButton jRegisterB;
	private JTextField jEmailId;
	private JPasswordField jPassword;
	private JPanel jHomePanel;
	private JLabel jWelcomeL;
	private JLabel jScreenL;
	private JButton jLogoutB;
	private JButton jAdminResetAll;
	private JTextArea jAdminTextArea;
	private JButton jListUserSeatsB;
	private JLabel jComboL;
	private JButton jListUserDetailsB;
	private String[] userList;
	private JComboBox jUserListCB;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private ArrayList<String> reservedSeats;
	private JButton jAdminRefresh;
	
	@SuppressWarnings("unchecked")
	public Server() {
		
		usersMap = new HashMap<String, User>();
		fileName = new File(file);
		serverTemp = new ArrayList<String>();
		// clients = new ArrayList<Socket>();
		try {

			if (fileName.exists()) {

				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
				usersMap = (HashMap<String, User>) ois.readObject();
				seq = (int) ois.readObject();
				serverTemp = (ArrayList<String>) ois.readObject();
				// clients = (ArrayList<Socket>) ois.readObject();
				// System.out.println("clients"+clients);
				System.out.println("serverTemp" + serverTemp);
				ois.close();
			} else {
				usersMap.put("admin@uic.edu", new User("admin@uic.edu", "Admin", "Doe", "adminUser", "admin123"));
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(file)));
				oos.writeObject(usersMap);
				seq = 123456;
				oos.writeObject(seq);
				oos.writeObject(serverTemp);
				// oos.writeObject(clients);
				oos.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {

		Server server = new Server();
		System.out.println("Server starting at 4567");
		Thread serverThread = new Thread(server);
		serverThread.start();
		System.out.println("Test!!!");
		server.displayLogin();
		//server.displayHomeScreen();

	}

	private void displayLogin() {

		jFrame = new JFrame();
		jLoginPanel = new javax.swing.JPanel();

		jFrame.getContentPane().removeAll();
		jFrame.setSize(600, 300);
		jFrame.setLocation(700, 300);
		jFrame.setTitle("Login");
		jLoginPanel.setLayout(null);
		JLabel jLabelId = new javax.swing.JLabel("Email Id");
		jLabelPassword = new javax.swing.JLabel("Password");
		jLoginB = new javax.swing.JButton("Login");
		jEmailId = new javax.swing.JTextField(12);
		jEmailId.setText("");
		jPassword = new javax.swing.JPasswordField(12);
		jPassword.setText("");
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		jLabelId.setBounds(70, 30, 100, 20);
		jLabelPassword.setBounds(70, 65, 100, 20);
		jEmailId.setBounds(140, 30, 200, 20);
		jPassword.setBounds(140, 65, 200, 20);
		jLoginB.setBounds(140, 100, 90, 20);
		
		jLoginB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jLoginBActionPerformed(evt);
			}
		});

		

		jLoginPanel.add(jLabelId);
		jLoginPanel.add(jLabelPassword);
		jLoginPanel.add(jLoginB);
		jLoginPanel.add(jEmailId);
		jLoginPanel.add(jPassword);
		jFrame.getContentPane().add(jLoginPanel);
		jFrame.setVisible(true);
	}

	private void jLoginBActionPerformed(ActionEvent evt) {
		String emailId = jEmailId.getText().trim();
		String userPass = new String(jPassword.getPassword()).trim();
		if (emailId == null || "".equals(emailId)) {
			JOptionPane.showMessageDialog(null, "Please Enter Email Id");
			jEmailId.requestFocus();
			return;
		}
		if (userPass == null || "".equals(userPass)) {
			JOptionPane.showMessageDialog(null, "Please Enter Password");
			jPassword.requestFocus();
			return;
		}
		if (emailId.equals("admin@uic.edu") && userPass.equals("admin123")) {
			user = usersMap.get(emailId);
			displayHomeScreen();
		} else {
			JOptionPane.showMessageDialog(null, "Invalid Admin Id or Password");
			return;
		}
		
	}

	class JSeats extends JButton {
		private static final long serialVersionUID = 1L;
		boolean isBooked = false;
		boolean isSelected = false;
	}

	private void displayHomeScreen() {
		jFrame.getContentPane().removeAll();
		jFrame.setSize(1500, 800);
		jFrame.setLocation(100, 100);
		jFrame.setTitle("Reservation Screen");
		jHomePanel = new javax.swing.JPanel();
		jHomePanel.setLayout(null);

		jWelcomeL = new javax.swing.JLabel();
		jWelcomeL.setText(user.getFirstName() + ", Welcome to Reservation screen!!");
		jScreenL = new javax.swing.JLabel();
		jScreenL.setText("Screen Here!!!");

		jLogoutB = new javax.swing.JButton();
		jLogoutB.setText("Logout");

		jWelcomeL.setBounds(10, 10, 350, 20);
		jScreenL.setBounds(700, 80, 100, 20);

		jLogoutB.setBounds(1300, 10, 100, 20);

		jHomePanel.add(jWelcomeL);
		jHomePanel.add(jScreenL);
		jHomePanel.add(jLogoutB);
		int y = 150;
		if (user.getUserId().equals("adminUser")) {
			y = 250;
			//jFrame.setSize(screenSize.width - 20, screenSize.height - 20);
			jFrame.setLocation(10, 10);
			jAdminResetAll = new javax.swing.JButton("Reset All Reservations");
			jAdminRefresh = new javax.swing.JButton("Refresh Page");
			jAdminTextArea = new javax.swing.JTextArea();
			jListUserSeatsB = new javax.swing.JButton("List All User Reservation");
			jComboL = new javax.swing.JLabel("Select User");
			jListUserDetailsB = new javax.swing.JButton("List All User Details");

			userList = new String[usersMap.size() + 1];
			userList[0] = "";
			int i = 1;
			for (String key : Server.usersMap.keySet()) {
				userList[i] = key;
				i++;
			}

			jUserListCB = new javax.swing.JComboBox(userList);

			jAdminTextArea.setBounds(screenSize.width - 400, 100, 500, 800);
			jScreenL.setBounds(635, 200, 100, 20);
			jAdminResetAll.setBounds(650, 600, 250, 20);
			jAdminRefresh.setBounds(650, 630, 250, 20);
			jListUserSeatsB.setBounds(1000, 20, 190, 20);
			jListUserDetailsB.setBounds(1000, 60, 190, 20);
			jComboL.setBounds(1000, 100, 100, 20);
			jUserListCB.setBounds(1100, 100, 190, 20);
			jLogoutB.setBounds(screenSize.width - 300, 10, 100, 20);
			jHomePanel.add(jAdminResetAll);
			jHomePanel.add(jAdminTextArea);
			jHomePanel.add(jAdminRefresh);
			jHomePanel.add(jListUserSeatsB);
			jHomePanel.add(jListUserDetailsB);
			jHomePanel.add(jUserListCB);
			jHomePanel.add(jComboL);

			jAdminResetAll.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					jAdminResetAllActionPerformed(evt);
				}
			});
			jAdminRefresh.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					jAdminRefreshActionPerformed(evt);
				}
			});

			jListUserSeatsB.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					jListUserBActionPerformed(evt);
				}
			});

			jListUserDetailsB.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					jListUserDetailsBActionPerformed(evt);
				}
			});

			jUserListCB.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					jUserListCBActionPerformed(evt);
				}
			});

		}

		JSeats seats[] = new JSeats[100];

		reservedSeats = new ArrayList<String>();
		for (String key : Server.usersMap.keySet()) {

			for (String seat : Server.usersMap.get(key).getSeats()) {
				reservedSeats.add(seat);
			}

		}

		String l = "";
		for (int j = 0; j < 5; j++) {
			int x = 60;
			switch (j) {
			case 0:
				l = "A-";
				break;
			case 1:
				l = "B-";
				break;
			case 2:
				l = "C-";
				break;
			case 3:
				l = "D-";
				break;
			case 4:
				l = "E-";
				break;
			}

			for (int i = 1; i <= 20; i++) {
				int s = i + j * 10;
				String label = new String(l + i);
				seats[s] = new JSeats();
				seats[s].setText(label);
				seats[s].setBounds(x, y, 60, 20);
				x += 70;

				if (reservedSeats.contains(label)) {
					seats[s].setBackground(Color.RED);
					seats[s].isBooked = true;
					seats[s].addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent evt) {
							listUserDetail(evt);
						}

					});
				} else {
					seats[s].addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent evt) {
							listUserDetail(evt);
						}

					});
				}

				jHomePanel.add(seats[s]);
			}
			x = 60;
			y += 40;
		}

		jFrame.add(jHomePanel);
		jFrame.setVisible(true);

		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

		jFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {

				JOptionPane.showMessageDialog(null, "Logging out and closing window");
				windowEvent.getWindow().dispose();
				System.exit(0);

			}
		});

		jLogoutB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jLogoutBActionPerformed(evt);
			}
		});

	}

	protected void jAdminRefreshActionPerformed(ActionEvent evt) {
		displayHomeScreen();
		
	}

	protected void jLogoutBActionPerformed(ActionEvent evt) {
		SwingUtilities.windowForComponent(((JButton) evt.getSource())).dispose();
		displayLogin();

	}

	private void listUserDetail(ActionEvent evt) {
		jAdminTextArea.setText("");
		JButton JTemp = (JButton) evt.getSource();
		User userTemp = new User();

		for (String key : usersMap.keySet()) {

			for (String seat : usersMap.get(key).getSeats()) {
				if (seat.equals(JTemp.getText())) {
					userTemp = usersMap.get(key);
					jAdminTextArea.append("Email ID: ".concat(userTemp.getEmailId()).concat("\n"));
					jAdminTextArea.append("First Name: ".concat(userTemp.getFirstName()).concat("\n"));
					jAdminTextArea.append("Last Name: ".concat(userTemp.getLastName()).concat("\n"));
					jAdminTextArea.append("User Id: ".concat(userTemp.getUserId()).concat("\n"));
					jAdminTextArea.append("Booked Seats: "
							.concat(userTemp.getSeats().toString().replace("[", "").replace("]", "")).concat("\n"));
					break;
				}

			}

		}

	}

	private void jUserListCBActionPerformed(ActionEvent evt) {

		JComboBox cb = (JComboBox) evt.getSource();
		String userMail = (String) cb.getSelectedItem();
		User userTemp = usersMap.get(userMail);
		if (null != userTemp) {
			jAdminTextArea.setText("");
			jAdminTextArea.append("Email ID: ".concat(userTemp.getEmailId()).concat("\n"));
			jAdminTextArea.append("First Name: ".concat(userTemp.getFirstName()).concat("\n"));
			jAdminTextArea.append("Last Name: ".concat(userTemp.getLastName()).concat("\n"));
			jAdminTextArea.append("User Id: ".concat(userTemp.getUserId()).concat("\n"));
			jAdminTextArea.append("Booked Seats: "
					.concat(userTemp.getSeats().toString().replace("[", "").replace("]", "")).concat("\n"));

		}
	}

	private void jListUserDetailsBActionPerformed(ActionEvent evt) {
		jAdminTextArea.setText("");
		String text = String.format("%1$-20s %2$-20s %3$-20s", "First Name  ", "Email Id  ", "User Id");
		jAdminTextArea.append(text.concat("\n"));

		for (String key : Server.usersMap.keySet()) {
			if (!Server.usersMap.get(key).getFirstName().equals("Admin")) {
				text = String.format("%1$-20s %2$-20s %3$-20s", Server.usersMap.get(key).getFirstName() + "  ",
						Server.usersMap.get(key).getEmailId() + "  ", Server.usersMap.get(key).getUserId());

				jAdminTextArea.append(text.concat("\n"));
			}

		}

	}

	private void jListUserBActionPerformed(ActionEvent evt) {
		jAdminTextArea.setText("");
		String text = String.format("%1$-20s %2$-20s %3$-20s", "Email Id", "  First Name", "  Seats Selected");
		jAdminTextArea.append(text.concat("\n"));
		for (String key : usersMap.keySet()) {
			if (!usersMap.get(key).getFirstName().equals("Admin") && usersMap.get(key).getSeats().size() > 0) {
				text = String.format("%1$-20s %2$-20s %3$-20s", usersMap.get(key).getEmailId(),
						"  " + usersMap.get(key).getFirstName(),
						"  " + usersMap.get(key).getSeats().toString().replace("[", "").replace("]", ""));

				jAdminTextArea.append(text.concat("\n"));
			}

		}

	}

	private void jAdminResetAllActionPerformed(ActionEvent evt) {
		for (String key : usersMap.keySet()) {

			usersMap.get(key).setSeats(new ArrayList<String>());
			try {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(file)));
				oos.writeObject(usersMap);
				seq = 123456;
				oos.writeObject(seq);
				oos.writeObject(serverTemp);
				oos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		displayHomeScreen();

	}

	@Override
	public void run() {
		try {
			this.serverSock = new ServerSocket(4568);

			while (true) {

				Socket client = this.serverSock.accept();
				Thread serverTask = new Thread(new ServerTasks(client, usersMap, file, serverTemp, seq));
				serverTask.start();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
