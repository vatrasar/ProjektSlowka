package net.zembrowski.julian.services;

import net.zembrowski.julian.domain.MediaSource;
import net.zembrowski.julian.domain.MediaStatus;
import net.zembrowski.julian.domain.Pytanie;
import net.zembrowski.julian.repository.MediaSourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Scope("session")
public class MediaSourceService {
    @Autowired
     MediaSourceRepository mediaSourceRepository;


    void persistanceMediaSource(MediaSource newest)
    {
        mediaSourceRepository.persistanceMediaSource(newest);
    }

    public List<MediaSource> getMediaForQuestion(Pytanie pytanie) {

        return mediaSourceRepository.getMediaForQuestion(pytanie);
    }

    /**
     * Groups media in three groups:first is for img,secound for audion and last for video
     * @param mediaForQuestion
     *
     * @return
     */
    public List<List<MediaSource>> groupByType(List<MediaSource> mediaForQuestion)throws IllegalArgumentException
    {


        List<List<MediaSource>>mediaGroups=new ArrayList<>();


        mediaGroups.add(getGroup("jpg png gif svg",mediaForQuestion));
        mediaGroups.add(getGroup("mp3 m4a",mediaForQuestion));
        mediaGroups.add(getGroup("mp4 avi",mediaForQuestion));
        if(isNotCorrectFormat(mediaGroups,mediaForQuestion))
            throw new IllegalArgumentException("incorrect format of media!");
        return mediaGroups;
    }

    private boolean isNotCorrectFormat(List<List<MediaSource>> mediaGroups,List<MediaSource> mediaForQuestion) {
        return mediaGroups.get(0).size() + mediaGroups.get(1).size() +mediaGroups.get(2).size()!=mediaForQuestion.size();
    }

    private List<MediaSource> getGroup(final String acceptedFormats, List<MediaSource> mediaForQuestion) {

        return mediaForQuestion.stream().filter(a->{
            String path=a.getPath();
            String type=path.substring(path.length()-3,path.length());

            if (acceptedFormats.contains(type))
            {
                return true;
            }
            else
                return false;
        }).collect(Collectors.toList());

    }

    /**
     * only media with right status(answer, question)  stays
     * @param madiaGroups
     * @param status
     */
    public void filterWithStatus(List<List<MediaSource>> madiaGroups, MediaStatus status) {

        for (int i=0;i<3;i++)
        {
            List<MediaSource>group=madiaGroups.get(i);
            group=group.stream().filter(a->a.getStatus()==status).collect(Collectors.toList());

            madiaGroups.set(i,group);
        }
    }

    public int getMaxId() {

        try {
            int id= mediaSourceRepository.getMaxId();
            return id;
        } catch (NullPointerException e) {
           return 0;
        }


    }

    public void dropMediaOfPytanie(Pytanie pytanie) {

        List<MediaSource>mediaList=mediaSourceRepository.getMediaForQuestion(pytanie);
        for (MediaSource a:mediaList)
        {

            mediaSourceRepository.dropMediaSource(a);
        }
    }
}
