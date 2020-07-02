import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Main {
	private static class ClipBoardEmptyException extends Exception{
		public ClipBoardEmptyException(){
			super("clipBoard is Empty...");
		}
	}
	
	private static class ClassWordNotFoundException extends Exception{
		public ClassWordNotFoundException(){
			super("cannot find a word \"class\" in contents...");
		}
	}
	
	private static String replaceClipboard() {
		// load clipboard
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = clipboard.getContents(clipboard);

		try{
			if(contents == null)
				throw new ClipBoardEmptyException();
			
			StringBuilder pasteString = new StringBuilder(
					(String) (contents.getTransferData(DataFlavor.stringFlavor)));

			// remove package
			int packageIndex = pasteString.indexOf("package");
			if (packageIndex > -1) {
				int endIndex = pasteString.indexOf(";", packageIndex) + 1;

				while (pasteString.charAt(endIndex) == '\n')
					endIndex++;

				pasteString.replace(packageIndex, endIndex, "");
			}

			// replace class Name
			int classIndex = pasteString.indexOf("class");
			if (classIndex > -1) {
				int sIndex = classIndex;
				int eIndex = classIndex;

				while (sIndex > 0 && pasteString.charAt(sIndex) != '\n')
					sIndex--;
				while (pasteString.charAt(eIndex) != '\n')
					eIndex++;

				sIndex++;

				pasteString.replace(sIndex, eIndex, "public class Main {");
			} else
				throw new ClassWordNotFoundException();

			// save clipboard
			clipboard.setContents(new StringSelection(pasteString.toString()), null);
			
		} catch(ClipBoardEmptyException cee){
			return cee.getMessage();
		} catch(ClassWordNotFoundException cnfe){
			return cnfe.getMessage();
		} catch(Exception e){
			return e.getMessage();
		}
		
		return "Conversion was completed!";
	}
	
	private static void gui(){
		JFrame mainFrame = new JFrame("Backjoon code Converter");
		JPanel btnPanel = new JPanel();
		JPanel labelPanel = new JPanel();
		JButton button = new JButton("Convert");
		JLabel label = new JLabel("I'm ready :)");
		
		mainFrame.setLayout(new GridLayout(2,1));;
		mainFrame.add(btnPanel);
		mainFrame.add(labelPanel);
		
		btnPanel.add(button);
		btnPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
		labelPanel.add(label);
		
		mainFrame.setSize(300, 200);
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				label.setText(replaceClipboard());
			}
		});
	}

	public static void main(String args[]) {
		gui();
	}
}
