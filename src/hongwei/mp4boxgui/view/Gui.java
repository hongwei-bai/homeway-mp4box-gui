package hongwei.mp4boxgui.view;

import hongwei.mp4boxgui.Constants;
import hongwei.mp4boxgui.controller.SplitController;
import hongwei.mp4boxgui.model.Mp4boxCommand;
import hongwei.mp4boxgui.model.Mp4boxParser;
import hongwei.mp4boxgui.view.InputPanel.ActionObserver;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Gui extends JFrame {
    private static final long serialVersionUID = 2818775310539858077L;
    private final static int DEFAULT_WIDTH = 480;
    private final static int DEFAULT_HEIGHT = 640;
    private int mWidth = DEFAULT_WIDTH;
    private int mHeight = DEFAULT_HEIGHT;

    private FileListPanel mFileListPanel;
    private InputPanel mInputPanel;
    private OutputPanel mOutputPanel;

    private SplitController mSplitController;
    private Executor mExecutor;

    public Gui() {
        initModel();
        initLayout();
        initListener();
    }

    private void initModel() {
        mSplitController = new SplitController();
        mExecutor = Executors.newScheduledThreadPool(4);
    }

    private void initListener() {
        mFileListPanel.setOnFileSelectObserver((filePath) -> {
            mExecutor.execute(() -> {
                setTitleForCompletionNotification(false);
                SwingUtilities.invokeLater(() -> mInputPanel.updateDuration("Calculating...", 0));
                String info = Mp4boxCommand.info(filePath);
                String durationString = Mp4boxParser.parseDurationString(info);
                long duration = Mp4boxParser.parseDurationLong(durationString);
                SwingUtilities.invokeLater(() -> mInputPanel.updateDuration(durationString, duration));
            });
        });

        mFileListPanel.setOnFileDragObserver((filePath) -> {
            mOutputPanel.clear();
            setTitleForCompletionNotification(false);
        });

        mInputPanel.setActionObserver(new ActionObserver() {

            @Override
            public void onError(String msg) {
                mOutputPanel.updateMsg("[ERROR]" + msg);
            }

            @Override
            public void onAction(long startTime, long endTime, long duration) {
                mExecutor.execute(() -> {
                    File file = mFileListPanel.getSelectedFile();
                    mSplitController.split(file, startTime, endTime, duration, line -> SwingUtilities.invokeLater(() -> mOutputPanel.updateMsg(line)));

                    List<File> outputFiles = mSplitController.getOutputFiles(file);
                    SwingUtilities.invokeLater(() -> {
                        mFileListPanel.addFiles(outputFiles);
                        setTitleForCompletionNotification(true);
                    });
                });
            }
        });
    }

    private void initLayout() {
        setPreferredSize(new Dimension(mWidth, mHeight));
        setCenterDisplay();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitleForCompletionNotification(false);

        Image icon = Toolkit.getDefaultToolkit().getImage(Constants.ICON);
        setIconImage(icon);

        setLayout(new BorderLayout());

        mFileListPanel = new FileListPanel(mWidth, 160);
        mFileListPanel.setPreferredSize(new Dimension(1, 160));

        mInputPanel = new InputPanel();
        mInputPanel.setPreferredSize(new Dimension(1, 100));

        mOutputPanel = new OutputPanel(mWidth, 360);

        add(mFileListPanel, BorderLayout.PAGE_START);
        add(mInputPanel, BorderLayout.CENTER);
        add(mOutputPanel, BorderLayout.PAGE_END);
    }

    private void setTitleForCompletionNotification(boolean isCompletionNofify) {
        if (isCompletionNofify) {
            setTitle(Constants.COMPLETION_NOTIFICATION_STRING + Constants.TITLE);
        } else {
            setTitle(Constants.TITLE);
        }
    }

    private void setCenterDisplay() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (mHeight > screenSize.getHeight()) {
            mHeight = (int) screenSize.getWidth();
        }
        if (mWidth > screenSize.getWidth()) {
            mWidth = (int) screenSize.getWidth();
        }

        this.setLocation((int) ((screenSize.getWidth() - mWidth) / 2), (int) ((screenSize.getHeight() - mHeight) / 2));
    }

    public static void main(String[] args) {
        Gui mainFrame = new Gui();
        mainFrame.pack();
    }
}
