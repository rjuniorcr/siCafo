/*
 * InterfaceTorre.java
 *
 * Created on 27 de Agosto de 2007
 */
package potengi.torre;

import javax.comm.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 *
 * @author Cap Olavo
 */
public class InterfaceTorre implements Runnable, SerialPortEventListener
{
    private int indice;
    private InputStream input;
    private OutputStream output;
    private SerialPort portaSerial;
    private ComunicacaoDataLogger informacao;
    private Thread read;
    private boolean rec;
    private double segundos, velocidade[], mantissa, direcao[], velocAux[], dirAux[];
    private byte info[];
    private DecimalFormat padir, padvel, padrao;
    private int hora1, hora2, minutos, dia, contaMedia = 0;
    private int sinal, expoente, contador = 0, diaI;
    private String hora, a, dataArq;
    private Transmissao tx;
    private Armazenagem armazena;
    private Calendar date;
    private Date data;
    private GraficoBarras grBarras;
    private Direcao grDir;
    private ArrayList componentes;
    private double compS1, compS2, compS3, compS4, compC1, compC2, compC3, compC4;

    public InterfaceTorre(GraficoBarras grBarras, Direcao grDir)
    {
        this.grBarras = grBarras;
        this.grDir = grDir;
        indice = 0;
        contador = 0;
        info = new byte[54];
        velocidade = new double[5];
        direcao = new double[4];
        velocAux = new double[5];
        dirAux = new double[5];
        padir = new DecimalFormat("000");
        padvel = new DecimalFormat("0.0");
        padrao = new DecimalFormat("00");
        componentes = new ArrayList();
        capturaPortas();
        criaArmazenagem();
    }

    public void run()
    {
        try
        {
            Thread.sleep(20000);
        } catch (InterruptedException e)
        {
        }
    }

    public void criaArmazenagem()
    {
        String vetData[] = new String[6];
        date = new GregorianCalendar();
        data = date.getTime();
        vetData = data.toString().split(" ");
        a = vetData[2];
        armazena = new Armazenagem(vetData[2] + vetData[1] + vetData[5]);
        dia = Integer.parseInt(a);
    }

    public void capturaPortas()
    {
        Enumeration listaPortas = CommPortIdentifier.getPortIdentifiers();

        while (listaPortas.hasMoreElements())
        {
            CommPortIdentifier idPorta = (CommPortIdentifier) listaPortas.nextElement();
            if (idPorta.getPortType() == CommPortIdentifier.PORT_SERIAL)
            {
                if (idPorta.getName().equals("COM1"))
                {
                    processaConexaoSerial(idPorta);
                }
            }
        }
    }

    public void processaConexaoSerial(CommPortIdentifier porta)
    {
        try
        {
            portaSerial = (SerialPort) porta.open("AquisitorMeteoro", 2000);
            System.out.println("Porta serial aberta...");
        } catch (PortInUseException e)
        {
        }
        try
        {
            input = portaSerial.getInputStream();
            output = portaSerial.getOutputStream();

            System.out.println("Correntes de dados estabelecidas...");

            informacao = new ComunicacaoDataLogger(output, this);
            informacao.start();
        } catch (IOException e)
        {
            System.out.println("Impossível estabelecer corrente!");
        }
        try
        {
            portaSerial.addEventListener(this);
            System.out.println("Aguardando dados da serial...");
            rec = true;
        } catch (TooManyListenersException e)
        {
        }
        portaSerial.notifyOnDataAvailable(true);
        try
        {
            portaSerial.setSerialPortParams(9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e)
        {
        }
        read = new Thread(this);
        read.start();
        tx = new Transmissao();
    }

    @Override
    public void serialEvent(SerialPortEvent ev)
    {
        switch (ev.getEventType())
        {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                byte[] readBuffer = new byte[1];
                if (rec)
                {
                    System.out.println("\nRecebendo dados....\n");
                }
                rec = false;
                try
                {
                    while (input.available() > 0)
                    {
                        int numBytes = input.read(readBuffer);
                        montaDado(readBuffer[0]);
                    }

                } catch (IOException e)
                {
                }
                break;
        }
    }

    public void montaDado(byte retorno)
    {
        if (indice < 53)
        {
            info[indice] = retorno;
            informacao.setRetorno(retorno);
            indice++;
        } else
        {
            indice = 0;
            montaHora();
        }
    }

    public void montaHora()
    {
        hora1 = (info[3] & 255) * (int) Math.pow(2, 8) + (info[4] & 255);
        hora2 = hora1 / 60;  // Hora
        minutos = hora1 % 60;  // Minutos
        segundos = (double) (((info[5] & 255) * (int) Math.pow(2, 8) + (info[6] & 255))) / 10;
        hora = (padrao.format((int) hora2) + ":" + padrao.format(
                (int) minutos) + ":" + padrao.format((int) segundos));
        montaVelocidade();
    }

    public void montaVelocidade()
    {
        for (int i = 0; i < 5; i++)
        {
            sinal = (((info[9 + i * 4] & 128) == 0) ? 1 : -1);
            expoente = (info[9 + i * 4] & 127) - 64;
            mantissa = (info[10 + i * 4] & 255) / Math.pow(2, 8) +
                    (info[11 + 4 * i] & 255) / Math.pow(2, 16) +
                    (info[12 + 4 * i] & 255) / Math.pow(2, 24);
            velocidade[i] = sinal * (mantissa * Math.pow(2, expoente));

            //faz a filtragem dos dados
            if (velocidade[i] < 0 || velocidade[i] > 20.0)
            {
                velocidade[i] = velocAux[i];
            }

            velocAux[i] = velocidade[i];
        }
        montaDirecao();
    }

    /**
     *Obtém a informação de direção.
     */
    public void montaDirecao()
    {
        for (int i = 0; i < 4; i++)
        {
            //obtem o sinal da direcao
            sinal = (((info[29 + i * 4] & 128) == 0) ? 1 : -1);
            //calcula o expoente
            expoente = (info[29 + i * 4] & 127) - 64;
            //calcula a mantissa
            mantissa = (info[30 + i * 4] & 255) / Math.pow(2, 8) +
                    (info[31 + 4 * i] & 255) / Math.pow(2, 16) +
                    (info[32 + 4 * i] & 255) / Math.pow(2, 24);
            //monta a informacao.
            direcao[i] = sinal * (mantissa * Math.pow(2, expoente));

            //faz a filtragem do dados
            if (direcao[i] < 0 || direcao[i] > 359)
            {
                direcao[i] = dirAux[i];
            }
            dirAux[i] = direcao[i];
        }
        transmiteDado();
        grBarras.atualiza(velocidade, hora);
        grDir.setDirecao(direcao[0], direcao[1], direcao[2], direcao[3]);
        
        calculaComponentes();
    }

    private void transmiteDado()
    {
        String dado = padvel.format(velocidade[0]) + "#" + padir.format(direcao[0]) + "#" +
                padvel.format(velocidade[1]) + "#" + padir.format(direcao[1]) + "#" +
                padvel.format(velocidade[2]) + "#" + padir.format(direcao[2]) + "#" +
                padvel.format(velocidade[3]) + "#" + padir.format(direcao[3]) + "#" +
                velocidade[4] + "#" + hora;
        tx.transmiteInformacao(dado);
    }

    public void setIndice(int i)
    {
        indice = i;
    }

    /**
     *DecompÃµe as velocidades em suas componentes seno e cosseno, com os
     *valores de direção transformados em radianos, de acordo com o argumen-
     *das funções matemÃ¡ticas.
     */
    public void calculaComponentes()
    {

        MediaVento mediaV = new MediaVento();

        //calcula as componentes seno e seta as variaveis correspondentes
        //do objeto da classe MediaVento
        mediaV.setCompSeno1(velocidade[0] * Math.sin(direcao[0] * Math.PI / 180));
        mediaV.setCompSeno2(velocidade[1] * Math.sin(direcao[1] * Math.PI / 180));
        mediaV.setCompSeno3(velocidade[2] * Math.sin(direcao[2] * Math.PI / 180));
        mediaV.setCompSeno4(velocidade[3] * Math.sin(direcao[3] * Math.PI / 180));

        //calcula as componentes cosseno e seta as variaveis correspondentes
        //do objeto da classe MediaVento
        mediaV.setCompCos1(velocidade[0] * Math.cos((direcao[0] * Math.PI / 180)));
        mediaV.setCompCos2(velocidade[1] * Math.cos((direcao[1] * Math.PI / 180)));
        mediaV.setCompCos3(velocidade[2] * Math.cos((direcao[2] * Math.PI / 180)));
        mediaV.setCompCos4(velocidade[3] * Math.cos((direcao[3] * Math.PI / 180)));

        //adiciona os objetos na lista de objetos
        componentes.add(mediaV);
        mediaV = null;

        //incrementa o contador de mÃ©dias
        contaMedia++;

        //se atingiu dez valores, calcula a mÃ©dia deles, caso
        //a tela de ajuste esteja ativa.
        if (contaMedia > 0 && contaMedia % 10 == 0)
        {
            calculaMedia();
        }
    }

    /**
     *Calcula a media das velocidades do vento em cada nível, com a 
     * respectiva direção e transmite os valores para o calculo de ajuste 
     * do lançador.
     */
    public void calculaMedia()
    {
        double vel[] = new double[4];
        double dir[] = new double[4];
        
        for (int i = 0; i < componentes.size(); i++)
        {
            compS1 += ((MediaVento) componentes.get(i)).getCompSeno1();
            compS2 += ((MediaVento) componentes.get(i)).getCompSeno2();
            compS3 += ((MediaVento) componentes.get(i)).getCompSeno3();
            compS4 += ((MediaVento) componentes.get(i)).getCompSeno4();

            compC1 += ((MediaVento) componentes.get(i)).getCompCos1();
            compC2 += ((MediaVento) componentes.get(i)).getCompCos2();
            compC3 += ((MediaVento) componentes.get(i)).getCompCos3();
            compC4 += ((MediaVento) componentes.get(i)).getCompCos4();
        }

        compS1 = compS1 / contaMedia;
        compS2 = compS2 / contaMedia;
        compS3 = compS3 / contaMedia;
        compS4 = compS4 / contaMedia;

        compC1 = compC1 / contaMedia;
        compC2 = compC2 / contaMedia;
        compC3 = compC3 / contaMedia;
        compC4 = compC4 / contaMedia;

        //velocidades medias em cada um dos quatro níveis da torre
        vel[0] = Math.sqrt(Math.pow(compS1, 2) + Math.pow(compC1, 2));
        vel[1] = Math.sqrt(Math.pow(compS2, 2) + Math.pow(compC2, 2));
        vel[2] = Math.sqrt(Math.pow(compS3, 2) + Math.pow(compC3, 2));
        vel[3] = Math.sqrt(Math.pow(compS4, 2) + Math.pow(compC4, 2));

        //calcula as direções em cada um dos quatro níveis da torre.
        dir[0] = calculaDirecao(compS1, compC1);
        dir[1] = calculaDirecao(compS2, compC2);
        dir[2] = calculaDirecao(compS3, compC3);
        dir[3] = calculaDirecao(compS4, compC4);

        

        //atualiza as informacoes da lista
        if (contaMedia == 120)
        {
            removeSessentaElementos(componentes);
            contaMedia = 60;
        }

        //zera as variaveis para a proxima media.
        compS1 = 0.0;
        compS2 = 0.0;
        compS3 = 0.0;
        compS4 = 0.0;

        compC1 = 0.0;
        compC2 = 0.0;
        compC3 = 0.0;
        compC4 = 0.0;

        contador++;
        if (contador == 6)
        {
            date = new GregorianCalendar();
            data = date.getTime();
            a = data.toString().substring(8, 10);
            diaI = Integer.parseInt(a);
            if (diaI != dia)
            {
                armazena.fechaArquivo();
                criaArmazenagem();
            }
            dataArq = data.toString().substring(11, 16);
            armazena.armazenaDados(vel, dir, dataArq);
            contador = 0;
        }
    }   

    /**
     *   Calcula a direção media, atraves das componentes Seno e Cosseno.
     */
    public double calculaDirecao(double sen, double cos)
    {
        double direc;
        if (sen > 0 && cos > 0)
        {
            direc = Math.atan(sen / cos) * 180 / Math.PI;
        } 
		else 
			if ((sen > 0 && cos < 0) || (sen < 0 && cos < 0))
            {
              direc = Math.atan(sen / cos) * 180 / Math.PI + 180;
            } 
			else
            {
              direc = Math.atan(sen / cos) * 180 / Math.PI + 360;
            }
        return direc;
    }

    /**
	   Remove os 100 primeiros dados,se a lista ultapassar o tamanho máximo (800 informações) 
	*/
    public void removeSessentaElementos(Collection ls)
    {
        Iterator iterator = ls.iterator();

        for (int i = 0; i < 60; i++)
        {
            if (iterator.next() instanceof MediaVento)
            {
                iterator.remove();
            }
        }
    }
}
