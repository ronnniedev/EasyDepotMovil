package ficheros.modelo;

import java.sql.Timestamp;

public class Reserva {
	
	private int idReserva;
	private String emailCliente;
	private String idCabina;
	private Timestamp fechaInicio;
	private Timestamp fechaSalida;
	private boolean incidencia;
	private String descripcionIncidencia;
	
	/**
	 * Constructor de 7 parametros de la clase Reserva, usado principalmente para la carga desde base de datos
	 * @param idReserva : int
	 * @param emailCliente : String
	 * @param idCabina : String
	 * @param fechaInicio : Timestamp
	 * @param fechaSalida : Timestamp
	 * @param incidencia : boolean
	 * @param descripcionIncidencia : String
	 */
	public Reserva(int idReserva, String emailCliente, String idCabina, Timestamp fechaInicio, Timestamp fechaSalida,
			boolean incidencia, String descripcionIncidencia) {
		this.idReserva = idReserva;
		this.emailCliente = emailCliente;
		this.idCabina = idCabina;
		this.fechaInicio = fechaInicio;
		this.fechaSalida = fechaSalida;
		this.incidencia = incidencia;
		this.descripcionIncidencia = descripcionIncidencia;
	}

	/**
	 * Constructor por defecto de la clase Reserva, tiene 4 parametros, es usado la primera vez que se crea un objeto
	 * en el sistema
	 * @param c : Cliente
	 * @param l : Local
	 * @param idReserva : int
	 * @param cab : Cabina
	 */
	public Reserva(Cliente c, Local l,int idReserva, Cabina cab) {
		this.idReserva = idReserva;
		this.emailCliente = c.getEmail();
		this.idCabina = cab.getIdCabina();
		this.fechaInicio = new Timestamp(System.currentTimeMillis());
		this.fechaSalida = null;
		this.incidencia = false;
		this.descripcionIncidencia = "Sin Incidencias";
		
	}


	/**
	 * @return the idReserva
	 */
	public int getIdReserva() {
		return idReserva;
	}

	/**
	 * @param idReserva the idReserva to set
	 */
	public void setIdReserva(int idReserva) {
		this.idReserva = idReserva;
	}

	/**
	 * @return the emailCliente
	 */
	public String getEmailCliente() {
		return emailCliente;
	}
	
	

	/**
	 * @param emailCliente the emailCliente to set
	 */
	public void setEmailCliente(String emailCliente) {
		this.emailCliente = emailCliente;
	}

	/**
	 * @return the idCabina
	 */
	public String getIdCabina() {
		return idCabina;
	}

	/**
	 * @param idCabina the idCabina to set
	 */
	public void setIdCabina(String idCabina) {
		this.idCabina = idCabina;
	}

	/**
	 * @return the fechaInicio
	 */
	public Timestamp getFechaInicio() {
		return fechaInicio;
	}

	/**
	 * @param fechaInicio the fechaInicio to set
	 */
	public void setFechaInicio(Timestamp fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**
	 * @return the fechaSalida
	 */
	public Timestamp getFechaSalida() {
		return fechaSalida;
	}

	/**
	 * @param fechaSalida the fechaSalida to set
	 */
	public void setFechaSalida(Timestamp fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	/**
	 * @return the incidencia
	 */
	public boolean isIncidencia() {
		return incidencia;
	}

	/**
	 * @param incidencia the incidencia to set
	 */
	public void setIncidencia(boolean incidencia) {
		this.incidencia = incidencia;
	}

	/**
	 * @return the descripcionIncidencia
	 */
	public String getDescripcionIncidencia() {
		return descripcionIncidencia;
	}

	/**
	 * @param descripcionIncidencia the descripcionIncidencia to set
	 */
	public void setDescripcionIncidencia(String descripcionIncidencia) {
		this.descripcionIncidencia = descripcionIncidencia;
	}

	@Override
	public String toString() {
		return "Reserva [idReserva=" + idReserva + ", emailCliente=" + emailCliente + ", idCabina=" + idCabina
				+ ", fechaInicio=" + fechaInicio + ", fechaSalida=" + fechaSalida + ", incidencia=" + incidencia
				+ ", descripcionIncidencia=" + descripcionIncidencia + "]";
	}

	
	
	
	
	
	
	

}
