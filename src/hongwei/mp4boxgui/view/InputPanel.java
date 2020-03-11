package hongwei.mp4boxgui.view;

import hongwei.mp4boxgui.model.Command;
import hongwei.mp4boxgui.model.Mp4boxParser;

import javax.swing.*;
import java.awt.*;

public class InputPanel extends JPanel {
    private static final long serialVersionUID = 7424902428277898182L;

    private JTextField mStartTimeTextField;
    private JTextField mEndTimeTextField;
    private JTextField mDurationTextField;
    private JButton mButton;
    private JButton mButtonOpenOutput;

    private long mDuration = 0;
    private ActionObserver mActionObserver;

    public InputPanel() {
        initLayout();
        initListener();
    }

    public void initLayout() {
        mStartTimeTextField = new JTextField(10);
        mEndTimeTextField = new JTextField(10);
        mDurationTextField = new JTextField();
        mDurationTextField.setEditable(false);
        mDurationTextField.setBorder(null);
        mButton = new JButton("split");
        mButtonOpenOutput = new JButton("open");

        setLayout(new FlowLayout());
        add(new JPanel());
        add(mStartTimeTextField);
        JTextField toTextField = new JTextField("-");
        toTextField.setEditable(false);
        toTextField.setBorder(null);
        add(toTextField);
        add(mEndTimeTextField);
        add(new JPanel());
        add(mDurationTextField);
        add(new JPanel());
        add(mButton);
        add(new JPanel());
        add(mButtonOpenOutput);
        add(new JPanel());
    }

    private void initListener() {
        mButtonOpenOutput.addActionListener((e) -> Command.open(System.getProperty("user.dir")));
        mButton.addActionListener((e) -> {
            long startTime = 0;
            long endTime = mDuration;
            try {
                startTime = Mp4boxParser.parseDurationLongForInput(mStartTimeTextField.getText());
            } catch (Exception exception) {

            }
            try {
                endTime = Mp4boxParser.parseDurationLongForInput(mEndTimeTextField.getText());
            } catch (Exception exception) {

            }

            if (endTime > mDuration) {
                reportError("Endtime > duration!");
            }

            if (startTime < 0) {
                reportError("startTime < 0!");
            }

            if (startTime >= endTime) {
                reportError("startTime MUST < endTime");
            }

            if (mActionObserver != null) {
                mActionObserver.onAction(startTime, endTime, mDuration);
            }
        });
    }

    private void reportError(String msg) {
        if (mActionObserver != null) {
            mActionObserver.onError(msg);
        }
    }

    public void updateDuration(String durationString, long duration) {
        mDuration = duration;
        mDurationTextField.setText("(" + durationString + ")");
        mStartTimeTextField.setText("0");
        mEndTimeTextField.setText("" + duration);
        revalidate();
    }

    public void setActionObserver(ActionObserver actionObserver) {
        mActionObserver = actionObserver;
    }

    public interface ActionObserver {
        void onAction(long startTime, long endTime, long duration);

        void onError(String msg);
    }
}
