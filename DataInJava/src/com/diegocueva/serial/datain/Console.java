package com.diegocueva.serial.datain;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Console extends JFrame implements SerialPortEventListener, MouseListener, WindowListener, ComponentListener {

	/** Serial version UID */
	private static final long serialVersionUID = 6206875506469171849L;
	
	/** Seial port */
	SerialPort serialPort;
	
	/**  Panels */
	private JPanel          mainPanel;
	private JPanel		buttonsPanel;
	private JPanel		centerPanel;
	private JPanel		titlesPanel;
	
	/** Buttons */
	private JButton  button1;
	private JButton  button2;
	
	/** Draw panles */
	private DrawnPanel lightPanel;
	private DrawnPanel potentiometerPanel;
	private DrawnPanel buttonsinPanel;
	
	/** Streams */
	private InputStream    serialIn;
	private OutputStream   serialOut;
	private BufferedReader serialReader;
	
	/** 
	 * Constructor
	 * Build all interface elements
	 */
	public Console(){
		super("Interfaz Sensores");
		
		mainPanel    = new JPanel(new BorderLayout());
		buttonsPanel = new JPanel(new GridLayout(1, 2, 0, 0));
		titlesPanel  = new JPanel(new GridLayout(1, 3, 0, 0));
		centerPanel  = new JPanel(new GridLayout(1, 3, 0, 0));
		
		lightPanel         = new Lamp();
		potentiometerPanel = new BarPanel();
		buttonsinPanel     = new ButtonsPanel();
		
		button1 = new JButton("ENCENDER LED");
		button2 = new JButton("APAGAR LED");
		buttonsPanel.add(button1);
		buttonsPanel.add(button2);
		
		titlesPanel.add(new MLabel("Sensor Proximidad"));
		titlesPanel.add(new MLabel("Sensor de Gas"));
		titlesPanel.add(new MLabel("Semáforo Advertencia"));
		
		centerPanel.add(lightPanel);
		centerPanel.add(potentiometerPanel);
		centerPanel.add(buttonsinPanel);		
		
		mainPanel.add(titlesPanel,  BorderLayout.PAGE_START);
		mainPanel.add(centerPanel,  BorderLayout.CENTER);
		mainPanel.add(buttonsPanel, BorderLayout.PAGE_END);
		
		button1.addMouseListener(this);
		button2.addMouseListener(this);
		this.addComponentListener(this);
		this.addWindowListener(this);
		this.addMouseListener(this);
		
		this.addComponentListener(this);
		this.addWindowListener(this);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setContentPane(mainPanel);
		this.pack();
		this.setResizable(false);
	}
	
	/**
	 * Show Console
	 * 
	 * Show window and open the serial port
	 * 
	 * @throws Exception In fail case like: NoSuchPortException, PortInUseException, UnsupportedCommOperationException 
	 */
	public void begin() throws Exception{
        this.setVisible(true);
        // Open port
        CommPortIdentifier port = CommPortIdentifier.getPortIdentifier("COM4"); 
        CommPort commPort = port.open(this.getClass().getName(),2000);
        serialPort = (SerialPort) commPort;
        serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        serialIn=serialPort.getInputStream();
        serialOut=serialPort.getOutputStream();
        serialReader = new BufferedReader( new InputStreamReader(serialIn) );
        serialPort.addEventListener(this);
        serialPort.notifyOnDataAvailable(true);
        
	}

	/**
	 * Each info send by ARDUINO is taken here
	 */
	@Override
	public void serialEvent(SerialPortEvent e) {
		Log.debug("serialEvent: "+e.toString());
		try {
			String line = serialReader.readLine();
			Log.debug("READ from serial: "+line);
                        System.out.println(line);
			if(line.startsWith("S1P:") ){
                            	visualizationSS( line );
			}
                        if(line.startsWith("S2G:")){
                            	visualizationS2( line );
			}
                                                
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Parsig the line send by ARDUINO and set the values to visualize.
	 * 
	 * @param line taken from ARDUINO
	 */
        private void visualizationSS(String line){
		// Parse the information
		String[] values = line.split(":");
		
		int light = Integer.parseInt(values[1]);
		lightPanel.setValue( light );
		lightPanel.repaint();
		
		
	}
	private void visualizationS2(String line){
		// Parse the information
		String[] values = line.split(":");
		
		
		int potentiometer = Integer.parseInt(values[1]);
		potentiometerPanel.setValue(potentiometer);
		potentiometerPanel.repaint();
		
		int buttons       = Integer.parseInt(values[1]);
		buttonsinPanel.setValue(buttons);
		buttonsinPanel.repaint();
//		
//		Log.debug("LIGHT="+light+"  POTENTIOMETER="+potentiometer+  " BUTTONS="+buttons);
		
	}
	
	@Override public void windowOpened(WindowEvent e)       {}
	@Override public void windowClosed(WindowEvent e)       {}
	@Override public void windowIconified(WindowEvent e)    {}
	@Override public void windowDeiconified(WindowEvent e)  {}
	@Override public void windowActivated(WindowEvent e)    {}
	@Override public void windowDeactivated(WindowEvent e)  {}
	@Override public void windowClosing(WindowEvent e)      {
		Log.debug("windowClosing: "+e);
		if(e.getComponent()==this){
			Log.debug("Closing port");
			System.exit(1);
		}
	}
	@Override public void componentShown(ComponentEvent e)  {}
	@Override public void componentHidden(ComponentEvent e) {}
	@Override public void componentResized(ComponentEvent e){}
	@Override public void componentMoved(ComponentEvent e)  {}
	@Override public void mouseClicked(MouseEvent e)    {}
	@Override	public void mousePressed(MouseEvent e)  {}
	@Override	public void mouseEntered(MouseEvent e)  {}
	@Override	public void mouseExited(MouseEvent e)   {}
	@Override	public void mouseReleased(MouseEvent e) {
                System.out.println("mouseReleased: "+e);
		
		if(e.getComponent() == button1){
			System.out.println("Revisar codigo fuente button1");
			try {
				serialOut.write("on".getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if(e.getComponent() == button2){
			System.out.println("Revisar codigo fuente button2");
			try {
				serialOut.write("off".getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}			
		}
	}
}
