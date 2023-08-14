package potengi.torre;
/*
 * Recepcao.java
 *
 * Created on 28 de Agosto de 2007, 11:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author olavo
 */

/*
 * ComunicacaoMeteoro.java
 *
 * Created on 15 de Agosto de 2005, 13:42
 */


import java.net.*;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.io.*;
import javax.swing.*;
/** 
 *
 * @author  Cap Olavo
 */
public class ComunicacaoCentral extends Thread
{  
    private boolean conecta1, primeira, conecta2;
    private ServerSocket servSoq;
    private Socket conexao;
    private JTextField temp, altit, veloc, direc;
    private JTextArea status;
    private String vetData[];
    private Conexao con[];
    private int contaConec;
    private int PORTA=5011;
       
    /** Construtor da Classe ComunicacaoMeteoro */
    public ComunicacaoCentral()
    {
        primeira = true;
        contaConec = 0;
        con = new Conexao[50];
        try
        {
            servSoq = null;
            servSoq = new ServerSocket(PORTA,10);
            conecta1 = true;
        }
        catch( IOException se )
        {
            conecta1 = false;
            System.out.println("Não conectou!");
        }
    }
        public void run()
        {
            if(conecta1)
            for(int i=0;i<con.length;i++)
            {
                if(!primeira)System.out.println("Aguardando conexão "+
                        (contaConec + 1) + "...");
                 else System.out.println("Aguardando conexão...");
                            
                esperaConexao();
                primeira = false;
                            
                con[contaConec] = new Conexao(conexao,contaConec);
                con[contaConec].start();
            }
        }
    
    public void esperaConexao() 
    {
        try{
                        
            conexao = servSoq.accept();
            contaConec++;
            vetData = new String[6];
            Calendar date = new GregorianCalendar();
            Date data = date.getTime();
            vetData = data.toString().split(" ");
            conecta2 = true;
            System.out.println("Aquisitor torre conectado em "+
                    vetData[2]+" "+ vetData[1]+" às "+ vetData[3]+"\n");
        }
        catch(IOException io){}
    }
    
    public void transmiteInformacao(String inf)
    {
        resetaConexao();
        for(int i=0;i<contaConec;i++)
        if(con[i+1] != null)
        {
            if(conecta2 && con[i+1].getCorrenteOK())
                con[i+1].transmiteInformacao(inf);
        }
    }
    
    public void resetaConexao()
    {
        if(!con[contaConec].getConexOK())contaConec--;
    }
       
    public Conexao getConexao()
    {
        return con[contaConec];
    }
    
    
}