/**
 * 
 */
package in.codifi.api.filter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
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
public class MarketwatchFilter implements Filter {

//    @Autowired
//	AccessLogRepo accessLogRepo;
//    AccessLogEntity accessLogEntity = null;

	@Inject
	Logger LOG;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		Instant start = Instant.now();
		try {
//			System.out.println("Log is acrivated");
			chain.doFilter(request, response);
//			accessLogEntity.setActive_status(0);
//			accessLogEntity.setContent_type(null);
//			accessLogEntity.setUri(((HttpServletRequest) request).getRequestURI());
//			accessLogEntity.setDevice_ip(((HttpServletRequest) request).getPathTranslated());
//			accessLogRepo.save(accessLogEntity);
		} finally {
			Instant finish = Instant.now();
			long time = Duration.between(start, finish).toMillis();
			System.out.println(
					"{}: {} ms " + ((HttpServletRequest) request).getRequestURI() + ":completed in:[" + time + "ms]");
			LOG.debug("{}: {} ms " + ((HttpServletRequest) request).getRequestURI() + ":completed in:[" + time + "ms]");
		}
	}
}
