package ficheros.modelo;

import java.util.List;

public class Articulo {

	private String idArticulo;
	private int idLocal;
	private String nombre;
	private int stock;
	private int precio;
	private String imagen;
	
	/**
	 * Constructor de seis parametros para la clase Articulo, usado por defecto en la carga de la base de datos
	 * y creacion base
	 * @param idArticulo : int
	 * @param idLocal : int
	 * @param nombre : String
	 * @param precio : double
	 * @param imagen : String
	 */
	public Articulo(String idArticulo, int idLocal, String nombre, int stock, int precio, String imagen) {
		super();
		this.idArticulo = idArticulo;
		this.idLocal = idLocal;
		this.nombre = nombre;
		this.stock = stock;
		this.precio = precio;
		this.imagen = imagen;
	}
	
	/**
	 * @return the stock
	 */
	public int getStock() {
		return stock;
	}



	/**
	 * @return the stock
	 */
	public void setStock(int stock) {
		this.stock = stock;
	}




	/**
	 * @return the idArticulo
	 */
	public String getIdArticulo() {
		return idArticulo;
	}


	/**
	 * @param idArticulo the idArticulo to set
	 */
	public void setIdArticulo(String idArticulo) {
		this.idArticulo = idArticulo;
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
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}


	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	/**
	 * @return the precio
	 */
	public int getPrecio() {
		return precio;
	}


	/**
	 * @param precio the precio to set
	 */
	public void setPrecio(int precio) {
		this.precio = precio;
	}


	/**
	 * @return the imagen
	 */
	public String getImagen() {
		return imagen;
	}


	/**
	 * @param imagen the imagen to set
	 */
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	/**
	 * Calcula la id del articulo basandose en los articulos previamente insertados y sumandole 1 a la ultima cifra de
	 * la id
	 * @param articulos
	 * @return
	 */
	public static int calcularId(List<Articulo> articulos) {
		if(articulos.size() == 0) {
			return 1;
		}
		
		String trozos [] = articulos.get(articulos.size()-1).getIdArticulo().split("-");
		return Integer.parseInt(trozos[1]) + 1;
	}


	@Override
	public String toString() {
		return "Articulo [idArticulo=" + idArticulo + ", idLocal=" + idLocal + ", nombre=" + nombre + ", stock=" + stock
				+ ", precio=" + precio + ", imagen=" + imagen + "]";
	}

	


	
	
	
	
	
	
}
