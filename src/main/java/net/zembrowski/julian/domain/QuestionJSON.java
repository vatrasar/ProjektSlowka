package net.zembrowski.julian.domain;

import org.aspectj.weaver.patterns.TypePatternQuestions;

import java.util.List;
import java.util.stream.Collectors;

/**
 * class designed to send question with informations about media
 */
public class QuestionJSON extends Pytanie {
    private List<String>photos;
    private List<String>videos;
    private List<String>sounds;
    public QuestionJSON(Pytanie question,List<MediaSource> photos,List<MediaSource> sounds,List<MediaSource> videos)
    {
        super(question);
        this.photos=photos.stream().map(MediaSource::getPath).collect(Collectors.toList());
        this.sounds=sounds.stream().map(MediaSource::getPath).collect(Collectors.toList());
        this.videos=videos.stream().map(MediaSource::getPath).collect(Collectors.toList());
        this.statistics=new Statistics(0,0,0);
    }

    public List<String> getPhotos() {
        return photos;
    }

    public List<String> getVideos() {
        return videos;
    }

    public List<String> getSounds() {
        return sounds;
    }
}
