/*
 * Transmissao.java
 *
 * Created on 17 de Junho de 2005, 08:43
 *
 */

package potengi.torre;
import java.net.*;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.*;

import javax.swing.*;
/**
 *
 * @author Administrador
 */
public class Transmissao extends JPanel
{
    private boolean conecta;
    private int indice = 0;
    private RandomAccessFile ra;
    private DatagramPacket packet;
    private InetAddress address;
    private DatagramSocket socket;
    private DocumentBuilderFactory docBFM;
    private Document docM;
    private DocumentBuilder docBM;
    private Element firstel;
    private String grupo;
    private int porta;
     /** Creates a new instance of Transmissao */
     
    public Transmissao() 
    {
        openFile();
        inicializaComunicacao();
    }
    
    /**
     *Estabelece a comunicação com o Sistema na Segurança de Vôo.
     */
    public void inicializaComunicacao()
    {
        grupo = updateParameters("grupo");
        porta = Integer.parseInt(updateParameters("porta"));
        
        try
        {
            socket = new DatagramSocket();
            address = InetAddress.getByName(grupo);
            
            conecta = true;
        } catch (SocketException se)
        {
            conecta = false;
        } catch (UnknownHostException ue)
        {
           
        } catch (IOException io)
        {
            
        }
    }

    public void openFile()
    {
        try
        {
            docBFM = DocumentBuilderFactory.newInstance();
            docBM = docBFM.newDocumentBuilder();

            File myXMLFile = new File(".\\Config\\config.xml");
            docM = docBM.parse(myXMLFile);

            NodeList myDefaultXML = docM.getElementsByTagName("configuracao");
            Node myFirstNode = myDefaultXML.item(0);
            firstel = (Element) myFirstNode;

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String updateParameters(String cp)
    {
        NodeList myList = firstel.getElementsByTagName("network");
        Node myLocalNode = myList.item(0);
        Element other = (Element) myLocalNode;
        NamedNodeMap node = other.getAttributes();
        Node atr = node.getNamedItem(cp);

        return atr.getNodeValue();
    }

    public void fechaConexao()
    {
        conecta = false;
    }
 
    public void transmiteInformacao(String dado)
    {
        if (conecta)
        {
            byte buffer[] = dado.getBytes();
            packet = new DatagramPacket(buffer, buffer.length, address, porta);
            try
            {
                socket.send(packet);
            } catch (IOException io)
            {
            }
        }
    }
    public boolean getConecta()
    {
        return conecta;
    }
 }
