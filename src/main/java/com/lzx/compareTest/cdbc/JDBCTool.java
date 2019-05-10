package com.lzx.compareTest.cdbc;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class JDBCTool {
	public static Logger log = LogManager.getLogger("handle");
	static Properties props = new Properties();
	static Connection conn = null;
	static PreparedStatement pstat = null;
	static ResultSet rs = null;

	static{
	     InputStream in = JDBCTool.class.getResourceAsStream("jdbc.properties");
	     try {
			props.load(in);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.info("读取mysql配置异常！",e);
		}
	}
	
	
	/**
	 * 连接数据库
	 * @return 连接  
	 */
	public static Connection buildMysql() {
		  
		try {
			Class.forName(props.getProperty("jdbc_driverClassName"));
			conn = DriverManager.getConnection(props.getProperty("jdbc_url"), 
					props.getProperty("jdbc_username"), 
					props.getProperty("jdbc_password"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			log.info("mysql连接异常！",e);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.info("mysql连接异常！",e);
		}
		return conn;

	}

	public static int exceuteUpdate(String sql, Object... prams) {
		int result = 0;
		conn = buildMysql();
		try {
			pstat = conn.prepareStatement(sql);
			for (int i = 0; i < prams.length; i++) {
				pstat.setObject(i + 1, prams[i]);
			}

			result = pstat.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeMysql();
		}
		return result;
	}
	// 关闭数据库
		public static void closeMysql() {

			try {
				if (conn != null) {
					conn.close();
				}
				if (pstat != null) {
					pstat.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

}
