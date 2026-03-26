package javatutorial;

import java.sql.*;

public class CConnection {
    public Connection cn;

private String url = "jdbc:mysql://127.0.0.1:3306/kampus?useSSL=false&serverTimezone=UTC";
private String user = "root";
private String password = "123456";

    public void openConnection() {
        try {
            cn = DriverManager.getConnection(url, user, password);

            if (cn != null) {
                System.out.println("✅ Database Connected!");
            } else {
                System.out.println("❌ CN NULL");
            }

        } catch (Exception ex) {
            System.out.println("❌ KONEKSI GAGAL!");
            ex.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (cn != null && !cn.isClosed()) {
                cn.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}