package com.Lucroar.iQueue.Repository;

import com.Lucroar.iQueue.Entity.Status;
import com.Lucroar.iQueue.Entity.Table;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableRepository extends MongoRepository <Table, String>{
    List<Table> findAllBySize(int size);
    Table findByTableNumber(int tableNumber);
    List<Table> findByStatus(Status status);
}
