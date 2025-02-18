package com.shq.oper.dao.mongo;

import org.springframework.stereotype.Repository;

import com.shq.oper.dao.MongoDao;
import com.shq.oper.model.domain.mongo.AdminActionLog;
import com.shq.oper.model.domain.mongo.GuestBrowsingLog;

@Repository
public class GuestBrowsingLogMongo extends MongoDao<GuestBrowsingLog> {

}
