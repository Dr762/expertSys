/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.abondar.university.expertsys.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author alex
 */
@Entity
@Table(name = "AnswerVariant")
@SequenceGenerator(name = "av_seq", sequenceName = "av_seq", initialValue = 1, allocationSize=1)
public class AnswerVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "av_seq")
    @Column(name = "ID")
    private Integer id;
    
    @Column(name="answer")
    private String answer;
    
    @ManyToOne(targetEntity=Question.class)
    @JoinColumn(name="quest_ID", referencedColumnName="ID")
    private Question question;
    
    @Column(name="price")
    private Integer price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 71 * hash + (this.answer != null ? this.answer.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AnswerVariant other = (AnswerVariant) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.answer == null) ? (other.answer != null) : !this.answer.equals(other.answer)) {
            return false;
        }
        return true;
    }
    
    public AnswerVariant withAnswer(String answer){
        setAnswer(answer);
        return this;
    }
    
    public AnswerVariant withQuestion(Question question){
        setQuestion(question);
        return this;
    }
    
    public AnswerVariant withPrice(Integer price){
        setPrice(price);
        return this;
    }

    @Override
    public String toString() {
        return  answer ;
    }

    

}
