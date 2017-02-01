package deployment;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.security.SecureRandom;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import main.java.Authority;
import main.java.AuthorityImpl;

public class DespliegueTest {

	private JTextField datos;
	
	private JButton md5_encript;
	private JButton md5_desencript;
	
	private String voto_md5_encript;
	private String voto_md5_desencript;
	
	private Integer token;
	private String votationId;
	
	
	private JFrame frame1;
	private JFrame frame2;
	private JFrame frame3;
	
	private JButton rsa_encript;
	private JButton rsa_desencript;
	
	private String voto_rsa_encript;
	private String voto_rsa_desencript;

//<--------------------------------- Datos necesarios ------------------------------------>
	
	private static final Integer[] tokenAuxList = {120338, 127508, 219240, 231958, 
			264907, 301200, 301415, 318851, 328237, 333555, 366710, 376217, 382413, 
			406463, 409921, 436780, 458841, 461513, 530897, 589116, 590265, 590815, 
			593252, 656720, 746976, 830375, 865247, 869061, 885540, 907197, 909246, 
			961864, 976931, 982612};
	
	
	/**
	 * Esta función hace el cálculo del Token necesario para el acceso al subsistema
	 * de verificación. Este token es el que se almacenará en la base de datos
	 * posteriormente. Este token se calcula de la siguiente forma:
	 * 1 - El id de la votación pasa a binario.
	 * 2 - Recorremos el número binario y multiplicamos del final hacia el 
	 *     principio con el índice correspondiente de la lista de números
	 *     estática.
	 * 3 - Vamos sumando el resultado de la multiplicación a lo que ya tuvieramos.
	 * 4 - Finalmente, multiplicamos por dos primos para aumentar el tamaño.
	 * @param votationId. Corresponde al id de la votación.
	 * @return token. Número entero que corresponde con el token generado.
	 * 
	 * 
	 */
	
	private static void checkId(Integer votationId) {
		assert votationId <= 999999998;
		
	}
	
	private static Integer calculateToken(Integer votationId){
		
		Integer token = 0;
		
		checkId(votationId);
		
		String binaryInteger = Integer.toBinaryString(votationId);
		char[] numberByNumber = binaryInteger.toCharArray();
		
		int j = 0;
		for(int i=numberByNumber.length-1; 0 <= i; i--){
			String binDigit = Character.toString(numberByNumber[i]);
			Integer digit = new Integer(binDigit);
			if(digit > 0){
				token += digit*tokenAuxList[tokenAuxList.length-1-j];
				
			}
			j++;
		}
		
		return token*17;
		
	}
	
	//<--------------------------------------------------------------------------------------->
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DespliegueTest window = new DespliegueTest();
					window.frame1.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DespliegueTest() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame1 = new JFrame();
		frame1.getContentPane().setLayout(null);
		
		frame1.setSize(450, 263);
		frame1.setTitle("Despliegue");
		
		JLabel label = new JLabel("Insertar votación: ");
		label.setBounds(106, 85, 102, 14);
		frame1.getContentPane().add(label);
		
		datos = new JTextField();
		datos.setBounds(218, 75, 500, 34);
		frame1.getContentPane().add(datos);
		
		//<-----------------------------------MD5----------------------------------------->
		
		md5_encript = new JButton("Encriptar(MD5)");
		md5_encript.setBounds(185, 133, 127, 23);
		frame1.getContentPane().add(md5_encript);
		 
		
		md5_encript.addActionListener(new ActionListener(){

			//ENCRIPTAR
			
		      public void actionPerformed(ActionEvent ae){
		    	  
		    	  
		    	  Authority au = new AuthorityImpl();
		    	  votationId = (new BigInteger(25, new SecureRandom())).toString();
		    	  token = calculateToken(new Integer(votationId));
		    	  au.postKeyLocal(votationId, token);
		    	  
		    	  voto_md5_encript = au.encryptLocal(votationId, datos.getText(), token);
		    	  frame2 = new JFrame();
		    	  frame2.getContentPane().setLayout(null);
		  		
		    	  frame2.setSize(450, 263);
		    	  frame2.setTitle("Despliegue");
		    	  
		    	  frame1.setVisible(false);
		    	  frame2.setVisible(true);
		    	  
		    	JLabel datos = new JLabel("Voto encriptado: ");
		  		datos.setBounds(107, 79, 128, 25);
		  		frame2.getContentPane().add(datos);
		  		
		  		JTextArea textArea = new JTextArea();
				textArea.setBounds(218, 81, 500, 34);
				textArea.setText(voto_md5_encript);
				frame2.getContentPane().add(textArea);
				
		  				
		  		md5_desencript = new JButton("Desencriptar(MD5)");
		  		md5_desencript.setBounds(139, 138, 175, 23);
		  		frame2.getContentPane().add(md5_desencript);
		  		md5_desencript.addActionListener(new ActionListener(){

		  			//DESENCRIPTAR
					
				      public void actionPerformed(ActionEvent ae){
				    	  
				    	  Authority au = new AuthorityImpl();
				    	
				    	  try{
				    		
				    		 
							voto_md5_desencript = au.decryptLocal(votationId, voto_md5_encript, token);
						
				    	  } catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    	  
				    	  frame3 = new JFrame();
				    	  frame3.getContentPane().setLayout(null);
				  		
				    	  frame3.setSize(450, 263);
				    	  frame3.setTitle("Despliegue");
				    	  
				    	  frame2.setVisible(false);
				    	  frame3.setVisible(true);
				    	  
				    	JLabel datos = new JLabel("Voto desencriptado: ");
				  		datos.setBounds(107, 79, 128, 25);
				  		frame3.getContentPane().add(datos);
				  		
				  		JTextArea textArea = new JTextArea();
						textArea.setBounds(225, 81, 500, 34);
						textArea.setText(voto_md5_desencript);
						frame3.getContentPane().add(textArea);
		    	  

		      }

		});
		
		      }
		});
		
	
		/*
		
		//<-----------------------RSA---------------------------------->
		
		JButton rsa_encript = new JButton("Encriptar(RSA)");
		rsa_encript.setBounds(139, 172, 127, 23);
		frame1.getContentPane().add(rsa_encript);
		md5_encript.addActionListener(new ActionListener(){

			//ENCRIPTAR
			
		      public void actionPerformed(ActionEvent ae){
		    	  
		    	
		    	  voto_rsa_encript = CryptoEngine.Encriptar(voto);
		    	  frame2 = new JFrame();
		    	  frame2.getContentPane().setLayout(null);
		  		
		    	  frame2.setSize(450, 263);
		    	  frame2.setTitle("Despliegue");
		    	  
		    	  frame1.setVisible(false);
		    	  frame2.setVisible(true);
		    	  
		    	JLabel datos = new JLabel("Voto encriptado: ");
		  		datos.setBounds(107, 79, 128, 25);
		  		frame2.getContentPane().add(datos);
		  		
		  		JTextArea textArea = new JTextArea();
				textArea.setBounds(218, 81, 500, 34);
				textArea.setText(voto_md5_encript);
				frame2.getContentPane().add(textArea);
				
		  				
		  		md5_desencript = new JButton("Desencriptar(MD5)");
		  		md5_desencript.setBounds(139, 138, 175, 23);
		  		frame2.getContentPane().add(md5_desencript);
		  		md5_desencript.addActionListener(new ActionListener(){

		  			//DESENCRIPTAR
					
				      public void actionPerformed(ActionEvent ae){
				    	  
				    	
				    	  try {
							voto_md5_desencript = AuthorityImpl.Desencriptar(voto_md5_encript);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    	  
				    	  frame3 = new JFrame();
				    	  frame3.getContentPane().setLayout(null);
				  		
				    	  frame3.setSize(450, 263);
				    	  frame3.setTitle("Despliegue");
				    	  
				    	  frame2.setVisible(false);
				    	  frame3.setVisible(true);
				    	  
				    	JLabel datos = new JLabel("Voto desencriptado: ");
				  		datos.setBounds(107, 79, 128, 25);
				  		frame3.getContentPane().add(datos);
				  		
				  		JTextArea textArea = new JTextArea();
						textArea.setBounds(225, 81, 500, 34);
						textArea.setText(voto_md5_desencript);
						frame3.getContentPane().add(textArea);
		    	  

		      }

		});
		
		      }
		});
		
		*/

		} 
	}