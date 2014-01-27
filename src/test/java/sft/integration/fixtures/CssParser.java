package sft.integration.fixtures;

import com.steadystate.css.parser.CSSOMParser;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class CssParser {

    private static final String SFT_CSS_FILE = "target/sft-result/sft-html-default/sft.css";
    private HashMap<String, CSSStyleRule> rules;

    public CssParser(){
        this(SFT_CSS_FILE);
    }

    public CssParser(String cssFile) {
        try {
            rules = extractCssStyleRules(cssFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap<String, CSSStyleRule> extractCssStyleRules(String cssFile) throws IOException {
        TestFileSystem.filesExists(cssFile);
        CSSOMParser cssParser = new CSSOMParser();
        CSSStyleSheet css = cssParser.parseStyleSheet(new InputSource(new FileReader(cssFile)), null, null);
        CSSRuleList cssRules = css.getCssRules();
        HashMap<String, CSSStyleRule> rules = new HashMap<String, CSSStyleRule>();
        for (int i = 0; i < cssRules.getLength(); i++) {
            CSSRule rule = cssRules.item(i);
            if (rule instanceof CSSStyleRule) {
                rules.put(((CSSStyleRule) rule).getSelectorText(), (CSSStyleRule) rule);
            }
        }
        return rules;
    }

    public CSSStyleRule get(String ruleName) {
        return rules.get(ruleName);
    }
}
