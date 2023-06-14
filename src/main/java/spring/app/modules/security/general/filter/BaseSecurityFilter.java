package spring.app.modules.security.general.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import spring.app.modules.security.general.SecurityDefinedConst;

import java.io.IOException;
import java.util.Arrays;

public class BaseSecurityFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return Arrays.stream(SecurityDefinedConst.ENDPOINTS.get("ALL")).anyMatch(e -> path.contains(e.replaceAll("[*]+", ""))) || request.getMethod().equals("OPTIONS");
    }
}
