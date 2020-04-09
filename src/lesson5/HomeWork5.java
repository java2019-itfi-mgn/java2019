package lesson5;
import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeWork5 {
    //Susanin S.
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JTree tree = new JTree();
        JScrollPane scrollPaneLeft = new JScrollPane(tree);
        JSplitPane splitPane = new JSplitPane();

        frame.add(splitPane);
        frame.setSize(1024,500);

        File file = new File("D:/");// устанавливаем свою деректорию
        createTable(file, splitPane); // начальное значение таблицы
        iniTree(file, tree); // инициализация 1 уровня дерева

        tree.setCellRenderer(new TreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree jTree, Object value, boolean b, boolean b1, boolean b2, int i, boolean b3) {
                JLabel label = new JLabel();
                File f = (File)(((DefaultMutableTreeNode)value).getUserObject());
                label.setText(f.getName()); // отображение имени файла в дереве
                return label;
            }
        });
        splitPane.setLeftComponent(scrollPaneLeft);



        tree.addTreeWillExpandListener(new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
                final DefaultMutableTreeNode lazyNode = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();

                if (lazyNode.getChildCount()==1)
                {
                    new SwingWorker<List<DefaultMutableTreeNode>, Void>(){
                        @Override
                        protected List<DefaultMutableTreeNode> doInBackground() throws Exception {

                           ArrayList list  = new ArrayList();
                           File f =  (File) (((DefaultMutableTreeNode) event.getPath().getLastPathComponent()).getUserObject()); // получаем доступ к файлу
                            for (File item: f.listFiles()) {
                                if (item.isDirectory())
                                {
                                    DefaultMutableTreeNode root = new DefaultMutableTreeNode(item, true);
                                    DefaultMutableTreeNode temp = new DefaultMutableTreeNode(item, true);
                                    root.add(temp);
                                    // добаляется пустая ветка для отображения следующей вложенности.
                                    // по этой причине верхний if проверяет это условие (lazyNode.getChildCount()==1)
                                    list.add(root);
                                }
                                else
                                {
                                    DefaultMutableTreeNode root = new DefaultMutableTreeNode(item, true);
                                    list.add(root);
                                }

                            }
                            return list;
                        }
                        protected void done() {
                            try {
                                for (DefaultMutableTreeNode node : get()) {
                                    ((DefaultTreeModel)tree.getModel()).insertNodeInto(node, lazyNode, lazyNode.getChildCount());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.execute();
                }
            }

            @Override
            public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {

            }
        });


        java.util.Timer timer = new Timer();
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            TimerTask timerTask = null;
            @Override
            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                File f = (File) (((DefaultMutableTreeNode)treeSelectionEvent.getNewLeadSelectionPath().getLastPathComponent()).getUserObject());
                if(timerTask != null)
                {
                    timerTask.cancel();// отменяем операцию если переходим на след. ветку
                }
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        createTable(f,splitPane);
                    }
                };
                timer.schedule(timerTask, 500); // запускается создание таблицы  по истечению времени (в другом потоке)

            }
        });
        tree.setCellRenderer(new TreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree jTree, Object value, boolean selected, boolean expanded, boolean leaf, int i, boolean hasFocus) {
                final JLabel	label = new JLabel(value.toString());
                if(hasFocus)
                {
                    label.setForeground(Color.BLUE);// выделение ветки на которой находится фокус
                }
                return  label;
            }
        });

        scrollPaneLeft.setMinimumSize(new Dimension(250, 500));// установка минимального размера панели с деревом
        frame.setVisible(true);
    }
    static public void iniTree(File file, JTree tree)
    {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(file, true);
        if(file.isDirectory())
        {
            for (File item: file.listFiles()) {

                if (item.isDirectory() && !item.isHidden()) {
                    DefaultMutableTreeNode root2 = new DefaultMutableTreeNode(item, true);
                    DefaultMutableTreeNode temp = new DefaultMutableTreeNode();
                    root2.add(temp);
                    // добаляется пустая ветка для отображения следующей вложенности.
                    root.add(root2);

                } else {
                    DefaultMutableTreeNode root2 = new DefaultMutableTreeNode(item, false);
                    root.add(root2);
                }

            }
        }else{
            DefaultMutableTreeNode root2 = new DefaultMutableTreeNode(file, false);
            root.add(root2);
        }
        DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
        treeModel.setRoot(root);
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
    File file;
    MyTableModel (File file)
    {
        this.file = file;
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
        if (file != null){
            return file.isDirectory()? file.listFiles().length:1;
        }
        return 0;
    }

    @Override
    public Object getValueAt(int rowNum, int colNum) {
        try {
            if (file.isFile()) // если file является файлом
            {
                BasicFileAttributes  attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class); // получение атрибутов файла
                switch (colNum) {
                    case 0 : return file.getName();
                    case 1 : return attr.lastModifiedTime() ;
                    case 2 : return attr.size();
                }
                return null;
            }
            // если fail является дерикторией
            File[] arrFile= file.listFiles();
            BasicFileAttributes  attr = Files.readAttributes(arrFile[rowNum].toPath(), BasicFileAttributes.class);// получение атрибутов файла
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
