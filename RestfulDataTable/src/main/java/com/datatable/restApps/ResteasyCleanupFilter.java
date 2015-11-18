
package com.datatable.restApps;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

/**
 *
 * @author Ashok
 */
public class ResteasyCleanupFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.getServletContext().setAttribute(ResteasyProviderFactory.class.getName(), null);
        request.getServletContext().setAttribute(Dispatcher.class.getName(), null);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        
    }

}
