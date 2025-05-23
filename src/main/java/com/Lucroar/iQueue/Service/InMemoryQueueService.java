package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.SeatedTableInfo;
import com.Lucroar.iQueue.Entity.QueueEntry;
import com.Lucroar.iQueue.Entity.Status;
import com.Lucroar.iQueue.Entity.Table;
import com.Lucroar.iQueue.Repository.QueueRepository;
import com.Lucroar.iQueue.Repository.TableRepository;
import lombok.Getter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class InMemoryQueueService {
    private final TableRepository tableRepository;
    private final QueueRepository queueRepository;
    private final WebSocketPublisher webSocketPublisher;
    private final Map<Integer, Queue<QueueEntry>> queuesByTier = new ConcurrentHashMap<>();

    private final List<Integer> tableTiers = Arrays.asList(2, 4, 6);
    @Getter
    private List<Table> allTables;

    public InMemoryQueueService(TableRepository tableRepository, QueueRepository queueRepository, WebSocketPublisher webSocketPublisher) {
        this.tableRepository = tableRepository;
        this.queueRepository = queueRepository;
        this.webSocketPublisher = webSocketPublisher;
        init();
    }

    private void init() {
        allTables = tableRepository.findAll(); // Load table structure

        // Initialize queue for each tier
        for (int tier : tableTiers) {
            queuesByTier.put(tier, new ConcurrentLinkedQueue<>());
        }
    }

    public void enqueue(QueueEntry entry) {
        int tier = findAppropriateTableTier(entry.getNum_people());
        if (tier > 0) {
            queuesByTier.get(tier).offer(entry);
        } else {
            throw new IllegalArgumentException("No table tier can handle " + entry.getNum_people());
        }
    }

    @Scheduled (fixedRate = 5000) // Runs every 5 seconds
    public void autoSeatCustomers() {
        seatNextAvailable();
    }

    public void seatNextAvailable() {
        List<Table> tables = tableRepository.findAll(); // Reload from DB

        for (Table table : tables) {
            if (table.getStatus() == Status.AVAILABLE) {
                int tableSize = table.getSize();
                QueueEntry nextEntry = getNextQueueEntryFitting(tableSize);
                if (nextEntry != null) {
                    // Mark table as occupied
                    table.setStatus(Status.OCCUPIED);
                    tableRepository.save(table);
                    nextEntry.setStatus(Status.SEATED);
                    nextEntry.setTable_number(table.getTableNumber());
                    queueRepository.save(nextEntry);
                    sendSeatedTableInfo(nextEntry, table);
                    return;
                }
            }
        }
    }

    private void sendSeatedTableInfo(QueueEntry entry, Table table) {
        String tier = String.valueOf(findAppropriateTableTier(entry.getNum_people()));
        SeatedTableInfo info = new SeatedTableInfo(
                table.getTableNumber(),
                entry.getQueueing_number()
        );
        webSocketPublisher.sendSeatedTableInfo(tier , info);
    }

    private QueueEntry getNextQueueEntryFitting(int tableSize) {
        for (int tier : tableTiers) {
            if (tier <= tableSize) {
                Queue<QueueEntry> queue = queuesByTier.get(tier);
                Iterator<QueueEntry> it = queue.iterator();
                while (it.hasNext()) {
                    QueueEntry entry = it.next();
                    if (entry.getNum_people() <= tableSize) {
                        it.remove();
                        return entry;
                    }
                }
            }
        }
        return null;
    }

    private int findAppropriateTableTier(int numPeople) {
        for (int tier : tableTiers) {
            if (numPeople <= tier) return tier;
        }
        return -1;
    }

    public void releaseTable(int tableNumber) {
        Table table = tableRepository.findByTableNumber(tableNumber);
        if (table != null) {
            table.setStatus(Status.DIRTY);
            tableRepository.save(table);
            seatNextAvailable(); // Try to seat someone right away
        }
    }

}
