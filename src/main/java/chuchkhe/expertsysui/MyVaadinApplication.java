package chuchkhe.expertsysui;

import chuchkhe.expertsysui.domain.AnswerVariant;
import chuchkhe.expertsysui.domain.Answers;
import java.util.Set;

import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import chuchkhe.expertsysui.domain.Question;
import chuchkhe.expertsysui.domain.QuestionTree;

import chuchkhe.expertsysui.ui.BasicCrudView;
import com.google.gwt.uibinder.elementparsers.IsEmptyParser;

import com.vaadin.Application;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.EntityItemProperty;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.filter.JoinFilter;
import com.vaadin.addon.jpacontainer.util.DefaultQueryModifierDelegate;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.filter.IsNull;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.terminal.gwt.server.WebApplicationContext;


import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;

import com.vaadin.ui.Label;

import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;

import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class MyVaadinApplication extends Application {

    public static final String PERSISTENCE_UNIT = "chuchkhe.expertsysui";
    private static final Logger LOG = Logger.getLogger(MyVaadinApplication.class.getName());

    @Override
    public void init() {
        //setMainWindow(new AutoCrudViews()); //сделать другой
        QuestUtil.InitQuest();//important 
        setMainWindow(new MainWindow());

    }

    @SuppressWarnings({"rawtypes", "unchecked"})


    class MainWindow extends Window {

        private String q_name = "Экспертная система,позволяющая оформить заказ на автомобиль и расчитать его цену";
    
        private Integer prevId = null;
        private String carId = null;

        private Integer getNextQuestion(EntityManager em, Integer prevId, String carId) {
            String qStr = "";
            if (prevId == null) {
                qStr = "Select qt From QuestionTree qt, Answers ans where qt.prev Is Null";

            } else {
                qStr = "Select distinct qt From QuestionTree qt LEFT OUTER JOIN qt.answ ans_var where qt.prev.id = :prevId";
            }
            //and ans_var.id IN ( SELECT ans.variant.id FROM Answers ans WHERE ans.cariD = :carId )
            Query query = em.createQuery(qStr);
            if (prevId != null) {
                query.setParameter("prevId", prevId);
//                query.setParameter("carId", carId);
            }
            LinkedList<QuestionTree> res = new LinkedList<QuestionTree>(query.getResultList());
            Collections.sort(res,
                    new Comparator<QuestionTree>() {
                        @Override
                        public int compare(QuestionTree o1, QuestionTree o2) {
                            int order;
                            if (o1 == null) {
                                if (o2 == null) {
                                    return 0;
                                } else {
                                    return -1;
                                }
                            } else {
                                if (o2 == null) {
                                    return 1;
                                } else {
                                    if (o1.getAnsw() == null) {
                                        if (o2.getAnsw() == null) {
                                            return 0;

                                        } else {
                                            if (o2.getAnsw().isEmpty()) {
                                                return 0;

                                            } else {
                                                return -1;
                                            }
                                        }
                                    } else {
                                        if (o1.getAnsw().isEmpty()) {
                                            if (o2 == null) {
                                                return 0;
                                            } else if (o2.getAnsw().isEmpty()) {
                                                return 0;
                                            } else {
                                                return -1;
                                            }
                                        } else {
                                            if (o2 == null) {
                                                return 1;
                                            } else if (o2.getAnsw().isEmpty()) {
                                                return 1;
                                            } else {
                                                order = o1.getAnsw().size() == o2.getAnsw().size() ? 0 : (o1.getAnsw().size() > o2.getAnsw().size() ? 1 : -1);
                                            }
                                        }
                                    }
                                }

                            }

                            return order;
                        }
                    });
            query = em.createQuery("SELECT DISTINCT ans.id FROM Answers ans WHERE ans.cariD = :carId and ans.variant.id IN :qVariant ");
            while (!res.isEmpty()) {
                QuestionTree qRes = res.pollLast();
                if (qRes.getAnsw() != null && !(qRes.getAnsw().isEmpty())) {
                    query.setParameter("carId", carId);
                    List<Integer> avid = new ArrayList<Integer>();
                    for (AnswerVariant av : qRes.getAnsw()) {
                        avid.add(av.getId());
                    }
                    List answRes = query.setParameter("qVariant", avid).getResultList();
                    if (answRes.size() == qRes.getAnsw().size()) {
                        return qRes.getId();
                    }
                } else {
                    return qRes.getId();
                }

            }
            return null;
        }

        public String getCarId() {

            if (carId == null) {
                carId = UUID.randomUUID().toString();

            }


            return carId;

        }

        public void fillAns(Answers ans, Question q, AnswerVariant av, String ansVal) {
            ans.setQuestion(q);
            ans.setCariD(getCarId());
            ans.setVariant(av);
            ans.setAnsw_value(ansVal);
        }

        public String showPrice(EntityManager em) {
            List res = em.createQuery("Select SUM(ans.variant.price) from Answers ans WHERE ans.cariD = :carId")
                             .setParameter("carId", getCarId())
                             .getResultList();
            
            return "Цена машины: " + res.get(0)+"$";
            
        }

        public String showCarDetails(EntityManager em){
        
         List<Object[]> res = em.createQuery("Select ans.question q,ans.variant v from Answers ans WHERE ans.cariD = :carId Order by ans.id")
                             .setParameter("carId", getCarId())
                             .getResultList();
//           
            String ret = "<p><b font=+2>Ваш заказ</b>:<br/>";
            for(Object[] row : res){
                AnswerVariant var= (AnswerVariant) row[1] ;
                Question ques = (Question) row[0] ;
                ret = ret + String.format("<b>%s:%s</b> $ %s<br/>",  ques.getQname(),var == null ? "" : var.getAnswer(), var == null ? "" : var.getPrice());
            }
//       
            return ret + "</p>";
           }
        
        public MainWindow() {



            HorizontalLayout hh = new HorizontalLayout();
            addComponent(hh);


            final Panel panel_quest = new Panel("Вопрос");
            panel_quest.setSizeUndefined();
            addComponent(panel_quest);


            final JPAContainer<QuestionTree> quest = JPAContainerFactory.make(QuestionTree.class, PERSISTENCE_UNIT);
            quest.addNestedContainerProperty("next.qname");
            quest.addNestedContainerProperty("prev.id");
  
            quest.setApplyFiltersImmediately(false);

            final JPAContainer<Question> question = JPAContainerFactory.make(Question.class, PERSISTENCE_UNIT);

            final Label QN = new Label(q_name);

            panel_quest.addComponent(QN);
            panel_quest.setWidth("600");
            panel_quest.setHeight("400");

            final OptionGroup vars = new OptionGroup("Варианты ответа:");
            vars.addItem("One");
            vars.addItem("Two");
            vars.addItem("Three");
            vars.setEnabled(false);
            vars.setVisible(false);
            panel_quest.addComponent(vars);


            final TextField ent_answ = new TextField();
            panel_quest.addComponent(ent_answ);
            ent_answ.setEnabled(false);
            ent_answ.setVisible(false);
            Button Next;
            Next = new Button("Далее", new Button.ClickListener() {
         @Override
         public void buttonClick(Button.ClickEvent event) {
             EntityItem<QuestionTree> item;
           
             if (prevId != null) {
                 EntityManager em = quest.getEntityProvider().getEntityManager();

                 Question q = question.getItem(prevId).getEntity();
                 String ansVal = ent_answ.isEnabled() ? ent_answ.getValue().toString() : null;
                 List<AnswerVariant> avList = new ArrayList<AnswerVariant>();
                 Object ansvalid = vars.getValue();
                 if (ansvalid != null) {
                     if (ansvalid.getClass().isAssignableFrom(AnswerVariant.class)) {
                         avList.add((AnswerVariant) ansvalid);
                     } else if (vars.isMultiSelect()) {
                         avList.addAll((Collection<AnswerVariant>) ansvalid);
                     }
                     if (avList.isEmpty()) {
                         avList.add(null);
                     }
                 }

                 em.getTransaction().begin();
                 for (AnswerVariant av : avList) {
                     Answers ans = new Answers();
                     fillAns(ans, q, av, ansVal);
                     em.persist(ans);
                 }
                 em.flush();
                 em.getTransaction().commit();
             } else if (carId != null) {
                 EntityManager em = quest.getEntityProvider().getEntityManager();
                 em.getTransaction().begin();
                 em.createQuery("DELETE from Answers ans WHERE ans.cariD = :carId")
                         .setParameter("carId", carId)
                         .executeUpdate();
                 em.getTransaction().commit();
             }

             Integer iid = getNextQuestion(quest.getEntityProvider().getEntityManager(), prevId, getCarId());
             LOG.log(Level.FINE, "iid:" + iid);
             item = quest.getItem(iid);


             if (iid == null) {
                 event.getButton().setEnabled(false);
             } else {
                 if (item.getEntity().getNext() != null) {
                     prevId = item.getEntity().getNext().getId();
                     EntityItemProperty p = item.getItemProperty("next.qname");
                     QN.setPropertyDataSource(p);

                     BeanItemContainer<AnswerVariant> ansvar = new BeanItemContainer<AnswerVariant>(AnswerVariant.class);
                     ansvar.addAll(item.getEntity().getNext().getAnswers());
                     vars.setContainerDataSource(ansvar);
                     vars.setEnabled(true);
                     vars.setVisible(true);
                     vars.setMultiSelect(item.getEntity().getNext().isMulti());
                     ent_answ.setVisible(item.getEntity().getNext().isText());
                     ent_answ.setEnabled(item.getEntity().getNext().isText());
                 } else {
                     prevId = null;
                     event.getButton().setEnabled(false);
                     VerticalLayout vl = new VerticalLayout();
                     vl.addComponent(new Label(showPrice(quest.getEntityProvider().getEntityManager())));
                     Label listLabel = new Label(showCarDetails(quest.getEntityProvider().getEntityManager()));
                     listLabel.setContentMode(ContentMode.HTML.ordinal());
                     vl.addComponent(listLabel);
                     panel_quest.setContent(vl);
                     
                 }

             }

         }
     });

            hh.addComponent(Next);

        

        }
    }
}
