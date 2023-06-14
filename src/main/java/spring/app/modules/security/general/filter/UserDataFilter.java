package spring.app.modules.security.general.filter;

import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import spring.app.modules.security.domain.Status;
import spring.app.modules.security.service.UserDataService;

import java.io.IOException;

@NoArgsConstructor
public class UserDataFilter extends BaseSecurityFilter {

    @Autowired
    private UserDataService dataService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Status status = dataService.writeAction(UserAgent.parseUserAgentString(request.getHeader("User-Agent")), request.getRequestURI());
        // Edit if using Postman
        if (status.isOk() || status.isAuthRequired()) {
            filterChain.doFilter(request, response);
        }
    }
}
