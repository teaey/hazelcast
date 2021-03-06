/*
 * Copyright (c) 2008-2017, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.collection.impl;

import com.hazelcast.collection.impl.collection.CollectionContainer;
import com.hazelcast.collection.impl.collection.CollectionItem;
import com.hazelcast.collection.impl.collection.CollectionService;
import com.hazelcast.collection.impl.list.ListService;
import com.hazelcast.collection.impl.queue.QueueContainer;
import com.hazelcast.collection.impl.queue.QueueItem;
import com.hazelcast.collection.impl.queue.QueueService;
import com.hazelcast.collection.impl.set.SetService;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spi.serialization.SerializationService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import static com.hazelcast.test.HazelcastTestSupport.getNodeEngineImpl;

public final class CollectionTestUtil {

    private CollectionTestUtil() {
    }

    public static <E> List<E> getBackupList(HazelcastInstance backupInstance, String listName) {
        CollectionService service = getNodeEngineImpl(backupInstance).getService(ListService.SERVICE_NAME);
        CollectionContainer collectionContainer = service.getContainerMap().get(listName);
        Map<Long, CollectionItem> map = collectionContainer.getMap();

        List<E> backupList = new ArrayList<E>(map.size());
        SerializationService serializationService = getNodeEngineImpl(backupInstance).getSerializationService();
        for (CollectionItem collectionItem : map.values()) {
            E value = serializationService.toObject(collectionItem.getValue());
            backupList.add(value);
        }
        return backupList;
    }

    public static <E> Queue<E> getBackupQueue(HazelcastInstance backupInstance, String queueName) {
        QueueService service = getNodeEngineImpl(backupInstance).getService(QueueService.SERVICE_NAME);
        QueueContainer container = service.getOrCreateContainer(queueName, true);
        Map<Long, QueueItem> map = container.getBackupMap();

        Queue<E> backupQueue = new LinkedList<E>();
        SerializationService serializationService = getNodeEngineImpl(backupInstance).getSerializationService();
        for (QueueItem queueItem : map.values()) {
            E value = serializationService.toObject(queueItem.getData());
            backupQueue.add(value);
        }
        return backupQueue;
    }

    public static <E> Set<E> getBackupSet(HazelcastInstance backupInstance, String setName) {
        CollectionService service = getNodeEngineImpl(backupInstance).getService(SetService.SERVICE_NAME);
        CollectionContainer collectionContainer = service.getContainerMap().get(setName);
        Map<Long, CollectionItem> map = collectionContainer.getMap();

        Set<E> backupSet = new HashSet<E>();
        SerializationService serializationService = getNodeEngineImpl(backupInstance).getSerializationService();
        for (CollectionItem collectionItem : map.values()) {
            E value = serializationService.toObject(collectionItem.getValue());
            backupSet.add(value);
        }
        return backupSet;
    }
}
