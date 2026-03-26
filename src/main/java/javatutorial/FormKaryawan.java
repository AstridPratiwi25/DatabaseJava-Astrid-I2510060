package javatutorial;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FormKaryawan extends JFrame {

    private JTextField txtNip, txtNama, txtTempLhr, txtTgl, txtBln, txtThn, txtJabatan;
    private JButton btnInsert, btnUpdate, btnDelete, btnClose;
    private JTable TblKar;
    private DefaultTableModel defTab;

    // TAMBAHAN: koneksi global
    private CConnection db;

    public FormKaryawan() {
        setTitle("Form Karyawan - CRUD Java Swing");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        db = new CConnection();
        db.openConnection();

        initComponents();
        fillTable(false);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel background = new JPanel(new BorderLayout());
        background.setBackground(new Color(210, 206, 188));
        background.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(background, BorderLayout.CENTER);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(245, 242, 233));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(178, 172, 147), 2),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));
        background.add(card, BorderLayout.CENTER);

        JPanel panelInput = new JPanel(new GridBagLayout());
        panelInput.setBackground(new Color(245, 242, 233));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panelInput.add(new JLabel("NIP"), gbc);
        gbc.gridx = 1; txtNip = new JTextField(18); panelInput.add(txtNip, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelInput.add(new JLabel("Nama"), gbc);
        gbc.gridx = 1; txtNama = new JTextField(22); panelInput.add(txtNama, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelInput.add(new JLabel("Temp Lhr"), gbc);
        gbc.gridx = 1; txtTempLhr = new JTextField(22); panelInput.add(txtTempLhr, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelInput.add(new JLabel("Tgl Lhr"), gbc);
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        txtTgl = new JTextField(2);
        txtBln = new JTextField(2);
        txtThn = new JTextField(4);
        datePanel.add(txtTgl); datePanel.add(new JLabel("-"));
        datePanel.add(txtBln); datePanel.add(new JLabel("-"));
        datePanel.add(txtThn);
        gbc.gridx = 1; panelInput.add(datePanel, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panelInput.add(new JLabel("Jabatan"), gbc);
        gbc.gridx = 1; txtJabatan = new JTextField(22); panelInput.add(txtJabatan, gbc);

        JPanel panelBtn = new JPanel();
        btnInsert = new JButton("Insert");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClose = new JButton("Close");

        panelBtn.add(btnInsert);
        panelBtn.add(btnUpdate);
        panelBtn.add(btnDelete);
        panelBtn.add(btnClose);

        TblKar = new JTable();
        JScrollPane scroll = new JScrollPane(TblKar);

        card.add(panelInput);
        card.add(panelBtn);
        card.add(scroll);

        // EVENT
        btnInsert.addActionListener(e -> insertData());
        btnUpdate.addActionListener(e -> updateData());
        btnDelete.addActionListener(e -> deleteData());
        btnClose.addActionListener(e -> dispose());
    }

    // FIX: pakai koneksi langsung
    private void fillTable(boolean filter) {
        Object[] colHeader = {"NIP", "Nama", "Temp Lahir", "Tgl Lahir", "Jabatan"};
        defTab = new DefaultTableModel(null, colHeader);

        try {
            Statement st = db.cn.createStatement();
            ResultSet rs;

            if (filter) {
                rs = st.executeQuery("SELECT * FROM karyawan WHERE nip='" + txtNip.getText() + "'");
            } else {
                rs = st.executeQuery("SELECT * FROM karyawan");
            }

            while (rs.next()) {
                String[] row = {
                    rs.getString("nip"),
                    rs.getString("nama"),
                    rs.getString("tempat_lahir"),
                    rs.getString("tanggal_lahir"),
                    rs.getString("jabatan")
                };
                defTab.addRow(row);
            }

            TblKar.setModel(defTab);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void insertData() {
        try {
            String sql = "INSERT INTO karyawan VALUES('" +
                    txtNip.getText() + "','" +
                    txtNama.getText() + "','" +
                    txtTempLhr.getText() + "','" +
                    txtThn.getText()+"-"+txtBln.getText()+"-"+txtTgl.getText() + "','" +
                    txtJabatan.getText() + "')";

            Statement st = db.cn.createStatement();
            st.execute(sql);

            JOptionPane.showMessageDialog(this, "Data Tersimpan");
            fillTable(false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void updateData() {
        try {
            String sql = "UPDATE karyawan SET nama='" + txtNama.getText() +
                    "', tempat_lahir='" + txtTempLhr.getText() +
                    "', tanggal_lahir='" + txtThn.getText()+"-"+txtBln.getText()+"-"+txtTgl.getText() +
                    "', jabatan='" + txtJabatan.getText() +
                    "' WHERE nip='" + txtNip.getText() + "'";

            Statement st = db.cn.createStatement();
            st.execute(sql);

            JOptionPane.showMessageDialog(this, "Data Diupdate");
            fillTable(false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void deleteData() {
        try {
            String sql = "DELETE FROM karyawan WHERE nip='" + txtNip.getText() + "'";
            Statement st = db.cn.createStatement();
            st.execute(sql);

            JOptionPane.showMessageDialog(this, "Data Dihapus");
            fillTable(false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}