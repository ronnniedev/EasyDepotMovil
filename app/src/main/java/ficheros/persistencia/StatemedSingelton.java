package ficheros.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import ficheros.apykeys.Apykeys;
import ficheros.excepciones.PersistenciaException;

public class StatemedSingelton {

	private static StatemedSingelton state;
	private static Connection con;
	private static Statement st;
	private static PreparedStatement ps;
	private final static String password = Apykeys.getPassword();
	private final static String direccion = Apykeys.getBaseDatosFinal();
	private static final String USER = "uhjqdapmvfvmnpog";
	
	private StatemedSingelton() {
		
	}
	
	public static Statement getInstance() throws PersistenciaException {
		if(state == null) {
			state = new StatemedSingelton();
		}
		try {
			con = DriverManager.getConnection(direccion, USER, password);
			st = con.createStatement();
		}catch (SQLException e) {
			throw new PersistenciaException(e.getMessage());
		}
        return st;
	}
	
	public static PreparedStatement getInstance(String consulta) throws PersistenciaException {
		if(state == null) {
			state = new StatemedSingelton();
		}
		try {
			con = DriverManager.getConnection(direccion, USER, password);
			ps = con.prepareStatement(consulta);
		}catch (SQLException e) {
			throw new PersistenciaException(e.getMessage());
		}
		return ps;
	}
	
	public static Connection getConnection() throws PersistenciaException {
		if(state == null) {
			state = new StatemedSingelton();
		}
		try {
			con = DriverManager.getConnection(direccion, USER, password);
		}catch (SQLException e) {
			throw new PersistenciaException("ERROR en el driver");
		}
		return con;
	}
	
	public static void close() throws PersistenciaException {
		if(st != null) {
			try {
				if(st != null) {
					st.close();
				}
				if(ps != null) {
					ps.close();
				}
				if(con != null) {
					con.close();
				}
				
			} catch (SQLException e) {
				throw new PersistenciaException("ERROR en el cierre");
			}
		}
		
	}

}
