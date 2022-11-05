package Registraduria.seguridad.Controladores;

import Registraduria.seguridad.Modelos.PermisoRol;
import Registraduria.seguridad.Modelos.Rol;
import Registraduria.seguridad.Repositorios.RepositorioRol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/permiso-rol")
public class ControladorPermisoRol {
    @Autowired
    private RepositorioPermisosRol RepositorioPermisosRol;
    @Autowired
    private RepositorioRol RepositorioRol;
    @Autowired
    private RepositorioPermiso RepositorioPermiso;



    @PostMapping("/rol/{idRol}/permiso/{idPermiso}")
    public PermisoRol crearPermisoRol(@PathVariable String idRol,@PathVariable String idPermiso){

        Rol rol = RepositorioRol.findById(idRol).orElseThrow(RuntimeException::new);

        Permiso permiso = RepositorioPermiso.findById(idPermiso).orElseThrow(RuntimeException::new);

        PermisoRol permisoRol = new PermisoRol(rol, permiso);

        return RepositorioPermisosRol.save(permisoRol);

    }
    @GetMapping
    public List<PermisoRol> listaPermisosRol(){

        return RepositorioPermisosRol.findAll();

    }

    @GetMapping("/validar-permiso/rol/{idRol}")
    public PermisoRol validarPermisosDelRol(@PathVariable String idRol, @RequestBody Permiso infoPermiso){
        Rol rolActual =RepositorioRol.findById(idRol).orElse(null);
        Permiso permisoActual= RepositorioPermiso.findByUrlAndMethod(infoPermiso.getUrl(),infoPermiso.getMetodo());
        if(rolActual != null && permisoActual !=null){
            String idRolActual =rolActual.get_id();
            String idPermisoActual= permisoActual.get_id();
            PermisoRol permisosRolActual = RepositorioPermisosRol.findByRolAndPermissions(rolActual.get_id(), permisoActual.get_id());
            return  permisosRolActual;
        }else{
            return null;
        }
    }


}
