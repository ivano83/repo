package it.fivano.symusic;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class AntiDdosUtility {
	
	private MyLogger log;
	
	public AntiDdosUtility() {
		log = new MyLogger(getClass());
	}
	
	/**
	 * Example:
	 * <script type="text/javascript">
  //<![CDATA[
  (function(){
    var a = function() {try{return !!window.addEventListener} catch(e) {return !1} },
    b = function(b, c) {a() ? document.addEventListener("DOMContentLoaded", b, c) : document.attachEvent("onreadystatechange", b)};
    b(function(){
      var a = document.getElementById('cf-content');a.style.display = 'block';
      setTimeout(function(){
        var t,r,a,f, sFFWaQb={"nvbotxImQWzc":+((!+[]+!![]+!![]+!![]+[])+(!+[]+!![]+!![]+!![]+!![]+!![]))};
        t = document.createElement('div');
        t.innerHTML="<a href='/'>x</a>";
        t = t.firstChild.href;r = t.match(/https?:\/\//)[0];
        t = t.substr(r.length); t = t.substr(0,t.length-1);
        a = document.getElementById('jschl-answer');
        f = document.getElementById('challenge-form');
        ;sFFWaQb.nvbotxImQWzc+=+((!+[]+!![]+!![]+[])+(!+[]+!![]+!![]));sFFWaQb.nvbotxImQWzc+=!+[]+!![]+!![]+!![]+!![]+!![]+!![];sFFWaQb.nvbotxImQWzc+=+((!+[]+!![]+[])+(!+[]+!![]+!![]+!![]));sFFWaQb.nvbotxImQWzc*=+((+!![]+[])+(!+[]+!![]+!![]+!![]+!![]+!![]+!![]));sFFWaQb.nvbotxImQWzc*=+((!+[]+!![]+!![]+!![]+[])+(!+[]+!![]+!![]+!![]+!![]+!![]));sFFWaQb.nvbotxImQWzc-=+((+!![]+[])+(!+[]+!![]+!![]+!![]+!![]));sFFWaQb.nvbotxImQWzc+=+((!+[]+!![]+!![]+[])+(+!![]));sFFWaQb.nvbotxImQWzc*=+((!+[]+!![]+!![]+[])+(!+[]+!![]));a.value = parseInt(sFFWaQb.nvbotxImQWzc, 10) + t.length;
        f.submit();
      }, 5850);
    }, false);
  })();
  //]]> </script>
   * 
   * http://0daymp3.com/cdn-cgi/l/chk_jschl?jschl_vc=1e6bbb2fc7b3371a3e12408718d0e13d&jschl_answer=2753163
	 * @param script
	 * @return
	 * @throws Exception
	 */
	
	public int calcolateAnswer(String script) throws Exception {
		
		int tot = 0;
//		log.info(script);
		String[] lines = script.split("\n");
		String varName = "";
		for(String scriptLine : lines) {
			
			if(scriptLine.contains("var t,r,a,f,")) {
				varName = scriptLine.replace("var t,r,a,f,", "").replaceAll("[{};\"]", "");
				
				String[] tmp = varName.split(":");
				varName = tmp[0].replace("=", ".").trim();
//				System.out.println(varName);
				tot = parseCalc(tot, "+", tmp[1]);
				
			}
			else if(scriptLine.contains(varName) && varName.length()>0) {
//				log.info(scriptLine);
				String[] tmp = scriptLine.split(";");
				for(String x : tmp) {
					if(!x.contains("parseInt") && x.trim().length()>0) {
						x = x.replace(varName, "");
						String operatore = x.split("=")[0];
						tot = parseCalc(tot, operatore, x.split("=")[1]);
					}
				}
				
			}
			
			
		}
		
//		tot += 11;
		tot += 9;
		
//		log.info("ANTI_DDOS TOT = "+tot);
		return tot;
	}

	private int parseCalc(int tot, String operatore, String value) {
		
		String totale = "";
		String[] tmp = value.split("\\)\\+\\(");
		for(String x : tmp) {
			int res = StringUtils.countMatches(x, "!!") + StringUtils.countMatches(x, "!+");
//			System.out.println(res+" --> "+x);
			totale += res+"";
		}
//		log.info(totale+" --> "+value);
//		System.out.println(totale+" --> "+value);
		int totInt = Integer.parseInt(totale);
		if(operatore.equals("+"))
			return tot+totInt;
		if(operatore.equals("-"))
			return tot-totInt;
		if(operatore.equals("*"))
			return tot*totInt;
		if(operatore.equals("/"))
			return tot/totInt;
		
		return tot;
	}

	public boolean isAntiDDOS(Document doc) {
		Elements res = doc.getElementsByClass("cf-browser-verification");
		log.info("DDOS protection: "+(res.size()==0 ? false : true));
		return res.size()==0 ? false : true;
	}
	
	public static void main(String[] args) throws Exception {
		String script = "var a = function() {try{return !!window.addEventListener} catch(e) {return !1} },\n"+
    "b = function(b, c) {a() ? document.addEventListener(\"DOMContentLoaded\", b, c) : document.attachEvent(\"onreadystatechange\", b)};\n"+
    "b(function(){\n"+
      "var a = document.getElementById('cf-content');a.style.display = 'block';\n"+
      "setTimeout(function(){\n"+
        "var t,r,a,f, ZrxcFYU={\"bODbAk\":+((+!![]+[])+(!+[]+!![]+!![]+!![]+!![]+!![]))};\n"+
        "t = document.createElement('div');\n"+
        "t.innerHTML=\"<a href='/'>x</a>\";\n"+
        "t = t.firstChild.href;r = t.match(/https?:\\/\\//)[0];\n"+
        "t = t.substr(r.length); t = t.substr(0,t.length-1);\n"+
        "a = document.getElementById('jschl-answer');\n"+
        "f = document.getElementById('challenge-form');\n"+
        ";ZrxcFYU.bODbAk+=+((+!![]+[])+(+[]));a.value = parseInt(ZrxcFYU.bODbAk, 10) + t.length;\n"+
        "f.submit();\n"+
      "}, 8000);\n"+
    "}, false);\n";
	// GET http://scnlog.eu/cdn-cgi/l/chk_jschl?jschl_vc=3aa3bf2aae0861f7f08447a9a6649b58&pass=1429731087.261-ugSb%2FE6DdZ&jschl_answer=35
		
		System.out.println(new AntiDdosUtility().calcolateAnswer(script));
	}

}
