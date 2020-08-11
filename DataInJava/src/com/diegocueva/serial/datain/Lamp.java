package com.diegocueva.serial.datain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Lamp extends DrawnPanel{
	
	/** Serial version UID 	 */
	private static final long serialVersionUID = -7494831192161401876L;
	

	/**
	 * Must be overriden
	 */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor( Color.black );
        g.fillOval(-25, 90, 40, 80);
        g.setColor( Color.BLACK );
        g.drawOval(-25, 90, 40, 80);
        
        int x=25+value;   
//        System.out.printf("x=%d%n");
        g.setColor( Color.black );
        g.fillOval(x, 120, 20, 20);
        g.setColor( Color.BLACK );
        g.drawOval(x, 120, 20, 20);
        
    }	
	

}
