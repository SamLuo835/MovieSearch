/* JdbcHelper.java
==============
PURPOSE:   
AUTHOR:Jainlin Luo
CREATED: 2017-09-23
UPDATED: 
*/
package luojianl;
import java.sql.*;
import java.util.ArrayList;
public class JdbcHelper {
    private String activeSql;
    private PreparedStatement activeStatement;
    public Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private String errorMsg;
    public JdbcHelper(){
        connection=null;
        statement=null;
        resultSet=null;
        errorMsg="";
    };
    public boolean connect(String url,String user,String pass)throws Exception{

        boolean connected =false;
        if(url==null ||url==null){
            return connected;
        }
        if(user==null ||user==null){
            return connected;
        }
        if(pass==null ||pass==null){
            return connected;
        }
        try{
            connection=DriverManager.getConnection(url,user,pass);
            statement=connection.createStatement();
            connected=true;
            return connected;
        }
        catch(SQLException e){
            System.err.print("SQLERROR: "+e.getSQLState()+" "+e.getMessage());
            errorMsg=e.getMessage();
            return connected;
        }
        catch(Exception e){
            System.err.print("ERROR: "+e.getMessage());
            errorMsg=e.getMessage();
            return connected;
        }
    }

    public void disconnect(){
        try{
            resultSet.close();
        }
        catch(Exception e){}
        try{
            statement.close();
        }
        catch(Exception e){}
        try{
            connection.close();
        }
        catch(Exception e){}
    }
    public ResultSet query(String sql){
        resultSet=null;
        try{
            if(connection ==null || connection.isClosed()){
            System.err.println("Connection is not established. make connection to Db before calling");
            return resultSet;}
            resultSet=statement.executeQuery(sql);
        }
        catch(SQLException e){
            System.err.print("SQLERROR: "+e.getSQLState()+" "+e.getMessage());
            errorMsg=e.getMessage();
        }
        catch(Exception e){
            System.err.print("ERROR: "+e.getMessage());
            errorMsg=e.getMessage();
        }
        return resultSet;
    }
    public int update(String sql){
        int result=-1;
        try{
            if(connection ==null || connection.isClosed()){
            System.err.println("Connection is not established. make connection to Db before calling");
            return result;}
            result=statement.executeUpdate(sql);
        }
        catch(SQLException e){
            System.err.print("SQLERROR: "+e.getSQLState()+" "+e.getMessage());
            errorMsg=e.getMessage();
        }
        catch(Exception e){
            System.err.print("ERROR: "+e.getMessage());
            errorMsg=e.getMessage();
        }
        return result;
    }
    public String getMsg(){
        return errorMsg;
    }
}

