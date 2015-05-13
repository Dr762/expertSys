/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chuchkhe.expertsysui.domain;

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
@Table(name = "Answers")
@SequenceGenerator(name = "a_seq", sequenceName = "a_seq", initialValue = 1, allocationSize=1)
public class Answers {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "a_seq")
    @Column(name = "ID")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name="quest_ID")
    private Question question;
    
    
    @Column(name="answ_value")
    private String answ_value;
    
    @ManyToOne
    @JoinColumn(name="answ_id")
    private AnswerVariant variant;
    
    @Column(name="car_ID")
    private String cariD;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getAnsw_value() {
        return answ_value;
    }

    public void setAnsw_value(String answ_value) {
        this.answ_value = answ_value;
    }

    public String getCariD() {
        return cariD;
    }

    public void setCariD(String cariD) {
        this.cariD = cariD;
    }

    public AnswerVariant getVariant() {
        return variant;
    }

    public void setVariant(AnswerVariant variant) {
        this.variant = variant;
    }
    
    
}
