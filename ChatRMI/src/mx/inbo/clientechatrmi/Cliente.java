/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.inbo.clientechatrmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Rainbow Dash
 */
public interface Cliente extends Remote{
    void notificacion (String nombreUsuario, String mensaje) throws RemoteException;
}
