package lesson6;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;


public class HomeWork6
{
    public static void main(String[] args) {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://localhost:5432/Test";
            String login = "postgres";
            String password = "postgres";

            connection = DriverManager.getConnection(url,login,password); // подключаемся к базе
            Statement statement = connection.createStatement();

            String tableName = "jc_contact";

            ResultSet resultSet = statement.executeQuery("select * from " + tableName);// выбираем таблицу
            ResultSetMetaData metaData = resultSet.getMetaData(); // получаем ее метаданные

            JFrame frame = new JFrame();
            JTree tree = new JTree();
            JScrollPane scrollPaneLeft = new JScrollPane(tree);

            JSplitPane splitPane = new JSplitPane();
            frame.add(splitPane);
            frame.setSize(1024,500);

            iniTree(metaData, tree); // инициализация дерева
            tree.setCellRenderer(new TreeCellRenderer() {
                @Override
                public Component getTreeCellRendererComponent(JTree jTree, Object value, boolean b, boolean b1, boolean b2, int i, boolean b3) {
                    JLabel label = new JLabel();
                    String f = (String) (((DefaultMutableTreeNode)value).getUserObject());
                    label.setText(f); // отображение имени колонки в дереве
                    return label;
                }
            });
            splitPane.setLeftComponent(scrollPaneLeft);
            splitPane.setRightComponent(new JLabel());
            scrollPaneLeft.setMinimumSize(new Dimension(250, 500));// установка минимального размера панели с деревом

            java.util.Timer timer = new Timer();
            tree.addTreeSelectionListener(new TreeSelectionListener() {
                TimerTask timerTask = null;
                @Override
                public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                    String ColumnLabel = (String) (((DefaultMutableTreeNode)treeSelectionEvent.getNewLeadSelectionPath().getLastPathComponent()).getUserObject());
                    // получаем имя колонки
                    if(timerTask != null)
                    {
                        timerTask.cancel();// отменяем операцию если переходим на след. ветку
                    }
                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            try {

                                createTable(resultSet, statement, ColumnLabel, splitPane);// строим таблицу
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
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

            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

   static void iniTree(ResultSetMetaData resultSetMetaData, JTree tree) throws SQLException {
            DefaultMutableTreeNode root = new DefaultMutableTreeNode(resultSetMetaData.getTableName(1), true); // ветка с названием таблицы
            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                DefaultMutableTreeNode root2 = new DefaultMutableTreeNode(resultSetMetaData.getColumnName(i), false); // колонки этой таблицы
                root.add(root2);
            }
            DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
            treeModel.setRoot(root);
    }

    static  public  void createTable (ResultSet resultSet, Statement statement, String ColumnLabel, JSplitPane splitPane) throws SQLException {
        MyTableModel myModel = new MyTableModel(resultSet, statement, ColumnLabel);
        JTable table = new JTable(myModel);
        JScrollPane scrollPaneRight = new JScrollPane(table);
        splitPane.setRightComponent(scrollPaneRight); // отрисовка таблицы
    }
}
class MyTableModel extends DefaultTableModel {
    ResultSet resultSet;
    String ColumnLabel;
    Statement statement;
    int MaxSizeRows = 1000; // максимальное количество строк
    int CurrentSizeRows = 0;
    MyTableModel (ResultSet resultSet, Statement statement, String ColumnLabel) throws SQLException {
        this.resultSet = resultSet;
        this.ColumnLabel = ColumnLabel;
        this.statement = statement;
    }

    @Override
    public Class<?> getColumnClass(int colNum) {
             return String.class;
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public String getColumnName(int colNum) {
        return ColumnLabel;
    }

    @Override
    public int getRowCount() {
            return resultSet==null? 0:MaxSizeRows;
    }

    @Override
    public Object getValueAt(int rowNum, int colNum) {
        rowNum++; // индексация в таблице начинается с 1
        try {
            if(rowNum == 1)
                resultSet = statement.executeQuery("select * from jc_contact");// обновляю положение курсора
                //Не знаю как по другому поднять курсор наверх
            if(resultSet.next())// перемещаем курсор на следующую позицию
            {
                CurrentSizeRows++; // увеличиваем текущие количество строк
                return resultSet.getString(ColumnLabel);
            }
            else
            {
                MaxSizeRows = CurrentSizeRows;// если в БД все строки кончились изменяем максимальный размер на текущий
                //Не знаю как получить количество строк в таблице
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
