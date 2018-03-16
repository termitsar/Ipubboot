package com.example.config;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.Groupsgoods;
import com.example.service.ForOtherSrv;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(    classes={MvcConfig.class})
@DirtiesContext
@WebAppConfiguration
public class ttt {
	
	@Resource
	ForOtherSrv srv;
	
	@Ignore
	@Test
	@Transactional
    public void testCalA() throws Exception {
		

		Groupsgoods needed = new Groupsgoods();
		needed.setId(4);
		needed.setName("�� ��� ������� ��");
		
		Groupsgoods gr = srv.GetGroup(4);

        assertEquals(needed.getName(), gr.getName());
     }
	
	@Ignore
	@Test
	@Transactional
    public void testSaveGroup() throws Exception {
		

		Groupsgoods needed = new Groupsgoods();
		needed.setName("На чем плавать то");
		
		srv.SaveGroup(needed);
    }
	
	@Test
	@Transactional
    public void testCRUDGroup() throws Exception {
		//создаем группу
		Groupsgoods needed = new Groupsgoods();
		needed.setName("ТестТестТест");
		// записываем
		srv.SaveGroup(needed);
		//получаем все группы и нахордим по имени
		List<Groupsgoods> list = srv.GetGroups();
		Groupsgoods n = list.stream().filter(s->s.getName()==needed.getName()).findFirst().orElse(null);
		// если не нашел то ошибка
		if (n==null) fail("Не создал новый элемент!");
		// прверяем на апдейт
		n.setName("ТестТестТестАпдейт");
		srv.SaveGroup(n);
		// удаляем элемент
		srv.deleteGroup(n.getId());
    }
	
	

}
