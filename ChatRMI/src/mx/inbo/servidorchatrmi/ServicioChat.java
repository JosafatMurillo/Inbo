/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.inbo.servidorchatrmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import mx.inbo.clientechatrmi.Cliente;

/**
 *
 * @author Rainbow Dash
 */
public interface ServicioChat extends Remote {
    
    void baja (Cliente cliente) throws RemoteException;
    
    void alta (Cliente cliente) throws RemoteException;
    
    void envio (Cliente cliente, String nombreUsuario, String mensaje) throws RemoteException;
}
