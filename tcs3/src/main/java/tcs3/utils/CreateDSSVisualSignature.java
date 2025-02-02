package tcs3.utils;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ThaiBuddhistChronology;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.fontbox.ttf.OpenTypeFont;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.examples.signature.CreateSignatureBase;
import org.apache.pdfbox.examples.signature.SigUtils;
import org.apache.pdfbox.examples.signature.ValidationTimeStamp;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName;
import org.apache.pdfbox.pdmodel.font.encoding.WinAnsiEncoding;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.ExternalSigningSupport;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureInterface;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureOptions;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;
import org.apache.pdfbox.util.Hex;
import org.apache.pdfbox.util.Matrix;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class CreateDSSVisualSignature extends CreateSignatureBase {
    public static final Logger logger = LoggerFactory.getLogger(CreateDSSVisualSignature.class);
    private SignatureOptions signatureOptions;
    private boolean lateExternalSigning = false;
    private File imageFile = null;
    private String reason = "Reason";
    private String location = "Location";
    private String contactInfo = "contactInfo";

    /**
     * Initialize the signature creator with a keystore (pkcs12) and pin that
     * should be used for the signature.
     *
     * @param keystore is a pkcs12 keystore.
     * @param pin      is the pin for the keystore / private key
     * @throws KeyStoreException         if the keystore has not been initialized
     *                                   (loaded)
     * @throws NoSuchAlgorithmException  if the algorithm for recovering the key
     *                                   cannot be found
     * @throws UnrecoverableKeyException if the given password is wrong
     * @throws CertificateException      if the certificate is not valid as signing
     *                                   time
     * @throws IOException               if no certificate could be found
     */
    public CreateDSSVisualSignature(KeyStore keystore, char[] pin)
            throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException,
            CertificateException {
        super(keystore, pin);
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public boolean isLateExternalSigning() {
        return lateExternalSigning;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    /**
     * Set late external signing. Enable this if you want to activate the demo code
     * where the
     * signature is kept and added in an extra step without using PDFBox methods.
     * This is disabled
     * by default.
     *
     * @param lateExternalSigning
     */
    public void setLateExternalSigning(boolean lateExternalSigning) {
        this.lateExternalSigning = lateExternalSigning;
    }

    /**
     * Sign pdf file and create new file that ends with "_signed.pdf".
     *
     * @param inputFile The source pdf document file.
     * @param humanRect rectangle from a human viewpoint (coordinates start at top
     *                  left)
     * @param tsaUrl    optional TSA url
     * @throws IOException
     */
    public ByteArrayOutputStream signPDF(ByteArrayOutputStream sourcOutputStream, int pageNum, Rectangle2D humanRect,
            String tsaUrl)
            throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        try (PipedInputStream in = new PipedInputStream();
                PipedOutputStream out = new PipedOutputStream(in)) {
            new Thread(() -> {
                try (out) {
                    sourcOutputStream.writeTo(out);
                } catch (IOException iox) {
                    // ...
                }
            }).start();

            outStream = this.signPDF(in, pageNum, humanRect, tsaUrl, null);
        }

        return outStream;
    }

    /**
     * Sign pdf file and create new file that ends with "_signed.pdf".
     *
     * @param inputFile          The source pdf document file.
     * @param humanRect          rectangle from a human viewpoint (coordinates start
     *                           at top left)
     * @param tsaUrl             optional TSA url
     * @param signatureFieldName optional name of an existing (unsigned) signature
     *                           field
     * @throws IOException
     */
    public ByteArrayOutputStream signPDF(InputStream inputStream, int pageNum, Rectangle2D humanRect, String tsaUrl,
            String signatureFieldName) throws IOException {
        if (inputStream == null) {
            throw new IOException("Document for signing does not exist");
        }

        setTsaUrl(tsaUrl);

        // creating output document and prepare the IO streams.
        ByteArrayOutputStream signedBaos = new ByteArrayOutputStream();

        try (PDDocument doc = Loader.loadPDF(inputStream.readAllBytes())) {

            addSignatureToPage(doc, pageNum, humanRect, "signature_page_" + pageNum);

            doc.saveIncremental(signedBaos);

        }

        // Do not close signatureOptions before saving, because some COSStream objects
        // within
        // are transferred to the signed document.
        // Do not allow signatureOptions get out of scope before saving, because then
        // the COSDocument
        // in signature options might by closed by gc, which would close COSStream
        // objects prematurely.
        // See https://issues.apache.org/jira/browse/PDFBOX-3743
        IOUtils.closeQuietly(signatureOptions);

        return signedBaos;
    }

    private void addSignatureToPage(PDDocument doc, int pageNum, Rectangle2D humanRect, String signatureFieldName)
            throws IOException {
        // call SigUtils.checkCrossReferenceTable(doc) if Adobe complains
        // and read https://stackoverflow.com/a/71293901/535646
        // and https://issues.apache.org/jira/browse/PDFBOX-5382

        int accessPermissions = SigUtils.getMDPPermission(doc);
        if (accessPermissions == 1) {
            throw new IllegalStateException(
                    "No changes to the document are permitted due to DocMDP transform parameters dictionary");
        }

        // Note that PDFBox has a bug that visual signing on certified files with
        // permission 2
        // doesn't work properly, see PDFBOX-3699. As long as this issue is open, you
        // may want to
        // be careful with such files.

        PDSignature signature = null;
        PDAcroForm acroForm = doc.getDocumentCatalog().getAcroForm(null);
        PDRectangle rect = null;

        // sign a PDF with an existing empty signature, as created by the
        // CreateEmptySignatureForm example.
        if (acroForm != null) {
            signature = findExistingSignature(acroForm, signatureFieldName);
            if (signature != null) {
                rect = acroForm.getField(signatureFieldName).getWidgets().get(0).getRectangle();
            }
        }

        if (signature == null) {
            // create signature dictionary
            signature = new PDSignature();
        }

        if (rect == null) {
            rect = createSignatureRectangle(doc, humanRect);
        }

        // Optional: certify
        // can be done only if version is at least 1.5 and if not already set
        // doing this on a PDF/A-1b file fails validation by Adobe preflight
        // (PDFBOX-3821)
        // PDF/A-1b requires PDF version 1.4 max, so don't increase the version on such
        // files.
        if (doc.getVersion() >= 1.5f && accessPermissions == 0) {
            SigUtils.setMDPPermission(doc, signature, 2);
        }

        if (acroForm != null && acroForm.getNeedAppearances()) {
            // PDFBOX-3738 NeedAppearances true results in visible signature becoming
            // invisible
            // with Adobe Reader
            if (acroForm.getFields().isEmpty()) {
                // we can safely delete it if there are no fields
                acroForm.getCOSObject().removeItem(COSName.NEED_APPEARANCES);
                // note that if you've set MDP permissions, the removal of this item
                // may result in Adobe Reader claiming that the document has been changed.
                // and/or that field content won't be displayed properly.
                // ==> decide what you prefer and adjust your code accordingly.
            } else {
                logger.warn("/NeedAppearances is set, signature may be ignored by Adobe Reader");
            }
        }

        // default filter
        signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);

        // subfilter for basic and PAdES Part 2 signatures
        signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);

        signature.setName("Name");
        signature.setLocation(location);
        signature.setReason(reason);
        signature.setContactInfo(contactInfo);

        // the signing date, needed for valid signature
        signature.setSignDate(Calendar.getInstance());

        // do not set SignatureInterface instance, if external signing used
        SignatureInterface signatureInterface = isExternalSigning() ? null : this;
        signatureOptions = new SignatureOptions();

        signatureOptions.setVisualSignature(createVisualSignatureTemplate(doc, pageNum, rect, signature));
        signatureOptions.setPage(pageNum);

        // register signature dictionary and sign interface
        doc.addSignature(signature, signatureInterface, signatureOptions);

    }

    private PDRectangle createSignatureRectangle(PDDocument doc, Rectangle2D humanRect) {
        float x = (float) humanRect.getX();
        float y = (float) humanRect.getY();
        float width = (float) humanRect.getWidth();
        float height = (float) humanRect.getHeight();
        PDPage page = doc.getPage(0);
        PDRectangle pageRect = page.getCropBox();
        PDRectangle rect = new PDRectangle();
        // signing should be at the same position regardless of page rotation.
        switch (page.getRotation()) {
            case 90:
                rect.setLowerLeftY(x);
                rect.setUpperRightY(x + width);
                rect.setLowerLeftX(y);
                rect.setUpperRightX(y + height);
                break;
            case 180:
                rect.setUpperRightX(pageRect.getWidth() - x);
                rect.setLowerLeftX(pageRect.getWidth() - x - width);
                rect.setLowerLeftY(y);
                rect.setUpperRightY(y + height);
                break;
            case 270:
                rect.setLowerLeftY(pageRect.getHeight() - x - width);
                rect.setUpperRightY(pageRect.getHeight() - x);
                rect.setLowerLeftX(pageRect.getWidth() - y - height);
                rect.setUpperRightX(pageRect.getWidth() - y);
                break;
            case 0:
            default:
                rect.setLowerLeftX(x);
                rect.setUpperRightX(x + width);
                rect.setLowerLeftY(pageRect.getHeight() - y - height);
                rect.setUpperRightY(pageRect.getHeight() - y);
                break;
        }
        return rect;
    }

    // create a template PDF document with empty signature and return it as a
    // stream.
    private InputStream createVisualSignatureTemplate(PDDocument srcDoc, int pageNum,
            PDRectangle rect, PDSignature signature) throws IOException {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(srcDoc.getPage(pageNum).getMediaBox());
            doc.addPage(page);
            PDAcroForm acroForm = new PDAcroForm(doc);
            doc.getDocumentCatalog().setAcroForm(acroForm);
            PDSignatureField signatureField = new PDSignatureField(acroForm);
            PDAnnotationWidget widget = signatureField.getWidgets().get(0);
            List<PDField> acroFormFields = acroForm.getFields();
            acroForm.setSignaturesExist(true);
            acroForm.setAppendOnly(true);
            acroForm.getCOSObject().setDirect(true);
            acroFormFields.add(signatureField);

            widget.setRectangle(rect);

            // from PDVisualSigBuilder.createHolderForm()
            PDStream stream = new PDStream(doc);
            PDFormXObject form = new PDFormXObject(stream);
            PDResources res = new PDResources();
            form.setResources(res);
            form.setFormType(1);
            PDRectangle bbox = new PDRectangle(rect.getWidth(), rect.getHeight());
            float height = bbox.getHeight();
            Matrix initialScale = null;
            switch (srcDoc.getPage(pageNum).getRotation()) {
                case 90:
                    form.setMatrix(AffineTransform.getQuadrantRotateInstance(1));
                    initialScale = Matrix.getScaleInstance(bbox.getWidth() / bbox.getHeight(),
                            bbox.getHeight() / bbox.getWidth());
                    height = bbox.getWidth();
                    break;
                case 180:
                    form.setMatrix(AffineTransform.getQuadrantRotateInstance(2));
                    break;
                case 270:
                    form.setMatrix(AffineTransform.getQuadrantRotateInstance(3));
                    initialScale = Matrix.getScaleInstance(bbox.getWidth() / bbox.getHeight(),
                            bbox.getHeight() / bbox.getWidth());
                    height = bbox.getWidth();
                    break;
                case 0:
                default:
                    break;
            }
            form.setBBox(bbox);
            // PDFont font = new PDType1Font(FontName.HELVETICA_BOLD);

            Resource sarabunTTF = new ClassPathResource("static/fonts/THSarabun.ttf");

            // PDFont font = PDTrueTypeFont.load(doc, sarabunTTF.getFile(),
            // WinAnsiEncoding.INSTANCE);

            PDFont font = PDType0Font.load(doc, sarabunTTF.getInputStream(), false);

            // from PDVisualSigBuilder.createAppearanceDictionary()
            PDAppearanceDictionary appearance = new PDAppearanceDictionary();
            appearance.getCOSObject().setDirect(true);
            PDAppearanceStream appearanceStream = new PDAppearanceStream(form.getCOSObject());
            appearance.setNormalAppearance(appearanceStream);
            widget.setAppearance(appearance);

            try (PDPageContentStream cs = new PDPageContentStream(doc, appearanceStream)) {
                // for 90° and 270° scale ratio of width / height
                // not really sure about this
                // why does scale have no effect when done in the form matrix???
                if (initialScale != null) {
                    cs.transform(initialScale);
                }

                // show background (just for debugging, to see the rect size + position)
                // cs.setNonStrokingColor(Color.LIGHT_GRAY);
                // cs.addRect(-5000, -5000, 10000, 10000);
                // cs.fill();

                if (imageFile != null) {
                    // show background image
                    // save and restore graphics if the image is too large and needs to be scaled
                    cs.saveGraphicsState();
                    cs.transform(Matrix.getScaleInstance(0.03f, 0.03f));
                    PDImageXObject img = PDImageXObject.createFromFileByExtension(imageFile, doc);
                    cs.drawImage(img, 0, 0);
                    cs.restoreGraphicsState();
                }

                // show text
                float fontSize = 9;
                float leading = fontSize * 0.9f;
                cs.beginText();
                cs.setFont(font, fontSize);
                cs.setNonStrokingColor(Color.black);
                cs.newLineAtOffset(fontSize * 3f, (height - leading) + 0.8f);
                cs.setLeading(leading);

                X509Certificate cert = (X509Certificate) getCertificateChain()[0];

                // https://stackoverflow.com/questions/2914521/
                X500Name x500Name = new X500Name(cert.getSubjectX500Principal().getName());
                RDN cn = x500Name.getRDNs(BCStyle.CN)[0];
                String name = IETFUtils.valueToString(cn.getFirst().getValue());

                // See https://stackoverflow.com/questions/12575990
                // for better date formatting
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                        "EEEE dd MMMM Gyyyy, เวลา HH:mm.ss น.")
                        .withLocale(new Locale("Th"))
                        .withChronology(ThaiBuddhistChronology.INSTANCE);

                /// String date = signature.getSignDate().getTime();
                String date = LocalDateTime.now().format(formatter);
                // String reason = signature.getReason();

                cs.showText("หนังสือฉบับนี้ถูกลงลายมือดิจิทัลด้วยใบรับรองของ: " + name);
                cs.newLine();
                cs.showText("ออก ณ วันที่: " + date);
                cs.newLine();
                cs.showText("วัตถุประสงค์ของหนังสือ: " + reason);

                cs.endText();
            }

            // no need to set annotations and /P entry

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos);
            return new ByteArrayInputStream(baos.toByteArray());
        }
    }

    // Find an existing signature (assumed to be empty). You will usually not need
    // this.
    private PDSignature findExistingSignature(PDAcroForm acroForm, String sigFieldName) {
        PDSignature signature = null;
        PDSignatureField signatureField;
        if (acroForm != null) {
            signatureField = (PDSignatureField) acroForm.getField(sigFieldName);
            if (signatureField != null) {
                // retrieve signature dictionary
                signature = signatureField.getSignature();
                if (signature == null) {
                    signature = new PDSignature();
                    // after solving PDFBOX-3524
                    // signatureField.setValue(signature)
                    // until then:
                    signatureField.getCOSObject().setItem(COSName.V, signature);
                } else {
                    throw new IllegalStateException("The signature field " + sigFieldName + " is already signed.");
                }
            }
        }
        return signature;
    }

}
