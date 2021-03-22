package org.geektimes.configuration.microprofile.config.source.servlet;

import org.geektimes.configuration.microprofile.config.source.MapBasedConfigSource;

import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.Map;

public class ServletContextConfigSource extends MapBasedConfigSource {

    private Map<String, String> configData;

    public ServletContextConfigSource(ServletContext servletContext) {
        super("ServletContext Init Parameters", 500);

        // TODO: ugly solution
        assert configData != null;
        Enumeration<String> nameEnum = servletContext.getInitParameterNames();
        while (nameEnum.hasMoreElements()) {
            String name = nameEnum.nextElement();
            configData.put(name, servletContext.getInitParameter(name));
        }
        configData = null;
    }

    @Override
    protected void prepareConfigData(Map configData) throws Throwable {
        this.configData = configData;
    }
}
