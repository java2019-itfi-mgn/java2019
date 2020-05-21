package lesson12;

import java.io.File;

import javax.script.*;

public class EvalScript {
    public static void main(String[] args) throws Exception {
        final ScriptEngineManager 	factory = new ScriptEngineManager();	// <1>
        final ScriptEngine 			engine = factory.getEngineByName("JavaScript");	// <2>
        final Invocable 			inv = (Invocable) engine;
        
        final File 					f = new File("test.txt");	// <3>

        engine.put("file", f);		// <4>
        
        engine.eval("print('Hello, World');\n"		// <5>
        		  + "print('Name='+file.getName());"
        		  + "function call(parm){print('Call: '+parm);}"
        		  + "var myObj = new Object(); myObj.run = function() { print('run called'); }");        
        
        inv.invokeFunction("call", "My parameter" );	// <6>
        
        final Object				obj = engine.get("myObj");	// <7>
        final Runnable				r = inv.getInterface(obj,Runnable.class);	// <8>
        
        r.run();	// <9>
    }
}