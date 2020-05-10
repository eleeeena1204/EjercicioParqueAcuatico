/*
***SERVIDOR***
* Esta clase nos va a servir para intercambiar los datos que se esten pidiendo 
* desde el cliente, para ello se implementan los métodos necesarios y se abren
* los canales de entrada y salida para dicho intercambio de información.
*/
package InterfazGrafica;

/*En los import, a parte de las librerías que vamos a necesitar para la ejecución,
importamos también las clases que vamos a tocar que tenemos en otro package.*/
import ParqueAcuatico.ParqueAcuatico;
import java.io.*;
import java.net.*;
import Usuarios.Usuario;

public class Servidor extends Thread 
{
    /*Declaramos los atributos privados que nos hacen falta para controlar el flujo.*/
    private final ParqueAcuatico pa;
    private ServerSocket servidor;
    private Socket conexion;
    private DataInputStream entrada;
    private DataOutputStream salida;
    private String ubicacion, actividades, numMenores, toboganA, toboganB, toboganC, aforoActualVestuario, aforoActualPiscinaOlas, aforoActualPiscinaNiños, aforoActualTumbonas, aforoActualToboganes, aforoActualPiscinaGrande;

    /*En el constructor vamos a necesitar básicamente el parque para poder acceder
    a toda la información.*/
    public Servidor(ParqueAcuatico p_pa) 
    {
        pa = p_pa;
    }

    /*Dependiendo del botón que se aprete, se va a realizar una acción u otra para la cual
    vamos a tener sólo en la primera entrada de información, y para el resto salida.*/
    @Override
    public void run() 
    {
        String operacion;
        try 
        {
            servidor = new ServerSocket(5000); 
            while (true) 
            {
                conexion = servidor.accept();     
                entrada = new DataInputStream(conexion.getInputStream());  
                salida = new DataOutputStream(conexion.getOutputStream());
                operacion = entrada.readUTF();
                switch (operacion) 
                {
                    case "UBICACION":
                        String identificadorUsuario = entrada.readUTF();
                        if ("".equals(identificadorUsuario)) 
                        {
                            ubicacion = "No se ha introducido.";
                            actividades = "";
                        }
                        else 
                        {
                            if (!buscarUsuario(identificadorUsuario))
                            {
                                ubicacion = "No se ha encontrado.";
                                actividades = "";
                            }
                        }
                        salida.writeUTF(ubicacion);
                        salida.writeUTF(actividades);
                        break;
                    case "NUMMENORES":
                        calcularNumMenores(); 
                        salida.writeUTF(numMenores);
                        break;
                    case "TOBOGANES":
                        usuarioEnToboganes(); 
                        salida.writeUTF(toboganA);
                        salida.writeUTF(toboganB);
                        salida.writeUTF(toboganC);
                        break;
                    case "AFORO":
                        aforos(); 
                        salida.writeUTF(aforoActualVestuario);
                        salida.writeUTF(aforoActualPiscinaOlas);
                        salida.writeUTF(aforoActualPiscinaNiños);
                        salida.writeUTF(aforoActualTumbonas);
                        salida.writeUTF(aforoActualToboganes);
                        salida.writeUTF(aforoActualPiscinaGrande);
                        break;
                }
                salida.close();
                entrada.close();
                conexion.close();
            }
        } 
        catch (IOException e) 
        {
            System.out.println("Error en la clase Servidor.");
        }
    }
    
    /*Este método se va a encargar de buscar el id que le pasemos entre todas las colas o 
    si está siendo atendido por un monitor y si no lo encuentra se imprimirá un mensaje.*/
    private boolean buscarUsuario(String id) 
    {
        //DENTRO VESTUARIOS
        for (int i = 0; i < pa.getVestuario().getDentro().size(); i++) 
        {
            Usuario u = pa.getVestuario().getDentro().get(i);
            if (u.toString().equals(id)) 
            {
                ubicacion = "Dentro vestuario.";
                actividades = Integer.toString(u.getContadorActividades());
                return true;
            }
        }
        //COLA ESPERA VESTUARIOS
        for (int i = 0; i < pa.getVestuario().getColaEspera().size(); i++) 
        {
            Usuario u = pa.getVestuario().getColaEspera().get(i);
            if (u.getIdCompleto().equals(id)) 
            {
                ubicacion = "Cola vestuario.";
                actividades = Integer.toString(u.getContadorActividades());
                return true;
            }
        }
        //MONITOR VESTUARIOS
        if (pa.getVestuario().getUsuarioAtendido() != null)
        {
            Usuario u = pa.getVestuario().getUsuarioAtendido();
            if (u.getIdCompleto().equals(id))
            {
                ubicacion = "Monitor vestuario.";
                actividades = Integer.toString (u.getContadorActividades());
                return true;
            }
        }
        //COLA ESPERA PISCINA NIÑOS
        for (int i = 0; i < pa.getPiscinaNiños().getColaEspera().size(); i++) 
        {
            Usuario u = pa.getPiscinaNiños().getColaEspera().get(i);
            if (u.getIdCompleto().equals(id)) 
            {
                ubicacion = "Cola piscina de niños.";
                actividades = Integer.toString(u.getContadorActividades());
                return true;
            }
        }
        //PISCINA NIÑOS
        for (int i = 0; i < pa.getPiscinaNiños().getDentro().size(); i++) 
        {
            Usuario u = pa.getPiscinaNiños().getDentro().get(i);
            if (u.getIdCompleto().equals(id)) 
            {
                ubicacion = "Dentro piscina de niños.";
                actividades = Integer.toString(u.getContadorActividades());
                return true;
            }
        }
        //COLA DE ESPERA ADULTOS DE LA PISCINA NIÑOS
        for (int i = 0; i < pa.getPiscinaNiños().getDentroEspera().size(); i++) 
        {
            Usuario u = pa.getPiscinaNiños().getDentroEspera().get(i);
            if (u.getIdCompleto().equals(id)) 
            {
                ubicacion = "Adultos piscina de niños.";
                actividades = Integer.toString(u.getContadorActividades());
                return true;
            }
        }
        //MONITOR PISCINA NIÑOS
        if (pa.getPiscinaNiños().getUsuarioAtendido() != null)
        {
            Usuario u = pa.getPiscinaNiños().getUsuarioAtendido();
            if (u.getIdCompleto().equals(id))
            {
                ubicacion = "Monitor piscina de niños.";
                actividades = Integer.toString (u.getContadorActividades());
                return true;
            }
        }
        //COLA ESPERA PISCINA GRANDE
        for (int i = 0; i < pa.getPiscinaGrande().getColaEspera().size(); i++) 
        {
            Usuario u = pa.getPiscinaGrande().getColaEspera().get(i);
            if (u.getIdCompleto().equals(id)) 
            {
                ubicacion = "Cola piscina grande.";
                actividades = Integer.toString(u.getContadorActividades());
                return true;
            }
        }
        //PISCINA GRANDE
        for (int i = 0; i < pa.getPiscinaGrande().getDentro().size(); i++) 
        {
            Usuario u = pa.getPiscinaGrande().getDentro().get(i);
            if (u.getIdCompleto().equals(id)) 
            {
                ubicacion = "Dentro piscina grande.";
                actividades = Integer.toString(u.getContadorActividades());
                return true;
            }
        }
        //MONITOR PISCINA GRANDE
        if (pa.getPiscinaGrande().getUsuarioAtendido() != null)
        {
            Usuario u = pa.getPiscinaGrande().getUsuarioAtendido();
            if (u.getIdCompleto().equals(id))
            {
                ubicacion = "Monitor piscina grande.";
                actividades = Integer.toString (u.getContadorActividades());
                return true;
            }
        }
        //COLA PISCINA OLAS 
        for (int i = 0; i < pa.getPiscinaOlas().getColaEspera().size(); i++) 
        {
            Usuario u = pa.getPiscinaOlas().getColaEspera().get(i);
            if (u.getIdCompleto().equals(id)) 
            {
                ubicacion = "Cola piscina de olas.";
                actividades = Integer.toString(u.getContadorActividades());
                return true;
            }
        }
        //PISCINA OLAS 
        for (int i = 0; i < pa.getPiscinaOlas().getDentro().size(); i++) 
        {
            Usuario u = pa.getPiscinaOlas().getDentro().get(i);
            if (u.getIdCompleto().equals(id)) 
            {
                ubicacion = "Dentro piscina de olas.";
                actividades = Integer.toString(u.getContadorActividades());
                return true;
            }
        }
        //MONITOR PISCINA PISCINA OLAS
        if (pa.getPiscinaOlas().getUsuarioAtendido() != null)
        {
            Usuario u = pa.getPiscinaOlas().getUsuarioAtendido();
            if (u.getIdCompleto().equals(id))
            {
                ubicacion = "Monitor piscina olas.";
                actividades = Integer.toString (u.getContadorActividades());
                return true;
            }
        }
        //COLA TUMBONAS
        for (int i = 0; i < pa.getTumbonas().getColaEspera().size(); i++) 
        {
            Usuario u = pa.getTumbonas().getColaEspera().get(i);
            if (u.getIdCompleto().equals(id)) 
            {
                ubicacion = "Cola tumbonas.";
                actividades = Integer.toString(u.getContadorActividades());
                return true;
            }
        }
        //TUMBONAS
        for (int i = 0; i < pa.getTumbonas().getDentro().size(); i++) 
        {
            Usuario u = pa.getTumbonas().getDentro().get(i);
            if (u.getIdCompleto().equals(id)) 
            {
                ubicacion = "Dentro tumbonas.";
                actividades = Integer.toString(u.getContadorActividades());
                return true;
            }
        }
        //MONITOR TUMBONAS
        if (pa.getTumbonas().getUsuarioAtendido() != null)
        {
            Usuario u = pa.getTumbonas().getUsuarioAtendido();
            if (u.getIdCompleto().equals(id))
            {
                ubicacion = "Monitor tumbonas.";
                actividades = Integer.toString (u.getContadorActividades());
                return true;
            }
        }
        //MONITOR TOBOGAN A
        if (pa.getToboganes().getUsuarioAtendidoA() != null)
        {
            Usuario u = pa.getToboganes().getUsuarioAtendidoA();
            if (u.getIdCompleto().equals(id))
            {
                ubicacion = "Monitor tobogán A.";
                actividades = Integer.toString (u.getContadorActividades());
                return true;
            }
        }
        //COLA TOBOGAN A
        for (int i = 0; i < pa.getToboganes().getColaA().size(); i++) 
        {
            Usuario u = pa.getToboganes().getColaA().get(i);
            if (u.getIdCompleto().equals(id)) 
            {
                ubicacion = "Cola Tobogan A";
                actividades = Integer.toString(u.getContadorActividades());
                return true;
            }
        }
        //MONITOR TOBOGAN B
        if (pa.getToboganes().getUsuarioAtendidoB() != null)
        {
            Usuario u = pa.getToboganes().getUsuarioAtendidoB();
            if (u.getIdCompleto().equals(id))
            {
                ubicacion = "Monitor tobogán B.";
                actividades = Integer.toString (u.getContadorActividades());
                return true;
            }
        }
        //COLA TOBOGAN B
        for (int i = 0; i < pa.getToboganes().getColaB().size(); i++) {
            Usuario usuario = pa.getToboganes().getColaB().get(i);
            if (usuario.getIdCompleto().equals(id)) {
                ubicacion = "Cola Tobogan B";
                actividades = Integer.toString(usuario.getContadorActividades());
                return true;
            }
        }
        //MONITOR TOBOGAN C
        if (pa.getToboganes().getUsuarioAtendidoC() != null)
        {
            Usuario u = pa.getToboganes().getUsuarioAtendidoC();
            if (u.getIdCompleto().equals(id))
            {
                ubicacion = "Monitor tobogán C.";
                actividades = Integer.toString (u.getContadorActividades());
                return true;
            }
        }
        //COLA TOBOGAN C
        for (int i = 0; i < pa.getToboganes().getColaC().size(); i++) {
            Usuario usuario = pa.getToboganes().getColaC().get(i);
            if (usuario.getIdCompleto().equals(id)) {
                ubicacion = "Cola Tobogan C";
                actividades = Integer.toString(usuario.getContadorActividades());
                return true;
            }
        }
        //COLA TOBOGANES 
        for (int i = 0; i < pa.getToboganes().getColaEspera().size(); i++) 
        {
            Usuario u = pa.getToboganes().getColaEspera().get(i);
            if (u.getIdCompleto().equals(id)) 
            {
                ubicacion = "Cola toboganes.";
                actividades = Integer.toString(u.getContadorActividades());
                return true;
            }
        }
        //TOBOGAN A
        if (pa.getToboganes().getEnToboganA() != null)
        {
            Usuario u = pa.getToboganes().getEnToboganA();
            if (u.getIdCompleto().equals(id)) {
                ubicacion = "Tobogán A.";
                actividades = Integer.toString(u.getContadorActividades());
                return true;
            }
        }
        //TOBOGAN B
        if (pa.getToboganes().getEnToboganB() != null)
        {
            Usuario u = pa.getToboganes().getEnToboganB();
            if (u.getIdCompleto().equals(id)) {
                ubicacion = "Tobogán B.";
                actividades = Integer.toString(u.getContadorActividades());
                return true;
            }
        }
        //TOBOGAN C
        if (pa.getToboganes().getEnToboganC() != null)
        {
            Usuario u = pa.getToboganes().getEnToboganC();
            if (u.getIdCompleto().equals(id)) {
                ubicacion = "Tobogán C.";
                actividades = Integer.toString(u.getContadorActividades());
                return true;
            }
        }
        return false; //No se ha encontrado
    }
    /*El calculo de menores simplemente extrae del parque la variable en la que los
    estábamos contando y la devuelve.*/
    private void calcularNumMenores() 
    {
        int menores = pa.getNumMenores();
        numMenores = Integer.toString(menores);
    }
    /*Se muestra por pantalla en cada momento si hay o no un usuario usando los toboganes.*/
    private void usuarioEnToboganes() 
    {
        if (pa.getToboganes().getEnToboganA() == null)
        {
            toboganA = "Estoy vacío.";
        }
        else
        {
            toboganA = pa.getToboganes().getEnToboganA().getIdCompleto();
        }
        if (pa.getToboganes().getEnToboganB() == null)
        {
            toboganB = "Estoy vacío.";
        }
        else
        {
            toboganB = pa.getToboganes().getEnToboganB().getIdCompleto();
        }
        if (pa.getToboganes().getEnToboganC() == null)
        {
            toboganC = "Estoy vacío.";
        }
        else
        {
            toboganC = pa.getToboganes().getEnToboganC().getIdCompleto();
        }
    }
    /*Se suma para cada actividad el aforo total, sumando las colas de entrada, 
    los usuarios que estén ya dentro y en caso de que el monitor también esté 
    atendiendo a alguien*/
    private void aforos() 
    {
        //Aforo en todo el vestuario
        int vestuario = pa.getVestuario().getColaEspera().size() + pa.getVestuario().getDentro().size();
        if (pa.getVestuario().getUsuarioAtendido() != null) 
        {
            vestuario++;
        }
        aforoActualVestuario = Integer.toString(vestuario);
        //Aforo en tumbonas
        int tumbonas = pa.getTumbonas().getColaEspera().size() + pa.getTumbonas().getDentro().size();
        if (pa.getTumbonas().getUsuarioAtendido() != null) 
        {
            tumbonas++;
        }
        aforoActualTumbonas = Integer.toString(tumbonas);
        //Aforo en toda la piscina de olas
        int piscinaOlas = pa.getPiscinaOlas().getColaEspera().size() + pa.getPiscinaOlas().getDentro().size();
        if (pa.getPiscinaOlas().getUsuarioAtendido() != null) 
        {
            piscinaOlas++;
        }
        aforoActualPiscinaOlas = Integer.toString(piscinaOlas);
        //Aforo en toda la piscina de niños
        int piscinaNiños = pa.getPiscinaNiños().getColaEspera().size() + pa.getPiscinaNiños().getDentro().size() + pa.getPiscinaNiños().getDentroEspera().size();
        if (pa.getPiscinaNiños().getUsuarioAtendido() != null) 
        {
            piscinaNiños++;
        }
        aforoActualPiscinaNiños = Integer.toString(piscinaNiños);
        //Aforo en toda la piscina grande
        int piscinaGrande = pa.getPiscinaGrande().getColaEspera().size() + pa.getPiscinaGrande().getDentro().size();
        if (pa.getPiscinaGrande().getUsuarioAtendido() != null) 
        {
            piscinaGrande++;
        }
        aforoActualPiscinaGrande = Integer.toString(piscinaGrande);
        //Aforo en todos los toboganes
        int numToboganes = pa.getToboganes().getColaEspera().size();
        if (pa.getToboganes().getEnToboganA() != null) 
        {
            numToboganes++;
        }
        if (pa.getToboganes().getUsuarioAtendidoA() != null) 
        {
            numToboganes++;
        }
        if (pa.getToboganes().getEnToboganB() != null) 
        {
            numToboganes++;
        }
        if (pa.getToboganes().getUsuarioAtendidoB() != null) 
        {
            numToboganes++;
        }
        if (pa.getToboganes().getEnToboganC() != null) 
        {
            numToboganes++;
        }
        if (pa.getToboganes().getUsuarioAtendidoC() != null) 
        {
            numToboganes++;
        }
        aforoActualToboganes = Integer.toString(numToboganes);
    }
}
