/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backendtest;

/**
// ----------------------------------------------------------------------------------
// Backendtest.java
// ----------------------------------------------------------------------------------
// Autor. Ing. Antonio Barajas del Castillo
// ----------------------------------------------------------------------------------
// Fecha Ultima Modificación
// 30/09/2018
// ----------------------------------------------------------------------------------
 */

// Definiciones
import com.google.gson.JsonParser;
import com.google.gson.Gson;
import com.google.gson.JsonObject; 
import com.google.gson.JsonArray;
import java.util.UUID;
import java.io.*;
import java.io.FileReader;
import java.io.IOException;

public class Backendtest {
     // Funciones para validacion
     public static boolean EsNumerico(String cadena) {

        boolean resultado;

        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }

        return resultado;
    }
     
     public static boolean EsNumericoDoble(String cadena) {

        boolean resultado;

        try {
            Double.parseDouble(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }

        return resultado;
    }
     
     
     
    public static void main(String[] args) {
        // Declara las variables
        int i=0;
        int user_id=0;  
        String sUser_Id="";
        String comando="";
        String Transaccion_Id="";
        
        String NombreArchivo="backendtest.txt";
                 
        //String parametro3="";

        // Declaracion de objetos
        Gson gson = new Gson();

        // Inicio de la aplicacion
        System.out.println("BACKEND TEST");         
        
        // Verificacion
        if(args.length==0){
            System.out.println("No tiene argumentos para procesar");  
            System.out.println("Ingrese los argumentos en el siguiente orgen:");
            System.out.println("Adicionar informacion: ./backendtest <user_id> add <transaction_json>");
            System.out.println("Mostrar informacion: ./backendtest <user_id> <transaction_id>");
            System.out.println("Listar informacion: ./backendtest <user_id> list");
            System.out.println("Sumar informacion: ./backendtest <user_id> sum");
            System.exit(0);
        }
        
       
        if(args.length<2){
            System.out.println("Argumentos incompletos");  
            System.out.println("Ingrese los argumentos en el siguiente orgen:");
            System.out.println("Adicionar informacion: ./backendtest <user_id> add <transaction_json>");
            System.out.println("Mostrar informacion: ./backendtest <user_id> <transaction_id>");
            System.out.println("Listar informacion: ./backendtest <user_id> list");
            System.out.println("Sumar informacion: ./backendtest <user_id> sum");
            System.exit(0);        
        }
        
        // Extrea los argumentos
        sUser_Id=args[0];
        sUser_Id=sUser_Id.trim();
        comando=args[1];         
        comando=comando.trim();
        
        // Verifica que el User Id sea numerico
        if (EsNumerico(sUser_Id) == false) {            
            System.out.println("User Id: No es numerico");
            System.exit(0);   
        } else {
            user_id=Integer.parseInt(sUser_Id);
        }
        
        // Verifica los comandos
        if(!comando.equals("add") && !comando.equals("list") && !comando.equals("sum")){
            if(args.length>3){
                System.out.println("Comando invalido");
                System.exit(0);
             } else {
                comando="show";
                Transaccion_Id=args[1]; 
                Transaccion_Id=Transaccion_Id.trim();                 
             }            
        }  
        
        switch(comando){
            case "add":  // Anexar registro                
                 // Extrae el tercer argumento
                 String parametro3="";     
                 
                 if(args.length>2){
                    
                    // Lee los datos para grabar                    
                    for(i=2;i<args.length;i++){  
                       parametro3=parametro3+args[i];
                    }
                  
                    // Separa los datos
                    char valor;
                    int bandInicio=0;
                    int bandDatos=0;
                    int bandInicioValor=0;
                    int Columna=0;
                    String Campo1="";
                    String Valor1="";
                    String Campo2="";
                    String Valor2="";
                    String Campo3="";
                    String Valor3="";
                    
                        
                    char[] caracteres = parametro3.toCharArray();
                    for(i=0;i<caracteres.length;i++){
                        valor=caracteres[i];
                    
                        if(bandInicioValor==0){
                           if(valor=='“' && bandDatos==0){
                              bandInicio=1;                           
                           } else {
                              if(valor=='”'){                        
                                 bandInicio=0;                       
                                 bandInicioValor=1;                      
                              }                    
                           }
                        }
                
                        if(bandInicioValor==1){
                           if(valor==':'){
                              bandDatos=1;
                           } else {
                              if(valor==','){                              
                                 bandDatos=0;
                                 bandInicioValor=0;
                                 Columna++;
                              }
                           }                      
                        }
                
                        if(bandInicio==1 && bandDatos==0){
                           switch(Columna){
                              case 0:
                                 if(valor!='“'){
                                    Campo1=Campo1+String.valueOf(valor);    
                                 }
                            
                                 break;
                              case 1:
                                 if(valor!='“'){
                                    Campo2=Campo2+String.valueOf(valor);    
                                 }
                            
                                 break;
                              case 2:
                                 if(valor!='“'){
                                    Campo3=Campo3+String.valueOf(valor);    
                                 }
                            
                                 break;
                        
                           }
                        } else {
                           if(bandInicio==0 && bandDatos==1){
                              switch(Columna){
                                 case 0:
                                    if(valor!=':'){
                                       Valor1=Valor1+String.valueOf(valor);     
                                    }
                                                         
                                    break;
                                 case 1:
                                    if(valor!=':'){
                                       Valor2=Valor2+String.valueOf(valor);     
                                    }
                                    break;
                                 case 2:
                                    if(valor!=':'&& valor!='}'){
                                       Valor3=Valor3+String.valueOf(valor);     
                                    }
                                    break;                       
                              }
                           }
                        }
                    }
                    
                    // Genera el UID
                    UUID TransaccionId = UUID.randomUUID();
                    String sTransaccionId=TransaccionId.toString();
                    
                    // Valida la inforamcion leida
                    if(EsNumericoDoble(Valor1)==false){
                         System.out.println("Monto No valido");
                         System.exit(0);
                    }  
                    
                     // Carga la informacion
                     Datos datos = new Datos();
                     datos.setUserId(user_id);                     
                     datos.setMonto(Double.parseDouble(Valor1));
                     datos.setDescripcion(Valor2);
                     datos.setFecha(Valor3);
                     datos.setTransaction_Id(sTransaccionId);
                     
                     // Graba los datos en el archivo
                     FileWriter archivo = null;
                     PrintWriter pw = null;
                     BufferedWriter Grabar = null; 
                     try
                     {
                         Grabar = new BufferedWriter(new FileWriter(NombreArchivo, true));   
                         Grabar.newLine();
                         Grabar.write(datos.getTransaction_Id() + "," + datos.getMonto() + "," + datos.getDescripcion() + "," + datos.getFecha() + "," + datos.getUserId());
                         Grabar.close();

                     } catch (Exception e) {
                         e.printStackTrace();
                     }  
                     
                     // Convierte a JSON
                     String cadenaJson=gson.toJson(datos);
                     
                     //Muestra la informacion
                     System.out.println(cadenaJson);                    
                } else {
                     System.out.println("Comando Invalido");     
                 }
                break;
                
            case "show": // Mostrar el registro solicitado
                System.out.println("Mostrar la transaccion definida");
                String cadena;
                 
                try
                {
                    String sId="";
                    int iId=0;
                    String sTId="";
                    String sMonto="";
                    double Monto=0;
                    String sDesc="";
                    String sFecha="";
                    char valor;
                    int columna=0;
                    int bandEncontrado=0;
                    
                    String sCadena="";
                    FileReader fr = new FileReader(NombreArchivo);
                    BufferedReader bf = new BufferedReader(fr);
                    while ((sCadena = bf.readLine())!=null) {
                          
                        sTId=""; 
                        sId="";
                        columna=0;
                        sMonto="";
                        sDesc="";
                        sFecha="";
                        
                        //System.out.println(sCadena);
                        
                        // Extrae el TransactionId y el UserId                        
                        char[] caracteres = sCadena.toCharArray();
                     
                        for(i=0;i<caracteres.length;i++){
                          
                            valor=caracteres[i];
                             
                            if(valor==','){
                                columna++;
                            } else {
                                switch(columna){
                                    case 0:
                                        sTId=sTId+String.valueOf(valor);  
                                        break;
                                    case 1:
                                        sMonto=sMonto+String.valueOf(valor);  
                                        break;
                                    case 2:
                                        sDesc=sDesc+String.valueOf(valor); 
                                        break;                                        
                                    case 3:
                                        sFecha=sFecha+String.valueOf(valor);
                                        break;
                                    case 4:   
                                        sId=sId+String.valueOf(valor);  
                                        break;
                                }
                            }
                             
                        }
      
                        if(sTId.equals(Transaccion_Id) && sId.equals(sUser_Id)){
                           
                            Datos datos = new Datos();
                             
                            if(EsNumerico(sId)==true){
                               iId=Integer.parseInt(sId);    
                            } else {
                               iId=0;
                            }                           
                            datos.setUserId(iId);
                             
                            if(EsNumericoDoble(sMonto)==true){
                               Monto=Double.parseDouble(sMonto);    
                            } else {
                               Monto=0;
                            }
                            datos.setMonto(Monto);
                            datos.setDescripcion(sDesc);
                            datos.setFecha(sFecha);
                            datos.setTransaction_Id(sTId);
                            
                            String cadenaJson=gson.toJson(datos);
                            System.out.println(cadenaJson);   
                          
                            bandEncontrado=1;
                        }                      
                    }
                    
                    if(bandEncontrado==0){
                        System.out.println("Transaction not found");                        
                    }                                        
                } catch (Exception e) {
                         e.printStackTrace();
                }  
                
                break;
                
            case "list": // Litar los regisros del usuario 
                System.out.println("Listado de las transacciones");
      
                         
                try
                {
                    String sId="";
                    int iId=0;
                    String sTId="";
                    String sMonto="";
                    String sDesc="";
                    String sFecha="";
                    char valor;
                    int columna=0;
                    int bandEncontrado=0;
                    
                  
                    String sCadena="";
                    FileReader fr = new FileReader(NombreArchivo);
                    BufferedReader bf = new BufferedReader(fr);
                    while ((sCadena = bf.readLine())!=null) {
                        
                        sTId=""; 
                        sId="";
                        sMonto="";
                        sDesc="";
                        sFecha="";
                        
                        columna=0;
                        
                        // Extrae el TransactionId y el UserId                        
                        char[] caracteres = sCadena.toCharArray();
                        for(i=0;i<caracteres.length;i++){
                            valor=caracteres[i];
                             
                            if(valor==','){
                                columna++;
                            } else {
                                switch(columna){
                                    case 0:
                                        sTId=sTId+String.valueOf(valor);  
                                        break;
                                    case 1:
                                        sMonto=sMonto+String.valueOf(valor);  
                                        break;
                                    case 2:
                                        sDesc=sDesc+String.valueOf(valor); 
                                        break;                                        
                                    case 3:
                                        sFecha=sFecha+String.valueOf(valor);
                                        break;
                                    case 4:   
                                        sId=sId+String.valueOf(valor);  
                                        break;
                                }
                            }
                        }
                        
      
                        if(sId.equals(sUser_Id)){
                           
                            Datos datos = new Datos();                             
                            sId=sId.trim();
                            
                            if(sId.length()>0){                            
                               iId=Integer.parseInt(sId);                             
                               datos.setUserId(iId);   
                               if(EsNumericoDoble(sMonto)==true){
                                 datos.setMonto(Double.parseDouble(sMonto));
                               } else {
                                 datos.setMonto(0);
                               }
                                 
                               
                               datos.setDescripcion(sDesc);
                               datos.setFecha(sFecha);
                               datos.setTransaction_Id(sTId);
                            
                               String cadenaJson=gson.toJson(datos);
                               System.out.println("datos Json: " + cadenaJson);   
                           
                            }
                          
                            bandEncontrado=1;
                            
                        }
                        
                    } 
                    
                    if(bandEncontrado==0){
                        System.out.println("Transaction not found");                        
                    }                                        
                } catch (Exception e) {
                         e.printStackTrace();
                }  
                
                break;
                
                
            case "sum": // Sumar las transacciones asociadas al usuario   
                 System.out.println("Listado de las transacciones");
       
                double Suma=0;
                
                try
                {
                    String sId="";
                    int iId=0;
                    String sTId="";
                    String sMonto="";
                    String sDesc="";
                    String sFecha="";
                    char valor;
                    int columna=0;
                    int bandEncontrado=0;
                    
                  
                    String sCadena="";
                    FileReader fr = new FileReader(NombreArchivo);
                    BufferedReader bf = new BufferedReader(fr);
                    while ((sCadena = bf.readLine())!=null) {
                        
                        sTId=""; 
                        sId="";
                        sMonto="";
                        sDesc="";
                        sFecha="";
                        
                        columna=0;
                        //System.out.println(sCadena);
                        
                  
                        // Extrae el TransactionId y el UserId                        
                        char[] caracteres = sCadena.toCharArray();
                        for(i=0;i<caracteres.length;i++){
                            valor=caracteres[i];
                             
                            if(valor==','){
                                columna++;
                            } else {
                                switch(columna){
                                    case 0:
                                        sTId=sTId+String.valueOf(valor);  
                                        break;
                                    case 1:
                                        sMonto=sMonto+String.valueOf(valor);  
                                        break;
                                    case 2:
                                        sDesc=sDesc+String.valueOf(valor); 
                                        break;                                        
                                    case 3:
                                        sFecha=sFecha+String.valueOf(valor);
                                        break;
                                    case 4:   
                                        sId=sId+String.valueOf(valor);  
                                        break;
                                }
                            }
                        }
                        
      
                        if(sId.equals(sUser_Id)){                                                                                     
                            sId=sId.trim();
                            
                            if(sId.length()>0){                            
                               iId=Integer.parseInt(sId);                                                             
                               if(EsNumericoDoble(sMonto)==false){                                  
                                 sMonto="0";
                               }
                             
                               Suma=Suma+Double.parseDouble(sMonto);                                                          
                            }
                          
                            bandEncontrado=1;                            
                        }
                        
                    } 
                    
                    if(bandEncontrado==0){
                        System.out.println("Transaction not found");                        
                    } else {
                         DatosSuma sumados = new DatosSuma();    
                         sumados.setUserId(user_id);   
                         sumados.setSuma(Suma); 
                         String cadenaJson=gson.toJson(sumados);
                         System.out.println("datos Json: " + cadenaJson);  
                    }                                       
                } catch (Exception e) {
                         e.printStackTrace();
                }  
                
                break;                
        }                  
    }
    
    public static class Datos {
       private String transaction_id;
       private double monto;
       private String descripcion;
       private String fecha;
       private int user_id;
    
       public String getTransaction_Id() {
          return transaction_id;
       }
       public void setTransaction_Id(String transaction_Id) {
         this.transaction_id = transaction_Id;
       }
    
       public double getMonto() {
         return monto;
       }    
       public void setMonto(double monto) {
         this.monto = monto;
       }
    
       public String getDescripcion() {
         return descripcion;
       }
       public void setDescripcion(String descripcion) {
         this.descripcion = descripcion;
       }
       
        public String getFecha() {
         return fecha;
       }
       public void setFecha(String fecha) {          
         this.fecha = fecha;
       }
       
       public int getUserId() {
         return user_id;
       }
       public void setUserId(int User_Id) {
         this.user_id = User_Id;
       }
    }
    
    public static class DatosSuma {                
       private int user_id;
       private double sum;     
    
       
       public double getSuma() {
         return sum;
       }    
       public void setSuma(double suma) {
         this.sum = suma;
       }
          
       public int getUserId() {
         return user_id;
       }
       public void setUserId(int User_Id) {
         this.user_id = User_Id;
       }
    }
    
}


