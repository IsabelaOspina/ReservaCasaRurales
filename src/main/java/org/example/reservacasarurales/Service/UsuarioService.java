package org.example.reservacasarurales.Service;

import lombok.RequiredArgsConstructor;
import org.example.reservacasarurales.Config.JwtUtil;
import org.example.reservacasarurales.DTOs.Request.ClienteRequest;
import org.example.reservacasarurales.DTOs.Request.PropietarioRequest;
import org.example.reservacasarurales.DTOs.Request.UsuarioRequest;
import org.example.reservacasarurales.Entity.Cliente;
import org.example.reservacasarurales.Entity.Propietario;
import org.example.reservacasarurales.Entity.Rol;
import org.example.reservacasarurales.Entity.Usuario;
import org.example.reservacasarurales.Mapper.ClienteMapper;
import org.example.reservacasarurales.Mapper.PropietarioMapper;
import org.example.reservacasarurales.Mapper.UsuarioMapper;
import org.example.reservacasarurales.Repository.ClienteRepository;
import org.example.reservacasarurales.Repository.PropietarioRepository;
import org.example.reservacasarurales.Repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final PropietarioRepository propietarioRepository;

    private final UsuarioMapper usuarioMapper;
    private final ClienteMapper clienteMapper;
    private final PropietarioMapper propietarioMapper;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String login(String correoElectronico, String password){

        Usuario usuario = usuarioRepository.findByCorreoElectronico(correoElectronico)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if(!passwordEncoder.matches(password, usuario.getPassword())){
            throw new RuntimeException("Contraseña incorrecta");
        }

        return jwtUtil.generarToken(usuario.getCorreoElectronico(), usuario.getRol().name());
    }

    public void registrarCliente(ClienteRequest clienteDTO){

        Usuario usuario = new Usuario();
        usuario.setUsername(clienteDTO.getUsername());
        usuario.setCorreoElectronico(clienteDTO.getCorreoElectronico());
        usuario.setPassword(passwordEncoder.encode(clienteDTO.getPassword()));
        usuario.setRol(Rol.ROLE_CLIENTE);


        usuarioRepository.save(usuario);

        Cliente cliente = clienteMapper.toEntity(clienteDTO);
        cliente.setUsuario(usuario);

        clienteRepository.save(cliente);
    }

    public void registrarPropietario(
            PropietarioRequest propietarioDTO) {

        Usuario usuario = new Usuario();
        usuario.setUsername(propietarioDTO.getUsername());
        usuario.setCorreoElectronico(propietarioDTO.getCorreoElectronico());
        usuario.setPassword(passwordEncoder.encode(propietarioDTO.getPassword()));
        usuario.setRol(Rol.ROLE_PROPIETARIO);

        usuarioRepository.save(usuario);

        Propietario propietario = propietarioMapper.toEntity(propietarioDTO);
        propietario.setUsuario(usuario);
        
        propietarioRepository.save(propietario);
    }
}