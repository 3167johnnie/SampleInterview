package com.mintstreet.common.security;

import java.time.LocalDateTime;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class XSSRequestWrapper extends HttpServletRequestWrapper {

	private static final Logger logger = LogManager.getLogger(XSSRequestWrapper.class);

	private static Pattern[] patterns = new Pattern[] {

			Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
			Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
			Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
			Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("alert", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("console", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("confirm", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

			// New Added Patterns
			Pattern.compile("<html(.*?)", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<body(.*?)", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<applet(.*?)", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<frameset(.*?)", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<IMG SRC=(.*?)", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<input(.*?)", Pattern.CASE_INSENSITIVE),
			Pattern.compile("prompt", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<form(.*?)", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<iframe(.*?)", Pattern.CASE_INSENSITIVE),
			Pattern.compile("onerror", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<meta(.*?)", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<svg(.*?)", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<h(.*?) ", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<style(.*?)", Pattern.CASE_INSENSITIVE),
			Pattern.compile("object data=(.*?)", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<var(.*?)", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<audio(.*?)", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<video(.*?)", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<marquee(.*?)", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<image(.*?)", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<div(.*?)", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<title(.*?)", Pattern.CASE_INSENSITIVE),
			Pattern.compile("document", Pattern.CASE_INSENSITIVE),

			
			  
			  
			  Pattern.compile("<a(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<x(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<s(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<base(.*?)",  Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<header(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<content(.*?)", Pattern.CASE_INSENSITIVE),
			  
			  Pattern.compile("<embed(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<math(.*?)", Pattern.CASE_INSENSITIVE), 
			  Pattern.compile("<link(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<object(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<br(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<keygen(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<select(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<menu(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<acronym(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<abbr(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<address(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<hgroup(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<spacer(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<span(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<strike(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<summary(.*?)", Pattern.CASE_INSENSITIVE),    
			  Pattern.compile("<table(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<tbody(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<canvas(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<caption(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<center(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<code(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<cite(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<template(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<mark(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<menuitem(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<meter(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<multicol(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<noframes(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<noscript(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<set(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<t:set(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<shadow(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<picture(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<plaintext(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<progress(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<label(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<legend(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<track(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<wbr(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<option(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<output(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<param(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<ruby(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<pre(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<samp(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<section(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<slot(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<small(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<source(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<sub(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<sup(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<textarea(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<aside(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<basefont(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<bdi(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<bdo(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<bgsound(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<blockquote(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<big(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<blink(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<button(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<command(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<colgroup(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<tfoot(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<xss(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<strong(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<dialog(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<dir(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<element(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<figcaption(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<fieldset(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<figure(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<frame(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<nextid(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<noembed(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<article(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<area(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<font(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<footer(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<head(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<main(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<map(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<optgroup(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<data(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<datalist(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<details(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<del(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<dfn(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<dd(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<col(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<b(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<dl(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<dt(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<em(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<hr(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<i(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<isindex(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<ins(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<kbd(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<li(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<listing(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<nav(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<time(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<nobr(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<ol(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<p(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<xmp(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<rb(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<q(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<rp(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<rt(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<rtc(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<tt(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<u(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<ul(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<thead(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<th(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<td(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<tr(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<layer(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<import(.*?)", Pattern.CASE_INSENSITIVE),
			//  Pattern.compile("xsl", Pattern.CASE_INSENSITIVE),   
			  Pattern.compile("<listener(.*?)", Pattern.CASE_INSENSITIVE),   
			  Pattern.compile("<maction(.*?)", Pattern.CASE_INSENSITIVE),     
			  Pattern.compile("<path(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<rect(.*?)", Pattern.CASE_INSENSITIVE),    
			//  Pattern.compile("xml", Pattern.CASE_INSENSITIVE), 
			  Pattern.compile("<!DOCTYPE(.*?)", Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<!ATTLIST(.*?)", Pattern.CASE_INSENSITIVE), 
			  Pattern.compile("<circle(.*?)",  Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<animation(.*?)",Pattern.CASE_INSENSITIVE), 
			  Pattern.compile("<foreignObject(.*?)",Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<animate(.*?)",Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<handler(.*?)",Pattern.CASE_INSENSITIVE),
			  Pattern.compile("<feImage(.*?)",Pattern.CASE_INSENSITIVE),
			  
			  
			 

			/*
			 * Pattern.compile("<", Pattern.CASE_INSENSITIVE), Pattern.compile(">",
			 * Pattern.CASE_INSENSITIVE), Pattern.compile("{", Pattern.CASE_INSENSITIVE),
			 * Pattern.compile("}", Pattern.CASE_INSENSITIVE), Pattern.compile("[",
			 * Pattern.CASE_INSENSITIVE), Pattern.compile("]", Pattern.CASE_INSENSITIVE),
			 * Pattern.compile("(", Pattern.CASE_INSENSITIVE), Pattern.compile(")",
			 * Pattern.CASE_INSENSITIVE), Pattern.compile("'", Pattern.CASE_INSENSITIVE),
			 * Pattern.compile("\"", Pattern.CASE_INSENSITIVE), Pattern.compile("/",
			 * Pattern.CASE_INSENSITIVE), Pattern.compile("\\", Pattern.CASE_INSENSITIVE),
			 * Pattern.compile("*", Pattern.CASE_INSENSITIVE), Pattern.compile(";",
			 * Pattern.CASE_INSENSITIVE), Pattern.compile(":", Pattern.CASE_INSENSITIVE),
			 * Pattern.compile("=", Pattern.CASE_INSENSITIVE), Pattern.compile("`",
			 * Pattern.CASE_INSENSITIVE), Pattern.compile("%", Pattern.CASE_INSENSITIVE),
			 * Pattern.compile("+", Pattern.CASE_INSENSITIVE), Pattern.compile("^",
			 * Pattern.CASE_INSENSITIVE), Pattern.compile("!", Pattern.CASE_INSENSITIVE),
			 * Pattern.compile("-", Pattern.CASE_INSENSITIVE),
			 */
	};

	public XSSRequestWrapper(HttpServletRequest servletRequest) {
		super(servletRequest);
		//logger.info("Inside XSSRequestWrapper Constructor=============at==============" + LocalDateTime.now());
	}

	@Override
	public String[] getParameterValues(String parameter) {
		logger.info(
				"getParameterValues Value is ========" + parameter + "==============at=========" + LocalDateTime.now());
		String[] values = super.getParameterValues(parameter);

		if (values == null) {
			return null;
		}

		int count = values.length;
		String[] encodedValues = new String[count];
		for (int i = 0; i < count; i++) {
			encodedValues[i] = stripXSS(values[i]);
		}

		return encodedValues;
	}

	@Override
	public String getParameter(String parameter) {
//		logger.info("getParameter Value is ========" + parameter + "==============at=========" + LocalDateTime.now());
		String value = super.getParameter(parameter);

		return stripXSS(value);
	}

	@Override
	public String getHeader(String name) {
//		logger.info("Header Value is ========" + name + "==============at=========" + LocalDateTime.now());
		String value = null;
		if (name.equalsIgnoreCase("<>") || name.equalsIgnoreCase("alert") || name.equalsIgnoreCase("<") || name.equalsIgnoreCase(">") || name.equalsIgnoreCase(" ") || name.equalsIgnoreCase(" (") || name.equalsIgnoreCase(" )") || name.equalsIgnoreCase(" <script>")) {
			logger.info("Header Value is INSIDE IF ========" + name + "==============at=========" + LocalDateTime.now());
		} else {
			value = super.getHeader(name);
		//	logger.info("Header Value is INSIDE ELSE ========" + name + "==============at=========" + LocalDateTime.now());
		}
		return stripXSS(value);
	  }
	
	private String stripXSS(String value) {    
		
		String regexValue="[`|;|:|(|)|<|>|{|}|^|!|*|%|\"|']";
		
//		logger.info("stripXSS Value is ========" + value + "==============at=========" + LocalDateTime.now());
		if (value != null) {
			try {
				value = value.replaceAll("\0", "");
				for (Pattern scriptPattern : patterns) {     
					value = scriptPattern.matcher(value).replaceAll("");
				}
				value=value.replaceAll(regexValue,"");  
//				logger.info("Final value After stripXSS scriptPattern value===" + value); 
			} catch (PatternSyntaxException e) {
				logger.info("XSSRequestWrapper.java LNo : 288 : Exception Caught ", e);
			} catch (Exception e) {
				logger.info("XSSRequestWrapper.java LNo : 290 : Exception Caught ", e);
			
			}
		}
		return value;
	}
}
