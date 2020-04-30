package lesson9_Pridannikov;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

public class Pridannikov_lesson9 {

	
	public static void main(String[] args) {
		// TODO make notepad by swing.
		// JFileChooser
		JMenu			menu;
		menu=new JMenu("File");
		
		JMenuItem clear=new JMenuItem("Clear");
		JMenuItem save=new JMenuItem("Save");
		JMenuItem open =new JMenuItem("Open");
		JMenuItem exit=new JMenuItem("Exit");
		
	
		menu.add(clear);
		menu.add(open);
		menu.add(save);
		menu.add(exit);
		
		
		JMenu menu_1;
		menu_1=new JMenu("Help");
		JMenuItem about=new JMenuItem("About the program");
		JMenuItem author=new JMenuItem("Program author");
		menu_1.add(about);
		menu_1.add(author);
	
		
		JMenuBar bar;
		bar=new JMenuBar();
		bar.add(menu);
		bar.add(menu_1);
		
		JFrame frame=new JFrame("Notepade");	
		JTextArea		ta=new JTextArea();
		JScrollPane		sp=new JScrollPane(ta);
		
		frame.setSize(1024,500);
		frame.add(bar,BorderLayout.NORTH);	
		frame.add(sp,BorderLayout.CENTER);
		
		author.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(null,"Author: Pridannikov Sergei","Program author",1);
			}});
		about.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(null,"Program version 1.0","About the program",1);
			}});

		
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}});
		
	
				
		clear.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			ta.setText("");
			
		}});
		
		
		
		open.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                JFileChooser fileopen = new JFileChooser();             
	                int ret = fileopen.showDialog(null, "Open file");                
	                if (ret == JFileChooser.APPROVE_OPTION) {
	                    File file = fileopen.getSelectedFile();
	                    //ta.setText(file.getAbsolutePath());
	                  try {
	                	  ta.setText("");
	                        // создаем экземпляр класса FileReader
	                        FileReader tfr = new FileReader(file.getAbsolutePath());
	                        // создаем в памяти буфер для чтения 8Кб символов за раз
	                        char[] buffer = new char[8096];
	                       
	                        int chars = tfr.read(buffer);
	                       
	                        // до тех пор пока есть символы в файле, читаем данные
	                        // и выводим
	                        while (chars != -1) {
	                        	
	                          ta.setText((String.valueOf(buffer, 0, chars)));
	                          chars = tfr.read(buffer);
	                        }
	                       
	                        // закрываем файл
	                        tfr.close();

	                      // отлавливаем исключение
	                      } catch (IOException e1) {
	                        e1.printStackTrace();
	                      }
	                }
	            }
	        });
	
		
		//муторно было//
		save.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 JFileChooser filesave= new JFileChooser(); 
			       filesave.setDialogTitle("Save file");
			        // Определение режима - только файл
			        filesave.setFileSelectionMode(JFileChooser.FILES_ONLY);
			        int result = filesave.showDialog(null, "Save");;
			        // Если имя файла написано, то представим его в сообщении
			        if (result == JFileChooser.APPROVE_OPTION )
			        {
			            String absoluteFilePath =(filesave.getSelectedFile().toString()+".txt");//расположение нового файла
			            File file = new File(absoluteFilePath);
			         
			                 try(FileWriter writer = new FileWriter(file, false))
			                    {
			                	 BufferedWriter fileOut = new BufferedWriter(new FileWriter(file, false));//долго искал//
				                 
			                         ta.write(fileOut);//запись текста в файл
			                	
			                		JOptionPane.showMessageDialog(null,absoluteFilePath + " File saved","Save",2);
			                		
			                      
			                      }
			                      catch(IOException ex){
			                           
			                          System.out.println(ex.getMessage());
			                      } 
 			         		            
			        }
			        }
            
        });

		frame.setVisible(true);	
		
	}

}
