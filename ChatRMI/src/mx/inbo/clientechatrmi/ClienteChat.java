/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.inbo.clientechatrmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rainbow Dash
 */
public class ClienteChat {
    
    private Cliente stub;
    
    public void conectarServidor() {
        try {
            Registry registro = LocateRegistry.getRegistry("192.168.1.2");
            stub = (Cliente) registro.lookup("conectarse");
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(ClienteChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void notificacion (String nombreUsuario, String mensaje) throws RemoteException{
        stub.notificacion(nombreUsuario, mensaje);
    }
}
