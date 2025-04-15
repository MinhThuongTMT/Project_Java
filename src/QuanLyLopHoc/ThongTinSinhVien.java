package QuanLyLopHoc;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

public class ThongTinSinhVien extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel ThongTinSinhVien;
	private JTextField MSSV_text1;
	private JTextField HoTen_text1;
	private JTextField MaMon_text1;
	private JTextField SoTin_text1;
	private JDateChooser NgaySinh_text;
	private JComboBox<String> GioiTinh_ComboBox;
	private JTextField Email_text1;
	private JTextField ThoiGian_text1;
	private JComboBox<String> MonHoc_comboBox1;
	private JComboBox<String> Lop_comboBox_1;
	private JLabel avata;

	// Các thuộc tính để lưu dữ liệu
	private String hoTen, mssv, lop, ngaySinh, gioiTinh, email, monHoc, maMon, soTin, thoiGian;
	private final String originalMssv;
	private final List<String[]> pendingCourses = new ArrayList<>(); // Lưu tạm danh sách môn học trước khi nhấn Lưu

	// Thông tin kết nối database
	private static final String DB_URL = "jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:6543/postgres?sslmode=require&pgbouncer=true";
	private static final String DB_USERNAME = "postgres.vpehkzjmzpcskfzjjyql";
	private static final String DB_PASSWORD = "MinhThuong0808";

	// Regex để kiểm tra email
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

	public ThongTinSinhVien() {
		this.originalMssv = "";
		initialize();
	}

	public ThongTinSinhVien(String hoTen, String mssv, String lop, String ngaySinh, String gioiTinh, String email,
			String monHoc, String maMon, String soTin, String thoiGian) {
		this.hoTen = hoTen;
		this.mssv = mssv;
		this.originalMssv = mssv;
		this.lop = lop;
		this.ngaySinh = ngaySinh;
		this.gioiTinh = gioiTinh;
		this.email = email;
		this.monHoc = monHoc;
		this.maMon = maMon;
		this.soTin = soTin;
		this.thoiGian = thoiGian;
		initialize();
		loadDataToFields();
	}

	private void initialize() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 895, 652);
		setLocationRelativeTo(null);

		ThongTinSinhVien = new JPanel();
		ThongTinSinhVien.setBackground(new Color(0, 0, 121));
		ThongTinSinhVien.setBorder(new EmptyBorder(5, 5, 5, 5));
		ThongTinSinhVien.setLayout(null);
		setContentPane(ThongTinSinhVien);

		// Panel tiêu đề
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 204, 0));
		panel.setBounds(0, 0, 881, 57);
		panel.setLayout(null);
		ThongTinSinhVien.add(panel);

		JLabel lblTitle = new JLabel("THÔNG TIN SINH VIÊN");
		lblTitle.setBounds(284, 11, 302, 37);
		lblTitle.setForeground(new Color(0, 0, 0));
		lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 25));
		panel.add(lblTitle);

		// Avatar
		avata = new JLabel();
		avata.setBounds(99, 95, 224, 227);
		avata.setBorder(new LineBorder(Color.WHITE, 3));
		avata.setBackground(Color.WHITE);
		avata.setOpaque(true);
		ThongTinSinhVien.add(avata);

		// Các trường thông tin
		HoTen_text1 = createTextField(539, 95, 310, 30);
		MSSV_text1 = createTextField(539, 146, 310, 30);
		Email_text1 = createTextField(539, 299, 310, 30);
		SoTin_text1 = createTextField(539, 401, 310, 30);
		MaMon_text1 = createTextField(539, 452, 310, 30);
		ThoiGian_text1 = createTextField(539, 503, 310, 30);

		Lop_comboBox_1 = new JComboBox<>(
				new String[] { "", "D21CQVTHI01-N", "D21CQVTVT01-N", "D21CQVTMD01-N", "D22CQVT01-N", "D23CQVTMD01-N" });
		Lop_comboBox_1.setBounds(539, 197, 310, 30);
		Lop_comboBox_1.setEnabled(false);
		Lop_comboBox_1.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		ThongTinSinhVien.add(Lop_comboBox_1);

		NgaySinh_text = new JDateChooser();
		NgaySinh_text.setBounds(539, 248, 150, 30);
		NgaySinh_text.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		NgaySinh_text.setDateFormatString("dd/MM/yyyy");
		NgaySinh_text.setEnabled(false);
		ThongTinSinhVien.add(NgaySinh_text);

		GioiTinh_ComboBox = new JComboBox<>(new String[] { "", "Nam", "Nữ" });
		GioiTinh_ComboBox.setBounds(793, 248, 56, 30);
		GioiTinh_ComboBox.setEnabled(false);
		GioiTinh_ComboBox.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		ThongTinSinhVien.add(GioiTinh_ComboBox);

		MonHoc_comboBox1 = new JComboBox<>(new String[] { "", "Lập Trình Hướng Đối Tượng", "Hệ Thống nhúng IOT",
				"Hệ Thống Cảm Biến", "Điện Toán Đám Mây", "Phát Triển ứng Dụng", "Tiếng Anh" });
		MonHoc_comboBox1.setBounds(539, 350, 310, 30);
		MonHoc_comboBox1.setEnabled(false);
		MonHoc_comboBox1.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		ThongTinSinhVien.add(MonHoc_comboBox1);

		// Các nhãn
		addLabel("HỌ TÊN :", 448, 95, 81, 29);
		addLabel("MSSV :", 448, 146, 81, 29);
		addLabel("LỚP :", 448, 197, 81, 29);
		addLabel("NGÀY SINH:", 448, 248, 103, 29);
		addLabel("GIỚI TÍNH:", 700, 248, 93, 29);
		addLabel("EMAIL :", 448, 299, 81, 29);
		addLabel("MÔN HỌC :", 448, 350, 93, 29);
		addLabel("SỐ TÍN:", 448, 401, 81, 29);
		addLabel("MÃ MÔN :", 448, 452, 81, 29);
		addLabel("THỜI GIAN:", 448, 503, 93, 29);

		// Các nút
		JButton Sua_button = createButton("SỬA", 61, 360, 120, 44, new Color(50, 150, 255));
		Sua_button.addActionListener(e -> enableEditing(true));

		JButton Luu_button = createButton("LƯU", 252, 360, 120, 44, new Color(50, 150, 255));
		Luu_button.addActionListener(e -> saveStudentInfo());

		JButton ThemMon_button = createButton("THÊM MÔN", 61, 415, 120, 44, new Color(50, 200, 50));
		ThemMon_button.addActionListener(e -> addNewCourse());

		JButton HienThiMon_button = createButton("HIỂN THỊ MÔN", 252, 415, 120, 44, new Color(50, 200, 50));
		HienThiMon_button.addActionListener(e -> displayCourses());

		JButton NopBaiTap_button = createButton("NỘP BÀI TẬP", 139, 471, 162, 44, new Color(255, 165, 80));
		NopBaiTap_button.addActionListener(e -> {
			BaiTap baiTapFrame = new BaiTap(getHoTen(), getMssv());
			baiTapFrame.setVisible(true);
		});

		JButton HuyBaiTap_button = createButton("HỦY BÀI TẬP", 139, 526, 162, 44, new Color(255, 69, 0));
		HuyBaiTap_button.addActionListener(e -> cancelSubmittedAssignment());

		MonHoc_comboBox1.addItemListener(e -> updateCourseDetails());
	}

	private void cancelSubmittedAssignment() {
		String mssv = MSSV_text1.getText().trim();
		if (mssv.isEmpty()) {
			showError("Vui lòng nhập MSSV!");
			return;
		}

		try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
			// Lấy danh sách môn học có bài tập đã nộp từ bảng baitap
			String selectSQL = "SELECT DISTINCT monhoc FROM baitap WHERE mssv = ?";

			List<String> subjects = new ArrayList<>();
			try (PreparedStatement pstmtSelect = conn.prepareStatement(selectSQL)) {
				pstmtSelect.setString(1, mssv);
				try (ResultSet rs = pstmtSelect.executeQuery()) {
					while (rs.next()) {
						subjects.add(rs.getString("monhoc"));
					}
				}
			}

			if (subjects.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Không có bài tập nào đã nộp!", "Thông tin",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			// Hiển thị dialog chọn môn học
			String selectedSubject = (String) JOptionPane.showInputDialog(this, "Chọn môn học để hủy bài tập:",
					"Hủy bài tập", JOptionPane.QUESTION_MESSAGE, null, subjects.toArray(), subjects.get(0));

			if (selectedSubject == null) {
				return; // Người dùng hủy dialog
			}

			// Xóa bài tập của môn học được chọn từ bảng baitap
			String deleteSQL = "DELETE FROM baitap WHERE mssv = ? AND monhoc = ?";

			try (PreparedStatement pstmtDelete = conn.prepareStatement(deleteSQL)) {
				pstmtDelete.setString(1, mssv);
				pstmtDelete.setString(2, selectedSubject);
				int rowsAffected = pstmtDelete.executeUpdate();

				JOptionPane.showMessageDialog(this,
						rowsAffected > 0 ? "Đã hủy tất cả bài tập của môn '" + selectedSubject + "' thành công!"
								: "Không tìm thấy bài tập để hủy!",
						rowsAffected > 0 ? "Thành công" : "Thông báo",
						rowsAffected > 0 ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
			}
		} catch (SQLException ex) {
			showError("Lỗi khi hủy bài tập: " + ex.getMessage());
		}
	}

	private JTextField createTextField(int x, int y, int width, int height) {
		JTextField textField = new JTextField();
		textField.setBounds(x, y, width, height);
		textField.setEditable(false);
		textField.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		ThongTinSinhVien.add(textField);
		return textField;
	}

	private void addLabel(String text, int x, int y, int width, int height) {
		JLabel label = new JLabel(text);
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Times New Roman", Font.BOLD, 15));
		label.setBounds(x, y, width, height);
		ThongTinSinhVien.add(label);
	}

	private JButton createButton(String text, int x, int y, int width, int height, Color bgColor) {
		JButton button = new JButton(text);
		button.setBounds(x, y, width, height);
		button.setFont(new Font("Times New Roman", Font.BOLD, 15));
		button.setBackground(bgColor);
		button.setForeground(Color.BLACK);
		button.setBorder(new LineBorder(Color.WHITE, 1));
		ThongTinSinhVien.add(button);
		return button;
	}

	private void enableEditing(boolean enable) {
		loadDataFromDatabase();
		HoTen_text1.setEditable(enable);
		MSSV_text1.setEditable(enable);
		NgaySinh_text.setEnabled(enable);
		GioiTinh_ComboBox.setEnabled(enable);
		Email_text1.setEditable(enable);
		SoTin_text1.setEditable(enable);
		MaMon_text1.setEditable(enable);
		ThoiGian_text1.setEditable(enable);
		Lop_comboBox_1.setEnabled(enable);
		MonHoc_comboBox1.setEnabled(enable);
	}

	private void saveStudentInfo() {
		if (!validateStudentInfo())
			return;

		hoTen = HoTen_text1.getText().trim();
		mssv = MSSV_text1.getText().trim();
		lop = (String) Lop_comboBox_1.getSelectedItem();
		ngaySinh = new SimpleDateFormat("dd/MM/yyyy").format(NgaySinh_text.getDate());
		gioiTinh = (String) GioiTinh_ComboBox.getSelectedItem();
		email = Email_text1.getText().trim();

		saveToDatabase();
		enableEditing(false);
		updateAvatar(gioiTinh);
	}

	private boolean validateStudentInfo() {
		if (HoTen_text1.getText().trim().isEmpty()) {
			showError("Họ tên không được để trống!");
			return false;
		}
		if (MSSV_text1.getText().trim().isEmpty()) {
			showError("MSSV không được để trống!");
			return false;
		}
		if (Lop_comboBox_1.getSelectedIndex() == 0) {
			showError("Vui lòng chọn lớp!");
			return false;
		}
		if (NgaySinh_text.getDate() == null) {
			showError("Ngày sinh không được để trống!");
			return false;
		}
		if (GioiTinh_ComboBox.getSelectedIndex() == 0) {
			showError("Vui lòng chọn giới tính!");
			return false;
		}
		if (Email_text1.getText().trim().isEmpty()) {
			showError("Email không được để trống!");
			return false;
		}
		if (!EMAIL_PATTERN.matcher(Email_text1.getText().trim()).matches()) {
			showError("Email không hợp lệ!");
			return false;
		}
		return true;
	}

	private boolean isCourseExists(String maMon) {
		try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
			String sql = "SELECT COUNT(*) FROM courses WHERE mssv = ? AND mamon = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, MSSV_text1.getText().trim());
				pstmt.setString(2, maMon);
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						return rs.getInt(1) > 0;
					}
				}
			}
		} catch (SQLException ex) {
			showError("Lỗi khi kiểm tra môn học: " + ex.getMessage());
		}
		return false;
	}

	private void addNewCourse() {
		MonHoc_comboBox1.setEnabled(true);
		MaMon_text1.setEditable(true);
		SoTin_text1.setEditable(true);
		ThoiGian_text1.setEditable(true);

		MonHoc_comboBox1.setSelectedIndex(0);
		MaMon_text1.setText("");
		SoTin_text1.setText("");
		ThoiGian_text1.setText("");

		JPanel dialogPanel = new JPanel(new java.awt.GridLayout(4, 2));
		dialogPanel.add(new JLabel("Môn Học:"));
		dialogPanel.add(MonHoc_comboBox1);
		dialogPanel.add(new JLabel("Mã Môn:"));
		dialogPanel.add(MaMon_text1);
		dialogPanel.add(new JLabel("Số Tín:"));
		dialogPanel.add(SoTin_text1);
		dialogPanel.add(new JLabel("Thời Gian:"));
		dialogPanel.add(ThoiGian_text1);

		if (JOptionPane.showConfirmDialog(this, dialogPanel, "Thêm Môn Học",
				JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
			if (!validateCourseInfo())
				return;

			String maMon = MaMon_text1.getText().trim();
			if (isCourseExists(maMon)) {
				showError("Môn học với mã " + maMon + " đã được đăng ký!");
				return;
			}

			// Lưu trực tiếp vào database
			saveCourseToDatabase(MSSV_text1.getText().trim(), (String) MonHoc_comboBox1.getSelectedItem(), maMon,
					Integer.parseInt(SoTin_text1.getText().trim()), ThoiGian_text1.getText().trim());

			enableEditing(false);
		}
	}

	private void saveCourseToDatabase(String mssv, String monHoc, String maMon, int soTin, String thoiGian) {
		try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
			String sqlInsertCourse = "INSERT INTO courses (mssv, monhoc, mamon, sotin, thoigian) VALUES (?, ?, ?, ?, ?)";
			try (PreparedStatement pstmtInsertCourse = conn.prepareStatement(sqlInsertCourse)) {
				pstmtInsertCourse.setString(1, mssv);
				pstmtInsertCourse.setString(2, monHoc);
				pstmtInsertCourse.setString(3, maMon);
				pstmtInsertCourse.setInt(4, soTin);
				pstmtInsertCourse.setString(5, thoiGian);
				pstmtInsertCourse.executeUpdate();

				JOptionPane.showMessageDialog(this, "Thêm môn học thành công!", "Thành công",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (SQLException ex) {
			showError("Lỗi khi lưu môn học: " + ex.getMessage());
		}
	}

	private boolean validateCourseInfo() {
		if (MonHoc_comboBox1.getSelectedIndex() == 0) {
			showError("Vui lòng chọn môn học!");
			return false;
		}
		if (MaMon_text1.getText().trim().isEmpty()) {
			showError("Mã môn không được để trống!");
			return false;
		}
		if (SoTin_text1.getText().trim().isEmpty()) {
			showError("Số tín không được để trống!");
			return false;
		}
		try {
			Integer.parseInt(SoTin_text1.getText().trim());
		} catch (NumberFormatException ex) {
			showError("Số tín phải là số nguyên!");
			return false;
		}
		if (ThoiGian_text1.getText().trim().isEmpty()) {
			showError("Thời gian không được để trống!");
			return false;
		}
		return true;
	}

	private void displayCourses() {
		List<String[]> courses = getCoursesFromDatabase();
		if (courses.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Chưa có môn học nào được đăng ký!", "Thông tin",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		String[] columnNames = { "Mã môn", "Môn học", "Số tín", "Thời gian" };
		DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
		JTable table = new JTable(tableModel);
		table.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		table.setRowHeight(25);
		table.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 14));
		table.getTableHeader().setBackground(new Color(255, 204, 0));
		table.getTableHeader().setForeground(Color.BLACK);

		// Thêm các môn từ database
		for (String[] course : courses) {
			tableModel.addRow(new String[] { course[1], course[0], course[2], course[3] });
		}

		JScrollPane scrollPane = new JScrollPane(table);
		JPanel panel = new JPanel(new java.awt.BorderLayout());
		panel.add(new JLabel("Số lượng môn đã đăng ký: " + courses.size()), java.awt.BorderLayout.NORTH);
		panel.add(scrollPane, java.awt.BorderLayout.CENTER);

		JOptionPane.showMessageDialog(this, panel, "Danh Sách Môn Học", JOptionPane.INFORMATION_MESSAGE);
	}

	private void updateAvatar(String gioiTinh) {
		String avatarPath = "/Icon/avata_nam.png";
		if ("Nữ".equalsIgnoreCase(gioiTinh)) {
			avatarPath = "/Icon/avata_nu.png";
		}

		try {
			ImageIcon avatarIcon = new ImageIcon(getClass().getResource(avatarPath));
			Image scaledImage = avatarIcon.getImage().getScaledInstance(224, 227, Image.SCALE_SMOOTH);
			avata.setIcon(new ImageIcon(scaledImage));
		} catch (Exception e) {
			avata.setText("Avatar not found");
			avata.setHorizontalAlignment(JLabel.CENTER);
		}
	}

	private void updateCourseDetails() {
		String selectedSubject = (String) MonHoc_comboBox1.getSelectedItem();
		switch (selectedSubject != null ? selectedSubject : "") {
		case "Lập Trình Hướng Đối Tượng":
			MaMon_text1.setText("TEL1448");
			SoTin_text1.setText("3");
			ThoiGian_text1.setText("Thứ 3,Tiết 7-10");
			break;
		case "Hệ Thống nhúng IOT":
			MaMon_text1.setText("TEL1457");
			SoTin_text1.setText("3");
			ThoiGian_text1.setText("Thứ 2,Tiết 7-10");
			break;
		case "Hệ Thống Cảm Biến":
			MaMon_text1.setText("TEL1467");
			SoTin_text1.setText("3");
			ThoiGian_text1.setText("Thứ 2,Tiết 1-4");
			break;
		case "Điện Toán Đám Mây":
			MaMon_text1.setText("TEL1447");
			SoTin_text1.setText("2");
			ThoiGian_text1.setText("Thứ 5,Tiết 7-10");
			break;
		case "Phát Triển ứng Dụng":
			MaMon_text1.setText("TEL1461");
			SoTin_text1.setText("3");
			ThoiGian_text1.setText("Thứ 4,Tiết 7-10");
			break;
		case "Tiếng Anh":
			MaMon_text1.setText("BAS1160");
			SoTin_text1.setText("4");
			ThoiGian_text1.setText("Thứ 7,Tiết 1-4");
			break;
		default:
			MaMon_text1.setText("");
			SoTin_text1.setText("");
			ThoiGian_text1.setText("");
		}
	}

	private void loadDataToFields() {
		HoTen_text1.setText(hoTen);
		MSSV_text1.setText(mssv);
		Lop_comboBox_1.setSelectedItem(lop);
		try {
			if (ngaySinh != null && !ngaySinh.isEmpty()) {
				NgaySinh_text.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(ngaySinh));
			}
		} catch (ParseException e) {
			showError("Lỗi định dạng ngày sinh!");
		}
		GioiTinh_ComboBox.setSelectedItem(gioiTinh);
		Email_text1.setText(email);
		MonHoc_comboBox1.setSelectedItem(monHoc);
		MaMon_text1.setText(maMon);
		SoTin_text1.setText(soTin);
		ThoiGian_text1.setText(thoiGian);
		updateAvatar(gioiTinh);
	}

	private void loadDataFromDatabase() {
		try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
			// Load student info
			String sqlStudent = "SELECT * FROM students WHERE mssv = ?";
			try (PreparedStatement pstmtStudent = conn.prepareStatement(sqlStudent)) {
				pstmtStudent.setString(1, originalMssv);
				try (ResultSet rsStudent = pstmtStudent.executeQuery()) {
					if (rsStudent.next()) {
						hoTen = rsStudent.getString("hoten");
						lop = rsStudent.getString("lop");
						gioiTinh = rsStudent.getString("gioitinh");
						email = rsStudent.getString("email");
						Date date = rsStudent.getDate("ngaysinh");
						ngaySinh = date != null ? new SimpleDateFormat("dd/MM/yyyy").format(date) : "";

						HoTen_text1.setText(hoTen);
						MSSV_text1.setText(originalMssv);
						Lop_comboBox_1.setSelectedItem(lop);
						GioiTinh_ComboBox.setSelectedItem(gioiTinh);
						Email_text1.setText(email);
						if (!ngaySinh.isEmpty()) {
							NgaySinh_text.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(ngaySinh));
						}
					} else {
						showError("Không tìm thấy thông tin sinh viên với MSSV: " + originalMssv);
					}
				}
			}

			// Load course info
			String sqlCourse = "SELECT * FROM courses WHERE mssv = ? LIMIT 1";
			try (PreparedStatement pstmtCourse = conn.prepareStatement(sqlCourse)) {
				pstmtCourse.setString(1, originalMssv);
				try (ResultSet rsCourse = pstmtCourse.executeQuery()) {
					if (rsCourse.next()) {
						monHoc = rsCourse.getString("monhoc");
						maMon = rsCourse.getString("mamon");
						soTin = String.valueOf(rsCourse.getInt("sotin"));
						thoiGian = rsCourse.getString("thoigian");

						MonHoc_comboBox1.setSelectedItem(monHoc);
						MaMon_text1.setText(maMon);
						SoTin_text1.setText(soTin);
						ThoiGian_text1.setText(thoiGian);
					}
				}
			}
		} catch (SQLException | ParseException ex) {
			showError("Lỗi khi tải dữ liệu: " + ex.getMessage());
		}
	}

	private void saveToDatabase() {
		String newMssv = MSSV_text1.getText().trim(); // Lấy MSSV mới từ giao diện
		try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
			conn.setAutoCommit(false); // Bắt đầu giao dịch
			try {
				if (!newMssv.equals(originalMssv)) { // Nếu MSSV thay đổi
					// Kiểm tra xem MSSV mới đã tồn tại chưa
					String sqlCheck = "SELECT COUNT(*) FROM students WHERE mssv = ?";
					try (PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck)) {
						pstmtCheck.setString(1, newMssv);
						try (ResultSet rs = pstmtCheck.executeQuery()) {
							if (rs.next() && rs.getInt(1) > 0) {
								showError("MSSV mới đã tồn tại!");
								conn.rollback();
								return;
							}
						}
					}

					// Thêm bản ghi sinh viên mới với MSSV mới
					String sqlInsertStudent = "INSERT INTO students (mssv, hoten, ngaysinh, gioitinh, lop, email) VALUES (?, ?, ?, ?, ?, ?)";
					try (PreparedStatement pstmtInsertStudent = conn.prepareStatement(sqlInsertStudent)) {
						pstmtInsertStudent.setString(1, newMssv);
						pstmtInsertStudent.setString(2, hoTen);
						pstmtInsertStudent.setDate(3, new java.sql.Date(NgaySinh_text.getDate().getTime()));
						pstmtInsertStudent.setString(4, gioiTinh);
						pstmtInsertStudent.setString(5, lop);
						pstmtInsertStudent.setString(6, email);
						pstmtInsertStudent.executeUpdate();
					}

					// Cập nhật các bảng liên quan để trỏ đến MSSV mới
					String[] tables = { "courses", "baitap", "diem", "chamdiem", "diemdanh" };
					for (String table : tables) {
						String sqlUpdate = "UPDATE " + table + " SET mssv = ? WHERE mssv = ?";
						try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {
							pstmtUpdate.setString(1, newMssv);
							pstmtUpdate.setString(2, originalMssv);
							pstmtUpdate.executeUpdate();
						}
					}

					// Xóa bản ghi sinh viên cũ
					String sqlDeleteStudent = "DELETE FROM students WHERE mssv = ?";
					try (PreparedStatement pstmtDeleteStudent = conn.prepareStatement(sqlDeleteStudent)) {
						pstmtDeleteStudent.setString(1, originalMssv);
						pstmtDeleteStudent.executeUpdate();
					}
				} else { // Nếu MSSV không thay đổi
					// Chỉ cập nhật thông tin sinh viên hiện có
					String sqlUpdateStudent = "UPDATE students SET hoten = ?, ngaysinh = ?, gioitinh = ?, lop = ?, email = ? WHERE mssv = ?";
					try (PreparedStatement pstmtUpdateStudent = conn.prepareStatement(sqlUpdateStudent)) {
						pstmtUpdateStudent.setString(1, hoTen);
						pstmtUpdateStudent.setDate(2, new java.sql.Date(NgaySinh_text.getDate().getTime()));
						pstmtUpdateStudent.setString(3, gioiTinh);
						pstmtUpdateStudent.setString(4, lop);
						pstmtUpdateStudent.setString(5, email);
						pstmtUpdateStudent.setString(6, originalMssv);
						pstmtUpdateStudent.executeUpdate();
					}
				}

				conn.commit(); // Xác nhận giao dịch
				JOptionPane.showMessageDialog(this, "Cập nhật thông tin sinh viên thành công!", "Thành công",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (SQLException ex) {
				conn.rollback(); // Hoàn tác nếu có lỗi
				throw ex;
			}
		} catch (SQLException ex) {
			showError("Lỗi khi lưu dữ liệu: " + ex.getMessage());
		}
	}

	private List<String[]> getCoursesFromDatabase() {
		List<String[]> courses = new ArrayList<>();
		try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
			String sqlCourse = "SELECT * FROM courses WHERE mssv = ?";
			try (PreparedStatement pstmtCourse = conn.prepareStatement(sqlCourse)) {
				pstmtCourse.setString(1, MSSV_text1.getText().trim());
				try (ResultSet rsCourse = pstmtCourse.executeQuery()) {
					while (rsCourse.next()) {
						courses.add(new String[] { rsCourse.getString("monhoc"), rsCourse.getString("mamon"),
								String.valueOf(rsCourse.getInt("sotin")), rsCourse.getString("thoigian") });
					}
				}
			}
		} catch (SQLException ex) {
			showError("Lỗi khi lấy danh sách môn học: " + ex.getMessage());
		}
		return courses;
	}

	private void showError(String message) {
		JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
	}

	public String getHoTen() {
		return HoTen_text1.getText().trim();
	}

	public String getMssv() {
		return MSSV_text1.getText().trim();
	}

	public String getLop() {
		return lop;
	}

	public String getNgaySinh() {
		return ngaySinh;
	}

	public String getGioiTinh() {
		return gioiTinh;
	}

	public String getEmail() {
		return email;
	}

	public String getMonHoc() {
		return monHoc;
	}

	public String getMaMon() {
		return maMon;
	}

	public String getSoTin() {
		return soTin;
	}

	public String getThoiGian() {
		return thoiGian;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				ThongTinSinhVien frame = new ThongTinSinhVien();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}