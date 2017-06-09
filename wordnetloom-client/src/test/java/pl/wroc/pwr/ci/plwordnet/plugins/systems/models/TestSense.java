package pl.wroc.pwr.ci.plwordnet.plugins.systems.models;

import org.junit.Test;

import pl.wroc.pwr.ci.plwordnet.model.Sense;
import pl.wroc.pwr.ci.plwordnet.utils.RemoteUtils;

public class TestSense {

	@Test
	public void getSysnsetTest(){
		
		Sense se = RemoteUtils.lexicalUnitRemote.dbGet(90L);
		System.out.println(se.toString());
	}
}
