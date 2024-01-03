/**
 * 
 */
package in.codifi.admin.filter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

/**
 * @author mohup
 *
 */
@Component
@WebFilter
public class ClientFilter implements Filter {

	@Inject
	Logger LOG;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		Instant start = Instant.now();
		try {
			chain.doFilter(request, response);
		} finally {
			Instant finish = Instant.now();
			long time = Duration.between(start, finish).toMillis();
			LOG.debug("{}: {} ms " + ((HttpServletRequest) request).getRequestURI() + ":completed in:[" + time + "ms]");
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
