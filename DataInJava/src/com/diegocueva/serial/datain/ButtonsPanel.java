package com.diegocueva.serial.datain;

import java.awt.Color;
import java.awt.Graphics;

public class ButtonsPanel extends DrawnPanel{
	
	/** Serial version UID 	 */
	private static final long serialVersionUID = -7494831192161401876L;
	

	/**
	 * Must be overriden
	 */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        boolean b1 = value>=80;
        g.setColor( b1?Color.red:Color.white );
        g.fillOval(80, 75-20, 40, 40);
        g.setColor( Color.BLACK );
        g.drawOval(80, 75-20, 40, 40);
        
        boolean b2 = value>=5 && value<80;
        g.setColor( b2?Color.yellow:Color.white );
        g.fillOval(80, 150-20, 40, 40);
        g.setColor( Color.BLACK );
        g.drawOval(80, 150-20, 40, 40);

        
        boolean b3 =value>=0 && value<5;
        g.setColor( b3?Color.GREEN:Color.white );
        g.fillOval(80, 225-20, 40, 40);
        g.setColor( Color.BLACK );
        g.drawOval(80, 225-20, 40, 40);
               
        
    }	
	

}
