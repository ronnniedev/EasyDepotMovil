package ficheros.modelo;

import java.util.LinkedList;
import java.util.List;

public class Local {
	
	private int idLocal;
	private String coordenadas;
	private int numeroReservas;
	private double ingresos;
	private String direccion;
	private List <Articulo> articulos;
	private List <Cabina> cabinas;
	
	/**
	 * Constructor de 5 parametros de la clase Local, llamado cuando se carga desde la base de datos o se crea
	 * un nuevo local
	 * @param localId : int
	 * @param coordenadas : String
	 * @param numeroReservas : int
	 * @param ingresos : double
	 * @param direccion : String
	 */
	public Local(int localId, String coordenadas, int numeroReservas, double ingresos, String direccion) {
		this.idLocal = localId;
		this.coordenadas = coordenadas;
		this.numeroReservas = numeroReservas;
		this.ingresos = ingresos;
		this.direccion = direccion;
		this.articulos = new LinkedList<Articulo>();
		this.cabinas = new LinkedList<Cabina>();
	}

	public Local(String coordenadas, int numeroReservas, int ingresos, String direccion) {
		this.coordenadas = coordenadas;
		this.numeroReservas = numeroReservas;
		this.ingresos = ingresos;
		this.direccion = direccion;
		this.articulos = new LinkedList<Articulo>();
		this.cabinas = new LinkedList<Cabina>();
	}

	/**
	 * Rellan la lista de cabinas de un local cuando este se crea inicialmente
	 * Actualmente configurada para que alla solo 6 cabinas pero puede modifcarse cambiando el valor del mismo
	 * @return
	 */
	public List<Cabina> rellenarCabinas() {
		this.cabinas = new LinkedList<Cabina>();
		
		while(cabinas.size() != 6) {
			if(cabinas.size() < 2) {
				cabinas.add(new Cabina(cabinas.size()+1,idLocal,"Pequeña"));
			}else if(cabinas.size() < 4) {
				cabinas.add(new Cabina(cabinas.size()+1,idLocal,"Mediana"));
			}else{
				cabinas.add(new Cabina(cabinas.size()+1,idLocal,"Grande"));
			}
		}
		return cabinas;
	}


	/**
	 * @return the localId
	 */
	public int getLocalId() {
		return idLocal;
	}


	/**
	 * @param localId the localId to set
	 */
	public void setLocalId(int localId) {
		this.idLocal = localId;
	}


	/**
	 * @return the coordenadas
	 */
	public String getCoordenadas() {
		return coordenadas;
	}


	/**
	 * @param coordenadas the coordenadas to set
	 */
	public void setCoordenadas(String coordenadas) {
		this.coordenadas = coordenadas;
	}


	/**
	 * @return the numeroReservas
	 */
	public int getNumeroReservas() {
		return numeroReservas;
	}


	/**
	 * @param numeroReservas the numeroReservas to set
	 */
	public void setNumeroReservas(int numeroReservas) {
		this.numeroReservas = numeroReservas;
	}


	/**
	 * @return the ingresos
	 */
	public double getIngresos() {
		return ingresos;
	}


	/**
	 * @param ingresos the ingresos to set
	 */
	public void setIngresos(double ingresos) {
		this.ingresos = ingresos;
	}


	/**
	 * @return the direccion
	 */
	public String getDireccion() {
		return direccion;
	}


	/**
	 * @param direccion the direccion to set
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	/**
	 * @return the articulos
	 */
	public List<Articulo> getArticulos() {
		return articulos;
	}


	/**
	 * @param articulos the articulos to set
	 */
	public void setArticulos(List<Articulo> articulos) {
		this.articulos = articulos;
	}


	/**
	 * @return the cabinas
	 */
	public List<Cabina> getCabinas() {
		return cabinas;
	}


	/**
	 * @param cabinas the cabinas to set
	 */
	public void setCabinas(List<Cabina> cabinas) {
		this.cabinas = cabinas;
	}

	@Override
	public String toString() {
		return "Local [localId=" + idLocal + ", coordenadas=" + coordenadas + ", numeroReservas=" + numeroReservas
				+ ", ingresos=" + ingresos + ", direccion=" + direccion + "]";
	}


	public String visualizarCabinas() {
		String texto = "------------Cabinas de local " + this.idLocal + "----------------\n";
		
		for(Cabina c: cabinas) {
			texto += c.toString() + "\n";
		}
		return texto + "------------------------------------\n";
	}


	
	
	
	
	
	
	
	
}
