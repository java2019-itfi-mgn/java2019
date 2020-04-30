package lesson9.Susanin;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class MyClass{
   static JFileChooser fileChooser;
   static  JTextArea textArea = new JTextArea();
   static  JFrame frame = new JFrame("Notepad");
   static File file= null;
    public static void main(String[] args) {
        // TODO make notepad by swing.
        JMenuItem open,save, saveAs, close, aboutProgram;
        open = new JMenuItem("Open");
        save = new JMenuItem("Save");
        saveAs = new JMenuItem("Save as...");
        close = new JMenuItem("Close");
        aboutProgram = new JMenuItem("About program");

        JMenu menuFile;
        menuFile = new JMenu("File");
        menuFile.add(open);
        menuFile.add(save);
        menuFile.add(saveAs);
        menuFile.add(close);

        JMenu menuHelp;
        menuHelp = new JMenu("Help");
        menuHelp.add(aboutProgram);

        JMenuBar bar;
        bar = new JMenuBar();
        bar.add(menuFile);
        bar.add(menuHelp);

        open.addActionListener(new ActionItem());
        save .addActionListener(new ActionItem());
        saveAs.addActionListener(new ActionItem());
        close.addActionListener(new ActionItem());
        aboutProgram.addActionListener(new ActionItem());

        JScrollPane sp = new JScrollPane(textArea);

        frame.add(bar, BorderLayout.NORTH);
        frame.add(sp, BorderLayout.CENTER);
        frame.setSize(1024, 500);
        frame.setVisible(true);
    }
    static void Action(String item)
    {
        switch (item)
        {
            case "Open":
                OpenFile();
                break;
            case "Save":
                SaveFile();
                break;
            case "Save as...":
                SaveFileAs();
                break;
            case "Close":
                Close();
                break;
            case "About program":
                AboutProgram();
                break;
        }
    }
    static void OpenFile()
    {
        fileChooser = new JFileChooser("Q:");
        int q = fileChooser.showOpenDialog(null);
        if (q == JFileChooser.APPROVE_OPTION)
        {
            file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            try {
                String str1 = "", str2 = "";
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                str2 = bufferedReader.readLine();
                while ((str1 = bufferedReader.readLine()) != null)
                {
                    str2 = str2 + "\n" + str1;
                }
                textArea.setText(str2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else
        {
            JOptionPane.showMessageDialog(frame,"the user cancelled the operation" );
        }
    }
    static  void SaveFile()
    {   if(file == null)
        {
            SaveFileAs();
        }
        else {
                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    FileWriter fileWriter = new FileWriter(file);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(textArea.getText());
                    bufferedWriter.flush();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
    static  void SaveFileAs()
    {
        fileChooser = new JFileChooser("Q:");
        int q = fileChooser.showSaveDialog(null);
        if (q == JFileChooser.APPROVE_OPTION)
        {
            file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            try {
                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(textArea.getText());
                bufferedWriter.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else
        {
            JOptionPane.showMessageDialog(frame,"the user cancelled the operation" );
        }
    }
    static void Close()
    {
        frame.setVisible(false);
        System.exit(0);
    }
    static void AboutProgram()
    { JOptionPane.showMessageDialog(frame,"Version 1.2" );
    }
}
