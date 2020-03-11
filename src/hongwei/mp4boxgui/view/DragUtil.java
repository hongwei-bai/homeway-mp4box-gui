package hongwei.mp4boxgui.view;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

import javax.swing.JPanel;

public class DragUtil {
    public static void enableDrag(JPanel target, DragObserver dragObserver) {
        new DropTarget(target, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                        @SuppressWarnings("unchecked")
                        List<File> list = (List<File>) (dtde.getTransferable()
                                .getTransferData(DataFlavor.javaFileListFlavor));
                        if (dragObserver != null) {
                            dragObserver.onDrag(list);
                        }
                        dtde.dropComplete(true);
                    } else {
                        System.out.println("rej");
                        dtde.rejectDrop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface DragObserver {
        public void onDrag(List<File> list);
    }
}
