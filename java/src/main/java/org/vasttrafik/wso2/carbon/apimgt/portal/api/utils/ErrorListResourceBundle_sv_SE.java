package org.vasttrafik.wso2.carbon.apimgt.portal.api.utils;

import org.vasttrafik.wso2.carbon.common.api.utils.AbstractErrorListResourceBundle;

/**
 * @author Lars Andersson
 *
 */
public final class ErrorListResourceBundle_sv_SE extends AbstractErrorListResourceBundle {

    private static Object[][] content = {
            // Identity server defined error codes
            {"17001","Felaktigt användarnamn","Användaren {0} existerar inte. Ange korrekt användarnamn och försök igen."},
            {"17002","Felaktigt lösenord","Det angivna lösenordet är felaktigt. Ange korrekt lösenord och försök igen."},
            {"17003","Kontot är låst","Användarkontot är låst och mäste låsas upp."},
            {"18001","Felaktig kod","Den angivna koden är felaktig. Ange korrekt kod och försök igen."},
            {"18002","Ogiltig nyckel/kod","Den angivna nyckeln/koden är inte längre giltig."},
            {"18003","Felaktigt användarnamn","Användaren {0} existerar inte. Ange korrekt användarnamn och försök igen"},
            {"18004","Felaktigt svar","Det angivna svaret {0} är felaktigt. försök igen."},
            {"18013","Felaktig kod","Den angivna koden är felaktig. Ange korrekt kod och försök igen."},
            // Custom error codes
            {"1000","Ogiltigt parametervärde","Det angivna värdet {0} för parametern {1} är ogiltigt."},
            {"1001","Resursen hittades inte","Den angivna resursen {0} hittades inte."},
            {"1002","Upptaget användarnamn","Användarnamnet {0} är upptaget. Var vänlig välj ett annat."},
            {"1003","Ogiltig token","Angiven token är inte längre giltig. Var vänlig logga in på nytt."},
            {"1004","Felaktig token","Angiven token är felaktig. Var vänlig ange en korrekt token."},
            {"1005","Felaktig token","Angiven token tillhör en annan användare. Var vänlig ange en korrekt token."},
            {"1006","Felaktigt användarnamn/lösenord","Angivet användarnamn/lösenord felaktigt. Var vänlig försök igen."},
            {"1007","Kontot är låst","Användarkontot är låst. Var vänlig kontakta supporten."},
            {"2000","Felaktigt attribut","Angivet attribut {0} felaktigt. Var vänlig ange ett korrekt attribut."},
            {"2001","Resursen hittades inte","Den angivna resursen {0} hittades inte."},
            {"2002","Store returnerade ett fel","Store returnerade ett fel {0}. Var vänlig försök igen lite senare."},
            {"2003","Ogiltig token","Angiven token är inte giltig. Var vänlig logga in på nytt."},
            {"2004","Ange användarnamn och lösenord","Både användarnamn och lösenord måste anges."},
            {"2005","Felaktig token","Angiven token tillhör en annan användare. Var vänlig ange en korrekt token."},
            {"2006","Felaktig token","Angiven token tillhör en annan användare. Var vänlig ange en korrekt token."},
            {"2007","Applikationen godkändes inte","Applikationen godkändes inte. Var vänlig försök igen lite senare."},
    };

    @Override
    protected Object[][] getContents() {
        return content;
    }
}
