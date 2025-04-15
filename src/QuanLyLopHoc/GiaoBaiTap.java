package QuanLyLopHoc;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import com.toedter.calendar.JDateChooser;

public class GiaoBaiTap extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField TenGV_text;
	private JTextField TieuDe_Text;
	private JTextArea ND_textArea;
	private JComboBox<String> Mon_comboBox;
	private JDateChooser NgayNop;
	private TimePicker timePicker;

	// Danh sách lưu bài tập
	public static ArrayList<BaiTapInfo> danhSachBaiTap = new ArrayList<>();

	// Thông tin kết nối database
	private static final String DB_URL = "jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:6543/postgres?sslmode=require&pgbouncer=true";
	private static final String DB_USERNAME = "postgres.vpehkzjmzpcskfzjjyql";
	private static final String DB_PASSWORD = "MinhThuong0808";

	// Constructor mặc định
	public GiaoBaiTap() {
		initialize();
		createTable();
	}

	// Constructor nhận thông tin tên giảng viên và môn học
	public GiaoBaiTap(String tenGV, String monHoc) {
		initialize();
		createTable();
		TenGV_text.setText(tenGV); // Hiển thị tên giảng viên
		Mon_comboBox.setSelectedItem(monHoc); // Hiển thị môn học
	}

	// Create table if not exists
	private void createTable() {
		String createTableSQL = """
				    CREATE TABLE IF NOT EXISTS giaobaitap (
				        id SERIAL PRIMARY KEY,
				        ten_giang_vien VARCHAR(255) NOT NULL,
				        mon_hoc VARCHAR(255) NOT NULL,
				        tieu_de VARCHAR(255) NOT NULL,
				        noi_dung TEXT NOT NULL,
				        han_nop TIMESTAMP NOT NULL,
				        thoi_gian_giao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
				    )
				""";

		try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
				PreparedStatement pstmt = conn.prepareStatement(createTableSQL)) {
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Lỗi khi tạo bảng: " + e.getMessage(), "Lỗi",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void initialize() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 895, 652);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 0, 121));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 204, 0));
		panel.setBounds(0, 0, 881, 57);
		panel.setLayout(null);
		contentPane.add(panel);

		JLabel lblTitle = new JLabel("GIAO BÀI TẬP");
		lblTitle.setBounds(342, 11, 198, 37);
		panel.add(lblTitle);
		lblTitle.setForeground(new Color(0, 0, 0));
		lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 25));

		JLabel lblTieuDe = new JLabel("TIÊU ĐỀ:");
		lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 15));
		lblTieuDe.setForeground(new Color(255, 255, 255));
		lblTieuDe.setBounds(25, 147, 138, 30);
		contentPane.add(lblTieuDe);

		TieuDe_Text = new JTextField();
		TieuDe_Text.setFont(new Font("Times New Roman", Font.BOLD, 15));
		TieuDe_Text.setBounds(155, 148, 704, 31);
		contentPane.add(TieuDe_Text);
		TieuDe_Text.setColumns(10);

		JLabel lblGiaoVien = new JLabel("GIẢNG VIÊN:");
		lblGiaoVien.setFont(new Font("Times New Roman", Font.BOLD, 15));
		lblGiaoVien.setForeground(new Color(255, 255, 255));
		lblGiaoVien.setBounds(25, 92, 145, 31);
		contentPane.add(lblGiaoVien);

		TenGV_text = new JTextField();
		TenGV_text.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		TenGV_text.setBounds(155, 94, 292, 31);
		TenGV_text.setEditable(false); // Không cho phép chỉnh sửa tên giảng viên
		contentPane.add(TenGV_text);
		TenGV_text.setColumns(10);

		JLabel lblMonHoc = new JLabel("TÊN MÔN:");
		lblMonHoc.setFont(new Font("Times New Roman", Font.BOLD, 15));
		lblMonHoc.setForeground(new Color(255, 255, 255));
		lblMonHoc.setBounds(469, 93, 114, 30);
		contentPane.add(lblMonHoc);

		Mon_comboBox = new JComboBox<>();
		Mon_comboBox.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		Mon_comboBox.setBounds(559, 93, 300, 30);
		Mon_comboBox.setModel(new DefaultComboBoxModel<>(new String[] { "", "Lập Trình Hướng Đối Tượng",
				"Hệ Thống nhúng IOT", "Hệ Thống Cảm Biến", "Điện Toán Đám Mây", "Phát Triển ứng Dụng", "Tiếng Anh" }));
		Mon_comboBox.setEnabled(false); // Không cho phép chỉnh sửa môn học
		contentPane.add(Mon_comboBox);

		JLabel lblNoiDung = new JLabel("NỘI DUNG BÀI TẬP");
		lblNoiDung.setForeground(Color.WHITE);
		lblNoiDung.setFont(new Font("Times New Roman", Font.BOLD, 15));
		lblNoiDung.setBounds(25, 190, 180, 41);
		contentPane.add(lblNoiDung);

		ND_textArea = new JTextArea();
		ND_textArea.setFont(new Font("Times New Roman", Font.BOLD, 15));
		JScrollPane scrollPane = new JScrollPane(ND_textArea); // Thêm JScrollPane để hỗ trợ cuộn
		scrollPane.setBounds(25, 229, 832, 244);
		contentPane.add(scrollPane);

		JLabel lblHanNop = new JLabel("HẠN NỘP:");
		lblHanNop.setFont(new Font("Times New Roman", Font.BOLD, 15));
		lblHanNop.setForeground(new Color(255, 255, 255));
		lblHanNop.setBounds(75, 501, 88, 28);
		contentPane.add(lblHanNop);

		// Sử dụng JDateChooser để chọn ngày
		NgayNop = new JDateChooser();
		NgayNop.setBounds(163, 501, 150, 30);
		NgayNop.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		NgayNop.setDateFormatString("dd/MM/yyyy");
		contentPane.add(NgayNop);

		// Hạn nộp - Giờ
		JLabel lblHanNopGio = new JLabel("Giờ nộp:");
		lblHanNopGio.setFont(new Font("Times New Roman", Font.BOLD, 15));
		lblHanNopGio.setForeground(new Color(255, 255, 255));
		lblHanNopGio.setBounds(510, 499, 88, 30);
		contentPane.add(lblHanNopGio);

		TimePickerSettings timeSettings = new TimePickerSettings();
		timeSettings.setDisplaySpinnerButtons(true);
		timeSettings.setColor(TimePickerSettings.TimeArea.TimePickerTextDisabled, new Color(0, 51, 102));
		timeSettings.setFormatForDisplayTime("hh:mm a"); // Hiển thị AM/PM trong TimePicker
		timePicker = new TimePicker(timeSettings);
		timePicker.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		timePicker.setBounds(608, 501, 150, 30);
		contentPane.add(timePicker);

		JButton Giaobai_bnt = new JButton("GIAO BÀI");
		Giaobai_bnt.setForeground(Color.BLACK);
		Giaobai_bnt.setFont(new Font("Times New Roman", Font.BOLD, 15));
		Giaobai_bnt.setBorder(new LineBorder(Color.WHITE, 1));
		Giaobai_bnt.setBackground(new Color(0, 221, 55));
		Giaobai_bnt.setBounds(356, 562, 162, 42);
		contentPane.add(Giaobai_bnt);

		// Thêm sự kiện cho nút GIAO BÀI
		Giaobai_bnt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String tenGV = TenGV_text.getText();
				String monHoc = (String) Mon_comboBox.getSelectedItem();
				String tieuDe = TieuDe_Text.getText();
				String noiDung = ND_textArea.getText();
				Date selectedDate = NgayNop.getDate();
				LocalTime selectedTime = timePicker.getTime(); // Lấy thời gian từ TimePicker

				// Kiểm tra xem thông tin có đầy đủ không
				if (tenGV.isEmpty() || monHoc == null || monHoc.isEmpty()) {
					JOptionPane.showMessageDialog(GiaoBaiTap.this,
							"Vui lòng điền đầy đủ thông tin giảng viên và môn học!", "Cảnh báo",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				if (tieuDe.trim().isEmpty()) {
					JOptionPane.showMessageDialog(GiaoBaiTap.this, "Vui lòng điền tiêu đề bài tập!", "Cảnh báo",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				if (noiDung.trim().isEmpty()) {
					JOptionPane.showMessageDialog(GiaoBaiTap.this, "Vui lòng điền nội dung bài tập!", "Cảnh báo",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				if (selectedDate == null) {
					JOptionPane.showMessageDialog(GiaoBaiTap.this, "Vui lòng chọn ngày hạn nộp hợp lệ!", "Cảnh báo",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				if (selectedTime == null) {
					JOptionPane.showMessageDialog(GiaoBaiTap.this, "Vui lòng chọn giờ hạn nộp hợp lệ!", "Cảnh báo",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				// Kết hợp ngày và giờ thành một đối tượng Date
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(selectedDate);

				// Lấy giờ và phút từ LocalTime
				int hour = selectedTime.getHour();
				int minute = selectedTime.getMinute();

				calendar.set(Calendar.HOUR_OF_DAY, hour);
				calendar.set(Calendar.MINUTE, minute);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);

				// Lưu bài tập vào danh sách
				Date hanNop = calendar.getTime();
				BaiTapInfo baiTap = new BaiTapInfo(tieuDe, noiDung, hanNop, tenGV, monHoc);
				danhSachBaiTap.add(baiTap);

				// Lưu vào database
				String insertSQL = "INSERT INTO giaobaitap (ten_giang_vien, mon_hoc, tieu_de, noi_dung, han_nop) VALUES (?, ?, ?, ?, ?)";
				try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
						PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
					pstmt.setString(1, tenGV);
					pstmt.setString(2, monHoc);
					pstmt.setString(3, tieuDe);
					pstmt.setString(4, noiDung);
					pstmt.setTimestamp(5, new java.sql.Timestamp(hanNop.getTime()));
					pstmt.executeUpdate();
				} catch (SQLException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(GiaoBaiTap.this, "Lỗi khi lưu vào database: " + ex.getMessage(),
							"Lỗi", JOptionPane.ERROR_MESSAGE);
					return;
				}

				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
				String hanNopStr = sdf.format(hanNop);
				String thongBao = "📌 Giao bài thành công!\n\n" + "Tiêu đề: " + tieuDe + "\n" + "Giảng viên: " + tenGV
						+ "\n" + "Môn học: " + monHoc + "\n" + "Hạn nộp: " + hanNopStr;

				JOptionPane.showMessageDialog(GiaoBaiTap.this, thongBao, "Thành công", JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GiaoBaiTap frame = new GiaoBaiTap();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}