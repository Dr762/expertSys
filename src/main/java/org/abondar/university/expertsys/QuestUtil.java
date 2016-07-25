/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.abondar.university.expertsys;

import org.abondar.university.expertsys.domain.AnswerVariant;
import org.abondar.university.expertsys.domain.Question;
import org.abondar.university.expertsys.domain.QuestionTree;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author alex
 */
public class QuestUtil {

    public static final String PERSISTENCE_UNIT = "chuchkhe.expertsysui";

    public static void InitQuest() {

        EntityManager em = JPAContainerFactory
                .createEntityManagerForPersistenceUnit(PERSISTENCE_UNIT);

        long size = (Long) em.createQuery("SELECT COUNT(q) FROM Question q").getSingleResult();
        if (size == 0) {
            em.getTransaction().begin();
        //  AnswerPair pair = new AnswerPair("", 0);
          QuestionTree qt = createQTree(null, createQuestion("Назначение автомобиля", new AnswerPair[]{ new AnswerPair("Автомобиль предствительского класса",100000), new AnswerPair("Суперкар",150000)}), null, null);
     
            em.persist(qt);

            QuestionTree qt1 = createQTree(qt.getNext(), createQuestion("Тип автомобиля", new AnswerPair[]{new AnswerPair("Внедорожник",30000),new AnswerPair("Легковая",20000)}), null, null);
            em.persist(qt1);

            qt = qt1;

            Set<Question> prevq = new HashSet<Question>();

            Map<String, AnswerVariant> avMap = new HashMap<String, AnswerVariant>();

            for (AnswerVariant av : qt.getNext().getAnswers()) {
                avMap.put(av.getAnswer(), av);
            }

            List<AnswerVariant> avs = new ArrayList<AnswerVariant>();
            avs.add(avMap.get("Легковая"));
            avs.addAll(getVariant(em, "Автомобиль предствительского класса"));
            qt1 = createQTree1(qt.getNext(), createQuestion("Тип кузова", new AnswerPair[]{new AnswerPair("Седан",100000), new AnswerPair("Купе",200000), new AnswerPair("Кабриолет",300000), new AnswerPair("Хэтчбэк",200000), new AnswerPair("Универсал",200000)}), null, avs);
            avs.clear();
            em.persist(qt1);
            prevq.add(qt1.getNext());

            avs.add(avMap.get("Легковая"));
            avs.addAll(getVariant(em, "Суперкар"));
            qt1 = createQTree1(qt.getNext(), createQuestion("Тип кузова", new AnswerPair[]{new AnswerPair("Седан",100000), new AnswerPair("Купе",200000),new AnswerPair( "Кабриолет",300000)}), null, avs);
            avs.clear();
            em.persist(qt1);
            prevq.add(qt1.getNext());



            Question q = createQuestion("Тип крыши", new AnswerPair[]{new AnswerPair("Жесткая",30000),new AnswerPair("Мягкая",25000)});
            for (Question qq : prevq) {
                for (AnswerVariant av : getVariant(em, "Кабриолет")) {
                    qt1 = createQTree(qq, q, null, av);
                    em.persist(qt1);
                }
            }
            prevq.add(q);



            qt1 = createQTree(qt.getNext(), createQuestion("Тип кузова", new AnswerPair[]{new AnswerPair("Кроссовер",1000000), new AnswerPair("Полноразмерный внедорожник",1005000),new AnswerPair("Пикап",1009000)}), null, avMap.get("Внедорожник"));
            em.persist(qt1);
            prevq.add(qt1.getNext());

            q = createQuestion("Количество дверей", new AnswerPair[]{new AnswerPair("2",10000), new AnswerPair("3",20000), new AnswerPair("4",30000), new AnswerPair("5",40000)});
            for (Question qq : prevq) {

                qt1 = createQTree(qq, q, null, null);
                em.persist(qt1);
            }

            prevq.clear();
            qt = createQTree(q, createQuestion("Материал кузова", new AnswerPair[]{new AnswerPair("Сталь",20000),new AnswerPair ("Алюминий",35000), new AnswerPair("Углепластик",120000), new AnswerPair("Броня",90000)}), null, null);
            prevq.add(qt.getNext());
            em.persist(qt);

            for (AnswerVariant av : qt.getNext().getAnswers()) {
                if (av.getAnswer().equals("Броня")) {
                    qt1 = createQTree(qt.getNext(), createQuestion("Тип брони", new AnswerPair[]{new AnswerPair("Сталь",20000),new AnswerPair("Комбинированная",40000)}), null, av);
                    em.persist(qt1);
                    qt1 = createQTree(qt1.getNext(), createQuestion("Толщина брони", (AnswerPair[]) null, false, true), null, null);
                    prevq.add(qt1.getNext());
                    em.persist(qt1);
                }
            }

            q = createQuestion("Отделка сидений", new AnswerPair[]{new AnswerPair("Кожа",12000), new AnswerPair("Вилюр",15000), new AnswerPair("Фибро-Карбон",50000)});
            for (Question qq : prevq) {

                qt1 = createQTree(qq, q, null, null);
                em.persist(qt1);
            }



            qt = createQTree(q, createQuestion("Выбор панелей", new AnswerPair[]{new  AnswerPair("Дерево",25000),new AnswerPair ("Пластик",15000),new AnswerPair ("Металл",35000), new AnswerPair("Карбон",60000)}), null, null);
            em.persist(qt);

            qt1 = createQTree(qt.getNext(), createQuestion("Выбор электроники", new AnswerPair[]{new AnswerPair("Система навигации",5000),new AnswerPair ("Аудиосистема",8000), new AnswerPair("Климат контроль",6500), new AnswerPair("Подогрев сидений",2700)}, true, false), null, null);
            em.persist(qt1);

            avMap.clear();

            for (AnswerVariant av : qt1.getNext().getAnswers()) {
                avMap.put(av.getAnswer(), av);
            }
            prevq.clear();
            prevq.add(qt1.getNext());

            
       Question    qn=createQuestion("Тип системы навигации", new  AnswerPair[]{new AnswerPair("Глонасс",2300),new AnswerPair ("GPS",2000),new AnswerPair( "Варианты 1 и 2 ",4100)});
            qt = createQTree(qt1.getNext(),qn , null, avMap.get("Система навигации"));
            em.persist(qt);
            prevq.add(qt.getNext());

            

            q = createQuestion("Выбор типа двигателя", new AnswerPair[]{new AnswerPair("Бензиновый",35000), new AnswerPair("Дизельный",45000), new AnswerPair("Гибридный",65000), new AnswerPair("Электрический",75000)});
            for (Question q1 : prevq) {
                qt = createQTree(q1, q, null, null);
                em.persist(qt);
            }

            avMap.clear();
            for (AnswerVariant av : qt.getNext().getAnswers()) {
                avMap.put(av.getAnswer(), av);
            }

            prevq.clear();
            Map<String, String[]> engines = new HashMap();
            engines.put("Бензиновый", new String[]{"JV6-24", "JV8-24", "JV12-30", "JV6-20"});
            engines.put("Бензиновый", new String[]{"JV6-24", "JV8-24", "JV12-30", "JV6-20"});

             qn = createQuestion("Двигатели Бензиновые", new AnswerPair[]{new AnswerPair("JV6-24",45000), new AnswerPair("JV8-24",55000), new AnswerPair("JV12-30",70000),new AnswerPair ("JV6-20",60000)});
            Question qn1 = createQuestion("Двигатели Бензиновые", new AnswerPair[]{new AnswerPair("S4-20",40000), new AnswerPair("SV8-24",45000), new AnswerPair("SV12-35",60000), new AnswerPair("S6-18",40000), new AnswerPair("SV6-30",45000), new AnswerPair("SV8-30",55000)});

            List<AnswerVariant> varList = getVariant(em, "Внедорожник");
            varList.add(avMap.get("Бензиновый"));
            qt1 = createQTree1(q, qn, null, varList);
            em.persist(qt1);
            prevq.add(qn);

            varList = getVariant(em, "Легковая");
            varList.add(avMap.get("Бензиновый"));
            qt1 = createQTree1(q, qn1, null, varList);
            em.persist(qt1);
            prevq.add(qn1);

            qn = createQuestion("Двигатели Дизельные", new  AnswerPair[]{ new AnswerPair("JDV6-24",55000),new AnswerPair("JV8-24",65000),new AnswerPair("JV12-30",75000)});
            qn1 = createQuestion("Двигатели Дизельные", new AnswerPair[]{new AnswerPair("SD4-20",50000), new AnswerPair("SDV8-24",60000), new AnswerPair("SD6-18",65000), new AnswerPair("SDV8-30",70000)});

            varList = getVariant(em, "Внедорожник");
            varList.add(avMap.get("Дизельный"));
            qt1 = createQTree1(q, qn, null, varList);
            em.persist(qt1);
            prevq.add(qn);


            varList = getVariant(em, "Легковая");
            varList.add(avMap.get("Дизельный"));
            qt1 = createQTree1(q, qn1, null, varList);
            em.persist(qt1);
            prevq.add(qn1);

            qn = createQuestion("Двигатели Гибридные", new AnswerPair[]{new AnswerPair("JH6-15",80000),new AnswerPair ("JHV6-18",85000), new AnswerPair("JHV8-20",90000)});
            qn1 = createQuestion("Двигатели Гибридные", new  AnswerPair[]{new AnswerPair("SH-20",75000), new AnswerPair("SHV6-24",85000), new AnswerPair("SH-16",80000),new AnswerPair("SHV8-26",90000)});

            varList = getVariant(em, "Внедорожник");
            varList.add(avMap.get("Гибридный"));
            qt1 = createQTree1(q, qn, null, varList);
            em.persist(qt1);
            prevq.add(qn);
            varList = getVariant(em, "Легковая");
            varList.add(avMap.get("Гибридный"));
            qt1 = createQTree1(q, qn1, null, varList);
            em.persist(qt1);
            prevq.add(qn1);

            qn = createQuestion("Двигатели Электрические", new AnswerPair[]{new AnswerPair("JE-1",90000),new AnswerPair( "JE-2",10000)});
            qn1 = createQuestion("Двигатели Электрические", new  AnswerPair[]{new AnswerPair("SE-1",85000), new AnswerPair("SE-2",90000), new AnswerPair("SE-3",100000), new AnswerPair("SES-1",110000), new AnswerPair("SES-2",120000)});

            varList = getVariant(em, "Внедорожник");
            varList.add(avMap.get("Электрический"));
            qt1 = createQTree1(q, qn, null, varList);
            em.persist(qt1);
            prevq.add(qn);
            varList = getVariant(em, "Легковая");
            varList.add(avMap.get("Электрический"));
            qt1 = createQTree1(q, qn1, null, varList);
            em.persist(qt1);
            prevq.add(qn1);


            qn = createQuestion("Тип КПП", new AnswerPair[]{new AnswerPair("Механическая",10000),new AnswerPair( "Полуавтоматическая",15000), new AnswerPair("Автоматическая",20000)});
            for (Question q1 : prevq) {

                qt1 = createQTree(q1, qn, null, null);
                em.persist(qt1);
            }


            qt = createQTree(qn, null, null, null);
            em.persist(qt);




            em.getTransaction().commit();
        }
    }
    
    public  AnswerPair pair(String a, Integer p){
        return new AnswerPair(a, p);
        
    }

    
    public static  class AnswerPair {
        private  String Answer;
        private  Integer price;
        

        public AnswerPair(String Answer, Integer price) {
            this.Answer = Answer;
            this.price = price;
        }

        public String getAnswer() {
            return Answer;
        }

        public AnswerPair setAnswer(String Answer) {
            this.Answer = Answer;
            return this;
        }

        public Integer getPrice() {
            return price;
        }

        public AnswerPair setPrice(Integer price) {
            this.price = price;
            return this;
        }
        
        public  AnswerPair o(String a, Integer p){
            return new AnswerPair(a, p);
            
        }
        
    }
    
    private static Question createQuestion(String quest, String[] answers) {
        return createQuestion(quest, answers, false, false);
    }
    
    private static Question createQuestion(String quest, AnswerPair[] answers) {
        return createQuestion(quest, answers, false, false);
    }

    private static Question createQuestion(String quest, String[] answers, Boolean isMultiple, Boolean isManual) {
        AnswerPair[] answp = new AnswerPair[answers.length];
        for(int i = 0; i < answers.length; i++){
            answp[i] = new AnswerPair(answers[i], 0);
        }
        return createQuestion(quest, answp, isMultiple, isManual);
        
    }
    

    private static Question createQuestion(String quest, AnswerPair[] answers, Boolean isMultiple, Boolean isManual) {
        Question q = new Question();
        q.setQname(quest);
        if (answers != null) {
            for (AnswerPair a : answers) {
                q.addAnswer(new AnswerVariant().withAnswer(a.getAnswer()).withPrice(a.getPrice()).withQuestion(q));
            }
        }
        q.setMulti(isMultiple);
        q.setText(isManual);
        return q;
    }

    private static List<AnswerVariant> getVariant(EntityManager em, String variant) {
        Query query = em.createQuery("Select av from AnswerVariant av where av.answer = :answer");
        query.setParameter("answer", variant);
        List<AnswerVariant> res = query.getResultList();
//        AnswerVariant av = null;
//        if (!res.isEmpty() && res.size() == 1) {
//            av = res.get(0);
//        }

        return res;
    }

    private static QuestionTree createQTree(Question pq, Question nq, String anval, AnswerVariant av) {
        QuestionTree qt = new QuestionTree();
        qt.setPrev(pq);
        qt.setNext(nq);
        if (av != null) {
            qt.addAnsw(av);
        } else if (anval != null) {
            qt.setVal(anval);
        }
        return qt;


    }

    private static QuestionTree createQTree1(Question pq, Question nq, String anval, List<AnswerVariant> varList) {
        QuestionTree qt = new QuestionTree();
        qt.setPrev(pq);
        qt.setNext(nq);
        if (varList != null) {
            for (AnswerVariant av : varList) {
                qt.addAnsw(av);
            }
        } else if (anval != null) {
            qt.setVal(anval);
        }
        return qt;
    }
    
}
