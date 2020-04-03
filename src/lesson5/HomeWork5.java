package lesson5;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class HomeWork5 {
    //Susanin S.
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JTree tree = new JTree();
        JScrollPane scrollPaneLeft = new JScrollPane(tree);
        JSplitPane splitPane = new JSplitPane();

        frame.add(splitPane);
        frame.setSize(1024,500);

        File file = new File("D:/");
        createTable(file, splitPane); // начальное значение таблицы

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(file, true);
        iniTree(file, root); // инициализация дерева

        DefaultTreeModel dtm = (DefaultTreeModel) tree.getModel();
        dtm.setRoot(root);

        tree.setCellRenderer(new TreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree jTree, Object value, boolean b, boolean b1, boolean b2, int i, boolean b3) {
                JLabel label = new JLabel();
                File f = (File)(((DefaultMutableTreeNode)value).getUserObject());
                label.setText(f.getName());
                return label;
            }
        });
        splitPane.setLeftComponent(scrollPaneLeft);

        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {

                File f = (File) (((DefaultMutableTreeNode)treeSelectionEvent.getNewLeadSelectionPath().getLastPathComponent()).getUserObject());
                createTable(f,splitPane);
            }
        });
        tree.setCellRenderer(new TreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree jTree, Object value, boolean selected, boolean expanded, boolean leaf, int i, boolean hasFocus) {
                final JLabel	label = new JLabel(value.toString());

                if(hasFocus)
                {
                    label.setForeground(Color.BLUE);
                }

                return  label;
            }
        });

        scrollPaneLeft.setMinimumSize(new Dimension(250, 500));
        frame.setVisible(true);

    }

    static public void iniTree(File file, DefaultMutableTreeNode root)
    {
        if(file.isDirectory() )
        {
            for (File item: file.listFiles()) {

                if (item.isDirectory() && !item.isHidden()) {
                    DefaultMutableTreeNode root2 = new DefaultMutableTreeNode(item, true);
                    root.add(root2);
                    iniTree(item, root2);
                } else {
                    DefaultMutableTreeNode root2 = new DefaultMutableTreeNode(item, true);
                    root.add(root2);
                }

            }
        }else {
            DefaultMutableTreeNode root2 = new DefaultMutableTreeNode(file, true);
            root.add(root2);
        }

    }
    static  public  void createTable(File f, JSplitPane splitPane)
    {
        MyTableModel myModel = new MyTableModel(f);
        JTable table = new JTable(myModel);

        JScrollPane scrollPaneRight = new JScrollPane(table);

        splitPane.setRightComponent(scrollPaneRight); // отрисовка таблицы

    }
}
class MyTableModel extends DefaultTableModel {
    File data;

    MyTableModel (File file)
    {
        data = file;
    }
    public void insertRow() {
    }
    @Override
    public Class<?> getColumnClass(int colNum) {
        switch (colNum) {
            case 0 : return String.class;
            case 1 : return FileTime.class;
            case 2 : return Long.class;
        }
        return null;
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int colNum) {
        switch (colNum) {
            case 0 : return "Name";
            case 1 : return "Date";
            case 2 : return "Size";
        }
        return null;
    }

    @Override
    public int getRowCount() {
        if (data != null){
            return data.isDirectory()?data.listFiles().length:1;
        }
        return 0;
    }

    @Override
    public Object getValueAt(int rowNum, int colNum) {



        try {
            if (data.isFile())
            {
                BasicFileAttributes  attr = Files.readAttributes(data.toPath(), BasicFileAttributes.class);
                switch (colNum) {
                    case 0 : return data.getName();
                    case 1 : return attr.lastModifiedTime() ;
                    case 2 : return attr.size();
                }
                return null;
            }
            File[] arrFile= data.listFiles();
            BasicFileAttributes  attr = Files.readAttributes(arrFile[rowNum].toPath(), BasicFileAttributes.class);
            switch (colNum) {
                case 0 : return arrFile[rowNum].getName();
                case 1 : return attr.lastModifiedTime() ;
                case 2 : return attr.size();
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public boolean isCellEditable(int rowNum, int colNum) {
        return false;
    }

    @Override
    public void setValueAt(Object arg0, int rowNum, int colNum) {

        fireTableRowsUpdated(rowNum,rowNum);
    }

}
