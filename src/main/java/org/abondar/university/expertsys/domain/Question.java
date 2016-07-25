/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.abondar.university.expertsys.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author alex
 */
@Entity
@Table(name = "Question")
@SequenceGenerator(name = "q_seq", sequenceName = "q_seq", initialValue = 1, allocationSize=1)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "q_seq")
    @Column(name = "ID")
    private Integer id;
    
    /**
     * Question
     */
    @Column(name="q_name")
    private String qname;

    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="question")
    private List<AnswerVariant> answers = new ArrayList<AnswerVariant>();
    
   private boolean Multi=false;
   private boolean Text=false;
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQname() {
        return qname;
    }

    public void setQname(String qname) {
        this.qname = qname;
    }

    public List<AnswerVariant> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerVariant> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return  qname ;
    }
     public Question addAnswer(AnswerVariant av){
     
         answers.add(av);
         return this;
     }
   
    public Question withQname(String qname)
    {
          setQname(qname);
     return this;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 67 * hash + (this.qname != null ? this.qname.hashCode() : 0);
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
        final Question other = (Question) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.qname == null) ? (other.qname != null) : !this.qname.equals(other.qname)) {
            return false;
        }
        return true;
    }

    public boolean isMulti() {
        return Multi;
    }

    public void setMulti(boolean Multi) {
        this.Multi = Multi;
    }

    public boolean isText() {
        return Text;
    }

    public void setText(boolean Text) {
        this.Text = Text;
    }
    
    
    
    
}
