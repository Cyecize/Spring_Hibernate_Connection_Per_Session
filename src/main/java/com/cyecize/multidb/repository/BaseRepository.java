package com.cyecize.multidb.repository;

import com.cyecize.multidb.areas.conn.services.SessionDbService;
import com.cyecize.multidb.repository.utils.ActionResult;
import com.cyecize.multidb.repository.utils.NoEntityManagerForSessionException;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Id;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class BaseRepository<E, I> {

    private final SessionDbService sessionDbService;

    protected Class<E> persistentClass;

    protected Class<I> primaryKeyType;

    protected String primaryKeyFieldName;

    protected EntityManager entityManager;

    protected CriteriaBuilder criteriaBuilder;

    @SuppressWarnings("unchecked")
    public BaseRepository(SessionDbService sessionDbService) {
        this.sessionDbService = sessionDbService;
        this.persistentClass = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.primaryKeyType = (Class<I>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        this.setPrimaryKeyFieldName();
    }

    public void persist(E entity) {
        this.execute((actionResult -> {
            this.getEntityManager().persist(entity);
            this.getEntityManager().flush();
        }));
    }

    public void merge(E entity) {
        this.execute(repositoryActionResult -> {
            this.getEntityManager().merge(entity);
            this.getEntityManager().flush();
        });
    }

    public void remove(E entity) {
        this.execute((actionResult -> {
            this.getEntityManager().remove(entity);
            this.getEntityManager().flush();
        }));
    }

    public Long count() {
        String query = String.format("SELECT count (t) FROM %s t", this.persistentClass.getSimpleName());
        return this.execute((ar) -> ar.set(this.getEntityManager().createQuery(query, Long.class).getSingleResult()), Long.class).get();
    }

    public E find(I id) {
        return this.queryBuilderSingle(((eCriteriaQuery, eRoot) -> eCriteriaQuery.where(this.criteriaBuilder.equal(eRoot.get(this.primaryKeyFieldName), id))));
    }

    public List<E> findAll() {
        //Empty method means no conditions, therefore select all
        return this.queryBuilderList(((eCriteriaQuery, eRoot) -> {}));
    }

    protected synchronized <T> ActionResult<T> execute(Consumer<ActionResult<T>> invoker, Class<? extends T> returnType) {
        this.entityManager = this.getEntityManager();
        this.criteriaBuilder = this.getEntityManager().getCriteriaBuilder();

        if (this.getEntityManager() == null) {
            throw new NoEntityManagerForSessionException("No entity manager for session.");
        }

        ActionResult<T> actionResult = new ActionResult<>();
        EntityTransaction transaction = this.getEntityManager().getTransaction();

        transaction.begin();
        try {
            invoker.accept(actionResult);
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
            throw new RuntimeException(ex);
        }

        return actionResult;
    }

    protected ActionResult<E> execute(Consumer<ActionResult<E>> invoker) {
        return this.execute(invoker, this.persistentClass);
    }

    @SuppressWarnings("unchecked")
    protected <T> T queryBuilderSingle(BiConsumer<CriteriaQuery<E>, Root<E>> invoker, Class<T> returnType) {
        return this.execute(ar -> {
            CriteriaQuery<E> criteria = this.criteriaBuilder.createQuery(this.persistentClass);
            Root<E> root = criteria.from(this.persistentClass);
            criteria.select(root);

            invoker.accept(criteria, root);

            ar.set((T) this.getEntityManager().createQuery(criteria).getResultStream().findFirst().orElse(null));
        }, returnType).get();
    }

    protected E queryBuilderSingle(BiConsumer<CriteriaQuery<E>, Root<E>> invoker) {
        return this.queryBuilderSingle(invoker, this.persistentClass);
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> queryBuilderList(BiConsumer<CriteriaQuery<E>, Root<E>> invoker, Class<T> returnType) {
        return this.execute(ar -> {
            CriteriaQuery<E> criteria = this.criteriaBuilder.createQuery(this.persistentClass);
            Root<E> root = criteria.from(this.persistentClass);
            criteria.select(root);

            invoker.accept(criteria, root);

            ar.set(this.getEntityManager().createQuery(criteria).getResultList());
        }, List.class).get();
    }

    protected List<E> queryBuilderList(BiConsumer<CriteriaQuery<E>, Root<E>> invoker) {
        return this.queryBuilderList(invoker, this.persistentClass);
    }

    protected EntityManager getEntityManager() {
        return this.sessionDbService.getConnection().getEntityManager();
    }

    private void setPrimaryKeyFieldName() {
        Field primaryKeyField = Arrays.stream(this.persistentClass.getDeclaredFields())
                .filter(f -> f.getType() == this.primaryKeyType && f.isAnnotationPresent(Id.class))
                .findFirst().orElse(null);

        if (primaryKeyField == null) {
            throw new RuntimeException(String.format("Entity %s does not have primary key", this.persistentClass.getName()));
        }

        Column columnAnnotation = primaryKeyField.getAnnotation(Column.class);

        if (columnAnnotation != null && !columnAnnotation.name().equals("")) {
            this.primaryKeyFieldName = columnAnnotation.name();
        } else {
            this.primaryKeyFieldName = primaryKeyField.getName();
        }
    }
}
