/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import Conexion.Conectar;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author deria
 */
public class Cls_Usuario {
    private PreparedStatement PS;
    private ResultSet RS;
    private final Conectar CN;
    private DefaultTableModel DT;
    private final String SQL_INSERT_USUARIOS = "INSERT INTO usuario (nombre, contrasena, correo) values (?,?,?)";
    private final String SQL_SELECT_USUARIOS = "SELECT *FROM usuario";
    
    public Cls_Usuario(){
        PS = null;
        CN = new Conectar();
    }
    
    public int loginUsuario(String nombre, String contrasena) {
        String SQL = "SELECT * FROM usuario WHERE nombre = ? AND contrasena = ?";
        ResultSet resultSet;
        int result = 0;
        try {
            PS = CN.getConnection().prepareStatement(SQL);
            PS.setString(1, nombre);
            PS.setString(2, contrasena);
            resultSet = PS.executeQuery();
            
            if(resultSet.next()){
                result = 1;
            } else {
                result = -1;
            }
        } catch (SQLException e) {
            System.err.println("Error al iniciar sesión "+e.getMessage());
        } finally{
            PS = null;
            RS = null;
            CN.desconectar();
        }
        return result;
    }
    
    private DefaultTableModel setTitulosUsuario(){
        DT = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
        };
        DT.addColumn("id");
        DT.addColumn("Nombre de usuario");
        DT.addColumn("Contraseña");
        DT.addColumn("Correo Electrónico");
        return DT;
    }
    
    public DefaultTableModel getDatosUsuarios(){
        try {
            setTitulosUsuario();
            PS = CN.getConnection().prepareStatement(SQL_SELECT_USUARIOS);
            RS = PS.executeQuery();
            Object[] fila = new Object[4];
            while(RS.next()){
                fila[0] = RS.getString(1);
                fila[1] = RS.getString(2);  
                //fila[2] = RS.getString(3);
                fila[2] = "****";
                fila[3] = RS.getString(4);
                DT.addRow(fila);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los datos."+e.getMessage());
        } finally{
            PS = null;
            RS = null;
            CN.desconectar();
        }
        return DT;
    }
    
    public int registrarUsuario(String nombre, String contrasena, String correo){
        int res=0;
        try {
            PS = CN.getConnection().prepareStatement(SQL_INSERT_USUARIOS);
            PS.setString(1, nombre);
            PS.setString(2, contrasena);
            PS.setString(3, correo);
            res = PS.executeUpdate();
            if(res > 0){
                JOptionPane.showMessageDialog(null, "Usuario registrado con éxito.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pudo registrar el usuario.");
            System.err.println("Error al registrar el usuario." +e.getMessage());
        } finally{
            PS = null;
            CN.desconectar();
        }
        return res;
    }
    
    public int actualizarUsuario(String nombre, String contrasena, String correo, String id){
        String SQL = "UPDATE usuario SET nombre='"+nombre+"',contrasena='"+contrasena+"',correo='"+correo+"' WHERE usuario_id='"+id+"'";
        int res=0;
        try {
            PS = CN.getConnection().prepareStatement(SQL);
            res = PS.executeUpdate();
            if(res > 0){
                JOptionPane.showMessageDialog(null, "Usuario actualizado con éxito");
            }
        } catch (SQLException e) {
            System.err.println("Error al modificar los datos." +e.getMessage());
        } finally{
            PS = null;
            CN.desconectar();
        }
        return res;
    }
    
    public int eliminarUsuario(String id){
        String SQL = "DELETE from usuario WHERE usuario_id='"+id+"'";
        int res=0;
        try {
            PS = CN.getConnection().prepareStatement(SQL);
            res = PS.executeUpdate();
            if(res > 0){
                JOptionPane.showMessageDialog(null, "Usuario eliminado con éxito");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No es posible eliminar el usuario.");
            System.err.println("Error al eliminar usuario." +e.getMessage());
        } finally{
            PS = null;
            CN.desconectar();
        }
        return res;
    }
}
