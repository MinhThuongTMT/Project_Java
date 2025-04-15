package QuanLyLopHoc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class QuanLyBaiTap extends JPanel {

	private static final long serialVersionUID = 1L;

	private JComboBox<String> TimMon_comboBox;
	private JTable table;
	private DefaultTableModel tableModel;

	// Thông tin kết nối database
	private static final String DB_URL = "jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:6543/postgres?sslmode=require&pgbouncer=true&prepareThreshold=0";
	private static final String DB_USERNAME = "postgres.vpehkzjmzpcskfzjjyql";
	private static final String DB_PASSWORD = "MinhThuong0808";

	public QuanLyBaiTap() {
		setBackground(new Color(0, 0, 121));
		setBounds(81, 11, 895, 652);
		setLayout(null);

		// Tiêu đề
		JLabel lblNewLabel = new JLabel("QUẢN LÝ BÀI TẬP");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 25));
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setBounds(300, 10, 242, 40);
		add(lblNewLabel);

		// Nút Bài Tập Đã Nộp
		JButton DaNop_btn = new JButton("Bài Tập Đã Nộp");
		DaNop_btn.setFont(new Font("Times New Roman", Font.BOLD, 14));
		DaNop_btn.setBackground(new Color(255, 204, 0));
		DaNop_btn.setForeground(Color.BLACK);
		DaNop_btn.setBounds(43, 577, 150, 40);
		add(DaNop_btn);

		// Nút Bài Tập Chưa Nộp
		JButton ChuaNop_btn = new JButton("Bài Tập Chưa Nộp");
		ChuaNop_btn.setFont(new Font("Times New Roman", Font.BOLD, 14));
		ChuaNop_btn.setBackground(new Color(255, 204, 0));
		ChuaNop_btn.setForeground(Color.BLACK);
		ChuaNop_btn.setBounds(229, 577, 162, 40);
		add(ChuaNop_btn);

		// Nút Xuất Excel
		JButton btnXuat = new JButton("Xuất Excel");
		btnXuat.setFont(new Font("Times New Roman", Font.BOLD, 14));
		btnXuat.setBackground(new Color(0, 204, 0));
		btnXuat.setForeground(Color.WHITE);
		btnXuat.setBounds(693, 577, 150, 40);
		add(btnXuat);

		// Nút Tìm Kiếm
		ImageIcon icon = new ImageIcon(getClass().getResource("/Icon/find.png"));
		Image scaledImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		icon = new ImageIcon(scaledImage);
		JButton btnTimKiem = new JButton("Tìm Kiếm", icon);
		btnTimKiem.setFont(new Font("Times New Roman", Font.BOLD, 14));
		btnTimKiem.setForeground(Color.BLACK);
		btnTimKiem.setBounds(576, 72, 131, 40);
		btnTimKiem.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnTimKiem.setVerticalTextPosition(SwingConstants.CENTER);
		add(btnTimKiem);

		// ComboBox Tìm kiếm môn học
		TimMon_comboBox = new JComboBox<>();
		TimMon_comboBox.setModel(new DefaultComboBoxModel<>(new String[] { "", "Lập Trình Hướng Đối Tượng",
				"Hệ Thống nhúng IOT", "Hệ Thống Cảm Biến", "Điện Toán Đám Mây", "Phát Triển ứng Dụng", "Tiếng Anh" }));
		TimMon_comboBox.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		TimMon_comboBox.setBounds(236, 72, 296, 40);
		add(TimMon_comboBox);

		// Tạo JTable với DefaultTableModel
		String[] columnNames = { "MSSV", "Họ tên", "Tiêu đề bài tập", "Môn học", "Trạng thái", "Thời gian nộp",
				"Tên tệp" };
		tableModel = new DefaultTableModel(columnNames, 0);
		table = new JTable(tableModel);
		table.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		table.setRowHeight(25);
		table.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 14));
		table.getTableHeader().setBackground(new Color(255, 204, 0));
		table.getTableHeader().setForeground(Color.BLACK);

		// Thêm JTable vào JScrollPane để hỗ trợ cuộn
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(21, 151, 853, 401);
		add(scrollPane);

		// Nút Xóa
		JButton btnDelete = new JButton("Xóa", null);
		btnDelete.setVerticalTextPosition(SwingConstants.CENTER);
		btnDelete.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnDelete.setForeground(new Color(255, 255, 255));
		btnDelete.setFont(new Font("Times New Roman", Font.BOLD, 14));
		btnDelete.setBackground(new Color(255, 0, 0));
		btnDelete.setBounds(802, 72, 67, 40);
		add(btnDelete);

		// Nút Back
		JButton btnBack = new JButton("Back");
		btnBack.setFont(new Font("Times New Roman", Font.BOLD, 14));
		btnBack.setBackground(new Color(128, 64, 0));
		btnBack.setForeground(new Color(255, 255, 255));
		btnBack.setBounds(428, 577, 80, 40);
		add(btnBack);

		JLabel labelMonHoc = new JLabel("Môn học:");
		labelMonHoc.setForeground(Color.WHITE);
		labelMonHoc.setFont(new Font("Times New Roman", Font.BOLD, 15));
		labelMonHoc.setBounds(141, 72, 85, 40);
		add(labelMonHoc);

		// Tải dữ liệu ban đầu từ database
		loadDataFromDatabase("");

		// Sự kiện nút Tìm Kiếm
		btnTimKiem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String monHoc = (String) TimMon_comboBox.getSelectedItem();
				if (monHoc != null && !monHoc.isEmpty()) {
					loadDataFromDatabase(monHoc);
				} else {
					loadDataFromDatabase("");
				}
			}
		});

		// Sự kiện nút Bài Tập Đã Nộp
		DaNop_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadDataByStatus("ĐÃ NỘP", "NỘP MUỘN");
			}
		});

		// Sự kiện nút Bài Tập Chưa Nộp
		ChuaNop_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadDataChuaNop();
			}
		});

		// Sự kiện nút Back
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String monHoc = (String) TimMon_comboBox.getSelectedItem();
				if (monHoc != null && !monHoc.isEmpty()) {
					loadDataFromDatabase(monHoc);
				} else {
					loadDataFromDatabase("");
				}
			}
		});

		// Sự kiện nút Xóa
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(QuanLyBaiTap.this, "Vui lòng chọn một bài tập để xóa!", "Cảnh báo",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				String mssv = tableModel.getValueAt(selectedRow, 0).toString();
				String tieuDe = tableModel.getValueAt(selectedRow, 2).toString();
				String monHoc = tableModel.getValueAt(selectedRow, 3).toString();

				int confirm = JOptionPane.showConfirmDialog(QuanLyBaiTap.this, "Bạn có chắc chắn muốn xóa bài tập này?",
						"Xác nhận xóa", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					try {
						deleteFromDatabase(mssv, tieuDe, monHoc);
						loadDataFromDatabase("");
						JOptionPane.showMessageDialog(QuanLyBaiTap.this, "Xóa bài tập thành công!", "Thành công",
								JOptionPane.INFORMATION_MESSAGE);
					} catch (SQLException ex) {
						JOptionPane.showMessageDialog(QuanLyBaiTap.this, "Lỗi khi xóa bài tập: " + ex.getMessage(),
								"Lỗi", JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
				}
			}
		});

		// Sự kiện nút Xuất Excel
		btnXuat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exportToExcel();
			}
		});
	}

	// Phương thức tải dữ liệu từ database
	private void loadDataFromDatabase(String monHocFilter) {
		tableModel.setRowCount(0);
		String sql = "SELECT s.mssv, s.hoten, b.tieude, b.monhoc, b.trangthai, b.thoigian_nop, b.ten_tep "
				+ "FROM students s " + "LEFT JOIN baitap b ON s.mssv = b.mssv " + "WHERE b.monhoc = ? OR ? = ''";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, monHocFilter);
			pstmt.setString(2, monHocFilter);

			rs = pstmt.executeQuery();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

			while (rs.next()) {
				String mssv = rs.getString("mssv");
				String hoTen = rs.getString("hoten");
				String tieuDe = rs.getString("tieude");
				String monHoc = rs.getString("monhoc");
				String trangThai = rs.getString("trangthai");
				Timestamp thoiGianNop = rs.getTimestamp("thoigian_nop");
				String tenTep = rs.getString("ten_tep");

				tableModel.addRow(new Object[] { mssv, hoTen, tieuDe != null ? tieuDe : "",
						monHoc != null ? monHoc : "", trangThai != null ? trangThai : "CHƯA NỘP",
						thoiGianNop != null ? sdf.format(thoiGianNop) : "", tenTep != null ? tenTep : "" });
			}

			if (tableModel.getRowCount() == 0) {
				tableModel.addRow(new Object[] { "", "", "Không tìm thấy bài tập nào.", "", "", "", "" });
			}

		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu từ database: " + ex.getMessage(), "Lỗi",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException closeEx) {
				closeEx.printStackTrace();
			}
		}
	}

	// Phương thức tải dữ liệu theo trạng thái (Đã Nộp hoặc Nộp Muộn)
	private void loadDataByStatus(String... statuses) {
		tableModel.setRowCount(0);
		String sql = "SELECT s.mssv, s.hoten, b.tieude, b.monhoc, b.trangthai, b.thoigian_nop, b.ten_tep "
				+ "FROM students s " + "JOIN baitap b ON s.mssv = b.mssv " + "WHERE b.trangthai IN (";
		for (int i = 0; i < statuses.length; i++) {
			sql += "?";
			if (i < statuses.length - 1)
				sql += ",";
		}
		sql += ")";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			pstmt = conn.prepareStatement(sql);

			for (int i = 0; i < statuses.length; i++) {
				pstmt.setString(i + 1, statuses[i]);
			}

			rs = pstmt.executeQuery();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

			while (rs.next()) {
				String mssv = rs.getString("mssv");
				String hoTen = rs.getString("hoten");
				String tieuDe = rs.getString("tieude");
				String monHoc = rs.getString("monhoc");
				String trangThai = rs.getString("trangthai");
				Timestamp thoiGianNop = rs.getTimestamp("thoigian_nop");
				String tenTep = rs.getString("ten_tep");

				tableModel.addRow(new Object[] { mssv, hoTen, tieuDe, monHoc, trangThai,
						thoiGianNop != null ? sdf.format(thoiGianNop) : "Chưa nộp", tenTep });
			}

			if (tableModel.getRowCount() == 0) {
				tableModel.addRow(new Object[] { "", "", "Không tìm thấy bài tập nào.", "", "", "", "" });
			}

		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu từ database: " + ex.getMessage(), "Lỗi",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException closeEx) {
				closeEx.printStackTrace();
			}
		}
	}

	// Phương thức tải danh sách sinh viên chưa nộp bài
	private void loadDataChuaNop() {
		tableModel.setRowCount(0);
		String sql = "SELECT s.mssv, s.hoten, g.tieu_de, g.mon_hoc " + "FROM students s " + "CROSS JOIN giaobaitap g "
				+ "LEFT JOIN baitap b ON s.mssv = b.mssv AND g.tieu_de = b.tieude AND g.mon_hoc = b.monhoc "
				+ "WHERE b.mssv IS NULL";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String mssv = rs.getString("mssv");
				String hoTen = rs.getString("hoten");
				String tieuDe = rs.getString("tieu_de");
				String monHoc = rs.getString("mon_hoc");

				tableModel.addRow(new Object[] { mssv, hoTen, tieuDe, monHoc, "CHƯA NỘP", "", "" });
			}

			if (tableModel.getRowCount() == 0) {
				tableModel.addRow(new Object[] { "", "", "Không tìm thấy bài tập chưa nộp.", "", "", "", "" });
			}

		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu từ database: " + ex.getMessage(), "Lỗi",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException closeEx) {
				closeEx.printStackTrace();
			}
		}
	}

	// Phương thức xóa bài tập từ database
	private void deleteFromDatabase(String mssv, String tieuDe, String monHoc) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmtDelete = null;
		try {
			conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			String sqlDelete = "DELETE FROM baitap WHERE mssv = ? AND tieude = ? AND monhoc = ?";
			pstmtDelete = conn.prepareStatement(sqlDelete);
			pstmtDelete.setString(1, mssv);
			pstmtDelete.setString(2, tieuDe);
			pstmtDelete.setString(3, monHoc);
			pstmtDelete.executeUpdate();

		} finally {
			try {
				if (pstmtDelete != null)
					pstmtDelete.close();
				if (conn != null)
					conn.close();
			} catch (SQLException closeEx) {
				closeEx.printStackTrace();
			}
		}
	}

	// Phương thức xuất dữ liệu ra file Excel
	private void exportToExcel() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
		fileChooser.setFileFilter(new FileNameExtensionFilter("Excel files (*.xlsx)", "xlsx"));

		int userSelection = fileChooser.showSaveDialog(this);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			String filePath = fileChooser.getSelectedFile().getAbsolutePath();
			if (!filePath.toLowerCase().endsWith(".xlsx")) {
				filePath += ".xlsx";
			}

			try (Workbook workbook = new XSSFWorkbook()) {
				Sheet sheet = workbook.createSheet("Danh sách bài tập");

				Row headerRow = sheet.createRow(0);
				String[] columns = { "MSSV", "Họ tên", "Tiêu đề bài tập", "Môn học", "Trạng thái", "Thời gian nộp",
						"Tên tệp" };
				for (int i = 0; i < columns.length; i++) {
					Cell cell = headerRow.createCell(i);
					cell.setCellValue(columns[i]);
					CellStyle headerStyle = workbook.createCellStyle();
					org.apache.poi.ss.usermodel.Font font = workbook.createFont();
					font.setBold(true);
					headerStyle.setFont(font);
					cell.setCellStyle(headerStyle);
				}

				for (int i = 0; i < tableModel.getRowCount(); i++) {
					Row row = sheet.createRow(i + 1);
					for (int j = 0; j < tableModel.getColumnCount(); j++) {
						Cell cell = row.createCell(j);
						Object value = tableModel.getValueAt(i, j);
						if (value != null) {
							cell.setCellValue(value.toString());
						}
					}
				}

				for (int i = 0; i < columns.length; i++) {
					sheet.autoSizeColumn(i);
				}

				try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
					workbook.write(fileOut);
					JOptionPane.showMessageDialog(this, "Xuất file Excel thành công tại: " + filePath, "Thành công",
							JOptionPane.INFORMATION_MESSAGE);
				}

			} catch (IOException ex) {
				JOptionPane.showMessageDialog(this, "Lỗi khi xuất file Excel: " + ex.getMessage(), "Lỗi",
						JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}
		}
	}

	// Phương thức đếm số lượng bài tập
	public int getAssignmentCount() {
		int count = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			String sql = "SELECT COUNT(*) FROM baitap";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Lỗi khi đếm bài tập: " + ex.getMessage(), "Lỗi",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException closeEx) {
				closeEx.printStackTrace();
			}
		}
		return count;
	}
}