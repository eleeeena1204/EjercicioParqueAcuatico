/*
***LISTADO***
* Se crean todos los ArrayList de las colas tanto de espera como de los usuarios
* de dentro.
*/
package Usuarios;

/*En los import, a parte de las librerías que vamos a necesitar para la ejecución,
importamos también las clases que vamos a tocar que tenemos en otro package.*/
import java.util.ArrayList;
import javax.swing.JTextField;

public class Listado 
{
    /*Declaramos los atributos privados que nos hacen falta para controlar el flujo.*/
    private ArrayList<Usuario> listadoUsuarios;
    private final JTextField textField;
    
    /*En el constructor vamos a necesitar el JTextField donde se va a mostrar el listado.*/
    public Listado (JTextField p_textField) 
    {
        textField = p_textField;
        listadoUsuarios = new ArrayList<>();
    }

    /*Se añaden los métodos getter y setter que necesitamos para poder actualizar
    o usar la información desde otras clases o desde la nuestra misma.*/
    public ArrayList<Usuario> getListadoUsuarios() 
    {
        return listadoUsuarios;
    }
    public void setListadoUsuarios(ArrayList<Usuario> listadoUsuarios) 
    {
        this.listadoUsuarios = listadoUsuarios;
    }
    
    /*Añade en el listado al usuario y lo muestra por pantalla.*/
    public synchronized void meter(Usuario u) 
    {
        listadoUsuarios.add(u);
        String contenido = "";
        for (Usuario listadoUsuario : listadoUsuarios) 
        {
            contenido = contenido + listadoUsuario.getIdCompleto() + ", ";
        }
        textField.setText(contenido);
    }
    /*Elimina del listado al usuario y muestra como queda.*/
    public synchronized void sacar(Usuario u) 
    {
        listadoUsuarios.remove(u);
        String contenido = "";
        for (Usuario listadoUsuario : listadoUsuarios) 
        {
            contenido = contenido + listadoUsuario.getIdCompleto() + ", ";
        }
        textField.setText(contenido);
    }
    /*Añade en el listado al usuario pero sin mostrarlo por pantalla.*/
    public synchronized void meterSinMostrar(Usuario u) 
    {
        listadoUsuarios.add(u);
    }
    /*Elimina del listado al usuario sin mostrar como queda por pantalla.*/
    public synchronized void sacarSinMostrar(Usuario u) 
    {
        listadoUsuarios.remove(u);
    }
    /*Se ve el usuario que se solicita.*/
    public Usuario seleccionarUsuario() 
    {
        if (!listadoUsuarios.isEmpty())
        {
            Usuario u = listadoUsuarios.get(0);
            return u;
        } 
        else
        {
            return null;
        }
    }
}
