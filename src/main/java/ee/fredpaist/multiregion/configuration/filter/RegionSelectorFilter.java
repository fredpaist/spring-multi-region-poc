package ee.fredpaist.multiregion.configuration.filter;

import ee.fredpaist.multiregion.configuration.RegionContext;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "mongodb.multi.enabled", havingValue = "true")
public class RegionSelectorFilter implements Filter {

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        var req = (HttpServletRequest) request;
        var regionHeader = req.getHeader("X-Region");
        RegionContext.setRegionId(regionHeader);
        chain.doFilter(request, response);
    }
}
