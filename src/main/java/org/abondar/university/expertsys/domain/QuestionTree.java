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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author alex
 */
@Entity
@Table(name = "QuestionTree")
@SequenceGenerator(name = "qt_seq", sequenceName = "qt_seq", initialValue = 1, allocationSize=1)
public class QuestionTree {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "qt_seq")
    @Column(name = "ID")
    private Integer id;
    
    @ManyToOne(cascade={CascadeType.ALL}, optional=true)
    @JoinColumn(name="q_prev")
    private Question prev;
    
    @ManyToOne(cascade={CascadeType.ALL}, optional=true)
    @JoinColumn(name="q_next")
    private Question next;
    
    @Column(name="answ_val")
    private String val;
    
    @OneToMany(fetch= FetchType.EAGER)
    private List<AnswerVariant> answ = new ArrayList();
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Question getPrev() {
        return prev;
    }

    public void setPrev(Question prev) {
        this.prev = prev;
    }

    public Question getNext() {
        return next;
    }

    public void setNext(Question next) {
        this.next = next;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public List<AnswerVariant> getAnsw() {
        return answ;
    }

    public void setAnsw(List<AnswerVariant> answ) {
        this.answ = answ;
    }
    
    public QuestionTree addAnsw(AnswerVariant avar){
        answ.add(avar);
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 37 * hash + (this.prev != null ? this.prev.hashCode() : 0);
        hash = 37 * hash + (this.next != null ? this.next.hashCode() : 0);
        hash = 37 * hash + (this.answ != null ? this.answ.hashCode() : 0);
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
        final QuestionTree other = (QuestionTree) obj;
        return true;
    }
    
}
