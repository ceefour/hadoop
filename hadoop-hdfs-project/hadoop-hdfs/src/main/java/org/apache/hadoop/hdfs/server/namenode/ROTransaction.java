/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.hdfs.server.namenode;

import java.io.IOException;
import java.nio.ByteBuffer;

class ROTransaction extends Transaction {
  ROTransaction(FSDirectory fsd) {
    super(fsd);
  }

  ROTransaction begin() {
    fsd.readLock();
    return this;
  }

  @Override
  FlatINode getINode(long id) {
    return getINodeFromDB(id);
  }

  @Override
  long getChild(long parentId, ByteBuffer localName) {
    return getChildFromDB(parentId, localName);
  }

  @Override
  DBChildrenView childrenView(long parent) {
    DB.INodeContainer c = fsd.db().getINode(parent);
    return new MemDBChildrenView(c.readOnlyChildren());
  }

  @Override
  public void close() throws IOException {
    fsd.readUnlock();
  }
}