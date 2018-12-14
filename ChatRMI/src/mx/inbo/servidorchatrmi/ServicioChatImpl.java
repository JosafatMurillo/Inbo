/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.inbo.servidorchatrmi;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;
import mx.inbo.clientechatrmi.Cliente;

/**
 *
 * @author Rainbow Dash
 */
public class ServicioChatImpl implements ServicioChat{
    
    List<Cliente> listaClientes;
    
    public static void main(String args[]) throws RemoteException{
        ServicioChatImpl server = new ServicioChatImpl();
        
        try {
            ServicioChat stub = (ServicioChat) UnicastRemoteObject.exportObject(server, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("servidorInboChat", stub);
            System.out.println("Server is listening...");
        } catch (RemoteException | AlreadyBoundException ex) {
            ex.getMessage();
        }
    }
    
    ServicioChatImpl () throws RemoteException{
        listaClientes = new LinkedList<Cliente>();
    }

    @Override
    public void baja(Cliente cliente) throws RemoteException {
        listaClientes.remove(listaClientes.indexOf(cliente));
    }

    @Override
    public void alta(Cliente cliente) throws RemoteException {
        listaClientes.add(cliente);
    }

    @Override
    public void envio(Cliente cliente, String nombreUsuario, String mensaje) throws RemoteException {
        for (Cliente clientes: listaClientes){
            if(!clientes.equals(cliente)){
                clientes.notificacion(nombreUsuario, mensaje);
            }
        }
    }
    
}
