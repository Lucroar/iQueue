package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.Entity.QueueEntry;
import com.Lucroar.iQueue.Entity.Status;
import com.Lucroar.iQueue.Entity.Table;
import com.Lucroar.iQueue.Repository.TableRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class InMemoryQueueService {
    private final TableRepository tableRepository;
    @Getter
    private final Map<Integer, Queue<QueueEntry>> queuesByTier = new ConcurrentHashMap<>();
    private final Set<Integer> occupiedTables = Collections.synchronizedSet(new HashSet<>());

    private final List<Integer> tableTiers = Arrays.asList(2, 4, 6);
    private List<Table> allTables;

    public InMemoryQueueService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
        init();
    }

    private void init() {
        allTables = tableRepository.findAll();
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

    public QueueEntry seatNextAvailable() {
        List<Table> tables = tableRepository.findAll(); // Reload from DB

        for (Table table : tables) {
            if (table.getStatus() == Status.AVAILABLE) {
                int tableSize = table.getSize();
                QueueEntry nextEntry = getNextQueueEntryFitting(tableSize);
                if (nextEntry != null) {
                    // Mark table as occupied
                    table.setStatus(Status.OCCUPIED);
                    tableRepository.save(table);

                    // Optionally assign tableId to QueueEntry
                    return nextEntry;
                }
            }
        }
        return null;
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

    public void releaseTable(String tableId) {
        Optional<Table> tableOpt = tableRepository.findById(tableId);
        if (tableOpt.isPresent()) {
            Table table = tableOpt.get();
            table.setStatus(Status.AVAILABLE);
            tableRepository.save(table);
        }
    }

    private int findAppropriateTableTier(int numPeople) {
        for (int tier : tableTiers) {
            if (numPeople <= tier) return tier;
        }
        return -1;
    }

    public List<Table> getAllTables() {
        return tableRepository.findAll();
    }

}
