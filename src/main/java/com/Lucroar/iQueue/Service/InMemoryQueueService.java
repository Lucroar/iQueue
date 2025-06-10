package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.CashierMainMenuDTO;
import com.Lucroar.iQueue.DTO.SeatedTableInfo;
import com.Lucroar.iQueue.DTO.TableOrderDTO;
import com.Lucroar.iQueue.Entity.LastSeated;
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
    private final LastSeatedService lastSeatedService;
    private final Map<Integer, Queue<QueueEntry>> queuesByTier = new ConcurrentHashMap<>();

    private final List<Integer> tableTiers = Arrays.asList(2, 4, 6);
    @Getter
    private List<Table> allTables;

    public InMemoryQueueService(TableRepository tableRepository,
                                QueueRepository queueRepository,
                                WebSocketPublisher webSocketPublisher,
                                LastSeatedService lastSeatedService) {
        this.tableRepository = tableRepository;
        this.queueRepository = queueRepository;
        this.webSocketPublisher = webSocketPublisher;
        this.lastSeatedService = lastSeatedService;
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
                QueueEntry nextEntry = getNextQueueEntryFitting(table.getSize());
                if (nextEntry != null) {
                    // Mark table as occupied
                    table.setStatus(Status.CONFIRMING);
                    tableRepository.save(table);
                    nextEntry.setStatus(Status.CONFIRMING);
                    nextEntry.setTable_number(table.getTableNumber());
                    queueRepository.save(nextEntry);
                    sendSeatedTableInfo(nextEntry, table);
                    return;
                }
            }
        }
    }

    private void sendSeatedTableInfo(QueueEntry entry, Table table) {
        int tier = findAppropriateTableTier(entry.getNum_people());
        SeatedTableInfo info = new SeatedTableInfo(
                table.getTableNumber(),
                entry.getQueueing_number()
        );
        CashierMainMenuDTO mainMenu = new CashierMainMenuDTO();
        mainMenu.setUsername(entry.getCustomer().getUsername());
        mainMenu.setTableNumber(table.getTableNumber());
        mainMenu.setSize(table.getSize());
        mainMenu.setStatus(table.getStatus());
        lastSeatedService.newLastSeatedByTier(new LastSeated(table.getTableNumber(),
                entry.getQueueing_number(), tier));
        webSocketPublisher.sendSeatedTableInfo(String.valueOf(tier) , info);
        webSocketPublisher.sendSeatedTableInfoToCashier(mainMenu);
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
        table.setStatus(Status.DIRTY);
        webSocketPublisher.sendSeatedTableInfoToCashier(new CashierMainMenuDTO(table.getTableNumber(),
                null, table.getSize(),table.getStatus()));
        webSocketPublisher.sendTableToClean(new TableOrderDTO(null, table.getTableNumber(), null));
        tableRepository.save(table);
    }

}
