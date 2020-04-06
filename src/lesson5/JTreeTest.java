package lesson5;

import java.awt.Component;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;

public class JTreeTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JTree			tree = new JTree();
		JScrollPane		scroll = new JScrollPane(tree);
		
		DefaultMutableTreeNode	root = new DefaultMutableTreeNode(new File("asasas"),true);

		DefaultMutableTreeNode	root1 = new DefaultMutableTreeNode("test string 1",true);
		DefaultMutableTreeNode	root2 = new DefaultMutableTreeNode("test string 2",true);
		DefaultMutableTreeNode	root3 = new DefaultMutableTreeNode("test string 3",true);

		DefaultMutableTreeNode	root31 = new DefaultMutableTreeNode("test string 31",true);
		DefaultMutableTreeNode	root32 = new DefaultMutableTreeNode("test string 32",true);

		root3.add(root31);  // java.io.File {listFiles(), getName()}
		root3.add(root32);
		
		root.add(root1);	// TODO: scan any directory in your disk, build tree and show it on screen
		root.add(root2);
		root.add(root3);

	
		tree.setRootVisible(true);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				// TODO Auto-generated method stub
//				DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)e.getNewLeadSelectionPath().getLastPathComponent();
				System.err.println("Changed: ");//+((File)dmtn.getUserObject()).getName());
			}
		});
		
		DefaultTreeModel dtm = (DefaultTreeModel) tree.getModel();
		dtm.setRoot(root);
//		tree.setCellRenderer(new TreeCellRenderer() {
//			@Override
//			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected
//					, boolean expanded, boolean leaf, int row, boolean hasFocus) {
//				return (Component)((DefaultMutableTreeNode)value).getUserObject();
//			}
//		});
		
		JOptionPane.showMessageDialog(null,scroll);
	}

}
