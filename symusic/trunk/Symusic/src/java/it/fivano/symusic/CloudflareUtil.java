package it.fivano.symusic;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class CloudflareUtil {

	private final static Pattern OPERATION_PATTERN = Pattern.compile("setTimeout\\(function\\(\\)\\{\\s+(var t,r,a,f.+?\\r?\\n[\\s\\S]+?a\\.value =.+?)\\r?\\n");
    private final static Pattern PASS_PATTERN = Pattern.compile("name=\"pass\" value=\"(.+?)\"");
    private final static Pattern CHALLENGE_PATTERN = Pattern.compile("name=\"jschl_vc\" value=\"(\\w+)\"");


    public boolean cloudFlareSolve(String responseString, String domain) {
        Context rhino = Context.enter();
        try {

            Thread.sleep(5000);

            Matcher operationSearch = OPERATION_PATTERN.matcher(responseString);
            Matcher challengeSearch = CHALLENGE_PATTERN.matcher(responseString);
            Matcher passSearch = PASS_PATTERN.matcher(responseString);
            if(!operationSearch.find() || !passSearch.find() || !challengeSearch.find())
                return false;

            String rawOperation = operationSearch.group(1);
            String challengePass = passSearch.group(1);
            String challenge = challengeSearch.group(1);


            String operation = rawOperation
                    .replaceAll("a\\.value =(.+?) \\+ .+?;", "$1")
                    .replaceAll("\\s{3,}[a-z](?: = |\\.).+", "");
            String js = operation.replace("\n", "");

            rhino.setOptimizationLevel(-1);
            Scriptable scope = rhino.initStandardObjects();

            // either do or die trying
            int result = ((Double) rhino.evaluateString(scope, js, "CloudFlare JS Challenge", 1, null)).intValue();
            String answer = String.valueOf(result + domain.length());

            final List<NameValuePair> params = new ArrayList<NameValuePair>(3);
            params.add(new BasicNameValuePair("jschl_vc", challenge));
            params.add(new BasicNameValuePair("pass", challengePass));
            params.add(new BasicNameValuePair("jschl_answer", answer));

//            HashMap<String, String> headers = new HashMap<String, String>(1);
//            headers.put("Referer", "http://" + domain + "/");

            String url = "http://" + domain + "/cdn-cgi/l/chk_jschl?" + URLEncodedUtils.format(params, "windows-1251");

            System.out.println(url);

            Document doc = Jsoup.connect(url).timeout(6000).header("Referer", "http://" + domain)
        			.userAgent(userAgent)
        			.ignoreHttpErrors(true).get();

            System.out.println(doc.html());
//            HttpResponse response = getPage(URI.create(url), headers);
//            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                response.getEntity().consumeContent();
                return true;
//            }
        } catch (Exception e) {
            return false;
        } finally {
            Context.exit();
        }
//        return false;
    }

    public int calcolateAnswer(String script) throws Exception {

		int tot = 0;
		String[] lines = script.split("\n");
		String varName = "";
		for(String scriptLine : lines) {

			if(scriptLine.contains("var t,r,a,f,")) {
				varName = scriptLine.replace("var t,r,a,f,", "").replaceAll("[{};\"]", "");

				String[] tmp = varName.split(":");
				varName = tmp[0].replace("=", ".").trim();
				System.out.println(varName);
				tot = parseCalc(tot, "+", tmp[1]);

			}
			else if(scriptLine.contains(varName) && varName.length()>0) {
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

    protected void bypassAntiDDOS(Document doc, String baseUrl, String urlToRedirect) throws Exception {
		String jschl_vc = doc.getElementsByAttributeValue("name", "jschl_vc").get(0).attr("value");
		String pass = doc.getElementsByAttributeValue("name", "pass").get(0).attr("value");
//		System.out.println(jschl_vc);

		this.cloudFlareSolve(doc.html(), baseUrl);

		int jschl_answer = 0;
		Elements scriptElements = doc.getElementsByTag("script");
		Integer numberCalcLine = null;
		for (Element element :scriptElements ){
			for (DataNode node : element.dataNodes()) {
				String text = node.getWholeData();


				jschl_answer = this.calcolateAnswer(text);


			}
//			System.out.println(numberCalcLine);
		}

		if(!baseUrl.endsWith("/"))
			baseUrl = baseUrl + "/";
		String urlPage = baseUrl+"cdn-cgi/l/chk_jschl";
		System.out.println(urlPage+"?jschl_vc="+jschl_vc+"&pass="+URLDecoder.decode(pass, "UTF-8")+"&jschl_answer="+jschl_answer);
    }

    private static String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:24.0) Gecko/20100101 Firefox/24.0";

    public static void main(String[] args) throws Exception {
		CloudflareUtil u = new CloudflareUtil();
		String url = "https://prescene.tk/index.php?genre=House";
    	Document doc = Jsoup.connect(url).timeout(6000)
    			.userAgent(userAgent)
    			.ignoreHttpErrors(true).get();

		u.bypassAntiDDOS(doc, "prescene.tk", url);

	}
}
