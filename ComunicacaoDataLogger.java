/*
 * Conexao.java
 *
 * Created on 12 de Julho de 2005, 08:12
 */

package potengi.torre;
import java.io.*;
import java.text.DecimalFormat;

/**
 *
 * @author c-olavo
 */
public class ComunicacaoDataLogger extends Thread {
    private OutputStream out;
    private byte retorno;
    private byte enter[]={13};
    private boolean flagAsterisco, porta, flagCodico,pronto;
    private byte codico[] = {'3','1','4','2','J',13};
    private byte config[]= {0,64,0,1,2,3,4,5,6,7,8,9,10,12,0};
    private byte k[]={'K'};
    private byte info[];
    private int indice = 0;
    private double segundos,velocidade[],mantissa,direcao[];
    private String st, hora;
    private DecimalFormat padrao;
    private InterfaceTorre i;
        
    /** Creates a new instance of Conexao */
    public ComunicacaoDataLogger(OutputStream o, InterfaceTorre i)
    {
        out = o;
        this.i = i;
    }
    public void run()
    {
        try{
           while(retorno != '*' && !porta && !flagAsterisco)
            {
                Thread.sleep(500);
                out.write(enter);
             }
           flagAsterisco = true;
           if(flagAsterisco && !flagCodico)
           {
               out.write(codico);
               Thread.sleep(1000);
               out.write(config);
            }

           flagCodico = true;
           solicitaDado();
        }
        catch(IOException io){}
        catch(InterruptedException ie){}
    }
    
    public void solicitaDado()
    {
      
        while(!porta)
       {
            try{
                i.setIndice(0);
                Thread.sleep(1000);
                out.write(k);
                out.write(enter);
            }
            catch(InterruptedException ie){}
            catch(IOException io){}
       }
    }
        
    public void setSinal(boolean p)
    {
        porta = p;
    }
    
    public String getInfo()
    {
        return st;
    }
    public boolean getPronto()
    {
        return pronto;
    }
    public void setPronto()
    {
        pronto = false;
    }
    public void setRetorno(byte r)
    {
        retorno = r;
    }
 }
