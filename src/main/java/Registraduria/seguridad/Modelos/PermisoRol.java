package Registraduria.seguridad.Modelos;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class PermisoRol {
    @Id
    private  String _id;
    //para indicar que hace referencia a otro modelo de base de datos
    @DBRef
    private  Rol rol;
    @DBRef
    private  Permiso permiso;

    public PermisoRol(Rol rol, Permiso permiso) {
        this.rol = rol;
        this.permiso = permiso;
    }
}
