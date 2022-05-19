package ui;

import javax.swing.*;
import java.awt.*;

public class SenderFrame extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    private Integer height = 800;
    private Integer width = 600;
    private LayoutManager layout;
    private JLabel title;
    private JButton addFileButton;
    private JButton addTaskButton;


    public SenderFrame(String title) throws HeadlessException {
        super(title);
        layout = new BorderLayout();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        setSize(new Dimension(width, height));
        setLayout(layout);
        setLocationRelativeTo(null);
        initComponents();

    }

    private void initComponents() {
        JPanel titlePanel = getNewPanel(FlowLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);

        title = new JLabel("Sender");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        titlePanel.add(title);

        JPanel actionButtons = getNewPanel(FlowLayout.LEADING);
        add(actionButtons, BorderLayout.WEST);
        addFileButton = new JButton("Add file");
        addTaskButton = new JButton("Add task");
        actionButtons.add(addFileButton);
        actionButtons.add(addTaskButton);


        JPanel centerPanel = getNewPanel(FlowLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");
        editorPane.setText("<html>\n" +
                "<body>\n" +
                "<h1>Sender</h1>\n" +
                "<p>This is a sender application. It is used to send files to the server.</p>\n" +
                "<p>You can add files and tasks to the queue and the server will process them.</p>\n"
                + "</body>\n" +
                "</html>");
        centerPanel.add(editorPane);




    }
    private JPanel getNewPanel(Integer TYPE){
        JPanel Panel = new JPanel();
        FlowLayout titleLayout = new FlowLayout();
        titleLayout.setAlignment(TYPE);
        titleLayout.setHgap(10);
        titleLayout.setVgap(10);
        Panel.setLayout(titleLayout);
        return Panel;
    }

    public static void main(String[] args) {
        new SenderFrame("Sender");
    }
}
