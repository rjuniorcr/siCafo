package potengi.torre;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.text.*;


public class Direcao extends JPanel
{
    private Ellipse2D.Double circuloExt, circuloInt;
    private int dir1, dir2, dir3, dir4;
    private double aux;
    private DecimalFormat padrao;
    private Font f;

    public Direcao()
    {
        this.setBackground(Color.blue.darker ());
        this.setPreferredSize(new Dimension(300, 240));//260
        
        padrao = new DecimalFormat("000");
        circuloExt = new Ellipse2D.Double(34, 50, 160, 160);
        f = new Font("Arial", Font.BOLD, 12);
    }
 
    public void paintComponent(Graphics g)
    {
        clear(g);
        Graphics2D g2d = (Graphics2D) g;
        drawThickCircleOutline(g2d);
        g2d.setColor(Color.white);
        g2d.setFont(f);
        g2d.drawString("Nível",205,20);
        g2d.drawString("Direção",242,20);
        g2d.setColor(Color.yellow);
        g2d.drawString("  1       " + padrao.format(dir1), 209, 40);
        g2d.setColor(Color.cyan);
        g2d.drawString("  2       " + padrao.format(dir2), 209, 60);
        g2d.setColor(Color.red);
        g2d.drawString("  3       " + padrao.format(dir3), 209, 80);
        g2d.setColor(Color.GREEN);
        g2d.drawString("  4       " + padrao.format(dir4), 209, 100);
        g2d.setColor(Color.white);
        g2d.drawString("090º", 104, 45);
        g2d.drawString("180º", 199, 134);
        g2d.drawString("270º", 104, 225);
        g2d.drawString("000º", 2, 134);
        
        g2d.setStroke(new BasicStroke());
        g2d.drawRect(202,5,90,20);
        g2d.drawRect(202,25,90,20);
        g2d.drawRect(202,45,90,20);
        g2d.drawRect(202,65,90,20);
        g2d.drawRect(202,85,90,20);
        g2d.drawLine(239,5,239,105);
        
        g2d.setStroke(new BasicStroke(2));

        g2d.translate(114.0, 130.0);
        g2d.setFont(new Font("Goudy Handtooled BT", Font.BOLD, 10));
        
        aux = dir1* Math.PI / 180.0 - Math.PI;
        g2d.rotate(aux);
        g2d.setColor(Color.yellow);
        g2d.drawLine(0, 0, 80, 0);
        g2d.rotate(-aux);

        aux = dir2 * Math.PI / 180.0 - Math.PI;
        g2d.rotate(aux);
        g2d.setColor(Color.cyan);
        g2d.drawLine(0, 0, 80, 0);
        g2d.rotate(-aux);

        aux = dir3 * Math.PI / 180.0 - Math.PI;
        g2d.rotate(aux);
        g2d.setColor(Color.red);
        g2d.drawLine(0, 0, 80, 0);
        g2d.rotate(-aux);

        aux = dir4 * Math.PI / 180.0 - Math.PI;
        g2d.rotate(aux);
        g2d.setColor(Color.GREEN);
        g2d.drawLine(0, 0, 80, 0);
        g2d.rotate(-aux);

    }

    protected Ellipse2D.Double getCircleExt()
    {
        return circuloExt;
    }
    
    protected Ellipse2D.Double getCircleInt()
    {
        return circuloInt;
    }

    public void setDirecao(double d1, double d2, double d3, double d4)
    {
        dir1 = (int) d1;
        dir2 = (int) d2;
        dir3 = (int) d3;
        dir4 = (int) d4;
        repaint();
    }

    protected void drawThickCircleOutline(Graphics2D g2d)
    {
        g2d.setPaint(Color.white);
        g2d.setStroke(new BasicStroke(8)); // 8-pixel largura
        g2d.draw(getCircleExt());
        //g2d.draw(getCircleExt1());
        g2d.setPaint(Color.white);
        //g2d.draw(getCircleInt());
    }

    protected void clear(Graphics g)
    {
        super.paintComponent(g);
    }

    public void limpaDirecao()
    {
        dir1 = dir2 = dir3 = dir4 = 0;
        repaint();
    }
}