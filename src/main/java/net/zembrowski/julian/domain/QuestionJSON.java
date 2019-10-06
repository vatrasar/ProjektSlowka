package net.zembrowski.julian.domain;

import org.aspectj.weaver.patterns.TypePatternQuestions;

import java.util.List;

/**
 * class designed to send question with informations about media
 */
public class QuestionJSON extends Pytanie {
    private List<MediaSource>photos;
    private List<MediaSource>videos;
    private List<MediaSource>sounds;
    public QuestionJSON(Pytanie question,List<MediaSource> photos,List<MediaSource> sounds,List<MediaSource> videos)
    {
        super(question);
        this.photos=photos;
        this.sounds=sounds;
        this.videos=videos;
    }

    public List<MediaSource> getPhotos() {
        return photos;
    }

    public List<MediaSource> getVideos() {
        return videos;
    }

    public List<MediaSource> getSounds() {
        return sounds;
    }
}
