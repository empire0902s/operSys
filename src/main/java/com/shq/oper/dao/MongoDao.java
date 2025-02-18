package com.shq.oper.dao;
/*public class MongoDao {
	
}*/


import java.util.Iterator;
import java.util.List;

import com.shq.oper.model.domain.mongo.HomePageModule;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import com.github.pagehelper.PageInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.shq.oper.model.dto.PageDto;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MongoDao<T>{
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public void insert(T entity){
		try {
			// 日志写入失败，不抛异常，确保业务逻辑正常执行
			mongoTemplate.insert(entity);
		} catch (Exception e) {
			log.error("[mongodb-insert]", e);
		}
	}
	public T findOneByObjectId(String oId, Class<T> entityClass){
		BasicDBObject obj = new BasicDBObject();
		obj.put("_id", new ObjectId(oId));
		Query query=new BasicQuery(obj);
		return mongoTemplate.findOne(query,entityClass);
	}
	
	public T findOneByuniqueKey(String uniqueKey,String uniqueValue,Class<T> entityClass) {
		Query query = Query.query(Criteria.where(uniqueKey).is(uniqueValue));
		return mongoTemplate.findOne(query, entityClass);
	}

	public void update(T entity){
		mongoTemplate.save(entity);
	}
	public void updateFirst(Query query, Update update, Class<?> entityClass){
		mongoTemplate.updateFirst(query, update, entityClass);
	}
	public WriteResult updateFirstResurnWriteResult(Query query, Update update, Class<?> entityClass){
		return mongoTemplate.updateFirst(query, update, entityClass);
	}
	public List<T> remove(Query query, Class<T> entityClass){
		return mongoTemplate.findAllAndRemove(query, entityClass);
	}
	public T findById(String id, Class<T> entityClass){
		return mongoTemplate.findById(id, entityClass);
	}
	public List<T> findByEntityAll(T entity){
		Query query = queryCondition(entity, null);
		List<T> entitys = (List<T>)mongoTemplate.find(query, entity.getClass());
		return entitys;
	}
	public List<T> findByCollectionNameAll(Query query,Class entity,String collectionName){
		List<T> entitys = (List<T>)mongoTemplate.find(query, entity, collectionName);
		return entitys;
	}
	
	public PageInfo<T> findByEntity(T entity,  PageDto page){
		Pageable pageAble = new PageRequest(page.getPage() - 1, page.getLimit());
		Query query = queryCondition(entity, null);
		int totalCount = (int)mongoTemplate.count(query, entity.getClass());
        query.with(pageAble);
        query.with(new Sort(Direction.DESC, "id"));
        List<T> entitys = (List<T>)mongoTemplate.find(query, entity.getClass());
        
        PageInfo<T> pageData = new PageInfo<>();
        pageData.setList(entitys);
        pageData.setPageNum(page.getPage());
        pageData.setPageSize(page.getLimit());
        pageData.setTotal(Long.valueOf(totalCount));
        return pageData;
	}

	public PageInfo<T> findByQuery( Query query,  PageDto page,Class<T> entityClass){
		Pageable pageAble = new PageRequest(page.getPage() - 1, page.getLimit());
		long totalCount = mongoTemplate.count(query, entityClass);
		query.with(pageAble);
		List<T> entitys = mongoTemplate.find(query, entityClass);

		PageInfo<T> pageData = new PageInfo<>();
		pageData.setList(entitys);
		pageData.setPageNum(page.getPage());
		pageData.setPageSize(page.getLimit());
		pageData.setTotal(totalCount);
		return pageData;
	}

	public PageInfo<T> findByQueryPage( Query query,  PageDto page,T entity){
		Pageable pageAble = new PageRequest(page.getPage() - 1, page.getLimit());

		int totalCount = (int)mongoTemplate.count(query, entity.getClass());

		query.with(pageAble);
		query.with(new Sort(Direction.DESC, "id"));
		List<T> entitys = (List<T>)mongoTemplate.find(query, entity.getClass());

		PageInfo<T> pageData = new PageInfo<>();
		pageData.setList(entitys);
		pageData.setPageNum(page.getPage());
		pageData.setPageSize(page.getLimit());
		pageData.setTotal(Long.valueOf(totalCount));
		return pageData;
	}

	protected Query queryCondition(T entity, Query query){
		DBObject dbDoc = new BasicDBObject();
		mongoTemplate.getConverter().write(entity, dbDoc);
		dbDoc.removeField("_id");
		
		if(query == null){
			query = new Query();
		}
		DBObject dbObject = query.getQueryObject();
		Iterator<String> it = dbDoc.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			Object value = dbDoc.get(key);
			if(value instanceof String && StringUtils.isEmpty(value.toString())){
				continue;
			}
			if("page".equals(key) || "limit".equals(key)){
				continue;
			}
			if(!dbObject.containsField(key)){
				query.addCriteria(Criteria.where(key).is(value));
			}
		}
		return query;
	}
	public T getHfRequestDataByOrId(String orderId,Class<T> entityClass) {
		Query query = Query.query(Criteria.where("ordId").is(orderId));
		return mongoTemplate.findOne(query, entityClass);
	}

	/**
	 *@author ltg
	 *@date 2018/6/26 9:55
	 * @params:[aggregation, tableName]
	 * @return: org.springframework.data.mongodb.core.aggregation.AggregationResults<com.mongodb.BasicDBObject>
	 */


	public AggregationResults<BasicDBObject> aggregate(Aggregation aggregation, String tableName){

		return  mongoTemplate.aggregate(aggregation, tableName, BasicDBObject.class);
	}

	public void updateMulti(Query query,Update update,Class<T> entityClass){
		mongoTemplate.updateMulti(query,update,entityClass);
	}

	public void insertAll(List<T> list){
		mongoTemplate.insertAll(list);
	}

	public T findOne(Query query,Class<T> entityClass) {
		return mongoTemplate.findOne(query, entityClass);
	}

	public void deleteOne(Query query,Class<T> entityClass){
		mongoTemplate.remove(query,entityClass);
	}

	public long selectCount(Query query,Class<T> entityClass){
		return mongoTemplate.count(query,entityClass);
	}

}
