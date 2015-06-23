package com.interlink.dao;

import org.neo4j.graphdb.traversal.TraversalDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * Created by DmitriyS on 23/06/2015.
 */
@Component
public class BaseDao<T> implements GraphRepository<T> {

    @Autowired
    protected Neo4jTemplate template;

    public Class<T> getClassOfT() {
        ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) superclass.getActualTypeArguments()[0];
    }

    @Override
    public <S extends T> S save(S s) {
        return template.save(s);
    }

    @Override
    public <S extends T> Iterable<S> save(Iterable<S> iterable) {
        return template.save(iterable);
    }

    @Override
    public T findOne(Long aLong) {
        return template.findOne(aLong, getClassOfT());
    }

    @Override
    public boolean exists(Long aLong) {
        return findOne(aLong) != null;
    }

    @Override
    public Result<T> findAll() {
        return template.findAll(getClassOfT());
    }

    @Override
    public Iterable<T> findAll(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public void delete(T t) {
        template.delete(t);
    }

    @Override
    public void delete(Iterable<? extends T> iterable) {
        for (T object : iterable) {
            template.delete(object);
        }
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Result<T> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Class getStoredJavaType(Object o) {
        return null;
    }

    @Override
    public Result<T> query(String s, Map<String, Object> map) {
        return (Result<T>) template.query(s, map);
    }

    @Override
    public <U extends T> void saveOnly(U u) {

    }

    @Override
    public T findByPropertyValue(String s, Object o) {
        return null;
    }

    @Override
    public Result<T> findAllByPropertyValue(String s, Object o) {
        return null;
    }

    @Override
    public Result<T> findAllByQuery(String s, Object o) {
        return null;
    }

    @Override
    public Result<T> findAllByRange(String s, Number number, Number number1) {
        return null;
    }

    @Override
    public T findBySchemaPropertyValue(String s, Object o) {
        return null;
    }

    @Override
    public Result<T> findAllBySchemaPropertyValue(String s, Object o) {
        return null;
    }

    @Override
    public <N> Iterable<T> findAllByTraversal(N n, TraversalDescription traversalDescription) {
        return (Iterable<T>) template.traverse(n, traversalDescription);
    }
}
