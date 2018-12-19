/*
 * Copyright 2018 adolf.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mx.inbo.servidorrmi;

import com.jfoenix.controls.JFXDialog;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;
import javafx.scene.layout.StackPane;
import mx.inbo.gui.tools.Mensaje;

/**
 *
 * @author adolf
 */
public class ServerConector {
    
    private static Operaciones stub;
    private  static JFXDialog dialog;
    
    private ServerConector(){}

    private static void initialize() {
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry("192.168.43.204");
            stub = (Operaciones) registry.lookup("servidorInbo");
        } catch (RemoteException | NotBoundException ex) {
            if(dialog != null){
                dialog.show();
            }
        }
    }

    public static Operaciones getStub() {
        if (stub == null) {
            initialize();
        }

        return stub;
    }

    public static void useErrorMessage(StackPane pane, ResourceBundle bundle) {
        Mensaje alerta = new Mensaje();
        alerta.setHeader(bundle.getString("key.alertConectionTitle"));
        alerta.setBody(bundle.getString("key.serverConectionMessage"));
        dialog = new JFXDialog(pane, alerta, JFXDialog.DialogTransition.CENTER);
    }

}
