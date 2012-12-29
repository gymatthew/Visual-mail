package sendmail_example;
 
 /**
  * Module:      Encrypt.java 
  * Description: ��������м��ܺͽ���
  * Company: 
  * Author:      ptp
  * Date:        Mar 6, 2012
  */
 public class Encrypt
 {
 
     public static final int pass1 = 10;
     public static final int pass2 = 1;
 
     public Encrypt()
     {
     }
 
     /**
      * @Title: DoEncrypt 
      * @Description: ��������м��ܵķ���
      * @param @param str
      * @param @return    �趨�ļ� 
      * @return String    �������� 
      * @throws
 */
     public static String DoEncrypt(String str)
     {
         StringBuffer enStrBuff = new StringBuffer();
         for (int i = 0; i < str.length(); i++)
         {
             int tmpch = str.charAt(i);
             tmpch ^= 1;
             tmpch ^= 0xa;
             enStrBuff.append(Integer.toHexString(tmpch));
         }
 
         return enStrBuff.toString().toUpperCase();
     }
 
     /**
      * @Title: DoDecrypt 
      * @Description: ��������н��ܵķ���
      * @param @param str
      * @param @return    �趨�ļ� 
      * @return String    �������� 
      * @throws
 */
     public static String DoDecrypt(String str)
     {
         String deStr = str.toLowerCase();
         StringBuffer deStrBuff = new StringBuffer();
         for (int i = 0; i < deStr.length(); i += 2)
         {
             String subStr = deStr.substring(i, i + 2);
             int tmpch = Integer.parseInt(subStr, 16);
             tmpch ^= 1;
             tmpch ^= 0xa;
             deStrBuff.append((char)tmpch);
         }
 
         return deStrBuff.toString();
     }
 
     public static void main(String args[])
     {
         String source = "123456";
         String s = DoEncrypt(source);
         System.out.println("de=" + s);
         
         source = DoDecrypt(s);
         System.out.println("en=" + source);
 
     }
 }