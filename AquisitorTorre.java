/*
 * AquisitorTorre.java
 *
 * Created on 28 de Agosto de 2007, 09:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package potengi.torre;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author olavo
 */
public class AquisitorTorre extends JFrame
{
    private InterfaceTorre inter;
    private GraficoBarras grafB;
    private Direcao grafD;
    /** Creates a new instance of AquisitorTorre */
    public AquisitorTorre(String s)
    {
        super("Aquisitor torre de anemômetros");
                
        if(s.equals("manutencao"))
        {
            setSize(680,320);
            setLayout(new FlowLayout());
            if (getOperacional().equals("WINDOWS"))
        {
            setIconImage(Toolkit.getDefaultToolkit().
                    createImage(".\\Config\\logo.png"));
        } else
        {
            setIconImage(Toolkit.getDefaultToolkit().
                    createImage("Config/logo.png"));
        }
            grafB = new GraficoBarras();
            grafD = new Direcao();
            inter = new InterfaceTorre(grafB, grafD);
            add(grafB);
            add(grafD);
            setVisible(true);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
        }        
//        else inter = new InterfaceTorre();        
    }

    public static String getOperacional()
    {
        if (System.getProperty("os.name").contains("Windows"))
        {
            return "WINDOWS";
        } else if (System.getProperty("os.name").contains("Linux"))
        {
            return "LINUX";
        } else
        {
            return "Sistema Desconhecido!";
        }
    }
    
    public static void main(String []args)
    {
        try{
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
       catch(Exception c){}   

        new AquisitorTorre("manutencao");
    }
    
}
