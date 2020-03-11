package homeway.mp4boxgui.view;

import homeway.mp4boxgui.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileListPanel extends JPanel {
    private static final long serialVersionUID = 6476563336172274268L;

    private List<File> mFiles = new ArrayList<>();
    private List<JRadioButton> mJRadioButtons = new ArrayList<>();

    private JScrollPane mJScrollPane;
    private JPanel mOutterJPanel;
    private JPanel mInnerPanel;

    private int mContainerWidth = 0;
    private int mContainerHeight = 0;

    private JTextField mEmptyHintTextArea;

    private OnFileSelectObserver mFileSelectObserver;

    private OnFileDragObserver mOnFileDragObserver;

    public interface OnFileSelectObserver {
        public void onFileSelect(String filePath);
    }

    public interface OnFileDragObserver {
        public void onFileDrag(String filePath);
    }

    public void setOnFileSelectObserver(OnFileSelectObserver observer) {
        mFileSelectObserver = observer;
    }

    public void setOnFileDragObserver(OnFileDragObserver observer) {
        mOnFileDragObserver = observer;
    }

    public void update() {
        final File selectedFile = getSelectedFile();
        mInnerPanel.removeAll();
        mJRadioButtons.clear();
        if (0 == mFiles.size()) {
            mInnerPanel.add(mEmptyHintTextArea);
            return;
        }

        for (File file : mFiles) {
            JRadioButton jRadioButton = new JRadioButton(file.getAbsolutePath());
            jRadioButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            mInnerPanel.add(jRadioButton);
            mInnerPanel.repaint();
            mInnerPanel.revalidate();
            jRadioButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    String selActionCommand = e.getActionCommand();
                    for (JRadioButton radioButton : mJRadioButtons) {
                        if (selActionCommand.equals(radioButton.getActionCommand())) {
                            radioButton.setSelected(true);
                            if (mFileSelectObserver != null) {
                                mFileSelectObserver.onFileSelect(file.getAbsolutePath());
                            }
                        } else {
                            radioButton.setSelected(false);
                        }
                    }
                }
            });
            mJRadioButtons.add(jRadioButton);
        }
        setSelectedFile(selectedFile);
    }

    public void addFiles(List<File> files) {
        if (null == files || files.isEmpty()) {
            return;
        }

        for (File file : files) {
            if (!mFiles.contains(file)) {
                mFiles.add(file);
            }
        }
        update();
    }

    public FileListPanel(int width, int height) {
        mContainerWidth = width;
        mContainerHeight = height;
        initLayout();
        initListener();
    }

    private void initLayout() {
        mOutterJPanel = new JPanel();
        mOutterJPanel.setLayout(new BorderLayout());
        mOutterJPanel.setPreferredSize(new Dimension(mContainerWidth, mContainerHeight));

        mInnerPanel = new JPanel();
        // mInnerPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        JPanel paddingL = new JPanel();
        JPanel paddingR = new JPanel();
        JPanel paddingTop = new JPanel();
        JPanel paddingBottom = new JPanel();
        paddingL.setPreferredSize(new Dimension(20, 1));
        paddingR.setPreferredSize(new Dimension(20, 1));
        paddingTop.setPreferredSize(new Dimension(1, 10));
        paddingBottom.setPreferredSize(new Dimension(1, 10));
        mOutterJPanel.add(paddingL, BorderLayout.LINE_START);
        mOutterJPanel.add(paddingR, BorderLayout.LINE_END);
        mOutterJPanel.add(paddingTop, BorderLayout.PAGE_START);
        mOutterJPanel.add(paddingBottom, BorderLayout.PAGE_END);

        mInnerPanel.setLayout(new BoxLayout(mInnerPanel, BoxLayout.Y_AXIS));

        mEmptyHintTextArea = new JTextField(Constants.TEXT.DRAG_HINT);
        mEmptyHintTextArea.setEditable(false);
        mEmptyHintTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        mEmptyHintTextArea.setPreferredSize(new Dimension(200, 20));
        mEmptyHintTextArea.setMaximumSize(new Dimension(200, 20));
        mEmptyHintTextArea.setBorder(null);

        mInnerPanel.add(mEmptyHintTextArea);

        mJScrollPane = new JScrollPane(mInnerPanel);
        mJScrollPane.setAutoscrolls(true);
        mJScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mJScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mOutterJPanel.add(mJScrollPane, BorderLayout.CENTER);
        this.add(mOutterJPanel);
    }

    private void initListener() {
        DragUtil.DragObserver dragObserver = new DragUtil.DragObserver() {

            @Override
            public void onDrag(List<File> list) {
                mFiles.clear();
                mFiles.addAll(list);
                update();
                if (mOnFileDragObserver != null) {
                    mOnFileDragObserver.onFileDrag(list.get(0).getAbsolutePath());
                }
            }
        };
        DragUtil.enableDrag(this, dragObserver);
    }

    public File getSelectedFile() {
        for (int i = 0; i < mJRadioButtons.size(); i++) {
            if (mJRadioButtons.get(i).isSelected()) {
                return mFiles.get(i);
            }
        }
        return null;
    }

    private void setSelectedFile(File file) {
        for (int i = 0; i < mFiles.size(); i++) {
            if (mFiles.get(i) == file) {
                mJRadioButtons.get(i).setSelected(true);
            }
        }
    }
}
