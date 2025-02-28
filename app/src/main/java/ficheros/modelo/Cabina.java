package ficheros.modelo;

public class Cabina {
	
	private String idCabina;
	private int idLocal;
	private Boolean abierto;
	private Boolean reservada;
	private String tipo;
	
	/**
	 * Constructor de cinco parametros para la cabina , usado para la carga desde la base de datos
	 * @param idCabina : String
	 * @param idLocal : int
	 * @param abierto : Boolean
	 * @param reservada : Boolean
	 * @param tipo : String
	 */
	public Cabina(String idCabina, int idLocal, Boolean abierto, Boolean reservada, String tipo) {
		this.idCabina = idCabina;
		this.idLocal = idLocal;
		this.abierto = abierto;
		this.reservada = reservada;
		this.tipo = tipo;
	}
	
	/**
	 * Constructor de tres parametros para la clase Cabina, tiene los valores por defecto que se usan en la primera vez
	 * que se crea un objeto
	 * @param idCabina : int
	 * @param idLocal : int
	 * @param tipo : String
	 */
	public Cabina(int idCabina, int idLocal, String tipo) {
		super();
		this.idCabina = idLocal + "-" + idCabina;
		this.idLocal = idLocal;
		this.abierto = false;
		this.reservada = false;
		this.tipo = tipo;
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
	 * @return the idLocal
	 */
	public int getIdLocal() {
		return idLocal;
	}

	/**
	 * @param idLocal the idLocal to set
	 */
	public void setIdLocal(int idLocal) {
		this.idLocal = idLocal;
	}

	/**
	 * @return the abierto
	 */
	public Boolean getAbierto() {
		return abierto;
	}

	/**
	 * @param abierto the abierto to set
	 */
	public void setAbierto(Boolean abierto) {
		this.abierto = abierto;
	}

	/**
	 * @return the reservada
	 */
	public Boolean getReservada() {
		return reservada;
	}

	/**
	 * @param reservada the reservada to set
	 */
	public void setReservada(Boolean reservada) {
		this.reservada = reservada;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	

	@Override
	public String toString() {
		return "Cabina [idCabina=" + idCabina + ", idLocal=" + idLocal + ", abierto=" + abierto + ", reservada="
				+ reservada + ", tipo=" + tipo + "]";
	}
	
	
	

}
