package ficheros.logica;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ficheros.excepciones.LogicaException;

public class GestorComprobaciones {
	
	/**
	 * Comprueba si la fecha de salida no es nula, en cuyo caso devuelve un boolean como true.
	 * @param fecha : Tiemstamp
	 * @return boolean
	 */
	public static boolean comprobarFechaSalida(Timestamp fecha) {
		
		if(fecha != null) {
			return true;
		}
		return false;
	}

	/**
	 * Comprueba que el correo esta bien escrito y entre los proovedores permitidos, si no lanza una excepcion.
	 * @param email : String
	 * @return Boolean
	 * @throws LogicaException
	 */
	public static boolean comprobarEmail(String email) throws LogicaException {
		List <String> correosCorrectos = new ArrayList<String>();
		correosCorrectos.add("@yahoo.com");
		correosCorrectos.add("@gmail.com");
		correosCorrectos.add("@outlook.com");
		correosCorrectos.add("@hotmail.com");
		correosCorrectos.add("@yahoo.es");
		correosCorrectos.add("@gmail.es");
		correosCorrectos.add("@outlook.es");
		correosCorrectos.add("@hotmail.es");
		
		if(email.startsWith("@")) {
			throw new LogicaException("ERROR formato email incorrecto");
		}
		
		for(String comprobante: correosCorrectos) {
			if(email.endsWith(comprobante)) {
				return true;
			}
		}
		throw new LogicaException("ERROR email no admitido");
	}
	/**
	 * Comprueba si hay campos vacios, en cuyo caso lanza excepcion
	 * @param palabras : List <String>
	 * @return Boolean
	 * @throws LogicaException
	 */
	public static boolean comprobarTextoVacio(List <String> palabras) throws LogicaException {
		
		
		for(String palabra: palabras) {
			if(palabra.isBlank()) {
				throw new LogicaException("ERROR no puede haber campos vacios");
			}
		}
		return true;
	}


    public static void comprobarTelefono(String telefono) throws LogicaException {
		try {
			int numero = Integer.parseInt(telefono);
			if(telefono.length() != 9){
				throw new LogicaException("Introduza un numero telefonico de 9 cifras");
			}
		}catch(NumberFormatException e){
			throw new LogicaException("Introduza un numero telefonico");
		}
    }
}
