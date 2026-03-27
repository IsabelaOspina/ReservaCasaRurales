package org.example.reservacasarurales.Service;

import org.example.reservacasarurales.Config.JwtUtil;
import org.example.reservacasarurales.DTOs.Request.LoginRequest;
import org.example.reservacasarurales.DTOs.Request.RegistroRequest;
import org.example.reservacasarurales.DTOs.Response.LoginResponse;
import org.example.reservacasarurales.DTOs.Response.RegistroResponse;
import org.example.reservacasarurales.Entity.Propietario;
import org.example.reservacasarurales.Mapper.UsuarioMapper;
import org.example.reservacasarurales.Repository.PropietarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private PropietarioRepository propietarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    //HU001
    public RegistroResponse registrarPropietario(RegistroRequest request) {
        if (propietarioRepository.findByCorreoElectronico(request.getCorreoElectronico()).isPresent()) {
            throw new IllegalArgumentException("Usuario ya existe");
        }
        Propietario propietario = usuarioMapper.toEntity(request);
        propietario.setPassword(passwordEncoder.encode(request.getContraseña()));

        propietarioRepository.save(propietario);

        return usuarioMapper.toResponse(propietario);

    }

    //HU002
    public LoginResponse login(LoginRequest request) {
        Optional<Propietario> optionalUsuario =
                propietarioRepository.findByCorreoElectronico(request.getCorreoElectronico());

        // Si no existe el usuario
        if (optionalUsuario.isEmpty()) {
            throw new BadCredentialsException("Correo o contraseña incorrectos");
        }

        Propietario propietario = optionalUsuario.get();

        // Si la contraseña no coincide
        if (!passwordEncoder.matches(request.getContraseña(), propietario.getPassword())) {
            throw new BadCredentialsException("Correo o contraseña incorrectos");
        }
        String token = jwtUtil.generarToken(propietario.getCorreoElectronico());
        return new LoginResponse(token);
    }
}
