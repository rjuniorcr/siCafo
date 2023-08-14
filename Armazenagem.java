/*
 * Armazenagem.java
 *
 * Created on 23 de Junho de 2005, 13:30
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package potengi.torre;
import java.io.*;
import javax.swing.*;
import java.text.*;
import java.util.*;
/**
 *
 * @author Administrador
 */
public class Armazenagem 
{
    private RandomAccessFile file;
    private DecimalFormat dir_f, vel_f;
       
    /** Creates a new instance of Armazenagem */
    public Armazenagem(String data)
    {
        dir_f = new DecimalFormat("000");
        vel_f = new DecimalFormat("00.0");
        try
        {
            file = new RandomAccessFile(".\\BancoTorre\\"+data+
                    ".txt", "rw" );
            while(file.readLine() != null);
        }
        catch ( IOException e )
        {
            JOptionPane.showMessageDialog( null,
                    "Arquivo inexistente",
                    "Nome de arquivo inválido!",
                    JOptionPane.ERROR_MESSAGE );
        }
    }
    public void fechaArquivo()
    {
        try{
        if(file != null)file.close();
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog( null,
               "Erro",
               "Não foi possível fechar o arquivo!",
               JOptionPane.ERROR_MESSAGE );
        }
    }
    public void armazenaDados(double v[], double d[], String data)
    {
        String v1,d1,v2,d2,v3,d3,v4,d4;
        v1 = vel_f.format((double)Math.round(v[0]*10)/10);
        v2 = vel_f.format((double)Math.round(v[1]*10)/10);
        v3 = vel_f.format((double)Math.round(v[2]*10)/10);
        v4 = vel_f.format((double)Math.round(v[3]*10)/10);
        
        d1 = dir_f.format((int)d[0]);
        d2 = dir_f.format((int)d[1]);
        d3 = dir_f.format((int)d[2]);
        d4 = dir_f.format((int)d[3]);
        
        try {           
            file.writeBytes(data+" "+ v1 +" "+ d1 + " " + v2 + " " + d2 + 
                        " " + v3 + " " + d3 + " " + v4 + " " + d4 +"\n");
         }
         catch ( IOException e ) { }
    }
  }
    

