import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import java.util.Observer;
import java.util.Observable;

public class Calculator extends Observable {
	
	JFrame frame;
	JPanel panel;
	JLabel with;
	JLabel without;
	JLabel with_heading;
	JTextField prize;
	JTextField duration;
	JLabel diff;
	JComboBox<WarrantyType> jcb;

	String[] warrantyLabels = {
		"mtl. Rate mit 5-Jahres-Garantie:",
		"mtl. Rate mit 4-Jahres-Garantie:",
		"mtl. Rate mit 4-Jahres-Plusschutz:",
		"mtl. Rate mit 2-Jahres-Plusschutz:"
	};
	
	public static void main (String[] args) {
		
		boolean helpmode = false;
		
		for (int i=0; i < args.length; i++) {
			if ( args[i].equals("--help") ) {
				helpmode = true;
				System.out.printf("\nDie Garantie kann beim starten des Programms mit dem Parameter\n\t \"-wt\" gesetzt werden.\n\nBeispiel:\n\t java -jar MMWarranty.jar -wt 3\n\n");
			}
		}
		
		if (!helpmode) {
			Calculator calculator = new Calculator(args);
		}
	}
	
	//constructor
	public Calculator(String[] args) {
		
		init(args);
	}
	
	public void init(String[] args) {
		
		frame = new JFrame("Media Markt Plus Garantie");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("media-markt-icon.jpg"));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setBounds(200, 200, 600, 270);
		
		panel = new JPanel();
		panel.setLayout(new GridLayout(5,2));
		
		WarrantyType[] wt = new WarrantyType[4];
		
		wt[0] = new WarrantyType(5, false, 0);
		wt[1] = new WarrantyType(4, false, 1);
		wt[2] = new WarrantyType(4, true,  2);
		wt[3] = new WarrantyType(2, true,  3);
		
		jcb = new JComboBox<WarrantyType>(wt);
		
		jcb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				calc();
				int n = jcb.getSelectedIndex();
				with_heading.setText(warrantyLabels[n]);
			}
		});
		
		frame.add(jcb, BorderLayout.NORTH);
		frame.add(panel, BorderLayout.CENTER);
		
		prize = new JTextField();
		duration = new JTextField("33");
		makeObservable(prize);
		makeObservable(duration);
		
		with = new JLabel("", SwingConstants.CENTER);
		without = new JLabel("", SwingConstants.CENTER);
		
		Font font = new Font("arial", Font.BOLD, 44);
		with.setFont(font);
		without.setFont(font);
		
		diff = new JLabel("", SwingConstants.CENTER);
		with_heading = new JLabel("mtl. Rate mit 5-Jahres-Garantie:");
		
		panel.add(new JLabel("Preis des Gerätes:", SwingConstants.CENTER));
		panel.add(new JLabel("Dauer der Finanzierung:", SwingConstants.CENTER));
		panel.add(prize); panel.add(duration);
		panel.add(new JLabel("mtl. Rate mit Hersteller-Garantie:", SwingConstants.CENTER));
		panel.add(with_heading);
		panel.add(without); panel.add(with);
		panel.add(new JLabel("", SwingConstants.CENTER));
		panel.add(diff);
		
		for (int i=0; i < args.length; i++) {
			if ( args[i].equals("-wt") || args[i].equals("--WarrantyType") ) {
				if ( 0 <= Integer.parseInt(args[i+1]) && Integer.parseInt(args[i+1]) <= 3) {
					
					jcb.setSelectedIndex( Integer.parseInt(args[i+1]) );
					with_heading.setText( warrantyLabels[ Integer.parseInt(args[i+1]) ] );
				}
			}
		}
		
		frame.setVisible(true);
	}
	
	private void makeObservable(JTextField jt) {
		
		jt.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void changedUpdate(DocumentEvent e) { calc(); }
			@Override
			public void removeUpdate(DocumentEvent e) { calc(); }
			@Override
			public void insertUpdate(DocumentEvent e)  { calc(); }
			
			
		});
	}
		
	//returns true if TextFields only contain numbers or a dot
	private boolean noLetter() {
		
		String s = prize.getText();
		for (char c : s.toCharArray()) {
			
			if ( !( (48 <= c && c <= 57) || c == 46 ) ) {
				return false;
			}
		}
		
		s = duration.getText();
		for (char c : s.toCharArray()) {
			
			if ( !( (48 <= c && c <= 57) || c == 46 ) ) {
				return false;
			}
		}
		
		return true;
	}
	
	public void calc() {
		
		if ( noLetter() && !prize.getText().isEmpty() && !duration.getText().isEmpty() ) {
			String result;
			double warrantyCost = 0;
			double p = Double.parseDouble(prize.getText());
			int dur  = Integer.parseInt(duration.getText());
			
			double p_without = (double)( (p + warrantyCost) / dur);
			
			result = String.format( "%.2f", p_without );
			without.setText("" + result + " €");
			
			//TODO
			switch (((WarrantyType)(jcb.getSelectedItem())).getType()) {
				case 0:
					//PLUS-GARANTIE 5 Jahre
					//Gilt für: Elektro-Groß-/ Einbau- und Kleingeräte,
					//TV, Hifi, DVD-/Blu-ray-Player, SAT-Receiver und -Anlagen
					if (0 < p && p <= 100) {
						warrantyCost = 30;
					} else if (100 < p && p <= 250) {
						warrantyCost = 50;
					} else if (250 < p && p <= 500) {
						warrantyCost = 70;
					} else if (500 < p && p <= 750) {
						warrantyCost = 90;
					} else if (750 < p && p <= 1000) {
						warrantyCost = 100;
					} else if (1000 < p && p <= 2000) {
						warrantyCost = 200;
					} else if (2000 < p) {
						warrantyCost = 400;
					}
					break;
				case 1:
					//PLUS-GARANTIE 4 Jahre
					//Gilt für: Note und Netbook, PC, Monitore,
					//Drucker, Fax- & Multifunktionsgeräte,
					//Telefone (schnurlos & schnurgebunden),
					//mobile Navigations- und Audiogeräte,
					//Spielekonsolen, Foto und Camcorder
					if (0 < p && p <= 100) {
						warrantyCost = 30;
					} else if (100 < p && p <= 250) {
						warrantyCost = 50;
					} else if (250 < p && p <= 500) {
						warrantyCost = 80;
					} else if (500 < p && p <= 750) {
						warrantyCost = 120;
					} else if (750 < p && p <= 1000) {
						warrantyCost = 150;
					} else if (1000 < p && p <= 2000) {
						warrantyCost = 250;
					} else if (2000 < p) {
						warrantyCost = 500;
					}
					break;
				case 2:
					//PLUSSCHUTZ 4 Jahre
					//Gilt für: Foto, Camcorder, Note- und Netbook,
					//Spielekonsolen, mobile Navigations- und Audiogeräte
					if (0 < p && p <= 100) {
						warrantyCost = 35;
					} else if (100 < p && p <= 200) {
						warrantyCost = 60;
					} else if (200 < p && p <= 300) {
						warrantyCost = 80;
					} else if (300 < p && p <= 400) {
						warrantyCost = 100;
					} else if (400 < p && p <= 500) {
						warrantyCost = 130;
					} else if (500 < p && p <= 750) {
						warrantyCost = 190;
					} else if (750 < p && p <= 1000) {
						warrantyCost = 250;
					} else if (1000 < p && p <= 2000) {
						warrantyCost = 400;
					} else if (2000 < p) {
						warrantyCost = 500;
					}
					break;
				case 3:
					//PLUSSCHUTZ 4 Jahre
					//Ausschließlich für Handy, Smartphone,
					//Tablet und E-Book-Reader, Smartwatches, Wearables
					if (0 < p && p <= 100) {
						warrantyCost = 40;
					} else if (100 < p && p <= 250) {
						warrantyCost = 70;
					} else if (250 < p && p <= 500) {
						warrantyCost = 120;
					} else if (500 < p && p <= 750) {
						warrantyCost = 180;
					} else if (750 < p && p <= 1000) {
						warrantyCost = 220;
					} else if (1000 < p && p <= 2000) {
						warrantyCost = 250;
					} else if (2000 < p) {
						warrantyCost = 500;
					}
					break;
				
			}
			
			double p_with = (double)( (p + warrantyCost) / dur);
			
			result = String.format( "%.2f", p_with );
			with.setText("" + result + " €");
			
			result = String.format( "%.2f", p_with - p_without );
			diff.setText("( mtl. +" + result + " € )");
			
		} else {
			
			without.setText("");
			with.setText("");
			diff.setText("");
		}
		panel.setVisible(true);
	}
	
	public class WarrantyType {
		
		private int duration;
		private boolean plusschutz;
		private int type;
		
		//constructor
		public WarrantyType(int duration, boolean plusschutz, int type) {
			this.duration = duration;
			this.plusschutz = plusschutz;
			this.type = type;
		}
		
		public int getType() {
			return type;
		}
		
		public String toString() {
			String s = "";
			s += duration;
			s += " Jahre Garantie";
			if (plusschutz) {
				s += " inkl. Plusschutz";
			}
			return s;
		}
	}
}
