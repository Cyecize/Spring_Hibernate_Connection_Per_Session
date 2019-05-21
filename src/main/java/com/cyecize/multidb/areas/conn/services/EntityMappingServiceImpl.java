package com.cyecize.multidb.areas.conn.services;

import com.cyecize.multidb.MultiDbApplication;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.StandardServletEnvironment;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Service
public class EntityMappingServiceImpl implements EntityMappingService {

    private List<Class<?>> mappedEntities;

    @Override
    public List<Class<?>> getMappedEntities() {
        if (this.mappedEntities == null) {
            this.findMappedEntities();
        }

        return this.mappedEntities;
    }

    private void findMappedEntities() {
        this.mappedEntities = new ArrayList<>();

        final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false, new StandardServletEnvironment());

        provider.addIncludeFilter(new AnnotationTypeFilter(Entity.class, false, false));

        for (BeanDefinition beanDefinition : provider.findCandidateComponents(this.getBasePackage())) {
            try {
                this.mappedEntities.add(Class.forName(beanDefinition.getBeanClassName()));
            } catch (ClassNotFoundException ignored) {
            }
        }
    }

    private String getBasePackage() {
        return MultiDbApplication.class.getPackageName();
    }
}
