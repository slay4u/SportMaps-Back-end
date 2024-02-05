package sport_maps.security.general.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import sport_maps.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import sport_maps.security.general.JwtProvider;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@NoArgsConstructor
public class JwtAuthenticationFilter extends BaseSecurityFilter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String header = request.getHeader(AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            if (request.getMethod().equals("GET")) // could be a better way, permitAll not working w/o it
                chain.doFilter(request, response); // maybe configure shouldNotFilter in BaseFilter
            return;
        }
        final String jwt = header.substring(7);
        try {
            jwtProvider.validateToken(jwt);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return;
        }
        final String userEmail = jwtProvider.getUserEmailFromJwt(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }
}
