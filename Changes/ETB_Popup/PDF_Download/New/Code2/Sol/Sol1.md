Your sample shows **missing Indian-script letters**, rather than the wrong language being selected. The backend already reads UTF-8, but this line uses XML Worker’s default fonts:

```java
XMLWorkerHelper.getInstance().parseXHtml(
        writer, document, htmlStream, StandardCharsets.UTF_8);
```

There are two requirements:

1. Embed fonts containing the selected language’s characters.
2. Shape characters correctly—for example, Hindi conjuncts and Malayalam vowel signs.

**iText 5 + XML Worker cannot correctly render all the Indian scripts in your notices.** Adding a Unicode font alone is therefore not a complete fix. iText’s documentation confirms that Indic-language rendering requires the newer renderer with pdfCalligraph. [iText PDF](https://www.itextpdf.com/products/features?utm_source=chatgpt.com)

The smallest complete change is to replace **only the backend HTML-to-PDF conversion**, keeping the working dropdown, AJAX, database lookup, and Base64 response.

### 1. Add the renderer dependencies

Use an organisation-approved compatible set of:

```text
com.itextpdf:html2pdf
com.itextpdf:typography
com.itextpdf:licensing-base
```

`html2pdf` brings the modern iText Core dependencies. `typography` is pdfCalligraph.

**pdfCalligraph requires a commercial licence and its licence setup.** Use the official [installation instructions](https://kb.itextpdf.com/itext/installing-itext-pdfcalligraph-for-java-developers) and [compatibility matrix](https://kb.itextpdf.com/itext/compatibility-matrix) to select matching versions. Package their runtime dependencies in `WEB-INF/lib`. [iText Knowledge Base](https://kb.itextpdf.com/itext/installing-itext-pdfcalligraph-for-java-developers?utm_source=chatgpt.com)

Keep your existing iText 5 JARs if other application features still use them. This change replaces their use only in this PDF method.

### 2. Add local font files

Create this directory inside the existing web application:

```text
WEB-INF/fonts/
```

Put **static Regular `.ttf` files** there with these names:

| Locale in your supplied data | Font file |
|---|---|
| `eng` | `NotoSans-Regular.ttf` |
| `hin`, `mar`, `dgo`, `mai` | `NotoSansDevanagari-Regular.ttf` |
| `ben`, `asm` | `NotoSansBengali-Regular.ttf` |
| `guj` | `NotoSansGujarati-Regular.ttf` |
| `pan` | `NotoSansGurmukhi-Regular.ttf` |
| `ory` | `NotoSansOriya-Regular.ttf` |
| `kan` | `NotoSansKannada-Regular.ttf` |
| `mal` | `NotoSansMalayalam-Regular.ttf` |
| `tel` | `NotoSansTelugu-Regular.ttf` |
| `kas` | `NotoNaskhArabic-Regular.ttf` |

Obtain the fonts from the [official Noto font project](https://github.com/notofonts), retain their licence files, and package them with the WAR. They are read locally; no external font request happens during download.

Each language’s actual notice still needs testing, especially Kashmiri’s additional letters and marks.

### 3. Add imports to `HomeLoanAction.java`

Add:

```java
import java.io.IOException;
import java.io.InputStream;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.layout.font.FontProvider;
```

Keep your existing imports if other methods use them.

### 4. Add these two helper methods

Insert them before your existing `preparePrivacyPdfXhtml()` method.

```java
private String[] getPrivacyPdfFont(String locale) {

    switch (locale) {

        case "eng":
            return new String[] {
                "NotoSans-Regular.ttf", "Noto Sans"
            };

        case "hin":
        case "mar":
        case "dgo":
        case "mai":
            return new String[] {
                "NotoSansDevanagari-Regular.ttf",
                "Noto Sans Devanagari"
            };

        case "ben":
        case "asm":
            return new String[] {
                "NotoSansBengali-Regular.ttf",
                "Noto Sans Bengali"
            };

        case "guj":
            return new String[] {
                "NotoSansGujarati-Regular.ttf",
                "Noto Sans Gujarati"
            };

        case "pan":
            return new String[] {
                "NotoSansGurmukhi-Regular.ttf",
                "Noto Sans Gurmukhi"
            };

        case "ory":
            return new String[] {
                "NotoSansOriya-Regular.ttf",
                "Noto Sans Oriya"
            };

        case "kan":
            return new String[] {
                "NotoSansKannada-Regular.ttf",
                "Noto Sans Kannada"
            };

        case "mal":
            return new String[] {
                "NotoSansMalayalam-Regular.ttf",
                "Noto Sans Malayalam"
            };

        case "tel":
            return new String[] {
                "NotoSansTelugu-Regular.ttf",
                "Noto Sans Telugu"
            };

        case "kas":
            return new String[] {
                "NotoNaskhArabic-Regular.ttf",
                "Noto Naskh Arabic"
            };

        default:
            throw new IllegalArgumentException(
                    "PDF font is not configured for locale: " + locale);
    }
}

private byte[] readPrivacyPdfFont(String filename) throws IOException {

    String resource = "/WEB-INF/fonts/" + filename;

    try (InputStream input = ServletActionContext
                .getServletContext()
                .getResourceAsStream(resource);
         ByteArrayOutputStream output = new ByteArrayOutputStream()) {

        if (input == null) {
            throw new IOException("Missing PDF font: " + resource);
        }

        byte[] buffer = new byte[8192];
        int count;

        while ((count = input.read(buffer)) != -1) {
            output.write(buffer, 0, count);
        }

        return output.toByteArray();
    }
}
```

Reading fonts through `getResourceAsStream()` supports both packaged and exploded WAR deployments.

### 5. Replace the conversion block — original Java lines 4214–4233

Keep this existing line:

```java
String xhtml = preparePrivacyPdfXhtml(privacyText);
```

Replace the block that creates the iText 5 `Document`, calls `parseXHtml()`, and reads `pdfBytes` with:

```java
byte[] pdfBytes;

String[] selectedFont = getPrivacyPdfFont(selectedLocale);

// Create a separate provider for this PDF.
FontProvider fontProvider = new FontProvider(selectedFont[1]);

if (!fontProvider.addFont(
        readPrivacyPdfFont("NotoSans-Regular.ttf"))) {

    throw new IOException("Unable to register the Latin PDF font.");
}

if (!"eng".equals(selectedLocale)) {

    if (!fontProvider.addFont(
            readPrivacyPdfFont(selectedFont[0]))) {

        throw new IOException(
                "Unable to register PDF font for: " + selectedLocale);
    }
}

// Your current helper wraps database fragments with <head></head>.
String pdfCss =
        "<style>"
        + "@page { size: A4; margin: 36pt; }"
        + "body { font-size: 11pt; }"
        + "* { font-family: '" + selectedFont[1]
        + "', 'Noto Sans' !important; }"
        + "</style>";

String pdfHtml = xhtml.replaceFirst(
        "(?i)</head\\s*>",
        pdfCss + "</head>");

if (pdfHtml.equals(xhtml)) {
    throw new IllegalArgumentException(
            "Privacy PDF HTML requires a head element.");
}

ConverterProperties properties = new ConverterProperties();

properties.setCharset("UTF-8");
properties.setFontProvider(fontProvider);

try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {

    HtmlConverter.convertToPdf(pdfHtml, output, properties);

    pdfBytes = output.toByteArray();
}
```

The modern converter uses the registered fonts and, with licensed pdfCalligraph installed, applies complex-script shaping. Preserve the existing `dir="rtl"` markup in your Kashmiri notice. [iText Knowledge Base](https://kb.itextpdf.com/itext/chapter-6-using-fonts-in-pdfhtml?utm_source=chatgpt.com)

Keep everything after this block, including:

```java
payload.put("pdfBase64",
        Base64.getEncoder().encodeToString(pdfBytes));
```

### 6. Remove obsolete cleanup from this method only

Inside `downloadPrivacyNoticePdf()`, remove:

```java
Document document = null;
```

Also remove its old `finally` block that closes `document`:

```java
finally {
    if (document != null && document.isOpen()) {
        // Old document-closing code
    }
}
```

Keep the existing `catch` blocks. `HtmlConverter.convertToPdf()` now performs the conversion and finalizes the PDF.

### 7. Verify the language PDFs

The JSP and Struts mapping need no changes for this rendering fix.

After installing the licence, dependencies, and fonts, test each locale against the popup. Check **joined letters, vowel placement, punctuation, and reading direction**, not merely whether characters appear.

I cannot honestly promise correct PDFs for all these languages using only the existing two iText 5 JARs. The replacement above addresses both missing fonts and shaping, but requires the licensed renderer and validation against your actual notices; I could not run it against your deployed application.



#############################################
###############################################
**There are no `5.5.13.2` versions of those modules compatible with iText 5.5.13.2.** `html2pdf` and `typography` belong to the newer iText architecture. They cannot extend your existing XML Worker renderer. [iText Knowledge Base](https://kb.itextpdf.com/itext/migration-guide-from-itext-5-to-itext-7?utm_source=chatgpt.com)

Your existing pair is correct for iText 5:

```text
itextpdf-5.5.13.2.jar
xmlworker-5.5.13.2.jar
```

However, that pair cannot provide complete Indic-language shaping.

Also, **I need to correct my earlier dependency name:** the licence library is **`com.itextpdf.licensing:licensing-base`**, not `com.itextpdf:licensing-base`. [iText Knowledge Base](https://kb.itextpdf.com/itext/installing-the-itext-license-key-and-license-key-l?utm_source=chatgpt.com)

A documented compatible **new-renderer example** is:

| Component | Maven coordinates | Version |
|---|---|---|
| Core, brought by pdfHTML dependencies | Modern iText Core modules | `9.2.0` |
| HTML converter | `com.itextpdf:html2pdf` | `6.2.0` |
| pdfCalligraph | `com.itextpdf:typography` | `5.0.2` |
| Licence library | `com.itextpdf.licensing:licensing-base` | `4.2.2` |

These versions match each other—not your iText 5 renderer. Use an organisation-approved release set from the [compatibility matrix](https://kb.itextpdf.com/itext/compatibility-matrix). [kb.itextpdf.com](https://kb.itextpdf.com/itext/release-itext-core-9-2-0?utm_source=chatgpt.com)

For that example, add normal Maven dependencies:

```xml
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>html2pdf</artifactId>
    <version>6.2.0</version>
</dependency>

<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>typography</artifactId>
    <version>5.0.2</version>
</dependency>

<dependency>
    <groupId>com.itextpdf.licensing</groupId>
    <artifactId>licensing-base</artifactId>
    <version>4.2.2</version>
</dependency>
```

Add the official repository inside your existing `<repositories>` section:

```xml
<repository>
    <id>itext-releases</id>
    <url>https://repo.itextsupport.com/releases</url>
</repository>
```

Get the JARs from:

- [Official iText repository](https://repo.itextsupport.com/releases)
- [Official download page](https://itextpdf.com/resources/downloads)
- [pdfCalligraph installation instructions](https://kb.itextpdf.com/itext/installing-itext-pdfcalligraph-for-java-developers)

**pdfCalligraph needs a commercial licence and licence initialization.** [iText Knowledge Base](https://kb.itextpdf.com/itext/installing-itext-pdfcalligraph-for-java-developers?utm_source=chatgpt.com)

Prefer Maven resolution for these new modules: manually adding just three JARs with `scope=system` does not bring their required dependencies. If you must install manually, include the complete runtime dependency set in `WebContent/WEB-INF/lib`.

You can retain your existing iText 5 dependencies for other reports and use the newer renderer only for privacy PDFs. **If you must use only iText 5.5.13.2 and XML Worker, no additional compatible JAR can guarantee correct rendering of all your Indian languages.**

