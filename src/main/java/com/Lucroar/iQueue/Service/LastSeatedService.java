package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.Entity.LastSeated;
import com.Lucroar.iQueue.Repository.LastSeatedRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LastSeatedService {
    private final LastSeatedRepository lastSeatedRepository;

    public LastSeatedService(LastSeatedRepository lastSeatedRepository) {
        this.lastSeatedRepository = lastSeatedRepository;
    }

    public LastSeated getLastSeatedByTier(int tier) {
        Optional<LastSeated> lastSeatedOpt = lastSeatedRepository.findLastSeatedByTier(tier);
        return lastSeatedOpt.orElse(null);
    }

    public void newLastSeatedByTier(LastSeated lastSeated) {
        LastSeated lastSeatedByTier = getLastSeatedByTier(lastSeated.getTier());
        if (lastSeatedByTier != null) lastSeatedRepository.delete(lastSeatedByTier);
        lastSeatedRepository.save(lastSeated);
    }
}
