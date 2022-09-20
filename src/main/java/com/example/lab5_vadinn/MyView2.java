package com.example.lab5_vadinn;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;

@Route(value = "index2")
public class MyView2  extends FormLayout {
    private Button goodword, badword, sentence, show;
    private TextField addword, addsentence;
    private ComboBox<String> boxbad, boxgood;
    private TextArea badwordarea, goodwordarea;
    private Word words = new Word();
    public MyView2(){
        this.setResponsiveSteps(new ResponsiveStep("40em", 2));
        addword = new TextField();
        addsentence = new TextField();
        addword.setLabel("Add Word");
        addsentence.setLabel("Add Sentence");
        goodword = new Button("Add Good Word");
        badword = new Button("Add Bad Word");
        sentence = new Button("Add Sentence");
        show = new Button("Show Sentence");
        boxbad = new ComboBox<>();
        boxbad.setItems(words.badWords);
        boxgood = new ComboBox<>();
        boxgood.setItems(words.goodWords);
        boxbad.setLabel("Bad Words");
        boxgood.setLabel("Good Words");
        badwordarea = new TextArea();
        badwordarea.setLabel("Bad Sentences");
        badwordarea.setEnabled(false);
        goodwordarea = new TextArea();
        goodwordarea.setLabel("Good Sentences");
        goodwordarea.setEnabled(false);
        add(addword, addsentence, goodword, sentence, badword, goodwordarea, boxgood, badwordarea, boxbad, show);
        goodword.addClickListener(event ->{
             String text = addword.getValue();
            ArrayList response = WebClient.create().post().uri("http://localhost:8080/addGood/" + text).retrieve().bodyToMono(ArrayList.class).block();
            boxgood.setItems(response);
        });
        badword.addClickListener(event ->{
            String text = addword.getValue();
            ArrayList response = WebClient.create().post().uri("http://localhost:8080/addBad/" + text).retrieve().bodyToMono(ArrayList.class).block();
            boxbad.setItems(response);
        });
        sentence.addClickListener(event ->{
            String text = addsentence.getValue();
            ArrayList response = WebClient.create().post().uri("http://localhost:8080/proof/" + text).retrieve().bodyToMono(ArrayList.class).block();
        });
        show.addClickListener(event ->{
            String text = addsentence.getValue();
            Sentence response = WebClient.create().get().uri("http://localhost:8080/getSentence/").retrieve().bodyToMono(Sentence.class).block();
            goodwordarea.setValue(response.goodSentences + "");
            badwordarea.setValue(response.badSentences + "");
        });
    }
}
