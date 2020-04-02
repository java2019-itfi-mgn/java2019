package lesson5;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;

public class JTreeTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JTree			tree = new JTree();
		JScrollPane		scroll = new JScrollPane(tree);
		
		DefaultMutableTreeNode	root = new DefaultMutableTreeNode(new JLabel("test string"),true);

		DefaultMutableTreeNode	root1 = new DefaultMutableTreeNode(new JLabel("test string 1"),true);
		DefaultMutableTreeNode	root2 = new DefaultMutableTreeNode(new JLabel("test string 2"),true);
		DefaultMutableTreeNode	root3 = new DefaultMutableTreeNode(new JLabel("test string 3"),true);

		DefaultMutableTreeNode	root31 = new DefaultMutableTreeNode(new JLabel("test string 31"),true);
		DefaultMutableTreeNode	root32 = new DefaultMutableTreeNode(new JLabel("test string 32"),true);

		root3.add(root31);
		root3.add(root32);
		
		root.add(root1);
		root.add(root2);
		root.add(root3);

		tree.setRootVisible(false);
		
		DefaultTreeModel dtm = (DefaultTreeModel) tree.getModel();
		dtm.setRoot(root);
		tree.setCellRenderer(new TreeCellRenderer() {
			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected
					, boolean expanded, boolean leaf, int row, boolean hasFocus) {
				return (Component)((DefaultMutableTreeNode)value).getUserObject();
			}
		});
		
		JOptionPane.showMessageDialog(null,scroll);
	}

}
