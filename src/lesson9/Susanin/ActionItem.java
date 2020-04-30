package lesson9.Susanin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionItem implements ActionListener
{

    @Override
    public void actionPerformed(ActionEvent e) {
        String  item = ((JMenuItem)e.getSource()).getText();
        MyClass.Action(item);
    }
}

