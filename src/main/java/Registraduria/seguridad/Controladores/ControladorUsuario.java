package Registraduria.seguridad.Controladores;


import Registraduria.seguridad.Modelos.Rol;
import Registraduria.seguridad.Modelos.Usuario;
import Registraduria.seguridad.Repositorios.RepositorioRol;
import Registraduria.seguridad.Repositorios.RepositorioUsuario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

//para los console log.info o log.error etc
@Slf4j
//para permitir llamadas http desde cualquier lado o la seguridad no permite
@CrossOrigin
// para indicar que sera de tipo rest
@RestController
// para indicar la ruta url
@RequestMapping("/usuario")
public class ControladorUsuario {

    @Autowired
    RepositorioUsuario repositorioUsuario;

    @Autowired
    RepositorioRol repositorioRol;

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return repositorioUsuario.findAll();
    }

    @GetMapping("{idUsuario}")
    public Usuario buscarUsuario(@PathVariable String idUsuario) {
        return repositorioUsuario.findById(idUsuario).orElse(null);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario infoUsuario) {
        String password = convertirSHA256(infoUsuario.getContrasenia());
        infoUsuario.setContrasenia(password);
        return repositorioUsuario.save(infoUsuario);
    }

    @PutMapping("{idUsuario}")
    public Usuario actuaizarUsuario(@PathVariable String idUsuario, @RequestBody Usuario infoUsuario) {
        Usuario usuarioActual = repositorioUsuario.findById(idUsuario).orElse(null);
        if (usuarioActual != null) {
            usuarioActual.setSeudonimo(infoUsuario.getSeudonimo());
            usuarioActual.setEmail(infoUsuario.getEmail());
            usuarioActual.setContrasenia(convertirSHA256(infoUsuario.getContrasenia()));
            return repositorioUsuario.save(usuarioActual);
        } else {
            return null;
        }
    }

    @PutMapping("/{idUsuario}/rol/{idRol}")
    public Usuario asignarRolAlUsuario(@PathVariable String idUsuario, @PathVariable String idRol) {
        Usuario usuarioActual = repositorioUsuario.findById(idUsuario).orElse(null);
        Rol rol = repositorioRol.findById(idRol).orElse(null);
            if (usuarioActual != null && rol != null) {
                usuarioActual.setRol(rol);
                return usuarioActual;
            } else {
                return null;
            }
    }


    @DeleteMapping("{idUsuario}")
    public void eliminarUsuario(@PathVariable String idUsuario){
        repositorioUsuario.deleteById(idUsuario);
    }

    @PostMapping("/validar-usuario")
    public  Usuario validarUsuario(@RequestBody Usuario infoUsuario){
        Usuario usuarioValido= repositorioUsuario.findByEmail(infoUsuario.getEmail());
        String pswRegistrada=usuarioValido.getContrasenia();
        String password= convertirSHA256(infoUsuario.getContrasenia());
        if(pswRegistrada.equals(password)){
            return usuarioValido;

        }else {
            return null;
        }
    }

    public String convertirSHA256(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        byte[] hash = md.digest(password.getBytes());
        StringBuffer sb = new StringBuffer();
        for(byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
