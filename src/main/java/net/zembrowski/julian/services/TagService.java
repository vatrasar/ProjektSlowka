package net.zembrowski.julian.services;


import net.zembrowski.julian.domain.Powtorzenie;
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
import java.util.stream.Collectors;

@Service
public class TagService {


    @Autowired
    private TagRepository tagRepository;


    public void addQuestionTags(Pytanie nowePytanie, String tags) {
        List<String>tagList= Arrays.asList(tags.split(" "));
        tagList = tagsNameToLowerCase(tagList);
        for(String tag:tagList)
        {

            tagRepository.createTag(new Tag(tag,nowePytanie));
        }
    }

    private List<String> tagsNameToLowerCase(List<String> tagList) {
        tagList=tagList.stream().map(a->a.toLowerCase()).collect(Collectors.toList());
        return tagList;
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
    public List<Powtorzenie> getTagsRepetitionList(String tags) {


        List<String>tagsNamesList=Arrays.asList(tags.split(" "));
        tagsNamesList=tagsNameToLowerCase(tagsNamesList);
        List<List<Tag>>tagsLists=new ArrayList<>();

        for (String tagName:tagsNamesList)
        {
            tagsLists.add(tagRepository.getTagsByName(tagName));
        }
        List<Powtorzenie>tagsRepetitionList=new ArrayList<>();
        if(tagsLists.size()>0)
        {

            tagsRepetitionList=getTagRepetitionsList(tagsLists.get(0));
        }
        for(List<Tag>tagList:tagsLists)
        {

           List<Powtorzenie>tagRepetitionList= getTagRepetitionsList(tagList);
           tagsRepetitionList.retainAll(tagRepetitionList);

        }

        return tagsRepetitionList;
    }

    private List<Powtorzenie> getTagRepetitionsList(List<Tag> tagList) {
        return tagList.stream().map(a->{
             Powtorzenie temp=a.getPowtorzenie();
             if(temp==null)
                 temp=a.getPytanie().getPowtorzenie();
            return new Powtorzenie(temp.getNazwa(),temp.getWlasciciel(),temp.getNumer());

         }).collect(Collectors.toList());
    }

    public void addRepetitionTags(String tags, Powtorzenie nowePowtorzenie) {


        List<String>tagList= Arrays.asList(tags.split(" "));
        for(String tag:tagList)
        {

            tagRepository.createTag(new Tag(tag,nowePowtorzenie));
        }
    }

    public void dropTagsOfRepetition(Powtorzenie akutalnePowtorzenie) {

        List<Tag>repetitionTags=getRepetitionTags(akutalnePowtorzenie);
        for ( Tag tag:repetitionTags)
        {
            tagRepository.dropTag(tag);
        }

    }

    private List<Tag> getRepetitionTags(Powtorzenie akutalnePowtorzenie) {

        return tagRepository.getRepetitionTags(akutalnePowtorzenie);
    }

    public void copyGlobalTags(Powtorzenie source,final Powtorzenie destination) {



        List<Tag>tags=getRepetitionTags(source);
      tags=tags.stream().map(a->new Tag(a.getName(),destination)).collect(Collectors.toList());

        for ( Tag tag:tags)
        {
            tagRepository.createTag(tag);
        }


    }


}
