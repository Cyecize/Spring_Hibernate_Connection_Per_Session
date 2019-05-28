package com.cyecize.multidb.areas.conn.interceptors;

import com.cyecize.multidb.areas.conn.annotations.DisableJpaCache;
import com.cyecize.multidb.areas.conn.constants.DbConnectionConstants;
import com.cyecize.multidb.areas.conn.models.UserDbConnection;
import com.cyecize.multidb.areas.conn.services.SessionDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class DatabaseConnectionInterceptor implements HandlerInterceptor {

    private final SessionDbService sessionDbService;

    @Autowired
    public DatabaseConnectionInterceptor(SessionDbService sessionDbService) {
        this.sessionDbService = sessionDbService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (handler instanceof HandlerMethod) {
            this.sessionDbService.sendKeepAlive();

            if (!request.getRequestURI().equals(DbConnectionConstants.DB_CONNECT_ROUTE)) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;

                if (!this.sessionDbService.hasOpenConnection()) {
                    this.sessionDbService.setConnection(null);

                    //Logout if user is present.
                    new SecurityContextLogoutHandler().logout(request, null, null);

                    response.sendRedirect(DbConnectionConstants.DB_CONNECT_ROUTE);
                    return false;
                }

                final UserDbConnection dbConnection = this.sessionDbService.getConnection();

                //Refresh the entity manager if required.
                if (dbConnection.getOrmConnection() != null) {
                    if (handlerMethod.getMethod().isAnnotationPresent(DisableJpaCache.class) || handlerMethod.getBeanType().isAnnotationPresent(DisableJpaCache.class)) {
                        dbConnection.getEntityManager().close();
                        dbConnection.setEntityManager(dbConnection.getOrmConnection().createEntityManager());
                    }
                }
            }
        }

        return true;
    }
}
