package Elicuci.czelada.recursoshumanos.security;

import Elicuci.czelada.recursoshumanos.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. Leemos el header Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. Si no tiene token, dejamos pasar (Security decidirá si bloquea)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraemos el token (quitamos "Bearer ")
        String token = authHeader.substring(7);

        // 4. Extraemos el email del token
        String email = jwtService.extraerEmail(token);

        // 5. Si el email existe y no hay sesión activa, autenticamos
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            usuarioRepository.findByEmail(email).ifPresent(usuario -> {
                if (jwtService.esValido(token, email) && usuario.isActivo()) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    new User(email, "", Collections.emptyList()),
                                    null,
                                    Collections.emptyList()
                            );
                    auth.setDetails(new WebAuthenticationDetailsSource()
                            .buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            });
        }

        filterChain.doFilter(request, response);
    }
}