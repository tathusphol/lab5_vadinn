package com.example.lab5_vadinn;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class WordPublisher {
    protected Word words = new Word();
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping(value = "/addBad/{word}", method = RequestMethod.POST)
    public ArrayList<String> addBadWord(@PathVariable("word") String s){
        words.badWords.add(s);
        return words.badWords;
    }
    @RequestMapping(value = "/addGood/{word}", method = RequestMethod.POST)
    public ArrayList<String> addGoodWord(@PathVariable("word") String s){
        words.goodWords.add(s);
        return words.goodWords;
    }
    @RequestMapping(value = "/delBad/{word}", method = RequestMethod.DELETE)
    public ArrayList<String> deleteBadWord(@PathVariable("word") String s){
        for(int i = 0; i < words.badWords.size() ; i++){
            if(words.badWords.get(i).equals(s)){
                words.badWords.remove(i);
            }
        }
        return words.badWords;
    }
    @RequestMapping(value = "/delGood/{word}", method = RequestMethod.DELETE)
    public ArrayList<String> deleteGoodWord(@PathVariable("word") String s){
        for(int i = 0; i < words.goodWords.size() ; i++){
            if(words.goodWords.get(i).equals(s)){
                words.goodWords.remove(i);
            }
        }
        return words.goodWords;
    }
    @RequestMapping(value = "/proof/{sentence}", method = RequestMethod.POST)
    public void proofSentence(@PathVariable("sentence") String s){
        boolean badword = false;
        boolean goodword = false;
        for (int i = 0 ; i < words.badWords.size() ; i++){
            if(s.indexOf(words.badWords.get(i)) > -1){
                badword = true;
            }
        }
        for (int j = 0 ; j < words.goodWords.size() ; j++){
            if(s.indexOf(words.goodWords.get(j)) > -1){
                goodword = true;
            }
        }
        if(badword && goodword){
            rabbitTemplate.convertAndSend("Fanout","",s);
            rabbitTemplate.convertAndSend("Fanout","",s);
        }
        else if(badword){
            rabbitTemplate.convertAndSend("Direct","bad",s);
        }
        else if(goodword){
            rabbitTemplate.convertAndSend("Direct","good",s);
        }
    }
    @RequestMapping(value = "/getSentence", method = RequestMethod.GET)
    public Sentence getSentence(){
        Object value =  rabbitTemplate.convertSendAndReceive("Direct","queue", "");
        return (Sentence)value;
    }
}
