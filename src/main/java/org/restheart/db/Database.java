/*
 * RESTHeart - the Web API for MongoDB
 * Copyright (C) SoftInstigate Srl
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.restheart.db;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.util.JSONParseException;


import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import org.bson.BsonDocument;

import org.restheart.handlers.IllegalQueryParamenterException;

/**
 *
 * @author Maurizio Turatti {@literal <maurizio@softinstigate.com>}
 */
public interface Database {

    BasicDBObject METADATA_QUERY = new BasicDBObject("_id", "_properties");

    /**
     *
     * @param dbName
     * @param collectionName
     * @param requestEtag
     * @param checkEtag
     * @return HTTP status code
     */
    OperationResult deleteCollection(String dbName, String collectionName, String requestEtag, boolean checkEtag);

    /**
     *
     * @param dbName
     * @param requestEtag
     * @param checkEtag
     * @return HTTP status code
     */
    OperationResult deleteDatabase(String dbName, String requestEtag, boolean checkEtag);

    /**
     * @param dbName
     * @return true if DB dbName exists
     *
     */
    boolean doesDbExist(String dbName);
    
    /**
     * @param dbName
     * @param collName
     * @return true if exists the collection collName exists in DB dbName
     *
     */
    boolean doesCollectionExist(String dbName, String collName);

    /**
     *
     * @param dbName
     * @param collectionName
     * @return A Collection
     */
    DBCollection getCollectionLegacy(String dbName, String collectionName);

    /**
     *
     * @param dbName
     * @param collectionName
     * @return A Collection
     */
    MongoCollection<BsonDocument> getCollection(String dbName, String collectionName);
    
    /**
     *
     * @param collection
     * @param page
     * @param pagesize
     * @param sortBy
     * @param filter
     * @param keys
     * @param cursorAllocationPolicy
     * @return Collection Data as ArrayList of DBObject
     */
    ArrayList<DBObject> getCollectionData(DBCollection collection, int page, int pagesize, Deque<String> sortBy, Deque<String> filter, Deque<String> keys, DBCursorPool.EAGER_CURSOR_ALLOCATION_POLICY cursorAllocationPolicy);

    /**
     *
     * @param dbName
     * @param collectionName
     * @return Collection properties
     */
    BsonDocument getCollectionProperties(String dbName, String collectionName);

    /**
     *
     * @param collection
     * @param filters
     * @return the number of documents in the given collection (taking into
     * account the filters in case)
     */
    long getCollectionSize(DBCollection collection, Deque<String> filters);

    /**
     *
     * @param dbName
     * @return the Mongo DB
     */
    DB getDB(String dbName);

    /**
     * @param collections the collection names
     * @return the number of collections in this db
     *
     */
    long getDBSize(List<String> collections);

    /**
     * @param dbName
     * @param collections the collections list as got from getCollectionNames()
     * @param page
     * @param pagesize
     * @return the db data
     * @throws org.restheart.handlers.IllegalQueryParamenterException
     *
     */
    List<BsonDocument> getDatabaseData(String dbName, List<String> collections, int page, int pagesize) throws IllegalQueryParamenterException;

    /**
     *
     * @return A List of database names
     */
    List<String> getDatabaseNames();

    /**
     *
     * @param db
     * @return A List of collection names
     */
    List<String> getCollectionNames(DB db);

    /**
     * @param dbName
     * @return the db props
     *
     */
    BsonDocument getDatabaseProperties(String dbName);

    /**
     *
     * @param dbName
     * @param collectionName
     * @param content
     * @param requestEtag
     * @param updating
     * @param patching
     * @param checkEtag
     * @return
     */
    OperationResult upsertCollection(String dbName, String collectionName, DBObject content, String requestEtag, boolean updating, boolean patching, boolean checkEtag);

    /**
     *
     * @param dbName
     * @param content
     * @param requestEtag
     * @param updating
     * @param patching
     * @param checkEtag
     * @return
     */
    OperationResult upsertDB(String dbName, DBObject content, String requestEtag, boolean updating, boolean patching, boolean checkEtag);

    /**
     *
     * @param dbName
     * @param collection
     * @param indexId
     * @return the operation result
     */
    int deleteIndex(String dbName, String collection, String indexId);

    /**
     *
     * @param dbName
     * @param collectionName
     * @return A List of indexes for collectionName in dbName
     */
    List<DBObject> getCollectionIndexes(String dbName, String collectionName);

    /**
     * Returs the DBCursor of the collection applying sorting and filtering.
     *
     * @param collection the mongodb DBCollection object
     * @param sortBy the Deque collection of fields to use for sorting (prepend
     * field name with - for descending sorting)
     * @param filters the filters to apply. it is a Deque collection of mongodb
     * query conditions.
     * @param keys
     * @return
     * @throws JSONParseException
     */
    DBCursor getCollectionDBCursor(DBCollection collection, Deque<String> sortBy, Deque<String> filters, Deque<String> keys) throws JSONParseException;

    /**
     *
     * @param dbName
     * @param collection
     * @param keys
     * @param options
     */
    void createIndex(String dbName, String collection, DBObject keys, DBObject options);

}
