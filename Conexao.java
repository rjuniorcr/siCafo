package potengi.torre;
/*
 * Conexao.java
 *
 * Created on 5 de Outubro de 2005, 14:45
 */

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.*;

/**
 *
 * @author  Cap Olavo
 */
public class Conexao extends Thread{
    
    private DataOutputStream output;
    private Socket conexao;
    private JTextArea status;
    private String vetDado[];
    private JTextField temp, altd, veloc, direc;
    private boolean correnteOK, transmOK;
    private int conta;
       
    /** Construtor da classe. Recebe os campos de texto para as informações
     * numéricas, o objeto dos gráficos de apresentação da sondagem, a área de
     *status, o socket de transmissão, o objeto de trajetografia e o arquivo de
     *sondagem.
     */
    public Conexao( Socket s, int c ) 
    {
        super("Conexao Meteoro " + c);
        conta = c;
        conexao = s;
    }
    /**
     *Método executor do processo.
     */
    public void run()
    {
        criaCorrentes();
    }
    /**
     *Cria a conexão com a meteorologia, com uma corrente de entrada de dados.
     */
    public void criaCorrentes()
    {
        try
        {
            output = new DataOutputStream(
                   new BufferedOutputStream(conexao.getOutputStream()));
            
            correnteOK = true;
            transmOK = true;
        }
        catch(IOException io)
        {
            correnteOK = false;
            System.out.println("Erro na corrente de dados " +conta+" !\n");
        }
    }
   
    public void transmiteInformacao(String info)
    {
        if(transmOK)
        {
            try
            {
                output.writeUTF(info);
                output.flush();
            }
            catch ( IOException exception )
            {
                System.out.println( "Conex�o "+conta+" encerrada." + "\n" );
                fechaConexao();
                transmOK = false;
            }
        }
    }

    /**
     *Fecha a conexão com a meteorologia.
     */
    public void fechaConexao()
   {
       try
       {
           if(output != null)output.close();
           if(conexao != null) conexao.close();
       }
       catch (IOException io)
       {
           System.out.println("Erro ao fechar a conex�o "+conta+" !\n");
       }
   }
    
    public boolean getCorrenteOK()
    {
        return correnteOK;
    }
    
    public boolean getConexOK()
    {
        return transmOK;
    }
}


