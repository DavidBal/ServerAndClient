package sqlbase;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import server.Message;

public class Database {
	private Connection conn = null;

	static int id = 1;
	/**
	 * Klasse Database wird erstellt.
	 */
	public Database() {
		this.conn = this.dbConnector();
		this.createTables();
	}
	
	
	/**
	 * Erstellt eine Verbindung zur Datenbank.
	 */
	private Connection dbConnector() {

		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:Datenbank\\messages.sqlite");
			JOptionPane.showMessageDialog(null, "Verbindung hergestellt");
			return conn;
		} catch (SQLException | ClassNotFoundException e) {
			// TODO
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Erstellt einen neuen Table falls nicht vorhanden.
	 */
	private void createTables() {
		try {
			Statement stmt = conn.createStatement();

			String sql = "CREATE TABLE IF NOT EXISTS LOGIN " + "(ID INT PRIMARY KEY NOT NULL,"
					+ " USERNAME TEXT NOT NULL, " + " PASSWORD TEXT NOT NULL, " + " BERECHTIGUNG TEXT NOT NULL); ";

			stmt.executeUpdate(sql);

			sql = "CREATE TABLE IF NOT EXISTS NACHRICHT " + "(ID INT PRIMARY KEY NOT NULL,"
					+ " NACHRICHT TEXT NOT NULL, " + " ABTEILUNG TEXT NOT NULL ," + " USERNAME INT NOT NULL, "
					+ " LASTCHANGE INT NOT NULL );";

			stmt.executeUpdate(sql);
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	/**
	 * 
	 * @param username
	 * Legt einen Benutzer an falls dieser noch nicht vorhanden ist.
	 * @param pw
	 * Legt das Passwort des Benutzer fest.
	 * @param berechtigung
	 * Legt die Berechtigung des Benutzer fest.
	 */
	public void addUser(String username, String pw, int berechtigung) {
		try {
			// TODO Funktion Auto-Generate ID
			// TODO Check Username Used only ones (WIP)

			Statement stmt = conn.createStatement();

			String check = "SELECT * FROM LOGIN WHERE USERNAME CONTAINS " + username + ";";

			if (conn.prepareStatement(check).execute() == false) {
				// TODO Exception
			}

			String sql = "INSERT INTO LOGIN (ID,USERNAME,PASSWORD,BERECHTIGUNG)" + "VALUES(null, '" + username
					+ "' , '" + pw + "' , " + berechtigung + " );";

			stmt.executeUpdate(sql);
			stmt.close();

			Database.id++;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param username
	 * @param pw
	 * @return 0 = User nicht exesitent
	 */
	public int getUserBerechtigung(String username, String pw) {

		int berechtigung = 0;

		try {
			String query = "select * from Login where USERNAME=? and PASSWORD=? ";
			PreparedStatement pst = conn.prepareStatement(query);

			pst.setString(1, username);
			pst.setString(2, pw);

			ResultSet rs = pst.executeQuery();

			// Überprüfen ob ResultSet nicht leer ist.

			if (!rs.wasNull()) {
				berechtigung = Integer.valueOf(rs.getString("BERECHTIGUNG"));
			}

			rs.close();
			pst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return berechtigung;
	}

	/**
	 * 
	 * @param username
	 * @return 0 if User not in DataBase
	 */
	public int getUserID(String username) {

		int id = 0;

		try {
			String query = "select * from Login where USERNAME=?";
			PreparedStatement pst = conn.prepareStatement(query);

			pst.setString(1, username);

			ResultSet rs = pst.executeQuery();

			// Überprüfen ob ResultSet nicht leer ist.

			if (!rs.wasNull()) {
				id = Integer.valueOf(rs.getString("ID"));
			}

			rs.close();
			pst.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
		}

		return id;
	}

	/**
	 * Delete a User out of DataBase
	 * 
	 * @param username
	 * @return 0 if Failed and ID if worked
	 */
	public int deleteUser(String username) {
		int id = this.getUserID(username);
		try {
			String del = "delete from Login where Id =" + id + "; ";
			PreparedStatement delete;
			delete = conn.prepareStatement(del);
			delete.execute();
			delete.close();
		} catch (SQLException e) {
			return 0;
		}
		return id;
	}

	/**
	 * Fuegt eine Nachricht zur Datenbank hinzu
	 * 
	 * @param msg
	 * @return
	 */
	public int addMessage(Message msg) {

		Statement stmt;
		try {
			stmt = conn.createStatement();

			String sql = "INSERT INTO NACHRICHT (ID,NACHRICHT,ABTEILUNG,USERNAME,LASTCHANGE)" + "VALUES(" + msg.getId()
					+ ", '" + msg.getText() + "' , '" + msg.getAbteilung() + "' , '" + msg.getUsername() + "' , "
					+ msg.getLastchange() + " );";

			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Löscht eine bestimmte Nachricht aus der Datenbank
	 * 
	 * @param msg
	 */
	public void deleteMessage(Message msg) {
		try {
			String del = "delete from NACHRICHT where ID = " + msg.getId() + ";";
			PreparedStatement delete;
			delete = conn.prepareStatement(del);
			delete.execute();
			delete.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void editMessage(Message newMsg, Message oldMsg) {
		this.deleteMessage(oldMsg);
		this.addMessage(newMsg);
	}

	/**
	 * Ladet alle Nachrichten von einem Bestimmen TimeStamp aus
	 * 
	 * @param time
	 * @return
	 */
	public ArrayList<Message> loadMessages(int time) {
		ArrayList<Message> msgs = new ArrayList<Message>();

		try {
			String query = "Select * from Nachrichten where LASTCHANGE >" + time + ";";
			PreparedStatement output = conn.prepareStatement(query);
			ResultSet rs = output.executeQuery();
			do {
				msgs.add(new Message(Integer.valueOf(rs.getString("ID")), rs.getString("NACHRICHT"),
						rs.getString("ABTEILUNG"), rs.getString("USERNAME"),
						Integer.valueOf(rs.getString("LASTCHANGE"))));

				rs.next();
			} while (!rs.isLast());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return msgs;
	}

}
