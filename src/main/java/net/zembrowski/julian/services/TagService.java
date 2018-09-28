package net.zembrowski.julian.services;


import net.zembrowski.julian.domain.Pytanie;
import net.zembrowski.julian.domain.Tag;
import net.zembrowski.julian.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TagService {


    @Autowired
    private TagRepository tagRepository;


    public void addQuestionTags(Pytanie nowePytanie, String tags) {
        List<String>tagList= Arrays.asList(tags.split(" "));
        for(String tag:tagList)
        {

            tagRepository.createTag(new Tag(tag,nowePytanie));
        }
    }

    @Transactional
    public void dropTagsOfPytanie(Pytanie pytanie) {

        List<Tag>tagsTodrop=tagRepository.getTagsOfPytanie(pytanie);
        for(Tag tag:tagsTodrop)
        {
            tagRepository.dropTag(tag);
        }
    }

    /**
     * returns tags of All users!
     * @param tags
     * @return
     */
    public List<Tag> getTagList(String tags) {


        List<String>tagsNamesList=Arrays.asList(tags.split(" "));
        List<List<Tag>>tagsLists=new ArrayList<>();

        for (String tagName:tagsNamesList)
        {
            tagsLists.add(tagRepository.getTagsByName(tagName));
        }
        List<Tag>tagsList= new ArrayList<>();

        if(tagsLists.size()>0)
        {
            tagsList.addAll(tagsLists.get(0));
        }
        for(List<Tag>tagList:tagsLists)
        {
            tagsList.retainAll(tagList);
        }

        return tagsList;
    }
}
