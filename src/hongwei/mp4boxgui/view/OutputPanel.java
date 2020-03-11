package hongwei.mp4boxgui.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class OutputPanel extends JPanel {
    private static final long serialVersionUID = 4003634450581856150L;

    private JScrollPane mJScrollPane;
    private JTextArea mJTextArea;
    private int mContainerWidth = 0;
    private int mContainerHeight = 0;

    public OutputPanel(int width, int height) {
        mContainerWidth = width;
        mContainerHeight = height;
        initLayout();
    }

    public void initLayout() {
        setLayout(new BorderLayout());
        mJTextArea = new JTextArea();
        mJTextArea.setEditable(false);
        mJTextArea.setLineWrap(true);

        mJScrollPane = new JScrollPane(mJTextArea);
        mJScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mJScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mJScrollPane.setPreferredSize(new Dimension(mContainerWidth - 80, mContainerHeight));
        add(mJScrollPane, BorderLayout.CENTER);
        add(new JPanel(), BorderLayout.LINE_START);
        add(new JPanel(), BorderLayout.LINE_END);
        add(new JPanel(), BorderLayout.PAGE_END);
    }

    public void updateMsg(String lines) {
        if (lines.endsWith("\n")) {
            mJTextArea.append(lines);
        } else {
            mJTextArea.append(lines + "\n");
        }
        mJTextArea.setCaretPosition(mJTextArea.getText().length());
    }

    public void clear() {
        mJTextArea.setText("");
    }
}
