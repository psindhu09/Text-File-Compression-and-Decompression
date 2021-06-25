import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.filechooser.*;
public class Project extends JFrame implements ActionListener{
	static JTextField text;
	static JLabel l1,l2,l3;
	Project(){}
	public static void main(String args[]) throws IOException {
		JFrame f = new JFrame("File compression and decompression");
		l1=new JLabel("");
		l2=new JLabel("");


		f.setVisible(true);

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		JButton button1 = new JButton("Select");
		JButton button2 = new JButton("Compress");
		JButton button3=new JButton("DeCompress");
		
		
		text=new JTextField("text");
                
	 	 
		

		Project f1 = new Project();

		
		button1.addActionListener(f1);
		button2.addActionListener(f1);
		button3.addActionListener(f1);
		
		f.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("C:\\\\Users\\\\panit\\\\Desktop\\\\SS\\\\project.png")))));
		
		button1.setBounds(300,170,120,30);
		button1.setBackground(Color.lightGray);
		button2.setBackground(Color.LIGHT_GRAY);
		button3.setBackground(Color.LIGHT_GRAY);
		text.setBackground(Color.WHITE);
		l1.setBackground(Color.WHITE);
		l2.setBackground(Color.white);
		
		text.setBounds(100,100,600,40);
		button2.setBounds(300,220,120,30);
		button3.setBounds(300,270,120,30);
		l1.setBounds(100,320,300,30);
		l2.setBounds(100,370,300,30);
		
		f.add(l1);
		f.add(l2);
		f.add(text);
		f.add(button1);
		f.add(button2);
		f.add(button3);
		
		f.setSize(800,800);
		f.setLayout(null);
		f.setVisible(true);
		
	}
	public void actionPerformed(ActionEvent evt) {
		String a=evt.getActionCommand();
		String file="";
		if(a.equals("Select")){

			JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			int r = j.showSaveDialog(null);

			if (r == JFileChooser.APPROVE_OPTION)

			{
				file=j.getSelectedFile().getAbsolutePath();
				text.setText(file);
				
			}
			else{
				text.setText("No file type is selected....please select again");
			}
		}
		if(a.equals("Compress")){
			String file1=text.getText();
			File fi=new File(file1);
			long len1=fi.length();
			
			Functions f=new Functions();
			try {
				f.compress(file1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			String n=f.getFile(file1);
			n=n+"\\output.txt";
			fi=new File(n);
			long len2=fi.length();
			
			l1.setOpaque(true);
			l2.setOpaque(true);
			l1.setText("input file size:"+len1+"bytes   ");
			
			l2.setText("compressed file size:"+len2+"bytes");
		}
		if(a.equals("DeCompress")) {
			String file1=text.getText();
			File fi=new File(file1);
			long len1=fi.length();
			
			Functions f=new Functions();
			try {
				f.Decompress(file1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String n=f.getFile(file1);
			n=n+"\\input.txt";
			fi=new File(n);
			long len2=fi.length();
			
			l1.setOpaque(true);
			l2.setOpaque(true);
			l1.setText("input file size:"+len1+"bytes   ");
			
			l2.setText("Decompressed file size:"+len2+"bytes");
		
		}
		
	}
	

}
