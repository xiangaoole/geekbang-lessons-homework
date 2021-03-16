package org.geektimes.projects.user.web.listener;

import org.geektimes.context.ComponentContext;
import org.geektimes.projects.user.management.Author;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.lang.management.ManagementFactory;

/**
 * {@link ComponentContext} 初始化器
 * ContextLoaderListener
 */
public class ComponentContextInitializerListener implements ServletContextListener {

    private ServletContext servletContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.servletContext = sce.getServletContext();
        ComponentContext context = new ComponentContext();
        context.init(servletContext);
        registerAuthorMXBean();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
//        ComponentContext context = ComponentContext.getInstance();
//        context.destroy();
        unregisterAuthorMXBean();
    }

    // Test jolokia JMX agent
    // http://localhost:8080/jolokia/read/com.haroldgao.projects.user.management:type=Author
    private void registerAuthorMXBean() {
        MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
        try {
            ObjectName objectName = new ObjectName("com.haroldgao.projects.user.management:type=Author");
            Author author = Author.getInstance();
            platformMBeanServer.registerMBean(author, objectName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void unregisterAuthorMXBean() {
        MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
        try {
            ObjectName objectName = new ObjectName("com.haroldgao.projects.user.management:type=Author");
            platformMBeanServer.unregisterMBean(objectName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
