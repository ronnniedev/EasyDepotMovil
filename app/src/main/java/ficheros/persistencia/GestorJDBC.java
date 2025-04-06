package ficheros.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;

import ficheros.excepciones.PersistenciaException;
import ficheros.modelo.Articulo;
import ficheros.modelo.Cabina;
import ficheros.modelo.Cliente;
import ficheros.modelo.Email;
import ficheros.modelo.Local;
import ficheros.modelo.Reserva;
import ficheros.persistencia.StatemedSingelton;

public class GestorJDBC {

	/**
	 * Constructor con  parametros que tiene toda la configuracion de las tablas alojadas en la base de datos
	 * En caso de que no existan las tablas este constructor las crea desde 0
	 * @throws PersistenciaException
	 * @throws SQLException
	 */
	public GestorJDBC() throws PersistenciaException, SQLException{
		crearTablas();
	}
	/**
	 * Crea las tablas para el funcionamiento correcto del sistema, tambien esta usado de manera auxiliar por 
	 * el JUnit
	 * @throws PersistenciaException
	 * @throws SQLException
	 */
	public static void crearTablas() throws PersistenciaException, SQLException {
		Statement st = null;
		st = StatemedSingelton.getInstance();

		
		String consultaClientes = "CREATE TABLE IF NOT EXISTS clientes ("
				+ "    emailCliente VARCHAR(100) PRIMARY KEY,"
				+ "    nombre VARCHAR(100) NOT NULL,"
				+ "    apellidos VARCHAR(100) NOT NULL,"
				+ "    password VARCHAR(100) NOT NULL,"
				+ "	   puntosTienda INT,"
				+ "    numeroReservas INT"
				+ ");";
		String consultaLocales = "CREATE TABLE IF NOT EXISTS locales ("
				+ "    idLocal INT AUTO_INCREMENT PRIMARY KEY,"
				+ "    coordenadas VARCHAR(100) NOT NULL,"
				+ "    numeroReservas INT NOT NULL,"
				+ "    ingresos NUMERIC(10,2) NOT NULL,"
				+ "	   direccion VARCHAR(100)"
				+ ");";
		String consultaCabinas = "CREATE TABLE IF NOT EXISTS cabinas ("
				+ "    idCabina VARCHAR(10) PRIMARY KEY,"
				+ "    idLocal INT NOT NULL,"
				+ "    abierto BOOLEAN,"
				+ "    reservada BOOLEAN,"
				+ "	   tipo VARCHAR(100) NOT NULL,"
				+ "    FOREIGN KEY (idLocal) REFERENCES locales(idLocal)"
				+ ");";
		String consultaReservas = "CREATE TABLE IF NOT EXISTS reservas ("
				+ "    idReserva INT AUTO_INCREMENT PRIMARY KEY,"
				+ "    emailCliente VARCHAR(100) NOT NULL,"
				+ "    idCabina VARCHAR(10) NOT NULL,"
				+ "    fechaInicio TIMESTAMP NOT NULL,"
				+ "    fechaSalida TIMESTAMP,"
				+ "	   incidencia BOOLEAN,"
				+ "    descripcionIncidencia VARCHAR(2000),"
				+ "    FOREIGN KEY (emailCliente) REFERENCES clientes(emailCliente),"
				+ "    FOREIGN KEY (idCabina) REFERENCES cabinas(idCabina)"
				+ ");";
		String consultaArticulos = "CREATE TABLE IF NOT EXISTS articulos ("
				+ "    idArticulo VARCHAR(10) PRIMARY KEY,"
				+ "    idLocal INT NOT NULL,"
				+ "    nombre VARCHAR(100) NOT NULL,"
				+ "    stock INT NOT NULL,"
				+ "    precio INT NOT NULL,"
				+ "    imagen VARCHAR(100),"
				+ "    FOREIGN KEY (idLocal) REFERENCES locales(idLocal)"
				+ ");";
		
		
		st.executeUpdate(consultaClientes);
		st.executeUpdate(consultaLocales);
		st.executeUpdate(consultaCabinas);
		st.executeUpdate(consultaReservas);
		st.executeUpdate(consultaArticulos);
		StatemedSingelton.close();
	}

	/**
	 * Lee todos los crientes y retorna una mapa de objetos Cliente al sistema
	 * @return Map : clientes
	 * @throws PersistenciaException
	 */
	public Map<Email, Cliente> leerClientes() throws PersistenciaException {
		Statement st = null;
		ResultSet rs = null;
		Map <Email,Cliente> clientes = new HashMap<Email,Cliente>();
		
		try {
			st = StatemedSingelton.getInstance();
			String consulta = "SELECT * FROM clientes";
			rs = st.executeQuery(consulta);
			while(rs.next()) {
				Cliente c = prepararCliente(rs);
				clientes.put(new Email(c.getEmail()), c);
			}
		} catch (PersistenciaException e) {
			System.out.println(e.getMessage());
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			try {
				rs.close();
				StatemedSingelton.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			
		}
		return clientes;
	}

	/**
	 * Lee todos los locales de la base de datos y los devuelve en forma de lista de objetos
	 * @return List : locales
	 * @throws PersistenciaException
	 */
	public List<Local> leerLocales() throws PersistenciaException {
		Statement st = null;
		ResultSet rs = null;
		List <Local> listaLocales = new LinkedList<Local>();
		
		try {
			st = StatemedSingelton.getInstance();
			String consulta = "SELECT * FROM locales";
			rs = st.executeQuery(consulta);
			while(rs.next()) {
				listaLocales.add(prepararLocal(rs));
			}
		} catch (PersistenciaException e) {
			System.out.println(e.getMessage());
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			try {
				rs.close();
				StatemedSingelton.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			
		}
		return listaLocales;
	}
	
	/**
	 * Lee todas las reservas del sistema y las devuelve en forma de lista de objetos Reserva
	 * @return Lis : reservas
	 * @throws PersistenciaException
	 */
	public List<Reserva> leerReservas() throws PersistenciaException {
		Statement st = null;
		ResultSet rs = null;
		List <Reserva> listaReservas = new LinkedList<Reserva>();
		
		try {
			st = StatemedSingelton.getInstance();
			String consulta = "SELECT * FROM reservas";
			rs = st.executeQuery(consulta);
			while(rs.next()) {
				listaReservas.add(prepararReserva(rs));
			}
		} catch (PersistenciaException e) {
			System.out.println(e.getMessage());
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			try {
				rs.close();
				StatemedSingelton.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			
		}
		return listaReservas;
		
	}
	
	/**
	 * Lee todas las cabinas del sistema y las devuelve en forma de lista de cabinas
	 * @return List : cabinas
	 * @throws PersistenciaException
	 */
	public List<Cabina> leerCabinas() throws PersistenciaException {
		Statement st = null;
		ResultSet rs = null;
		List <Cabina> listaCabinas = new LinkedList<Cabina>();
		
		try {
			st = StatemedSingelton.getInstance();
			String consulta = "SELECT * FROM cabinas";
			rs = st.executeQuery(consulta);
			while(rs.next()) {
				listaCabinas.add(prepararCabina(rs));
			}
		} catch (PersistenciaException e) {
			System.out.println(e.getMessage());
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			try {
				rs.close();
				StatemedSingelton.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			
		}
		return listaCabinas;
		
	}
	
	/**
	 * Lee todos los articulos del sistema y las devuelve en forma de lista de articulos
	 * @return List <Articulo>
	 */
	public List<Articulo> leerArticulos() {
		Statement st = null;
		ResultSet rs = null;
		List <Articulo> listaArticulos = new LinkedList<Articulo>();
		
		try {
			st = StatemedSingelton.getInstance();
			String consulta = "SELECT * FROM articulos";
			rs = st.executeQuery(consulta);
			while(rs.next()) {
				listaArticulos.add(prepararArticulo(rs));
			}
		} catch (PersistenciaException e) {
			System.out.println(e.getMessage());
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			try {
				rs.close();
				StatemedSingelton.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} catch (PersistenciaException e) {
				System.out.println(e.getMessage());
			}
			
		}
		return listaArticulos;
		
	}
	
	
	/**
	 * Inserta un cliente dentro de la base de datos
	 * @param c : Cliente
	 */
	public void insertarCliente(Cliente c) {
		PreparedStatement ps = null;
		try {
			String insertCliente = "INSERT INTO clientes (emailCliente,nombre,apellidos,password"
									+ ",puntosTienda,numeroReservas)"
									+ " VALUES (?,?,?,?,?,?)";
			ps = StatemedSingelton.getInstance(insertCliente);
			ps.setString(1, c.getEmail());
			ps.setString(2, c.getNombre());
			ps.setString(3, c.getApellidos());
			ps.setString(4, c.getPassword());
			ps.setInt(5, c.getPuntosTienda());
			ps.setInt(6, c.getNumeroReservas());
			ps.executeUpdate();
		} catch (PersistenciaException e1) {
			System.out.println(e1.getMessage());
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
		}finally {
			try {
				StatemedSingelton.close();
			} catch (PersistenciaException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	/**
	 * Inserta un local dentro de la base de datos
	 * @param l : Local
	 */
	public void insertarLocal(Local l) {
		PreparedStatement ps = null;
		try {
			String insertLocal = "INSERT INTO locales (coordenadas,numeroReservas,ingresos,direccion)"
									+ " VALUES (?,?,?,?)";
			ps = StatemedSingelton.getInstance(insertLocal);
			ps.setString(1, l.getCoordenadas());
			ps.setInt(2, l.getNumeroReservas());
			ps.setDouble(3,l.getIngresos());
			ps.setString(4, l.getDireccion());
			ps.executeUpdate();
		} catch (PersistenciaException e1) {
			System.out.println(e1.getMessage());
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
		}finally {
			try {
				StatemedSingelton.close();
			} catch (PersistenciaException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	/**
	 * Inserta las cabinas de un local dentro de la base de datos
	 * @param c : cabina
	 */
	public void insertarCabina(Cabina c) {
		PreparedStatement ps = null;
		try {
			String insertCabina = "INSERT INTO cabinas (idCabina,idLocal,abierto,reservada,tipo)"
					+ " VALUES (?,?,?,?,?)";
			ps = StatemedSingelton.getInstance(insertCabina);
			ps.setString(1, c.getIdCabina());
			ps.setInt(2, c.getIdLocal());
			ps.setBoolean(3,c.getAbierto());
			ps.setBoolean(4,c.getReservada());
			ps.setString(5, c.getTipo());
			ps.executeUpdate();
		} catch (PersistenciaException e1) {
			System.out.println(e1.getMessage());
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
		}finally {
			try {
				StatemedSingelton.close();
			} catch (PersistenciaException e) {
				System.out.println(e.getMessage());
			}
		}
		
	}
	
	/**
	 * Inserta una reserva dentro de la base de datos 
	 * @param r : Reserva
	 */
	public void insertarReserva(Reserva r) {
		PreparedStatement ps = null;
		try {
			String insertReserva = "INSERT INTO reservas (emailCliente,idCabina,fechaInicio,fechaSalida"
								+ ",incidencia,descripcionIncidencia)"
					+ " VALUES (?,?,?,?,?,?)";
			ps = StatemedSingelton.getInstance(insertReserva);
			ps.setString(1, r.getEmailCliente());
			ps.setString(2,r.getIdCabina());
			ps.setTimestamp(3,r.getFechaInicio());
			ps.setTimestamp(4, r.getFechaSalida());
			ps.setBoolean(5, r.isIncidencia());
			ps.setString(6, r.getDescripcionIncidencia());
			ps.executeUpdate();
			
		} catch (PersistenciaException e1) {
			System.out.println(e1.getMessage());
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
		}finally {
			try {
				StatemedSingelton.close();
			} catch (PersistenciaException e) {
				System.out.println(e.getMessage());
			}
		}
		
	}
	
	/**
	 * Inserta un Articulo dentro de la base de datos
	 * @param a : articulo
	 */
	public void insertarArticulo(Articulo a) {
		PreparedStatement ps = null;
		try {
			String insertArticulo = "INSERT INTO articulos (idArticulo,idLocal,nombre,stock"
									+ ",precio,imagen)"
									+ " VALUES (?,?,?,?,?,?)";
			ps = StatemedSingelton.getInstance(insertArticulo);
			ps.setString(1, a.getIdArticulo());
			ps.setInt(2, a.getIdLocal());
			ps.setString(3, a.getNombre());
			ps.setInt(4, a.getStock());
			ps.setDouble(5, a.getPrecio());
			ps.setString(6, a.getImagen());
			ps.executeUpdate();
			
		} catch (PersistenciaException e1) {
			System.out.println(e1.getMessage());
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
		}finally {
			try {
				StatemedSingelton.close();
			} catch (PersistenciaException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	/**
	 * Actualiza los valores modificables de un cliente
	 * @param c : Cliente
	 */
	public void actualizarCliente(Cliente c) {
		PreparedStatement ps = null;
		try {
			String updateCliente = "UPDATE clientes SET nombre = ?, apellidos = ?, password = ?, puntosTienda = ?, "
									+ "numeroReservas = ? WHERE emailCliente ='"+c.getEmail()+"'";
			ps = StatemedSingelton.getInstance(updateCliente);
			ps.setString(1, c.getNombre());
			ps.setString(2, c.getApellidos());
			ps.setString(3, c.getPassword());
			ps.setInt(4, c.getPuntosTienda());
			ps.setInt(5, c.getNumeroReservas());
			ps.executeUpdate();
		} catch (PersistenciaException e1) {
			System.out.println(e1.getMessage());
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
		}finally {
			try {
				StatemedSingelton.close();
			} catch (PersistenciaException e) {
				System.out.println(e.getMessage());
			}
		}
		
	}
	
	/**
	 * Actualiza los valores modificables de un local
	 * @param l : Local
	 */
	public void actualizarLocal(Local l) {
		PreparedStatement ps = null;
		try {
			String updateLocal = "UPDATE locales SET coordenadas = ?, numeroReservas = ?, ingresos = ?"
									+ ", direccion = ? "
									+ "WHERE idLocal = "+l.getLocalId()+"";
			ps = StatemedSingelton.getInstance(updateLocal);
			ps.setString(1, l.getCoordenadas());
			ps.setInt(2, l.getNumeroReservas());
			ps.setDouble(3, l.getIngresos());
			ps.setString(4, l.getDireccion());
			ps.executeUpdate();
		} catch (PersistenciaException e1) {
			System.out.println(e1.getMessage());
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
		}finally {
			try {
				StatemedSingelton.close();
			} catch (PersistenciaException e) {
				System.out.println(e.getMessage());
			}
		}
		
	}
	
	/**
	 * Actualiza los valores modificables de una cabina
	 * @param c : Cabina
	 */
	public void actualizarCabina(Cabina c) {
		PreparedStatement ps = null;
		try {
			String updateCabina = "UPDATE cabinas SET abierto = ?, reservada = ?"
									+ " WHERE idCabina ='"+ c.getIdCabina() +"'";
			ps = StatemedSingelton.getInstance(updateCabina);
			ps.setBoolean(1, c.getAbierto());
			ps.setBoolean(2, c.getReservada());
			ps.executeUpdate();
		} catch (PersistenciaException e1) {
			System.out.println(e1.getMessage());
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
		}finally {
			try {
				StatemedSingelton.close();
			} catch (PersistenciaException e) {
				System.out.println(e.getMessage());
			}
		}
		
	}
	
	/**
	 * Actualiza los valores modificables de una cabin
	 * @param a : Articulo
	 */
	public void actualizarArticulo(Articulo a) {
		PreparedStatement ps = null;
		try {
			String updateCabina = "UPDATE articulos SET nombre = ?, stock = ?, precio = ?, imagen = ? "
									+ " WHERE idArticulo ='"+ a.getIdArticulo() +"'";
			ps = StatemedSingelton.getInstance(updateCabina);
			ps.setString(1, a.getNombre());
			ps.setInt(2, a.getStock());
			ps.setDouble(3, a.getPrecio());
			ps.setString(4, a.getImagen());
			ps.executeUpdate();
		} catch (PersistenciaException e1) {
			System.out.println(e1.getMessage());
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
		}finally {
			try {
				StatemedSingelton.close();
			} catch (PersistenciaException e) {
				System.out.println(e.getMessage());
			}
		}
		
	}
	
	/**
	 * Actualiza los valores modificables de una reserva
	 * @param r : Reserva
	 */
	public void actualizarReserva(Reserva r) {
		PreparedStatement ps = null;
		try {
			String updateReserva = "UPDATE reservas SET emailCliente = ?, fechaSalida = ?, incidencia = ?,"
									+ " descripcionIncidencia = ?"
									+ " WHERE idReserva = "+ r.getIdReserva();
			ps = StatemedSingelton.getInstance(updateReserva);
			ps.setString(1, r.getEmailCliente());
			ps.setTimestamp(2, r.getFechaSalida());
			ps.setBoolean(3, r.isIncidencia());
			ps.setString(4, r.getDescripcionIncidencia());
			ps.executeUpdate();
		} catch (PersistenciaException e1) {
			System.out.println(e1.getMessage());
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
		}finally {
			try {
				StatemedSingelton.close();
			} catch (PersistenciaException e) {
				System.out.println(e.getMessage());
			}
		}
		
	}
	
	/**
	 * Elimina un cliente concreto y eiqueta las reservas asociadas con el email eliminado 
	 * @param c : Cliente
	 * @param reservas : List<Reserva>
	 */
	public void eliminarCliente(Cliente c,List <Reserva> reservas) {
		Statement st = null;
		try {
			for(Reserva r: reservas) {
				r.setEmailCliente("Eliminado");
				actualizarReserva(r);
			}
			String consulta = "DELETE FROM clientes where emailCliente = '"+c.getEmail()+"'";
			st = StatemedSingelton.getInstance();
			st.executeUpdate(consulta);
		} catch (PersistenciaException e1) {
			System.out.println(e1.getMessage());
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
		}finally {
			try {
				StatemedSingelton.close();
			} catch (PersistenciaException e) {
				System.out.println(e.getMessage());
			}
		}
		
	}
	/**
	 * Elimina el cliente de la base de datos
	 * @param c : Cliente
	 */
	public void eliminarCliente(Cliente c) {
		Statement st = null;
		try {
			String consulta = "DELETE FROM clientes where emailCliente = '"+c.getEmail()+"'";
			st = StatemedSingelton.getInstance();
			st.executeUpdate(consulta);
		} catch (PersistenciaException e1) {
			System.out.println(e1.getMessage());
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
		}finally {
			try {
				StatemedSingelton.close();
			} catch (PersistenciaException e) {
				System.out.println(e.getMessage());
			}
		}
		
	}
	
	/**
	 * Elimina el articulo de la base de datos
	 * @param a : Articulo
	 */
	public void eliminarArticulo(Articulo a) {
		Statement st = null;
		try {
			String consulta = "DELETE FROM articulos where idArticulo ='"+a.getIdArticulo()+"'";
			st = StatemedSingelton.getInstance();
			st.executeUpdate(consulta);
		} catch (PersistenciaException e1) {
			System.out.println(e1.getMessage());
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
		}finally {
			try {
				StatemedSingelton.close();
			} catch (PersistenciaException e) {
				System.out.println(e.getMessage());
			}
		}
		
	}
	
	/**
	 * Prepara el resultset asociado a un Cliente
	 * @param rs : ResultSet
	 * @return new Cliente
	 * @throws SQLException
	 */
	private Cliente prepararCliente(ResultSet rs) throws SQLException {
		
		String email = rs.getString(1);
		String nombre = rs.getString(2);
		String apellidos = rs.getString(3);
		String password = rs.getString(4);
		int puntosTienda = rs.getInt(5);
		int numeroReservas = rs.getInt(6);
		return new Cliente(email,nombre,apellidos,password,puntosTienda,numeroReservas);
	}

	/**
	 * Prepara el resultset asociado a un Local
	 * @param rs : ResultSet
	 * @return new Local
	 * @throws SQLException
	 */
	private Local prepararLocal(ResultSet rs) throws SQLException {
		
		int idLocal = rs.getInt(1);
		String coordenadas = rs.getString(2);
		int numeroReservas = rs.getInt(3);
		double ingresos = rs.getDouble(4);
		String direccion = rs.getString(5);
		return new Local(idLocal,coordenadas,numeroReservas,ingresos,direccion);
	}
	
	/**
	 * Prepara el resultset asociado a una Reserva
	 * @param rs : ResultSet
	 * @return new Reserva
	 * @throws SQLException
	 */
	private Reserva prepararReserva(ResultSet rs) throws SQLException {
		
		int idReserva = rs.getInt(1);
		String emailCliente = rs.getString(2);
		String idCabina = rs.getString(3);
		Timestamp fechaInicio = rs.getTimestamp(4);
		Timestamp fechaSalida = rs.getTimestamp(5);
		boolean incidencia = rs.getBoolean(6);
		String descripcionIncidencia = rs.getString(7);
		
		return new Reserva(idReserva,emailCliente,idCabina,fechaInicio,fechaSalida,incidencia,descripcionIncidencia);
	}
	
	/**
	 * Prepara el resultset asociado a un Cliente
	 * @param rs : ResultSet
	 * @return new Cliente
	 * @throws SQLException
	 */
	private Cabina prepararCabina(ResultSet rs) throws SQLException {
		
		String idCabina = rs.getString(1);
		int idLocal = rs.getInt(2);
		Boolean abierto = rs.getBoolean(3);
		Boolean reservada = rs.getBoolean(4);
		String tipo = rs.getString(5);
		
		return new Cabina(idCabina,idLocal,abierto,reservada,tipo);
	}
	/**
	 * Prepara el resultset asociado a un articulo
	 * @param rs : ResultSet
	 * @return new Articulo
	 * @throws SQLException
	 */
	private Articulo prepararArticulo(ResultSet rs) throws SQLException {
		
		String idArticulo = rs.getString(1);
		int idLocal = rs.getInt(2);
		String nombre = rs.getString(3);
		int stock = rs.getInt(4);
		int precio = rs.getInt(5);
		String imagen = rs.getString(6);
		
		return new Articulo(idArticulo,idLocal,nombre,stock,precio,imagen);
	}

	/**
	 * Reinicia la base de datos del sistema, esta funcion es exclusiva de la version de desarrollo
	 * se utiliza principalmente para la actualizacion de configuraciones en las tablas y pruebas en vacio
	 * @throws PersistenciaException
	 * @throws SQLException
	 */
	public static Boolean reiniciarPersistencia() throws PersistenciaException, SQLException {
		Statement st = StatemedSingelton.getInstance();
		String consultaClientes = "DROP TABLE IF EXISTS clientes";
		String consultaLocales = "DROP TABLE IF EXISTS locales";
		String consultaCabinas = "DROP TABLE IF EXISTS cabinas";
		String consultaReservas = "DROP TABLE IF EXISTS reservas";
		String consultaArticulos = "DROP TABLE IF EXISTS articulos";
		st.executeUpdate(consultaReservas);
		st.executeUpdate(consultaArticulos);
		st.executeUpdate(consultaCabinas);
		st.executeUpdate(consultaClientes);
		st.executeUpdate(consultaLocales);
		StatemedSingelton.close();
		return true;
	}
	
	
	

	

}
