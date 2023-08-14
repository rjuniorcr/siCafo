/*
 * MediaVento.java
 *
 * Created on 5 de Novembro de 2007, 09:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package potengi.torre;

/**
 *
 * @author olavo
 */
public class MediaVento
{
    private double compS1, compS2, compS3, compS4,compC1,compC2,compC3,compC4;
    /** Construtor da classe MediaVento */
    public MediaVento()
    {
        compS1 = 0.0;
        compS2 = 0.0;
        compS3 = 0.0;
        compS4 = 0.0;
        
        compC1 = 0.0;
        compC2 = 0.0;
        compC3 = 0.0;
        compC4 = 0.0;
    }
    
    public void setCompSeno1(double s1)
    {
        compS1 = s1;
    }
    
    public void setCompSeno2(double s2)
    {
        compS2 = s2;
    }
    
    public void setCompSeno3(double s3)
    {
        compS3 = s3;
    }
    
    public void setCompSeno4(double s4)
    {
        compS4 = s4;
    }
    
    public void setCompCos1(double c1)
    {
        compC1 = c1;
    }
    
    public void setCompCos2(double c2)
    {
        compC2 = c2;
    }
    
    public void setCompCos3(double c3)
    {
        compC3 = c3;
    }
    
    public void setCompCos4(double c4)
    {
        compC4 = c4;
    }
    
    public double getCompSeno1()
    {
        return compS1;
    }
    
    public double getCompSeno2()
    {
        return compS2;
    }
    
    public double getCompSeno3()
    {
        return compS3;
    }
    
    public double getCompSeno4()
    {
        return compS4;
    }
    
    public double getCompCos1()
    {
        return compC1;
    }
    
    public double getCompCos2()
    {
        return compC2;
    }
    
    public double getCompCos3()
    {
        return compC3;
    }
    
    public double getCompCos4()
    {
        return compC4;
    }
    
}
