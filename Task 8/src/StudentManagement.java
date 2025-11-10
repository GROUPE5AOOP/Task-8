import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class StudentManagement extends JFrame {

    JTextField nameField = new JTextField(10);
    JTextField courseField = new JTextField(10);
    JTextField searchField = new JTextField(20);

    JTable table;
    DefaultTableModel model;

    StudentDAO dao = new StudentDAO();

    public StudentManagement() {
        setTitle("Student Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0,0));

        /* ------------------ HEADER (GRADIENT BAR) ------------------ */
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g){
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0,0,new Color(0,90,160), getWidth(), getHeight(), new Color(0,140,200));
                g2.setPaint(gp);
                g2.fillRect(0,0,getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(0,60));
        header.setLayout(new BorderLayout());
        JLabel title = new JLabel(" STUDENT  MANAGEMENT  SYSTEM", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        header.add(title);
        add(header, BorderLayout.NORTH);

        /* ------------------ LEFT SIDEBAR CARD ------------------ */
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(30,25,30,25));
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(280, getHeight()));
        formPanel.setOpaque(true);

        JLabel lblName = new JLabel("Name:");
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(lblName);
        styleField(nameField);
        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(15));

        JLabel lblCourse = new JLabel("Course:");
        lblCourse.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(lblCourse);
        styleField(courseField);
        formPanel.add(courseField);
        formPanel.add(Box.createVerticalStrut(25));

        JButton addBtn = styledBtn("Add", new Color(0,120,215));
        addBtn.addActionListener(e -> {
            dao.addStudent(nameField.getText(), courseField.getText());
            refresh();
        });
        formPanel.add(addBtn);

        add(formPanel, BorderLayout.WEST);


        /* ------------------ SEARCH BAR + TABLE AREA ------------------ */
        JPanel centerPanel = new JPanel(new BorderLayout(10,10));
        centerPanel.setBorder(new EmptyBorder(15,15,15,15));
        centerPanel.setBackground(new Color(245,248,255));

        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(new Color(245,248,255));
        searchPanel.add(new JLabel("Search Name:"));
        styleField(searchField);
        searchPanel.add(searchField);

        JButton searchBtn = styledBtn("Search", new Color(0,120,215));
        searchBtn.addActionListener(e -> dao.searchStudent(model, searchField.getText()));
        searchPanel.add(searchBtn);

        centerPanel.add(searchPanel, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"ID", "Name", "Course"}, 0){
            @Override public boolean isCellEditable(int row, int col){ return col != 0; }
        };

        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(28);

        // table header style
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(0,90,160));
        table.getTableHeader().setForeground(Color.WHITE);

        // center-align cells
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, center);

        // UPDATE on ENTER
        model.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE){
                int row = e.getFirstRow();
                int id = Integer.parseInt(model.getValueAt(row, 0).toString());
                String n = model.getValueAt(row, 1).toString();
                String c = model.getValueAt(row, 2).toString();
                dao.editStudent(id, n, c);
            }
        });

        // DELETE on DELETE KEY
        table.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_DELETE && table.getSelectedRow()!=-1){
                    int row = table.getSelectedRow();
                    int id = Integer.parseInt(model.getValueAt(row,0).toString());
                    if(JOptionPane.showConfirmDialog(null,"Delete selected student?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                        dao.deleteStudent(id);
                        refresh();
                    }
                }
            }
        });

        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        refresh();
        setVisible(true);
    }

    private void styleField(JTextField tf){
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180,180,180),1),
                new EmptyBorder(5,8,5,8)
        ));
    }

    private JButton styledBtn(String text, Color color){
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8,18,8,18));
        return btn;
    }

    private void refresh() {
        dao.loadStudents(model);
        nameField.setText("");
        courseField.setText("");
    }
}
