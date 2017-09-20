import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Client extends JFrame {

	private static final long serialVersionUID = 1L;
	private User user;
	private JFrame jFrame = new JFrame();
	// Login Screen
	private javax.swing.JLabel jLabelId;
	private javax.swing.JLabel jLabelPassword;
	private javax.swing.JTextField jEmailId;
	private javax.swing.JPanel jLoginPanel;
	private javax.swing.JButton jLoginB;
	private javax.swing.JPasswordField jPassword;
	private javax.swing.JButton jRegisterB;

	// Register User Screen
	private javax.swing.JLabel jFirstNameRegL;
	private javax.swing.JTextField jFirstNameRegT;
	private javax.swing.JLabel jLastNameRegL;
	private javax.swing.JTextField jLastNameRegT;
	private javax.swing.JPanel jRegisterP;
	private javax.swing.JLabel jPassRegL;
	private javax.swing.JPasswordField jPassRegP;
	private javax.swing.JButton jUserExitB;
	private javax.swing.JLabel jEmailRegL;
	private javax.swing.JTextField jEmailRegT;
	private javax.swing.JButton jUserRegisterB;

	// Home Screen
	// private javax.swing.JButton jClearSelctB;
	private javax.swing.JButton jLogoutB;
	private javax.swing.JPanel jHomePanel;
	private javax.swing.JButton jReserveSeatB;
	private javax.swing.JLabel jScreenL;
	private javax.swing.JTextArea jUserTextArea;
	private javax.swing.JLabel jWelcomeL;

	private javax.swing.JLabel jTimerL;
	private javax.swing.JLabel jTimerCount;
	private javax.swing.Timer jTimer;
	private javax.swing.Timer jTimerRefresh;
	private ArrayList<String> bookedSeats;
	static private ArrayList<String> reservedSeats = new ArrayList<String>();
	int seconds = 60;
	int port = 4568;
	private String ip = "localhost"; //"10.1.229.125";
	private Socket socket;
	private BufferedWriter buffWriter;
	private BufferedReader buffReader;
	private String split = "::";
	private ArrayList<String> tempSelectedSeats;

	public Client() {
		try {
			socket = new Socket(InetAddress.getByName(ip), port);
			buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void displayLogin() {

		jFrame = new JFrame();
		jFrame.getContentPane().removeAll();
		jFrame.setSize(600, 300);
		jFrame.setLocation(700, 300);
		jLoginPanel = new javax.swing.JPanel();
		jFrame.setTitle("Login");
		jLoginPanel.setLayout(null);
		jLabelId = new javax.swing.JLabel("Email Id");
		jLabelPassword = new javax.swing.JLabel("Password");
		jLoginB = new javax.swing.JButton("Login");
		jRegisterB = new javax.swing.JButton("Register");
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
		jRegisterB.setBounds(140, 140, 90, 20);

		jLoginB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jLoginBActionPerformed(evt);
			}
		});

		jRegisterB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jRegisterBActionPerformed(evt);
			}
		});

		jLoginPanel.add(jLabelId);
		jLoginPanel.add(jLabelPassword);
		jLoginPanel.add(jLoginB);
		jLoginPanel.add(jEmailId);
		jLoginPanel.add(jPassword);
		jLoginPanel.add(jRegisterB);
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
		StringBuffer sb = new StringBuffer("Login").append(split).append(emailId).append(split).append(userPass);
		try {
			buffWriter.write(sb.toString() + "\n");
			buffWriter.flush();
			String response = buffReader.readLine();
			if (null != response && !response.equals("")) {
				String[] values = response.split(split);
				if (values[0].equals("Login")) {
					loginResponse(response);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void loginResponse(String response) {

		user = null;
		if (null != response) {
			System.out.println("response:" + response);
			String[] values = response.split(split);
			bookedSeats = new ArrayList<String>();
			if (values.length >= 7 && values[1].equals("Success")) {
				user = new User(values[2], values[3], values[4], values[5], values[6]);
				if (values.length > 7) {
					bookedSeats = new ArrayList<String>();
					for (String temp : values[7].split(",")) {
						bookedSeats.add(temp.trim());
					}
					user.setSeats(bookedSeats);
					System.out.println("login:" + bookedSeats.toString());
				}

				tempSelectedSeats = new ArrayList<String>();
				displayHomeScreen();
			} else if (values[1].equals("Fail")) {
				JOptionPane.showMessageDialog(null, "Invalid Email Id or Password");
				return;
			}
		} else {
			JOptionPane.showMessageDialog(null, "Invalid Email Id or Password");
			return;
		}
	}

	private void registerResponse(String response) {
		user = null;
		if (null != response && !response.equals("")) {
			System.out.println("response:" + response);
			String[] values = response.split(split);
			if (!values[1].equals("Success")) {

				JOptionPane.showMessageDialog(null,
						"This email id already exists.\nPlease Login or use different id to register");
				return;
			} else {

				JOptionPane.showMessageDialog(null,
						"Registration Successfull.\nPlease login to continue with reservation");
				jFrame.getContentPane().removeAll();
				jFrame.dispose();
				displayLogin();
			}
		}
	}

	private void jRegisterBActionPerformed(java.awt.event.ActionEvent evt) {
		displayRegisterUser();
	}

	private void displayRegisterUser() {

		jFrame.getContentPane().removeAll();
		jFrame.setSize(600, 300);
		jFrame.setLocation(700, 300);
		jFrame.setTitle("Register User");
		jRegisterP = new javax.swing.JPanel();
		jRegisterP.setLayout(null);
		jEmailRegL = new javax.swing.JLabel("Email Id");
		jFirstNameRegL = new javax.swing.JLabel("First Name");
		jLastNameRegL = new javax.swing.JLabel("Last Name");
		jUserRegisterB = new javax.swing.JButton("Register");
		jUserExitB = new javax.swing.JButton("Exit");
		jEmailRegT = new javax.swing.JTextField(12);
		jEmailRegT.setText("");
		jLastNameRegT = new javax.swing.JTextField(30);
		jLastNameRegT.setText("");
		jFirstNameRegT = new javax.swing.JTextField(30);
		jFirstNameRegT.setText("");
		jPassRegP = new javax.swing.JPasswordField(12);
		jPassRegP.setText("");
		jPassRegL = new javax.swing.JLabel("Password");
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		jEmailRegL.setBounds(70, 30, 100, 20);
		jFirstNameRegL.setBounds(70, 65, 100, 20);
		jLastNameRegL.setBounds(70, 100, 100, 20);
		jPassRegL.setBounds(70, 135, 100, 20);
		jEmailRegT.setBounds(140, 30, 200, 20);
		jFirstNameRegT.setBounds(140, 65, 200, 20);
		jLastNameRegT.setBounds(140, 100, 200, 20);
		jPassRegP.setBounds(140, 135, 200, 20);
		jUserRegisterB.setBounds(100, 180, 90, 20);
		jUserExitB.setBounds(250, 180, 90, 20);
		jRegisterP.add(jEmailRegL);
		jRegisterP.add(jFirstNameRegL);
		jRegisterP.add(jLastNameRegL);
		jRegisterP.add(jPassRegL);
		jRegisterP.add(jEmailRegT);
		jRegisterP.add(jFirstNameRegT);
		jRegisterP.add(jLastNameRegT);
		jRegisterP.add(jPassRegP);
		jRegisterP.add(jUserRegisterB);
		jRegisterP.add(jUserExitB);

		jFrame.getContentPane().add(jRegisterP);
		jFrame.setVisible(true);

		jUserExitB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jUserExitBActionPerformed(evt);
			}
		});

		jUserRegisterB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jUserRegisterBActionPerformed(evt);
			}

		});

	}

	private void jUserExitBActionPerformed(ActionEvent evt) {
		jFrame.getContentPane().removeAll();
		jFrame.dispose();
		displayLogin();

	}

	private void jUserRegisterBActionPerformed(ActionEvent evt) {

		String firstName = jFirstNameRegT.getText().trim();
		String lastName = jLastNameRegT.getText().trim();
		String pass = new String(jPassRegP.getPassword()).trim();
		String emailId = jEmailRegT.getText().trim();
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);

		if (emailId == null || "".equals(emailId) || !pattern.matcher(emailId).matches()) {
			JOptionPane.showMessageDialog(null, "Please enter valid Email Id");
			jEmailRegT.requestFocus();
			return;
		}
		if (firstName == null || "".equals(firstName)) {
			JOptionPane.showMessageDialog(null, "Please enter First Name");
			jFirstNameRegT.requestFocus();
			return;
		}
		if (lastName == null || "".equals(lastName)) {
			JOptionPane.showMessageDialog(null, "Please enter Last Name");
			jLastNameRegT.requestFocus();
			return;
		}
		if (pass == null || "".equals(pass) || pass.length() < 6 || pass.length() > 12) {
			JOptionPane.showMessageDialog(null, "Password should contain minimum of 6 and maximum 12 characters");
			jPassRegP.requestFocus();
			return;
		}

		StringBuffer sb = new StringBuffer("Register").append(split).append(emailId).append(split).append(firstName)
				.append(split).append(lastName).append(split).append(pass);
		try {
			buffWriter.write(sb.toString() + "\n");
			buffWriter.flush();
			String response = buffReader.readLine();
			if (null != response && !response.equals("")) {
				String[] values = response.split(split);
				if (values[0].equals("Register")) {
					registerResponse(response);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
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
		if (jTimerRefresh == null) {
			jTimerRefresh = new javax.swing.Timer(2000, new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					jTimerRefreshActionPerformed(evt);
				}
			});
			jTimerRefresh.setInitialDelay(5000);
			jTimerRefresh.start();
		}

		try {
			System.out.println("399");
			buffWriter.write("GetUserSeat::" + user.getEmailId() + "\n");
			buffWriter.flush();
			String response = buffReader.readLine();
			System.out.println("While495:" + response);
			if (null != response && !response.equals("")) {
				String[] values = response.split(split);
				if (values[0].equals("GetUserSeat")) {
					setUserSeat(response);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		jUserTextArea = new javax.swing.JTextArea();
		jReserveSeatB = new javax.swing.JButton("Reserve Seats");
		jUserTextArea.append("Email ID: ".concat(user.getEmailId()).concat("\n"));
		jUserTextArea.append("First Name: ".concat(user.getFirstName()).concat("\n"));
		jUserTextArea.append("Last Name: ".concat(user.getLastName()).concat("\n"));
		jUserTextArea.append("User Id: ".concat(user.getUserId()).concat("\n"));
		jUserTextArea.append(
				"Booked Seats: ".concat(user.getSeats().toString().replace("[", "").replace("]", "")).concat("\n"));
		jUserTextArea.append(
				"Seats in Cart: ".concat(tempSelectedSeats.toString().replace("[", "").replace("]", "")).concat("\n"));
		jUserTextArea.setBounds(550, 520, 500, 200);
		jReserveSeatB.setBounds(635, 400, 250, 20);
		jHomePanel.add(jUserTextArea);
		jHomePanel.add(jReserveSeatB);
		if (jTimer == null) {
			jTimerL = new javax.swing.JLabel("Remaining Time (Seconds)");
			jTimerCount = new javax.swing.JLabel("60");
			jTimerL.setVisible(false);
			jTimerCount.setVisible(false);

		}
		jTimerL.setBounds(1100, 10, 200, 20);
		jTimerCount.setBounds(1100, 30, 200, 20);
		jHomePanel.add(jTimerL);
		jHomePanel.add(jTimerCount);
		jReserveSeatB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jReserveSeatBActionPerformed(evt);
			}
		});

		JSeats seats[] = new JSeats[100];
		try {
			System.out.println("While497:");
			String temp = "GetReservedSeats::";
			if (user.getUserId().equals("adminUser")) {
				temp = temp + "Admin";
			} else {
				temp = temp + "User";
			}
			buffWriter.write(temp + "\n");
			buffWriter.flush();

			String response = buffReader.readLine();
			System.out.println("While495:" + response);
			if (null != response && !response.equals("")) {
				String[] values = response.split(split);
				if (values[0].equals("ReservedSeats")) {
					setReservedSeats(response);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
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
				
				if (user.getSeats().contains(label) || tempSelectedSeats.contains(label)) {
					seats[s].setBackground(Color.GREEN);
					seats[s].isSelected = true;

					seats[s].addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent evt) {
							modifyUserSelections(evt);
						}

					});
				} else if (reservedSeats.contains(label)) {
					seats[s].setBackground(Color.RED);
					seats[s].isBooked = true;
				} else {
					seats[s].addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent evt) {
							modifyUserSelections(evt);
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
				StringBuffer sb;
				for (String temp : tempSelectedSeats) {
					sb = new StringBuffer("RemoveCart").append(split).append(temp.trim());
					try {
						buffWriter.write(sb.toString() + "\n");
						buffWriter.flush();
						buffReader.readLine();
					} catch (Exception e) {

					}
				}

				tempSelectedSeats.clear();
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

	private void jTimerRefreshActionPerformed(ActionEvent evt) {
		displayHomeScreen();

	}

	private void modifyUserSelections(ActionEvent evt) {
		JSeats temp = (JSeats) evt.getSource();
		temp.isSelected = !temp.isSelected;
		// boolean tempServer = false;
		System.out.println("724");
		if (temp.isSelected && (bookedSeats.size() + tempSelectedSeats.size()) < 4) {
			JOptionPane.showMessageDialog(null, "Seat has been added to Cart."
					+ "\n Please select 'Reserve Seat' option in 60 Seconds to confirm booking");
			if (jTimer == null) {
				jTimer = new Timer(1000, new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						jTimerActionPerformed(evt);
					}
				});
				jTimer.start();
			}
			jTimerL.setVisible(true);
			jTimerCount.setVisible(true);
			jTimerCount.setText("60");

			seconds = 60;
			temp.setBackground(Color.GREEN);
			if (!tempSelectedSeats.contains(temp.getText())) {
				tempSelectedSeats.add(temp.getText());
			}
			System.out.println("744:" + tempSelectedSeats.toString());
			jUserTextArea.setText("");
			jUserTextArea.append("Email ID: ".concat(user.getEmailId()).concat("\n"));
			jUserTextArea.append("First Name: ".concat(user.getFirstName()).concat("\n"));
			jUserTextArea.append("Last Name: ".concat(user.getLastName()).concat("\n"));
			jUserTextArea.append("User Id: ".concat(user.getUserId()).concat("\n"));
			jUserTextArea.append(
					"Booked Seats: ".concat(user.getSeats().toString().replace("[", "").replace("]", "")).concat("\n"));
			jUserTextArea.append("Seats in Cart: "
					.concat(tempSelectedSeats.toString().replace("[", "").replace("]", "")).concat("\n"));

			StringBuffer sb = new StringBuffer("AddCart").append(split).append(temp.getText());
			try {
				buffWriter.write(sb.toString() + "\n");
				buffWriter.flush();
				String response = buffReader.readLine();
				if (null != response && !response.equals("")) {
					String[] values = response.split(split);
					if (values[0].equals("AddCart") && values[1].equals("Success")) {

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (!temp.isSelected) {
			if (bookedSeats.contains(temp.getText())) {
				if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null,
						"Seat is already boooked.\n Please select Yes to continue and cancel booking.", "",
						JOptionPane.YES_NO_OPTION)) {
					StringBuffer sb = new StringBuffer("RemoveSeat").append(split).append(user.getEmailId())
							.append(split).append(temp.getText());
					try {
						buffWriter.write(sb.toString() + "\n");
						buffWriter.flush();
						String response = buffReader.readLine();
						if (null != response && !response.equals("")) {
							String[] values = response.split(split);
							if (values[0].equals("RemoveSeat") && values[1].equals("Success")) {

								temp.setBackground(null);
								bookedSeats.remove(temp.getText());
								user.setSeats(bookedSeats);
								reservedSeats.remove(temp.getText());
								// displayHomeScreen();
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

					System.out.println("755:" + tempSelectedSeats.toString());
				} else {
					temp.isSelected = !temp.isSelected;
					return;
				}

			} else {
				StringBuffer sb = new StringBuffer("RemoveCart").append(split).append(temp.getText());
				try {
					buffWriter.write(sb.toString() + "\n");
					buffWriter.flush();
					String response = buffReader.readLine();
					if (null != response && !response.equals("")) {
						String[] values = response.split(split);
						if (values[0].equals("RemoveCart") && values[1].equals("Success")) {
							tempSelectedSeats.remove(temp.getText());
							temp.setBackground(null);
							System.out.println("763:" + tempSelectedSeats.toString());
							if (tempSelectedSeats.size() == 0) {
								if (jTimer != null) {
									jTimer.stop();
									jTimer = null;
									jTimerL.setVisible(false);
									jTimerCount.setVisible(false);
									jTimerCount.setText("60");
								}
							}
							jUserTextArea.setText("");
							jUserTextArea.append("Email ID: ".concat(user.getEmailId()).concat("\n"));
							jUserTextArea.append("First Name: ".concat(user.getFirstName()).concat("\n"));
							jUserTextArea.append("Last Name: ".concat(user.getLastName()).concat("\n"));
							jUserTextArea.append("User Id: ".concat(user.getUserId()).concat("\n"));
							jUserTextArea.append("Booked Seats: "
									.concat(user.getSeats().toString().replace("[", "").replace("]", "")).concat("\n"));
							jUserTextArea.append("Seats in Cart: "
									.concat(tempSelectedSeats.toString().replace("[", "").replace("]", ""))
									.concat("\n"));

						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.out.println("775:" + tempSelectedSeats.toString());
			temp.isSelected = false;
			JOptionPane.showMessageDialog(null, "You can select maximun of 4 seats!!");
		}

	}

	private void jTimerActionPerformed(ActionEvent evt) {
		if (seconds > 0) {
			seconds--;
			jTimerCount.setText(Integer.toString(seconds));
		} else {
			jTimer.stop();
			jTimer = null;
			StringBuffer sb;
			for (String temp : tempSelectedSeats) {
				sb = new StringBuffer("RemoveCart").append(split).append(temp.trim());
				try {
					buffWriter.write(sb.toString() + "\n");
					buffWriter.flush();
					buffReader.readLine();
				} catch (Exception e) {

				}
			}
			tempSelectedSeats.clear();
			JOptionPane.showMessageDialog(null, "Time Out!! Cart has been cleared!!");
			displayHomeScreen();
		}
	}

	private void jReserveSeatBActionPerformed(java.awt.event.ActionEvent evt) {
		if (tempSelectedSeats.size() > 0) {
			if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Select YES to reserve selected seats",
					"", JOptionPane.YES_NO_OPTION)) {
				ArrayList<String> temp = new ArrayList<String>();
				temp.addAll(tempSelectedSeats);

				StringBuffer sb = new StringBuffer("BookSeats").append(split).append(user.getEmailId()).append(split)
						.append(temp.toString().replaceAll("\\[|\\]", ""));
				try {
					buffWriter.write(sb.toString() + "\n");
					buffWriter.flush();
					String response = buffReader.readLine();
					if (null != response && !response.equals("")) {
						String[] values = response.split(split);
						if (values[0].equals("BookSeats") && values[1].equals("Fail")) {
							JOptionPane.showMessageDialog(null,
									"Seats are already booked by other Users.\n Please select other seats");
						} else if (values[1].equals("Success")) {
							temp.addAll(bookedSeats);
							user.setSeats(temp);
							bookedSeats = user.getSeats();
						}
						tempSelectedSeats.clear();
						if (jTimer != null) {
							jTimer.stop();
							jTimer = null;
							jTimerL.setVisible(false);
							jTimerCount.setVisible(false);
							jTimerCount.setText("60");
						}
						displayHomeScreen();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		} else {
			JOptionPane.showMessageDialog(null, "Please select Seats to Reserve");
		}
	}

	private void jLogoutBActionPerformed(java.awt.event.ActionEvent evt) {
		SwingUtilities.windowForComponent(((JButton) evt.getSource())).dispose();
		if (jTimerRefresh != null) {
			jTimerRefresh.stop();
			jTimerRefresh = null;
		}
		if (jTimer != null) {
			jTimer.stop();
			jTimer = null;
		}
		StringBuffer sb;
		for (String temp : tempSelectedSeats) {
			sb = new StringBuffer("RemoveCart").append(split).append(temp.trim());
			try {
				buffWriter.write(sb.toString() + "\n");
				buffWriter.flush();
				buffReader.readLine();
			} catch (Exception e) {

			}
		}

		tempSelectedSeats.clear();
		displayLogin();

	}

	private void setReservedSeats(String response) {
		reservedSeats = new ArrayList<String>();
		if (response.split(split).length >= 2) {
			ArrayList<String> seats = new ArrayList<String>(Arrays.asList(response.split(split)[1].split(",")));
			for (String temp : seats) {
				reservedSeats.add(temp.trim());
			}
		}
	}

	private void setUserSeat(String response) {
		System.out.println("Client:" + response);
		ArrayList<String> userSeats = new ArrayList<String>();
		if (response.split(split).length > 1) {
			ArrayList<String> userLT = new ArrayList<String>(Arrays.asList(response.split(split)[1].split(",")));

			for (String temp : userLT) {
				userSeats.add(temp.trim());
			}
		}
		bookedSeats = new ArrayList<String>(userSeats);
		user.setSeats(userSeats);

	}

	public static void main(String[] args) {

		Client client = new Client();
		client.displayLogin();

	}

}
