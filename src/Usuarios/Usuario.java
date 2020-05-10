/*
***USUARIO***
* Para llevar un control de los atributos y funciones que desempeña cada usuario
* dentro del parque.
*/
package Usuarios;

/*En los import, a parte de las librerías que vamos a necesitar para la ejecución,
importamos también las clases que vamos a tocar que tenemos en otro package.*/
import InterfazGrafica.Paso;
import ParqueAcuatico.ParqueAcuatico;
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Usuario extends Thread
{
    /*Declaramos los atributos privados que nos hacen falta para controlar el flujo.*/
    private int identificador, edad, idAcompañante, edadNiñoAcompañado, contadorActividades, numActividades;
    private String idCompleto, actividadActual;
    private final ParqueAcuatico pa;
    private boolean esAcompañante;
    private Paso paso;
    private final CyclicBarrier parejaAcompañante;
    private Usuario acompañante;
    ArrayList<String> actividades;
    
    /*En el constructor vamos a necesitar el identificador, la barrera para el acompañante, 
    el parque acuático en sí y la clase paso para el permiso de ejecución.*/
    public Usuario (int p_identificador, CyclicBarrier p_barrera, ParqueAcuatico p_pa, Paso p_paso) 
    {
        identificador = p_identificador;
        parejaAcompañante = p_barrera;
        paso = p_paso;
        pa = p_pa;
        edadNiñoAcompañado = 0;
        idAcompañante = 0;
        contadorActividades = 0;
        idCompleto = "";
        actividades = new ArrayList<>();
        actividades.add ("PiscinaOlas");
        actividades.add ("PiscinaNiños");
        actividades.add ("Tumbonas");
        actividades.add ("PiscinaGrande");
        actividades.add ("Toboganes");
    }

    /*Se añaden los métodos getter y setter que necesitamos para poder actualizar
    o usar la información desde otras clases o desde la nuestra misma.*/
    public int getNumActividades() 
    {
        return numActividades;
    }
    public void setNumActividades(int numActividades) 
    {
        this.numActividades = numActividades;
    }
    public String getActividadActual()
    {
        return actividadActual;
    }
    public void setActividadActual(String actividadActual) 
    {
        this.actividadActual = actividadActual;
    }
    public Usuario getAcompañante() 
    {
        return acompañante;
    }
    public void setAcompañante(Usuario acompañante) 
    {
        this.acompañante = acompañante;
    }
    public int getEdad() 
    {
        return edad;
    }
    public void setEdad(int edad) 
    {
        this.edad = edad;
    }
    public String getIdCompleto() 
    {
        return idCompleto;
    }
    public void setIdCompleto(String idCompleto) 
    {
        this.idCompleto = idCompleto;
    }
    public int getIdentificador() 
    {
        return identificador;
    }
    public void setIdentificador(int identificador) 
    {
        this.identificador = identificador;
    }
    public int getIdAcompañante() 
    {
        return idAcompañante;
    }
    public void setIdAcompañante(int idAcompañante) 
    {
        this.idAcompañante = idAcompañante;
    }
    public int getEdadNiñoAcompañado() 
    {
        return edadNiñoAcompañado;
    }
    public void setEdadNiñoAcompañado(int edadNiñoAcompañado) 
    {
        this.edadNiñoAcompañado = edadNiñoAcompañado;
    }
    public boolean isEsAcompañante() 
    {
        return esAcompañante;
    }
    public void setEsAcompañante(boolean esAcompañante) 
    {
        this.esAcompañante = esAcompañante;
    }
    public Paso getPaso() 
    {
        return paso;
    }
    public void setPaso(Paso paso) 
    {
        this.paso = paso;
    }
    public int getContadorActividades() 
    {
        return contadorActividades;
    }
    public void setContadorActividades(int contadorActividades) 
    {
        this.contadorActividades = contadorActividades;
    }
    
    /*Dependiendo de la clase de usuario que seas, ejecutas tus acciones de una forma
    u otra y en un orden, ya que las actividades son aleatorias.*/
    @Override
    public void run() 
    {
        if (!esAcompañante && edad > 10) 
        {//Niño sin acompañante o adulto
            numActividades = ((int) ((Math.random() * 10) + 5)); 
            paso.mirar();
            pa.entrar(this);
            paso.mirar();
            pa.getVestuario().entrar(this);
            paso.mirar();
            try 
            {
                Thread.sleep(3000);
            } 
            catch (InterruptedException e) 
            {
                System.out.println("Error en la acción del Vestuario.");
            }
            paso.mirar();
            pa.getVestuario().salir(this); 
            paso.mirar();
            while (contadorActividades < numActividades) 
            {
                int aleatorio = (int) ((Math.random() * actividades.size()));
                actividadActual = actividades.get(aleatorio);
                switch (actividadActual) 
                {
                    case "PiscinaNiños":
                        paso.mirar();
                        if (pa.getPiscinaNiños().entrar(this)) 
                        {
                            paso.mirar();
                            try 
                            {
                                Thread.sleep((int) (Math.random() * 2000) + 1000);
                            } 
                            catch (InterruptedException e) 
                            {
                                System.out.println("Error en la accion de la Piscina de niños.");
                            }
                            paso.mirar();
                            pa.getPiscinaNiños().salir(this);
                            paso.mirar();
                            contadorActividades++;
                        }
                        break;
                    case "PiscinaOlas":
                        paso.mirar();
                        if (pa.getPiscinaOlas().entrar(this)) 
                        {
                            paso.mirar();
                            try 
                            {
                                Thread.sleep((int) (Math.random() * 3000) + 2000);
                            } 
                            catch (InterruptedException e) 
                            {
                                System.out.println("Error en la accion de la Psicina de olas.");
                            }
                            paso.mirar();
                            pa.getPiscinaOlas().salir(this);
                            paso.mirar();
                            contadorActividades++;
                        }
                        break;
                    case "PiscinaGrande":
                        paso.mirar();
                        pa.getPiscinaGrande().entrar(this);
                        paso.mirar();
                        try 
                        {
                            Thread.sleep((int) (Math.random() * 2000) + 3000);
                        } 
                        catch (InterruptedException e) 
                        {
                            System.out.println("Error en la acción de la Piscina grande.");
                        }
                        paso.mirar();
                        pa.getPiscinaGrande().salir(this);
                        paso.mirar();
                        contadorActividades++;
                        break;
                    case "Tumbonas": 
                        paso.mirar();
                        if (pa.getTumbonas().entrar(this)) 
                        {
                            paso.mirar();
                            try 
                            {
                                Thread.sleep((int) (Math.random() * 2000) + 2000);
                            } 
                            catch (InterruptedException e) 
                            {
                                System.out.println("Error en la acción de las Tumbonas.");
                            }
                            paso.mirar();
                            pa.getTumbonas().salir(this);
                            paso.mirar();
                            contadorActividades++;
                        }
                        break;
                    case "Toboganes":
                        paso.mirar();
                        if (pa.getToboganes().entrar(this)) 
                        {
                            paso.mirar();
                            try 
                            {
                                Thread.sleep((int) (Math.random() * 1000) + 2000);
                            } 
                            catch (InterruptedException e) 
                            {
                                System.out.println("Error en la acción de los Toboganes.");
                            }
                            paso.mirar();
                            pa.getToboganes().caerEnPiscinaGrande(this);
                            paso.mirar();
                            contadorActividades++;
                        }
                        break;
                }
            }
            paso.mirar();
            pa.getVestuario().entrar(this);
            paso.mirar();
            try 
            {
                Thread.sleep(3000);
            } 
            catch (InterruptedException e) 
            {
                System.out.println("Error en la accion del Vestuario.");
            }
            paso.mirar();
            pa.getVestuario().salir(this);
            paso.mirar();
        } 
        else if (edad <= 10) 
        {//Niño con acompañante
            numActividades = ((int) ((Math.random() * 10) + 5));
            acompañante.setNumActividades(numActividades);
            try 
            {
                paso.mirar();
                parejaAcompañante.await(); //Espera a su acompañante
                pa.entrar(this);
                paso.mirar();
                parejaAcompañante.await();
                pa.getVestuario().entrar(this);
                paso.mirar();
                try 
                {
                    Thread.sleep(3000);
                } 
                catch (InterruptedException e) 
                {
                    System.out.println("Error en la accion del Vestuario.");
                }
                paso.mirar();
                parejaAcompañante.await();
                paso.mirar();
                pa.getVestuario().salir(this);
                parejaAcompañante.await();
                paso.mirar();
                while (contadorActividades < numActividades) 
                {  
                    int aleatorio = (int) (Math.random()* actividades.size());
                    actividadActual = actividades.get(aleatorio);
                    acompañante.setActividadActual(actividadActual);
                    parejaAcompañante.await();  
                    parejaAcompañante.await();
                    switch (actividadActual) 
                    {
                        case "PiscinaNiños":
                            paso.mirar();
                            parejaAcompañante.await();
                            if (pa.getPiscinaNiños().entrar(this)) 
                            {
                                paso.mirar();
                                parejaAcompañante.await();
                                try 
                                {
                                    Thread.sleep((int) (Math.random() * 2000) + 1000);
                                } 
                                catch (InterruptedException e) 
                                {
                                    System.out.println("Error en la accion de la Piscina de niños.");
                                }
                                paso.mirar();
                                parejaAcompañante.await();
                                pa.getPiscinaNiños().salir(this);
                                paso.mirar();
                                parejaAcompañante.await();
                                paso.mirar();
                                contadorActividades++;
                            }
                            break;
                        case "PiscinaOlas":
                            paso.mirar();
                            parejaAcompañante.await();
                            if (pa.getPiscinaOlas().entrar(this)) 
                            {
                                paso.mirar();
                                parejaAcompañante.await();
                                try 
                                {
                                    Thread.sleep((int) (Math.random() * 2000) + 3000);
                                } 
                                catch (InterruptedException e) 
                                {
                                    System.out.println("Error en la accion de la Psicina de olas.");
                                }
                                parejaAcompañante.await();
                                paso.mirar();
                                pa.getPiscinaOlas().salir(this);
                                parejaAcompañante.await();
                                paso.mirar();
                                contadorActividades++;
                            }
                            break;
                        case "PiscinaGrande":
                            paso.mirar();
                            parejaAcompañante.await();
                            pa.getPiscinaGrande().entrar(this);
                            try 
                            {
                                try 
                                {
                                    Thread.sleep((int) (Math.random() * 3000) + 2000);
                                } 
                                catch (InterruptedException e) 
                                {
                                    System.out.println("Error en la acción de la Piscina grande.");
                                }
                                parejaAcompañante.await();
                                paso.mirar();
                                pa.getPiscinaGrande().salir(this);
                                parejaAcompañante.await();
                                paso.mirar();
                            } 
                            catch (InterruptedException e) 
                            {
                                System.out.println ("Error en el Ususario al ir a la Piscina Grande.");
                            }
                            contadorActividades++;
                            break;
                        case "Tumbonas":
                            paso.mirar();
                            parejaAcompañante.await();
                            if (pa.getTumbonas().entrar(this)) 
                            {
                                paso.mirar();
                                parejaAcompañante.await();
                                try 
                                {
                                    Thread.sleep((int) (Math.random() * 2000) + 2000);
                                } 
                                catch (InterruptedException e) 
                                {
                                    System.out.println("Error en la acción de las Tumbonas.");
                                }
                                parejaAcompañante.await();
                                paso.mirar();
                                pa.getTumbonas().salir(this);
                                parejaAcompañante.await();
                                paso.mirar();
                                contadorActividades++;
                            }
                            break;
                        case "Toboganes":
                            paso.mirar();
                            parejaAcompañante.await();
                            contadorActividades++;
                            break;
                    }
                }
                paso.mirar();
                parejaAcompañante.await();
                pa.getVestuario().entrar(this);
                paso.mirar();
                try 
                {
                    Thread.sleep(3000);
                } 
                catch (InterruptedException e) 
                {
                    System.out.println("Error en la accion del Vestuario.");
                }
                paso.mirar();
                parejaAcompañante.await();
                paso.mirar();
                pa.getVestuario().salir(this);
                paso.mirar();
            } 
            catch (BrokenBarrierException | InterruptedException ex) 
            {
                Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        else if (esAcompañante) 
        {//Acompañante
            try 
            {
                numActividades = this.getAcompañante().getNumActividades(); //Determina el numero de actividades del acompañante cogiendo la del niño
                paso.mirar();
                parejaAcompañante.await(); 
                pa.entrar(this);
                paso.mirar();
                parejaAcompañante.await();
                pa.getVestuario().entrar(this);
                paso.mirar();
                parejaAcompañante.await();
                paso.mirar();
                pa.getVestuario().salir(this);
                paso.mirar();
                while (contadorActividades < numActividades) 
                {
                    parejaAcompañante.await();
                    actividadActual = acompañante.getActividadActual(); //Coge la actividad que vaya a realizar el niño
                    parejaAcompañante.await();
                    switch (actividadActual) 
                    {
                        case "PiscinaNiños":
                            paso.mirar();
                            parejaAcompañante.await();
                            if (pa.getPiscinaNiños().entrar(this)) 
                            {
                                paso.mirar();
                                parejaAcompañante.await();      
                                paso.mirar();
                                parejaAcompañante.await();      
                                paso.mirar();
                                pa.getPiscinaNiños().salir(this);
                                paso.mirar();
                                parejaAcompañante.await();
                                paso.mirar();
                                contadorActividades++;
                            }
                            break;
                        case "PiscinaOlas": 
                            paso.mirar();
                            parejaAcompañante.await();
                            if (pa.getPiscinaOlas().entrar(this)) 
                            {
                                paso.mirar();
                                parejaAcompañante.await();         
                                paso.mirar();
                                parejaAcompañante.await();
                                paso.mirar();                       
                                pa.getPiscinaOlas().salir(this);
                                parejaAcompañante.await();
                                paso.mirar();
                                contadorActividades++;
                            }
                            break;
                        case "PiscinaGrande":        
                            paso.mirar();
                            parejaAcompañante.await();
                            pa.getPiscinaGrande().entrar(this);
                            paso.mirar();
                            parejaAcompañante.await();      
                            paso.mirar();
                            pa.getPiscinaGrande().salir(this);
                            parejaAcompañante.await();
                            paso.mirar();
                            contadorActividades++;
                            break;
                        case "Tumbonas":                       
                            paso.mirar();
                            parejaAcompañante.await();
                            if (pa.getTumbonas().entrar(this)) 
                            {
                                paso.mirar();
                                parejaAcompañante.await();     
                                paso.mirar();
                                parejaAcompañante.await();
                                paso.mirar();
                                pa.getTumbonas().salir(this);
                                parejaAcompañante.await();
                                paso.mirar();
                                contadorActividades++;
                            }
                            break;
                        case "Toboganes": 
                            paso.mirar();
                            parejaAcompañante.await();
                            contadorActividades++;
                            break;
                    }
                }
                paso.mirar();
                parejaAcompañante.await();
                pa.getVestuario().entrar(this);
                paso.mirar();
                parejaAcompañante.await();  
                paso.mirar();
                pa.getVestuario().salir(this);
                paso.mirar();
            } 
            catch (InterruptedException | BrokenBarrierException ex) 
            {
                Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        pa.salir(this);
    }
}
