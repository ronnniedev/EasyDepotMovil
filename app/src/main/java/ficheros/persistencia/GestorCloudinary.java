/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ficheros.persistencia;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import ficheros.apykeys.Apykeys;


/**
 *
 * @author mario
 */
public class GestorCloudinary {

    // Objeto tipo Cloudinary
    private Cloudinary cloudinary;

    // Constructor
    public GestorCloudinary() {
        this.cloudinary = new Cloudinary(Apykeys.obtenerConfiguracion());
    }

    // Se crea un void para replicar la llamada a: api.resources(Map options);
    // Se cambia a lista de string para poder devolver los public_id
    @SuppressWarnings({"unchecked"})
    public List<String> getResources() {
        List<String> lista = new ArrayList<String>();
        Map<String, String> options = new HashMap<String, String>();

        // Del objeto Cloudinary se coge el objeto Api y se llama al método que
        // devolverá ApiResponse
        
        Object next_cursor = "";
        try {
            do{
            ApiResponse ar = this.cloudinary.api().resources(options);

            // Al hacer el syso salen errores que no importan mucho pero se pueden quitar
            // System.out.println(ar);
            // Básicamente nos está enseñando a leer JSON con Java
            // Devuelve una lista de Maps para cada imagen
            ArrayList<Map<String, Object>> resources = (ArrayList<Map<String, Object>>) ar.get("resources");
            // Se recorre el diccionario y se van sacando cada public_id y añadiéndolos a
            // las listas
            for (Map<String, Object> elemento : resources) {
                String publicId = elemento.get("public_id").toString();
                lista.add(publicId);
            }
            
            lista.add("--------------Display name--------------");
            
            for (Map<String, Object> elemento : resources) {
                String publicId = elemento.get("display_name").toString();
                lista.add(publicId);
            }
            // saco el next_cursor
            next_cursor = ar.get("next_cursor");
            if(next_cursor != null){
                options.put("next_cursor", next_cursor.toString());
            }
            
            }
            while(next_cursor != null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return lista;
    }
    
    /**
     * Devuelve el url asociado a un public id
     * @param public_id
     */
    public String getUrl(String public_id){
        String url = null;
        Map <String,Object> options = new HashMap();
        List <Map<String,Object>> l = null;
        try{
            ApiResponse ar= cloudinary.api().resourcesByIds(ObjectUtils.asArray(public_id),options);
            l = (List <Map<String,Object>>) ar.get("resources");
        }catch(Exception e){
            e.printStackTrace();
        }
        
        if(l == null) return "ERROR no devuelve nada";
        return  l.get(0).get("secure_url").toString();
    }
    public void crearFolder(String nombre) {
        List<String> lista = new ArrayList<String>();
        Map<String, String> options = new HashMap<String, String>();

        // Del objeto Cloudinary se coge el objeto Api y se llama al método que
        // devolverá ApiResponse
        try {
            this.cloudinary.api().createFolder(nombre, options);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    /**
     * Dado un public_id, lo borra (si puede)
     * @param public_id : String
     * @return 
     */
    public boolean  borrar(String public_id){
        boolean exito = false;
        List <String> ids = new <String>ArrayList();
        Map<String, String> options = new HashMap<String, String>();
        ids.add(public_id);
        try{
            Map <String,Object> respuesta = cloudinary.uploader().destroy(public_id, options);
            
            if(!respuesta.get("result").toString().contains("ok")){
                exito = true;
                System.out.println("No se ha podido borrar");
            }else{
                System.out.println("Se ha podido borrar");
                exito = true;
            }
        } catch (Exception ex) {
            Logger.getLogger(GestorCloudinary.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return exito;
    }

    public void subirImagen(String ruta,String nombre) {
        Map<String, String> options = new HashMap<String, String>();
        options.put("folder", "Pepito");
        Map<String, String> pepo = new HashMap<String, String>();

        // Del objeto Cloudinary se coge el objeto Api y se llama al método que
        // devolverá ApiResponse
        try {
           Map <String,Object> respuesta = this.cloudinary.uploader().upload(ruta, options);
           System.out.println(respuesta.get("public_id").toString());
           cloudinary.uploader().rename(respuesta.get("public_id").toString(), nombre,pepo);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    public String get(String public_id){
        Map<String, String> options = new HashMap<String, String>();
        options.put("folder", "Pepito");
    
        try {
            // necesita una lista de public_ids
            ApiResponse ar = this.cloudinary.api().resourcesByIds(ObjectUtils.asArray(public_id), options);
            
        } catch (Exception ex) {
            Logger.getLogger(GestorCloudinary.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    public void descargarImagen(String ruta) {
        Map<String, String> options = new HashMap<String, String>();
        options.put("asset_folder", "Pepito");

        // Del objeto Cloudinary se coge el objeto Api y se llama al método que
        // devolverá ApiResponse
        try {
            this.cloudinary.uploader().upload(ruta, options);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void borrarImagen() {
        List<String> lista = new ArrayList<String>();
        Map<String, String> options = new HashMap<String, String>();
        options.put("asset_folder", "Pepito");

        ApiResponse ar =  null;
        try {
            ar = this.cloudinary.api().deleteAllResources(options);
        } catch (Exception ex) {
            Logger.getLogger(GestorCloudinary.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**Devuelve la lista de los public_id de los assets de un
     * fichero concreto
     * @param nameFolder el nombre del fichero con sus assets
     * @return {@link List} la lista de public ids (vacía si no hay)*/
    @SuppressWarnings("unchecked")
    public List<String> listAssets(String nameFolder) {
        System.out.println(nameFolder);
        Map<String, String> options = new HashMap<>();
        options.put("max_results", "500");


        List<String> lista = new ArrayList<>();
        Object next_cursor = ""; // luego será null
        try {
            while(next_cursor!=null) {
                // llamada a la API
                ApiResponse ar = this.cloudinary.
                        api().
                        resourcesByAssetFolder(nameFolder, options);
                // Devuelve un ArrayList de HashMaps
                //System.out.println(ar);
                ArrayList<HashMap<String, String>> l =
                        (ArrayList<HashMap<String, String>>) ar.get("resources");
                System.out.println("Devuelve: " + l.size());
                for(HashMap<String, String> e: l) {
                    lista.add(e.get("public_id"));
                }

                // sacamos el next_cursor
                next_cursor = ar.get("next_cursor");
                if (next_cursor!=null) {
                    //System.out.println("Sacando más...");
                    options.put("next_cursor", next_cursor.toString());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

}
