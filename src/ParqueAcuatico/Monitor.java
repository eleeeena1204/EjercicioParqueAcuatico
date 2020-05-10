/*
***MONITOR***
* Cada monitor tiene una funcionalidad en cada actividad a la que se le asigna
* dependiendo de su id.
*/
package ParqueAcuatico;

/*En los import, a parte de las librerías que vamos a necesitar para la ejecución,
importamos también las clases que vamos a tocar que tenemos en otro package.*/
import InterfazGrafica.Paso;
import Usuarios.Usuario;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;

public class Monitor extends Thread
{    
    /*Declaramos los atributos privados que nos hacen falta para controlar el flujo.*/
    private final Paso paso;
    private final int identificador;
    private final ParqueAcuatico pa;
    JTextField usuarioAtendido;
    
    /*En el constructor vamos a necesitar el identificador, el parque acuático 
    en sí, la clase paso para el permiso de ejecución, y el JTextField para actualizar
    los cambios del usuario que está siendo atendido.*/
    public Monitor (int p_identificador, ParqueAcuatico p_pa, Paso p_paso, JTextField p_usuarioAtendido)
    {
        identificador = p_identificador;
        paso = p_paso;
        usuarioAtendido = p_usuarioAtendido;
        pa = p_pa;
    }
    
    /*Dependiendo de que monitor seas tienes una funcionalidad u otra para llevar
    a cabo el control de tu actividad.*/
    @Override
    public void run()
    {
        boolean seguir = true;
        Usuario u;
        switch (identificador)
        {
            //Monitor del vestuario
            case 1: 
                paso.mirar();
                while (seguir)
                {
                    paso.mirar();
                    if (!pa.getVestuario().getColaEspera().isEmpty())
                    {
                        paso.mirar();
                        u = pa.getVestuario().siguienteUsuario();
                        paso.mirar();
                        usuarioAtendido.setText(u.getIdCompleto());
                        paso.mirar();
                        try 
                        {
                            Thread.sleep(1000);
                        } 
                        catch (InterruptedException e) 
                        {
                            System.out.println("Error en el Monitor 1.");
                        }
                        paso.mirar();
                        usuarioAtendido.setText(" ");
                        paso.mirar();
                        pa.getVestuario().accionMonitor(u);
                        paso.mirar();
                    }
                }
                break;
            //Monitor de la piscina de niños
            case 2: 
                paso.mirar();
                while (seguir)
                {
                    paso.mirar();
                    if (!pa.getPiscinaNiños().getColaEspera().isEmpty())
                    {
                        paso.mirar();
                        u = pa.getPiscinaNiños().siguienteUsuario();
                        paso.mirar();
                        usuarioAtendido.setText(u.getIdCompleto());
                        paso.mirar();
                        try 
                        {
                            Thread.sleep((int) (Math.random() * 500) + 1000);
                        } 
                        catch (InterruptedException e) 
                        {
                            System.out.println("Error en el Monitor 2.");
                        }
                        paso.mirar();
                        usuarioAtendido.setText(" ");
                        paso.mirar();
                        pa.getPiscinaNiños().accionMonitor(u);
                        paso.mirar();
                    }
                }
                break;
            //Monitor de la piscina grande
            case 3: 
                paso.mirar();
                while (seguir) 
                {
                    paso.mirar();
                    if (!pa.getPiscinaGrande().getColaEspera().isEmpty())
                    {
                        paso.mirar();
                        u = pa.getPiscinaGrande().siguienteUsuario();
                        paso.mirar();
                        usuarioAtendido.setText(u.getIdCompleto());
                        paso.mirar();
                        try 
                        {
                            Thread.sleep(500);
                        } 
                        catch (InterruptedException ex) 
                        {
                            Logger.getLogger(Monitor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        paso.mirar();
                        if (pa.getPiscinaGrande().getDentro().size() == pa.getPiscinaGrande().getAforo())
                        {
                            paso.mirar();
                            try 
                            {
                                Thread.sleep ((int) (Math.random() * 500) + 1000);
                            } 
                            catch (InterruptedException ex) 
                            {
                                System.out.println("Error en el Monitor 3.");
                            }
                            paso.mirar();
                            usuarioAtendido.setText(" ");
                            paso.mirar();
                            pa.getPiscinaGrande().accionMonitorExpulsar();
                            paso.mirar();
                        }
                        else
                        {
                            paso.mirar();
                            usuarioAtendido.setText(" ");
                            paso.mirar();
                            pa.getPiscinaGrande().accionMonitor();
                            paso.mirar();
                        }
                    }
                }
                break;
            //Monitor de la piscina de olas
            case 4: 
                paso.mirar();
                while (seguir)
                {
                    paso.mirar();
                    if (!pa.getPiscinaOlas().getColaEspera().isEmpty())
                    {
                        paso.mirar();
                        u = pa.getPiscinaOlas().siguienteUsuario();
                        paso.mirar();
                        usuarioAtendido.setText(u.getIdCompleto());
                        paso.mirar();
                        try 
                        {
                            Thread.sleep(1000);
                        } 
                        catch (InterruptedException e) 
                        {
                            System.out.println("Error en el Monitor 3.");
                        }
                        paso.mirar();
                        usuarioAtendido.setText(" ");
                        paso.mirar();
                        pa.getPiscinaOlas().accionMonitor(u);
                        paso.mirar();
                    }
                }
                break;
            //Monitor de las tumbonas
            case 5: 
                paso.mirar();
                while (seguir)
                {
                    paso.mirar();
                    if (!pa.getTumbonas().getColaEspera().isEmpty())
                    {
                        paso.mirar();
                        u = pa.getTumbonas().siguienteUsuario();
                        paso.mirar();
                        usuarioAtendido.setText(u.getIdCompleto());
                        paso.mirar();
                        try 
                        {
                            Thread.sleep((int) (Math.random() * 400) + 500);
                        } 
                        catch (InterruptedException e) 
                        {
                            System.out.println("Error en el Monitor 5.");
                        }
                        paso.mirar();
                        usuarioAtendido.setText(" ");
                        paso.mirar();
                        pa.getTumbonas().accionMonitor(u);
                        paso.mirar();
                    }
                }
                break;
            //Monitor del tobogán A
            case 6: 
                paso.mirar();
                while (seguir)
                {
                    paso.mirar();
                    if (!pa.getToboganes().getColaA().isEmpty())
                    {
                        paso.mirar();
                        u = pa.getToboganes().siguienteUsuarioA();
                        paso.mirar();
                        usuarioAtendido.setText(u.getIdCompleto());
                        paso.mirar();
                        try 
                        {
                            Thread.sleep((int) (Math.random() * 100) + 400);
                        } 
                        catch (InterruptedException e) 
                        {
                            System.out.println("Error en el Monitor 6.");
                        }
                        paso.mirar();
                        usuarioAtendido.setText(" ");
                        paso.mirar();
                        pa.getToboganes().accionMonitorA(u);
                        paso.mirar();
                    }
                }
                break;
            //Monitor del tobogán B
            case 7: 
                paso.mirar();
                while (seguir)
                {
                    paso.mirar();
                    if (!pa.getToboganes().getColaB().isEmpty())
                    {
                        paso.mirar();
                        u = pa.getToboganes().siguienteUsuarioB();
                        paso.mirar();
                        usuarioAtendido.setText(u.getIdCompleto());
                        paso.mirar();
                        try 
                        {
                            Thread.sleep((int) (Math.random() * 100) + 400);
                        }
                        catch (InterruptedException e) 
                        {
                            System.out.println("Error en el Monitor 7.");
                        }
                        paso.mirar();
                        usuarioAtendido.setText(" ");
                        paso.mirar();
                        pa.getToboganes().accionMonitorB(u);
                        paso.mirar();
                    }
                }
                break;
            //Monitor del tobogán C
            case 8: 
                paso.mirar();
                while (seguir)
                {
                    paso.mirar();
                    if (!pa.getToboganes().getColaC().isEmpty())
                    {
                        paso.mirar();
                        u = pa.getToboganes().siguienteUsuarioC();
                        paso.mirar();
                        usuarioAtendido.setText(u.getIdCompleto());
                        paso.mirar();
                        try 
                        {
                            Thread.sleep((int) (Math.random() * 100) + 400);
                        } 
                        catch (InterruptedException e) 
                        {
                            System.out.println("Error en el Monitor 8.");
                        }
                        paso.mirar();
                        usuarioAtendido.setText(" ");
                        paso.mirar();
                        pa.getToboganes().accionMonitorC(u);
                        paso.mirar();
                    }
                }
                break;
        }
    }
}
