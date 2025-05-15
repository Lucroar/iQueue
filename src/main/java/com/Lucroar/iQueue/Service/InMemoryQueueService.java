package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.Entity.QueueEntry;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class InMemoryQueueService {
    private final List<Integer> tableTiers = Arrays.asList(2, 4, 6);
    @Getter
    private final Map<Integer, Queue<QueueEntry>> queuesByTier = new ConcurrentHashMap<>();

    public InMemoryQueueService() {
        // Initialize queues for each table tier
        for (int tier : tableTiers) {
            queuesByTier.put(tier, new ConcurrentLinkedQueue<>());
        }
    }

    private int findAppropriateTableTier(int groupSize) {
        return tableTiers.stream()
                .filter(tier -> tier >= groupSize)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Group too large for any available table tier"));
    }

    public void enqueue(QueueEntry customerQueueEntry) {
        int assignedTier = findAppropriateTableTier(customerQueueEntry.getNum_people());
        queuesByTier.get(assignedTier).offer(customerQueueEntry);
    }

    public QueueEntry seatNextForTable(int tableTier) {
        Queue<QueueEntry> queue = queuesByTier.get(tableTier);
        if (queue == null) return null;

        Iterator<QueueEntry> iterator = queue.iterator();
        while (iterator.hasNext()) {
            QueueEntry candidate = iterator.next();
            if (candidate.getNum_people() <= tableTier) {
                iterator.remove();
                return candidate;
            }
        }

        // No exact fit found, look for smaller queues to upgrade
        for (int lowerTier : tableTiers) {
            if (lowerTier < tableTier) {
                Queue<QueueEntry> lowerQueue = queuesByTier.get(lowerTier);
                Iterator<QueueEntry> lowIter = lowerQueue.iterator();
                while (lowIter.hasNext()) {
                    QueueEntry candidate = lowIter.next();
                    if (candidate.getNum_people() <= tableTier) {
                        lowIter.remove();
                        return candidate;
                    }
                }
            }
        }

        return null; // No one suitable
    }

}
