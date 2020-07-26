package mainmc.folders;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {

	private final String url;
	final String driver;
	String user = "";
	String database = "";
	String password = "";
	String port = "";
	String host = "";
	Connection c = null;

	public DataBase(String filePath) {
		url = "jdbc:sqlite:" + new File(filePath).getAbsolutePath();
		driver = ("org.sqlite.JDBC");
	}

	public Connection open() {
		try {
			Class.forName(driver);
			this.c = DriverManager.getConnection(url);
			createUserTable();
			createNickTable();
			createTempMuteTable();
			createTempBanTable();
			createJailTable();
			createIpTable();
			createUnLockTable();
			createKitTable();
			createTimeTable();
			createEcoTable();
			return c;
		} catch (SQLException e) {
			System.out.println("Could not connect to SQLite server! because: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("SQLite file (JDBC Driver) not found!");
		}
		return this.c;
	}

	public boolean checkConnection() {
		if (this.c != null) {
			return true;
		} else {
			return false;
		}
	}

	public Connection getConnection() {
		return this.c;
	}

	public void closeConnection(Connection c) {
		c = null;
	}

	private void createUserTable() {
		String sql = "CREATE TABLE IF NOT EXISTS user_table (id integer PRIMARY KEY,username text,uuid text,socialspy boolan,muted boolean,banned boolean);";

		try {
			Statement stmt = this.c.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createNickTable() {
		String sql = "CREATE TABLE IF NOT EXISTS nick_table (id integer PRIMARY KEY,username text,nickname text);";

		try {
			Statement stmt = this.c.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createEcoTable() {
		String sql = "CREATE TABLE IF NOT EXISTS eco_table (id integer PRIMARY KEY,username text,money double);";

		try {
			Statement stmt = this.c.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createTimeTable() {
		String sql = "CREATE TABLE IF NOT EXISTS time_table (id integer PRIMARY KEY,username text,time text);";
		
		try {
			Statement stmt = this.c.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createUnLockTable() {
		String sql = "CREATE TABLE IF NOT EXISTS lock_table (id integer PRIMARY KEY,uuid text,keyword text,locked boolean);";

		try {
			Statement stmt = this.c.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createKitTable() {
		String sql = "CREATE TABLE IF NOT EXISTS kit_table (id integer PRIMARY KEY,uuid text,kit text,expire text);";

		try {
			Statement stmt = this.c.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createJailTable() {
		String sql = "CREATE TABLE IF NOT EXISTS jail_table (id integer PRIMARY KEY,username text,jail text);";

		try {
			Statement stmt = this.c.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createIpTable() {
		String sql = "CREATE TABLE IF NOT EXISTS ip_table (id integer PRIMARY KEY,address text,boolean banned);";

		try {
			Statement stmt = this.c.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createTempMuteTable() {
		String sql = "CREATE TABLE IF NOT EXISTS mute_table (id integer PRIMARY KEY,username text,expire text);";

		try {
			Statement stmt = this.c.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createTempBanTable() {
		String sql = "CREATE TABLE IF NOT EXISTS ban_table (id integer PRIMARY KEY,username text,expire text);";

		try {
			Statement stmt = this.c.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createuser(String name, String UUID, boolean socialspy, boolean banned, boolean mute) {
		name = name.toLowerCase();

		String sql = "INSERT INTO user_table(username,uuid,socialspy,banned,muted) VALUES(?,?,?,?,?)";

		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

			pstmt.setString(1, name);
			pstmt.setString(2, UUID);
			pstmt.setBoolean(3, socialspy);
			pstmt.setBoolean(4, banned);
			pstmt.setBoolean(5, mute);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createBalance(String name, double money) {
		name = name.toLowerCase();

		String sql = "INSERT INTO eco_table(username,money) VALUES(?,?)";

		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

			pstmt.setString(1, name);
			pstmt.setDouble(2, money);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setLockAccount(String UUID, String keyword) {

		String sql = "INSERT INTO lock_table(uuid,keyword,locked) VALUES(?,?,?)";

		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

			pstmt.setString(1, UUID);
			pstmt.setString(2, keyword);
			pstmt.setBoolean(3, false);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setKitDelay(String UUID, String kit, String expire) {

		String sql = "INSERT INTO kit_table(uuid,kit,expire) VALUES(?,?,?)";

		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

			pstmt.setString(1, UUID);
			pstmt.setString(2, kit);
			pstmt.setString(3, expire);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean hasKeyword(String UUID) {

		String sql = "SELECT id FROM lock_table where uuid = '" + UUID + "';";

		try {

			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			return rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean hasBalance(String user) {
		user = user.toLowerCase();

		String sql = "SELECT money FROM eco_table where username = '" + user + "';";

		try {
			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			return rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean isLocked(String UUID) {

		String sql = "SELECT locked FROM lock_table where uuid = '" + UUID + "';";

		try {
			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			return rs.getBoolean("locked");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public void updateKeyword(String UUID, String keyword) {

		String sql = "UPDATE lock_table SET keyword = ? WHERE uuid = ?";

		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

			pstmt.setString(1, keyword);
			pstmt.setString(2, UUID);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setBalance(String user, double money) {
		user = user.toLowerCase();

		String sql = "UPDATE eco_table SET money = ? WHERE username = ?";

		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

			pstmt.setDouble(1, money);
			pstmt.setString(2, user);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setLocked(String UUID, boolean locked) {

		String sql = "UPDATE lock_table SET locked = ? WHERE uuid = ?";

		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

			pstmt.setBoolean(1, locked);
			pstmt.setString(2, UUID);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteKit(String UUID, String kit) {

		String sql = "DELETE FROM kit_table WHERE uuid = ? AND kit = ?";
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

			pstmt.setString(1, UUID);
			pstmt.setString(2, kit);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteKeyword(String UUID) {

		String sql = "DELETE FROM lock_table WHERE uuid = ?";
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

			pstmt.setString(1, UUID);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getKyword(String UUID) {

		String sql = "SELECT keyword FROM lock_table WHERE uuid = '" + UUID + "';";

		try {

			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			return rs.getString("keyword");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void banIp(String address) {

		String sql = "INSERT INTO ip_table(address,banned) VALUES(?,?)";

		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

			pstmt.setString(1, address);
			pstmt.setBoolean(3, true);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setJail(String name, String jail) {
		name = name.toLowerCase();

		String sql = "INSERT INTO jail_table(username,jail) VALUES(?,?)";

		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

			pstmt.setString(1, name);
			pstmt.setString(2, jail);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertNick(String name, String nick) {
		name = name.toLowerCase();

		String sql = "INSERT INTO nick_table(username,nickname) VALUES(?,?)";

		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

			pstmt.setString(1, name);
			pstmt.setString(2, nick);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setExpire(String user, String expire) {
		user = user.toLowerCase();

		String sql = "SELECT expire FROM mute_table WHERE username = '" + user + "';";

		try {

			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				sql = "UPDATE mute_table SET expire = ? WHERE username = ?";

				try {
					PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

					pstmt.setString(1, expire);
					pstmt.setString(2, user);

					pstmt.executeUpdate();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			} else {
				sql = "INSERT INTO mute_table(username,expire) VALUES(?,?)";

				try {
					PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

					pstmt.setString(1, user);
					pstmt.setString(2, expire);
					pstmt.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setTime(String user, String time) {
		user = user.toLowerCase();

		String sql = "SELECT time FROM time_table where username = '" + user + "';";

		try {

			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				sql = "UPDATE time_table SET time = ? WHERE username = ?";

				try {
					PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

					pstmt.setString(1, time);
					pstmt.setString(2, user);

					pstmt.executeUpdate();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			} else {
				sql = "INSERT INTO time_table(username,time) VALUES(?,?)";

				try {
					PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

					pstmt.setString(1, user);
					pstmt.setString(2, time);
					pstmt.executeUpdate();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setBanExpire(String user, String expire) {
		user = user.toLowerCase();

		String sql = "SELECT expire FROM ban_table where username = '" + user + "'";

		try {

			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				sql = "UPDATE ban_table SET expire = ? WHERE username = ?";

				try {
					PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

					pstmt.setString(1, expire);
					pstmt.setString(2, user);

					pstmt.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				sql = "INSERT INTO ban_table(username,expire) VALUES(?,?)";

				try {
					PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

					pstmt.setString(1, user);
					pstmt.setString(2, expire);
					pstmt.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isTempMuted(String user) {
		user = user.toLowerCase();

		String sql = "SELECT id FROM mute_table where username = '" + user + "';";

		try {

			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			return rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean hasDelay(String UUID, String kit) {

		String sql = "SELECT id FROM kit_table where uuid = '" + UUID + "' AND kit = '" + kit + "';";

		try {

			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			return rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public String getTime(String user) {
		user = user.toLowerCase();

		String sql = "SELECT time FROM time_table WHERE username = '" + user + "';";

		try {

			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			return rs.getString("time");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public double getBalance(String user) {
		user = user.toLowerCase();

		String sql = "SELECT money FROM eco_table WHERE username = '" + user + "';";

		try {

			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			return rs.getDouble("money");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return -1;
	}

	public String getDelay(String UUID, String kit) {

		String sql = "SELECT expire FROM kit_table WHERE uuid = '" + UUID + "' AND kit = '" + kit + "';";

		try {

			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			return rs.getString("expire");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean isBannedIp(String address) {

		String sql = "SELECT id FROM ip_table where address = '" + address + "';";

		try {

			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			return rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean isTempBanned(String user) {
		user = user.toLowerCase();

		String sql = "SELECT id FROM ban_table where username = '" + user + "';";

		try {

			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			return rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public String getMuteExpire(String user) {

		user = user.toLowerCase();

		String sql = "SELECT expire FROM mute_table username = '" + user + "';";

		try {

			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			return rs.getString("expire");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getBanExpire(String user) {

		user = user.toLowerCase();

		String sql = "SELECT expire FROM ban_table WHERE username = '" + user + "';";

		try {

			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			return rs.getString("expire");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean hasNick(String user) {
		user = user.toLowerCase();

		String sql = "SELECT id FROM nick_table where username = '" + user + "';";

		try {

			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			return rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean isJailed(String user) {
		user = user.toLowerCase();

		String sql = "SELECT jail FROM jail_table where username = '" + user + "';";

		try {

			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			return rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public String getJail(String user) {
		user = user.toLowerCase();

		String sql = "SELECT jail FROM jail_table WHERE username = '" + user + "';";

		try {

			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			return rs.getString("jail");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean exists(String user) {
		user = user.toLowerCase();

		String sql = "SELECT id FROM user_table where username = '" + user + "';";

		try {

			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			return rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public String getNick(String user) {

		user = user.toLowerCase();

		String sql = "SELECT nickname FROM nick_table WHERE username = '" + user + "';";

		try {

			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			return rs.getString("nickname");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getReal(String user) {

		user = user.toLowerCase();

		String sql = "SELECT username FROM nick_table WHERE nickname = '" + user + "';";

		try {

			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);
            if(rs.next())
			 return rs.getString("username");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isUsed(String nick) {

		String sql = "SELECT username FROM nick_table where nickname = '" + nick + "';";

		try {

			Statement stmt = this.getConnection().createStatement();

			ResultSet rs = stmt.executeQuery(sql);

			return rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean getMuted(String user) {

		user = user.toLowerCase();

		String sql = "SELECT muted FROM user_table where username = '" + user + "';";

		try {

			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			return rs.getBoolean("muted");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean getBanned(String user) {

		user = user.toLowerCase();

		String sql = "SELECT banned FROM user_table WHERE username = '" + user + "';";

		try {

			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			return rs.getBoolean("banned");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean getSocialspy(String user) {

		user = user.toLowerCase();

		String sql = "SELECT socialspy FROM user_table WHERE username = '" + user + "';";

		try {

			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			return rs.getBoolean("socialspy");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void setNick(String user, String nick) {

		user = user.toLowerCase();

		String sql = "UPDATE nick_table SET nickname = ? WHERE username = ?";

		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

			pstmt.setString(1, nick);
			pstmt.setString(2, user);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void removeNick(String name) {
		name = name.toLowerCase();
		String sql = "DELETE FROM nick_table WHERE username = ?";
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

			pstmt.setString(1, name);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void removeJail(String name) {
		name = name.toLowerCase();
		String sql = "DELETE FROM jail_table WHERE username = ?";
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

			pstmt.setString(1, name);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void unBanIp(String address) {
		String sql = "DELETE FROM ip_table WHERE address = ?";
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			pstmt.setString(1, address);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void removeTempMute(String name) {
		name = name.toLowerCase();

		if (!this.isTempMuted(name))
			return;

		String sql = "DELETE FROM mute_table WHERE username = ?";
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

			pstmt.setString(1, name);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void removeTempBan(String name) {
		name = name.toLowerCase();

		if (!this.isTempBanned(name))
			return;

		String sql = "DELETE FROM ban_table WHERE username = ?";
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

			pstmt.setString(1, name);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setMuted(String user, boolean mute) {

		user = user.toLowerCase();

		String sql = "UPDATE user_table SET muted = ? WHERE username = ?";

		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

			pstmt.setBoolean(1, mute);
			pstmt.setString(2, user);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setBanned(String user, boolean ban) {

		user = user.toLowerCase();

		String sql = "UPDATE user_table SET banned = ? WHERE username = ?";

		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

			pstmt.setBoolean(1, ban);
			pstmt.setString(2, user);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setSocialspy(String user, boolean social) {

		user = user.toLowerCase();

		String sql = "UPDATE user_table SET socialspy = ? WHERE username = ?";

		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

			pstmt.setBoolean(1, social);
			pstmt.setString(2, user);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
