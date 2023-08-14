/*
 * GraficoBarras.java
 *
 * Created on 14 de Julho de 2005, 09:50
 */

package potengi.torre;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

/**
 * @author c-olavo
 */
public class GraficoBarras extends JPanel
{
    private JProgressBar intensidade1, intensidade2, intensidade3,
            intensidade4;
    private JLabel nivel1, nivel2, nivel3, nivel4;
    private JPanel panel1, panel2;
    private JTextField taH, taB, maxV;
    private double maior;
 
    /** Creates a new instance of GraficoBarras */
    public GraficoBarras()
    {
        maior = 0;
        panel1 = new JPanel();//Pain√©is para barras de ventos de superf√≠cie.
        panel1.setPreferredSize(new Dimension(320,150));
        panel1.setLayout(new GridBagLayout());
        
        panel2 = new JPanel();
        
        taH = new JTextField(5);
        taH.setEditable(false);
        taH.setBackground(Color.white);
        taH.setForeground(Color.blue);

        taB = new JTextField(4);
        taB.setEditable(false);
        taB.setBackground(Color.white);
        taB.setForeground(Color.blue);
        
        maxV = new JTextField(3);
        maxV.setEditable(false);
        maxV.setBackground(Color.white);
        maxV.setForeground(Color.blue);

        intensidade1 = new JProgressBar();
        intensidade1.setPreferredSize(new Dimension(250, 25));
        intensidade1.setMaximum(250);
        intensidade1.setForeground(Color.GREEN.darker());

        intensidade2 = new JProgressBar();
        intensidade2.setPreferredSize(new Dimension(250, 25));
        intensidade2.setMaximum(250);
        intensidade2.setForeground(Color.GREEN.darker());

        intensidade3 = new JProgressBar();
        intensidade3.setPreferredSize(new Dimension(250, 25));
        intensidade3.setMaximum(250);
        intensidade3.setForeground(Color.GREEN.darker());

        intensidade4 = new JProgressBar();
        intensidade4.setPreferredSize(new Dimension(250, 25));
        intensidade4.setMaximum(250);
        intensidade4.setForeground(Color.GREEN.darker());

        intensidade1.setBackground(Color.white);
        intensidade2.setBackground(Color.white);
        intensidade3.setBackground(Color.white);
        intensidade4.setBackground(Color.white);

        nivel1 = new JLabel("NÌvel 1");
        nivel2 = new JLabel("NÌvel 2");
        nivel3 = new JLabel("NÌvel 3");
        nivel4 = new JLabel("NÌvel 4");

        intensidade1.setStringPainted(true);
        intensidade2.setStringPainted(true);
        intensidade3.setStringPainted(true);
        intensidade4.setStringPainted(true);

        intensidade1.setString("0.0 m/s");
        intensidade2.setString("0.0 m/s");
        intensidade3.setString("0.0 m/s");
        intensidade4.setString("0.0 m/s");

        GridBagConstraints ba = new GridBagConstraints();
        ba.insets = new Insets(6,2,6,2);
        ba.anchor = GridBagConstraints.WEST;
         
        panel1.add(nivel4,ba);
        ba.gridx = 1;
        panel1.add(intensidade4, ba);
        ba.gridy = 1;
        ba.gridx =0;
        panel1.add(nivel3,ba);
        ba.gridx = 1;
        panel1.add(intensidade3,ba);
        ba.gridy = 2;
        ba.gridx = 0;
        panel1.add(nivel2,ba);
        ba.gridx = 1;
        panel1.add(intensidade2,ba);
        ba.gridy = 3;
        ba.gridx = 0;
        panel1.add(nivel1,ba);
        ba.gridx = 1;
        panel1.add(intensidade1,ba);
        
        
        panel2.setLayout(new GridBagLayout());
        panel2.setPreferredSize(new Dimension(310, 90));
        GridBagConstraints bb = new GridBagConstraints();
        bb.insets = new Insets(3,3,3,3);
        bb.anchor = GridBagConstraints.WEST;
        
        bb.gridy = 1;
        bb.gridx = 0;
        panel2.add(new JLabel("Hora Datalogger: "),bb);
        bb.gridx = 1;
        panel2.add(taH,bb);
        bb.gridy = 2;
        bb.gridx = 0;
        panel2.add(new JLabel("Tens„o da Bateria [V]: "),bb);
        bb.gridx = 1;
        panel2.add(taB,bb);
        panel2.setPreferredSize(new Dimension(310,80));

        //this.setLayout(new GridLayout(5, 1));
        this.setPreferredSize(new Dimension(350, 270));//300
        this.setBorder(new TitledBorder(null, "Intensidade do Vento",
                TitledBorder.LEFT, TitledBorder.TOP));

        this.add(panel1);
        this.add(panel2);
     }
     /*
      * Atualiza os valores de velocidade nas barras, hora do datalogger e tens√£o
      *da bateria.
      */
    public void atualiza(double [ ] vel, String ho)
    {

        intensidade1.setValue((int) Math.round(vel[0] * 10));
        intensidade2.setValue((int) Math.round(vel[1] * 10));
        intensidade3.setValue((int) Math.round(vel[2] * 10));
        intensidade4.setValue((int) Math.round(vel[3] * 10));

        intensidade1.setString((double) Math.round(vel[0] * 10) / 10 + " m/s");
        intensidade2.setString((double) Math.round(vel[1] * 10) / 10 + " m/s");
        intensidade3.setString((double) Math.round(vel[2] * 10) / 10 + " m/s");
        intensidade4.setString((double) Math.round(vel[3] * 10) / 10 + " m/s");
        
        taH.setText(ho);
        taB.setText(String.valueOf((double) Math.round(vel[4] * 100) / 100));
    }

    public void limpaBarras()
    {
        intensidade1.setValue(0);
        intensidade2.setValue(0);
        intensidade3.setValue(0);
        intensidade4.setValue(0);
        intensidade1.setString("0 m/s");
        intensidade2.setString("0 m/s");
        intensidade3.setString("0 m/s");
        intensidade4.setString("0 m/s");
    }
 }