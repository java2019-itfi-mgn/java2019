package lesson12;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class BatScript {

	public static void main(String[] args) throws ScriptException {
		// TODO Auto-generated method stub
        final ScriptEngineManager 	factory = new ScriptEngineManager();	
        final ScriptEngine 			engine = factory.getEngineByName("bat");
        
        System.err.println(engine.eval("echo test\nmkdir testdir\nexit")); // ��� ������� - test 
	}
}
