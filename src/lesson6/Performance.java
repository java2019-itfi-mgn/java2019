package lesson6;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;

public class Performance {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		Timer	t = new Timer();
		
		TimerTask	tt = new TimerTask() {
			@Override
			public void run() {
				System.err.println("Tick!");
			}
		};
		
		t.schedule(tt,2000);
		tt.cancel();
		System.err.println("the end!!");
		Runtime.getRuntime().halt(0);
		
		// Левая половина - дерево БД:
		// - схемы:
		//     - компоненты (таблицы, представления, хранимые функции и процедуры, последовательности и проч)
		//			- колонки таблиц
		// Правая половина - содержимое выбранной таблицы или представления
		Connection	conn = null;
		
		DatabaseMetaData dbmd = conn.getMetaData();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from имя_таблицы");
		ResultSetMetaData rsmd = rs.getMetaData();
		
		//Postgresql 12;
		 
		
	}

}
