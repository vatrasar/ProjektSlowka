package net.zembrowski.julian.services;

import net.zembrowski.julian.domain.MediaSource;
import net.zembrowski.julian.repository.MediaSourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("session")
public class MediaSourceService {
    @Autowired
     MediaSourceRepository mediaSourceRepository;


    void persistanceMediaSource(MediaSource newest)
    {
        mediaSourceRepository.persistanceMediaSource(newest);
    }

}
