//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.itextpdf.signatures;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.PdfSigFieldLock;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfSignatureFormField;
import com.itextpdf.io.source.ByteBuffer;
import com.itextpdf.io.source.IRandomAccessSource;
import com.itextpdf.io.source.RASInputStream;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.io.util.DateTimeUtil;
import com.itextpdf.io.util.FileUtil;
import com.itextpdf.io.util.StreamUtil;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfAConformanceLevel;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDate;
import com.itextpdf.kernel.pdf.PdfDeveloperExtension;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfLiteral;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfOutputStream;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.kernel.pdf.annot.PdfWidgetAnnotation;
import com.itextpdf.pdfa.PdfADocument;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bouncycastle.asn1.esf.SignaturePolicyIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PdfSigner {
    public static final int NOT_CERTIFIED = 0;
    public static final int CERTIFIED_NO_CHANGES_ALLOWED = 1;
    public static final int CERTIFIED_FORM_FILLING = 2;
    public static final int CERTIFIED_FORM_FILLING_AND_ANNOTATIONS = 3;
    protected int certificationLevel;
    protected String fieldName;
    protected RandomAccessFile raf;
    protected byte[] bout;
    protected long[] range;
    protected PdfDocument document;
    protected PdfSignature cryptoDictionary;
    private PdfName digestMethod;
    protected PdfSigner.ISignatureEvent signatureEvent;
    protected OutputStream originalOS;
    protected ByteArrayOutputStream temporaryOS;
    protected File tempFile;
    protected Map<PdfName, PdfLiteral> exclusionLocations;
    protected boolean preClosed;
    protected PdfSigFieldLock fieldLock;
    protected PdfSignatureAppearance appearance;
    protected Calendar signDate;
    protected boolean closed;

    /** @deprecated */
    @Deprecated
    public PdfSigner(PdfReader reader, OutputStream outputStream, boolean append) throws IOException {
        this(reader, outputStream, (String)null, append);
    }

    /** @deprecated */
    @Deprecated
    public PdfSigner(PdfReader reader, OutputStream outputStream, String path, boolean append) throws IOException {
        this(reader, outputStream, path, initStampingProperties(append));
    }

    public PdfSigner(PdfReader reader, OutputStream outputStream, StampingProperties properties) throws IOException {
        this(reader, outputStream, (String)null, properties);
    }

    public PdfSigner(PdfReader reader, OutputStream outputStream, String path, StampingProperties properties) throws IOException {
        this.certificationLevel = 0;
        this.preClosed = false;
        StampingProperties localProps = properties.preserveEncryption();
        if (path == null) {
            this.temporaryOS = new ByteArrayOutputStream();
            this.document = this.initDocument(reader, new PdfWriter(this.temporaryOS), localProps);
        } else {
            this.tempFile = FileUtil.createTempFile(path);
            this.document = this.initDocument(reader, new PdfWriter(FileUtil.getFileOutputStream(this.tempFile)), localProps);
        }

        this.originalOS = outputStream;
        this.signDate = DateTimeUtil.getCurrentTimeCalendar();
        this.fieldName = this.getNewSigFieldName();
        this.appearance = new PdfSignatureAppearance(this.document, new Rectangle(0.0F, 0.0F), 1);
        this.appearance.setSignDate(this.signDate);
        this.closed = false;
    }

    protected PdfDocument initDocument(PdfReader reader, PdfWriter writer, StampingProperties properties) {
        PdfAConformanceLevel conformanceLevel = reader.getPdfAConformanceLevel();
        return (PdfDocument)(null == conformanceLevel ? new PdfDocument(reader, writer, properties) : new PdfADocument(reader, writer, properties));
    }

    public Calendar getSignDate() {
        return this.signDate;
    }

    public void setSignDate(Calendar signDate) {
        this.signDate = signDate;
        this.appearance.setSignDate(signDate);
    }

    public PdfSignatureAppearance getSignatureAppearance() {
        return this.appearance;
    }

    public int getCertificationLevel() {
        return this.certificationLevel;
    }

    public void setCertificationLevel(int certificationLevel) {
        this.certificationLevel = certificationLevel;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public PdfSignature getSignatureDictionary() {
        return this.cryptoDictionary;
    }

    public PdfSigner.ISignatureEvent getSignatureEvent() {
        return this.signatureEvent;
    }

    public void setSignatureEvent(PdfSigner.ISignatureEvent signatureEvent) {
        this.signatureEvent = signatureEvent;
    }

    public String getNewSigFieldName() {
        PdfAcroForm acroForm = PdfAcroForm.getAcroForm(this.document, true);
        String name = "Signature";

        int step;
        for(step = 1; acroForm.getField(name + step) != null; ++step) {
        }

        return name + step;
    }

    public void setFieldName(String fieldName) {
        if (fieldName != null) {
            if (fieldName.indexOf(46) >= 0) {
                throw new IllegalArgumentException("Field names cannot contain a dot.");
            }

            PdfAcroForm acroForm = PdfAcroForm.getAcroForm(this.document, true);
            if (acroForm.getField(fieldName) != null) {
                PdfFormField field = acroForm.getField(fieldName);
                if (!PdfName.Sig.equals(field.getFormType())) {
                    throw new IllegalArgumentException("Field type is not a signature field type.");
                }

                if (field.getValue() != null) {
                    throw new IllegalArgumentException("Field has been already signed.");
                }

                this.appearance.setFieldName(fieldName);
                List<PdfWidgetAnnotation> widgets = field.getWidgets();
                if (widgets.size() > 0) {
                    PdfWidgetAnnotation widget = (PdfWidgetAnnotation)widgets.get(0);
                    this.appearance.setPageRect(this.getWidgetRectangle(widget));
                    this.appearance.setPageNumber(this.getWidgetPageNumber(widget));
                }
            }

            this.fieldName = fieldName;
        }

    }

    public PdfDocument getDocument() {
        return this.document;
    }

    protected void setDocument(PdfDocument document) {
        this.document = document;
    }

    public void setOriginalOutputStream(OutputStream originalOS) {
        this.originalOS = originalOS;
    }

    public PdfSigFieldLock getFieldLockDict() {
        return this.fieldLock;
    }

    public void setFieldLockDict(PdfSigFieldLock fieldLock) {
        this.fieldLock = fieldLock;
    }

    public void signDetached(IExternalDigest externalDigest, IExternalSignature externalSignature, Certificate[] chain, Collection<ICrlClient> crlList, IOcspClient ocspClient, ITSAClient tsaClient, int estimatedSize, PdfSigner.CryptoStandard sigtype) throws IOException, GeneralSecurityException {
        this.signDetached(externalDigest, externalSignature, chain, crlList, ocspClient, tsaClient, estimatedSize, sigtype, (SignaturePolicyIdentifier)null);
    }

    public void signDetached(IExternalDigest externalDigest, IExternalSignature externalSignature, Certificate[] chain, Collection<ICrlClient> crlList, IOcspClient ocspClient, ITSAClient tsaClient, int estimatedSize, PdfSigner.CryptoStandard sigtype, SignaturePolicyInfo signaturePolicy) throws IOException, GeneralSecurityException {
        this.signDetached(externalDigest, externalSignature, chain, crlList, ocspClient, tsaClient, estimatedSize, sigtype, signaturePolicy.toSignaturePolicyIdentifier());
    }

    public void signDetached(IExternalDigest externalDigest, IExternalSignature externalSignature, Certificate[] chain, Collection<ICrlClient> crlList, IOcspClient ocspClient, ITSAClient tsaClient, int estimatedSize, PdfSigner.CryptoStandard sigtype, SignaturePolicyIdentifier signaturePolicy) throws IOException, GeneralSecurityException {
        if (this.closed) {
            throw new PdfException("This instance of PdfSigner has been already closed.");
        } else if (this.certificationLevel > 0 && this.isDocumentPdf2() && this.documentContainsCertificationOrApprovalSignatures()) {
            throw new PdfException("Certification signature creation failed. Document shall not contain any certification or approval signatures before signing with certification signature.");
        } else {
            Collection<byte[]> crlBytes = null;

            for(int i = 0; crlBytes == null && i < chain.length; crlBytes = this.processCrl(chain[i++], crlList)) {
            }

            if (estimatedSize == 0) {
                estimatedSize = 8192;
                byte[] element;
                if (crlBytes != null) {
                    for(Iterator var12 = crlBytes.iterator(); var12.hasNext(); estimatedSize += element.length + 10) {
                        element = (byte[])var12.next();
                    }
                }

                if (ocspClient != null) {
                    estimatedSize += 4192;
                }

                if (tsaClient != null) {
                    estimatedSize += 4192;
                }
            }

            PdfSignatureAppearance appearance = this.getSignatureAppearance();
            appearance.setCertificate(chain[0]);
            if (sigtype == PdfSigner.CryptoStandard.CADES && !this.isDocumentPdf2()) {
                this.addDeveloperExtension(PdfDeveloperExtension.ESIC_1_7_EXTENSIONLEVEL2);
            }

            String hashAlgorithm = externalSignature.getHashAlgorithm();
            PdfSignature dic = new PdfSignature(PdfName.Adobe_PPKLite, sigtype == PdfSigner.CryptoStandard.CADES ? PdfName.ETSI_CAdES_DETACHED : PdfName.Adbe_pkcs7_detached);
            dic.setReason(appearance.getReason());
            dic.setLocation(appearance.getLocation());
            dic.setSignatureCreator(appearance.getSignatureCreator());
            dic.setContact(appearance.getContact());
            dic.setDate(new PdfDate(this.getSignDate()));
            this.cryptoDictionary = dic;
            this.digestMethod = this.getHashAlgorithmNameInCompatibleForPdfForm(hashAlgorithm);
            Map<PdfName, Integer> exc = new HashMap();
            exc.put(PdfName.Contents, estimatedSize * 2 + 2);
            this.preClose(exc);
            PdfPKCS7 sgn = new PdfPKCS7((PrivateKey)null, chain, hashAlgorithm, (String)null, externalDigest, false);
            if (signaturePolicy != null) {
                sgn.setSignaturePolicy(signaturePolicy);
            }

            InputStream data = this.getRangeStream();
            byte[] hash = DigestAlgorithms.digest(data, SignUtils.getMessageDigest(hashAlgorithm, externalDigest));
            List<byte[]> ocspList = new ArrayList();
            byte[] ocsp;
            if (chain.length > 1 && ocspClient != null) {
                for(int j = 0; j < chain.length - 1; ++j) {
                    ocsp = ocspClient.getEncoded((X509Certificate)chain[j], (X509Certificate)chain[j + 1], (String)null);
                    if (ocsp != null) {
                        ocspList.add(ocsp);
                    }
                }
            }

            byte[] sh = sgn.getAuthenticatedAttributeBytes(hash, sigtype, ocspList, crlBytes);
            ocsp = externalSignature.sign(sh);
            sgn.setExternalDigest(ocsp, (byte[])null, externalSignature.getEncryptionAlgorithm());
            byte[] encodedSig = sgn.getEncodedPKCS7(hash, sigtype, tsaClient, ocspList, crlBytes);
            if (estimatedSize < encodedSig.length) {
                throw new IOException("Not enough space");
            } else {
                byte[] paddedSig = new byte[estimatedSize];
                System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);
                PdfDictionary dic2 = new PdfDictionary();
                dic2.put(PdfName.Contents, (new PdfString(paddedSig)).setHexWriting(true));
                this.close(dic2);
                this.closed = true;
            }
        }
    }

    public void signExternalContainer(IExternalSignatureContainer externalSignatureContainer, int estimatedSize) throws GeneralSecurityException, IOException {
        if (this.closed) {
            throw new PdfException("This instance of PdfSigner has been already closed.");
        } else {
            PdfSignature dic = new PdfSignature();
            PdfSignatureAppearance appearance = this.getSignatureAppearance();
            dic.setReason(appearance.getReason());
            dic.setLocation(appearance.getLocation());
            dic.setSignatureCreator(appearance.getSignatureCreator());
            dic.setContact(appearance.getContact());
            dic.setDate(new PdfDate(this.getSignDate()));
            externalSignatureContainer.modifySigningDictionary((PdfDictionary)dic.getPdfObject());
            this.cryptoDictionary = dic;
            Map<PdfName, Integer> exc = new HashMap();
            exc.put(PdfName.Contents, estimatedSize * 2 + 2);
            this.preClose(exc);
            InputStream data = this.getRangeStream();
            byte[] encodedSig = externalSignatureContainer.sign(data);
            if (estimatedSize < encodedSig.length) {
                throw new IOException("Not enough space");
            } else {
                byte[] paddedSig = new byte[estimatedSize];
                System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);
                PdfDictionary dic2 = new PdfDictionary();
                dic2.put(PdfName.Contents, (new PdfString(paddedSig)).setHexWriting(true));
                this.close(dic2);
                this.closed = true;
            }
        }
    }

    public void timestamp(ITSAClient tsa, String signatureName) throws IOException, GeneralSecurityException {
        if (this.closed) {
            throw new PdfException("This instance of PdfSigner has been already closed.");
        } else {
            int contentEstimated = tsa.getTokenSizeEstimate();
            if (!this.isDocumentPdf2()) {
                this.addDeveloperExtension(PdfDeveloperExtension.ESIC_1_7_EXTENSIONLEVEL5);
            }

            this.setFieldName(signatureName);
            PdfSignature dic = new PdfSignature(PdfName.Adobe_PPKLite, PdfName.ETSI_RFC3161);
            dic.put(PdfName.Type, PdfName.DocTimeStamp);
            this.cryptoDictionary = dic;
            Map<PdfName, Integer> exc = new HashMap();
            exc.put(PdfName.Contents, contentEstimated * 2 + 2);
            this.preClose(exc);
            InputStream data = this.getRangeStream();
            MessageDigest messageDigest = tsa.getMessageDigest();
            byte[] buf = new byte[4096];

            int n;
            while((n = data.read(buf)) > 0) {
                messageDigest.update(buf, 0, n);
            }

            byte[] tsImprint = messageDigest.digest();

            byte[] tsToken;
            try {
                tsToken = tsa.getTimeStampToken(tsImprint);
            } catch (Exception var14) {
                throw new GeneralSecurityException(var14.getMessage(), var14);
            }

            if (contentEstimated + 2 < tsToken.length) {
                throw new IOException("Not enough space");
            } else {
                byte[] paddedSig = new byte[contentEstimated];
                System.arraycopy(tsToken, 0, paddedSig, 0, tsToken.length);
                PdfDictionary dic2 = new PdfDictionary();
                dic2.put(PdfName.Contents, (new PdfString(paddedSig)).setHexWriting(true));
                this.close(dic2);
                this.closed = true;
            }
        }
    }

    public static void signDeferred(PdfDocument document, String fieldName, OutputStream outs, IExternalSignatureContainer externalSignatureContainer) throws IOException, GeneralSecurityException {
        SignatureUtil signatureUtil = new SignatureUtil(document);
        PdfSignature signature = signatureUtil.getSignature(fieldName);
        if (signature == null) {
            throw (new PdfException("There is no field in the document with such name: {0}.")).setMessageParams(new Object[]{fieldName});
        } else if (!signatureUtil.signatureCoversWholeDocument(fieldName)) {
            throw (new PdfException("Signature with name {0} is not the last. It doesn't cover the whole document.")).setMessageParams(new Object[]{fieldName});
        } else {
            PdfArray b = signature.getByteRange();
            long[] gaps = b.toLongArray();
            if (b.size() == 4 && gaps[0] == 0L) {
                IRandomAccessSource readerSource = document.getReader().getSafeFile().createSourceView();
                InputStream rg = new RASInputStream((new RandomAccessSourceFactory()).createRanged(readerSource, gaps));
                byte[] signedContent = externalSignatureContainer.sign(rg);
                int spaceAvailable = (int)(gaps[2] - gaps[1]) - 2;
                if ((spaceAvailable & 1) != 0) {
                    throw new IllegalArgumentException("Gap is not a multiple of 2");
                } else {
                    spaceAvailable /= 2;
                    if (spaceAvailable < signedContent.length) {
                        throw new PdfException("Available space is not enough for signature.");
                    } else {
                        StreamUtil.copyBytes(readerSource, 0L, gaps[1] + 1L, outs);
                        ByteBuffer bb = new ByteBuffer(spaceAvailable * 2);
                        byte[] var13 = signedContent;
                        int k = signedContent.length;

                        for(int var15 = 0; var15 < k; ++var15) {
                            byte bi = var13[var15];
                            bb.appendHex(bi);
                        }

                        int remain = (spaceAvailable - signedContent.length) * 2;

                        for(k = 0; k < remain; ++k) {
                            bb.append((byte)48);
                        }

                        byte[] bbArr = bb.toByteArray();
                        outs.write(bbArr);
                        StreamUtil.copyBytes(readerSource, gaps[2] - 1L, gaps[3] + 1L, outs);
                    }
                }
            } else {
                throw new IllegalArgumentException("Single exclusion space supported");
            }
        }
    }

    protected Collection<byte[]> processCrl(Certificate cert, Collection<ICrlClient> crlList) {
        if (crlList == null) {
            return null;
        } else {
            List<byte[]> crlBytes = new ArrayList();
            Iterator var4 = crlList.iterator();

            while(var4.hasNext()) {
                ICrlClient cc = (ICrlClient)var4.next();
                if (cc != null) {
                    Collection<byte[]> b = cc.getEncoded((X509Certificate)cert, (String)null);
                    if (b != null) {
                        crlBytes.addAll(b);
                    }
                }
            }

            if (crlBytes.size() == 0) {
                return null;
            } else {
                return crlBytes;
            }
        }
    }

    protected void addDeveloperExtension(PdfDeveloperExtension extension) {
        this.document.getCatalog().addDeveloperExtension(extension);
    }

    protected boolean isPreClosed() {
        return this.preClosed;
    }

    protected void preClose(Map<PdfName, Integer> exclusionSizes) throws IOException {
        if (this.preClosed) {
            throw new PdfException("Document has been already pre closed.");
        } else {
            this.preClosed = true;
            PdfAcroForm acroForm = PdfAcroForm.getAcroForm(this.document, true);
            SignatureUtil sgnUtil = new SignatureUtil(this.document);
            String name = this.getFieldName();
            boolean fieldExist = sgnUtil.doesSignatureFieldExist(name);
            acroForm.setSignatureFlags(3);
            PdfSigFieldLock fieldLock = null;
            if (this.cryptoDictionary == null) {
                throw new PdfException("No crypto dictionary defined.");
            } else {
                ((PdfDictionary)this.cryptoDictionary.getPdfObject()).makeIndirect(this.document);
                int flags;
                PdfDictionary ap;
                if (fieldExist) {
                    PdfSignatureFormField sigField = (PdfSignatureFormField)acroForm.getField(this.fieldName);
                    sigField.put(PdfName.V, this.cryptoDictionary.getPdfObject());
                    fieldLock = sigField.getSigFieldLockDictionary();
                    if (fieldLock == null && this.fieldLock != null) {
                        ((PdfDictionary)this.fieldLock.getPdfObject()).makeIndirect(this.document);
                        sigField.put(PdfName.Lock, this.fieldLock.getPdfObject());
                        fieldLock = this.fieldLock;
                    }

                    sigField.put(PdfName.P, this.document.getPage(this.appearance.getPageNumber()).getPdfObject());
                    sigField.put(PdfName.V, this.cryptoDictionary.getPdfObject());
                    PdfObject obj = ((PdfDictionary)sigField.getPdfObject()).get(PdfName.F);
                    flags = 0;
                    if (obj != null && obj.isNumber()) {
                        flags = ((PdfNumber)obj).intValue();
                    }

                    flags |= 128;
                    sigField.put(PdfName.F, new PdfNumber(flags));
                    ap = new PdfDictionary();
                    ap.put(PdfName.N, this.appearance.getAppearance().getPdfObject());
                    sigField.put(PdfName.AP, ap);
                    sigField.setModified();
                } else {
                    PdfWidgetAnnotation widget = new PdfWidgetAnnotation(this.appearance.getPageRect());
                    widget.setFlags(132);
                    PdfSignatureFormField sigField = PdfFormField.createSignature(this.document);
                    sigField.setFieldName(name);
                    sigField.put(PdfName.V, this.cryptoDictionary.getPdfObject());
                    sigField.addKid(widget);
                    if (this.fieldLock != null) {
                        ((PdfDictionary)this.fieldLock.getPdfObject()).makeIndirect(this.document);
                        sigField.put(PdfName.Lock, this.fieldLock.getPdfObject());
                        fieldLock = this.fieldLock;
                    }

                    flags = this.appearance.getPageNumber();
                    widget.setPage(this.document.getPage(flags));
                    ap = widget.getAppearanceDictionary();
                    if (ap == null) {
                        ap = new PdfDictionary();
                        widget.put(PdfName.AP, ap);
                    }

                    ap.put(PdfName.N, this.appearance.getAppearance().getPdfObject());
                    acroForm.addField(sigField, this.document.getPage(flags));
                    if (((PdfDictionary)acroForm.getPdfObject()).isIndirect()) {
                        acroForm.setModified();
                    } else {
                        this.document.getCatalog().setModified();
                    }
                }

                this.exclusionLocations = new HashMap();
                PdfLiteral lit = new PdfLiteral(80);
                this.exclusionLocations.put(PdfName.ByteRange, lit);
                this.cryptoDictionary.put(PdfName.ByteRange, lit);
                Iterator var22 = exclusionSizes.entrySet().iterator();

                while(var22.hasNext()) {
                    Entry<PdfName, Integer> entry = (Entry)var22.next();
                    PdfName key = (PdfName)entry.getKey();
                    lit = new PdfLiteral((Integer)entry.getValue());
                    this.exclusionLocations.put(key, lit);
                    this.cryptoDictionary.put(key, lit);
                }

                if (this.certificationLevel > 0) {
                    this.addDocMDP(this.cryptoDictionary);
                }

                if (fieldLock != null) {
                    this.addFieldMDP(this.cryptoDictionary, fieldLock);
                }

                if (this.signatureEvent != null) {
                    this.signatureEvent.getSignatureDictionary(this.cryptoDictionary);
                }

                if (this.certificationLevel > 0) {
                    PdfDictionary docmdp = new PdfDictionary();
                    docmdp.put(PdfName.DocMDP, this.cryptoDictionary.getPdfObject());
                    this.document.getCatalog().put(PdfName.Perms, docmdp);
                    this.document.getCatalog().setModified();
                }

                ((PdfDictionary)this.cryptoDictionary.getPdfObject()).flush(false);
                this.document.close();
                this.range = new long[this.exclusionLocations.size() * 2];
                long byteRangePosition = ((PdfLiteral)this.exclusionLocations.get(PdfName.ByteRange)).getPosition();
                this.exclusionLocations.remove(PdfName.ByteRange);
                int idx = 1;

                PdfLiteral lit1;
                long n;
                for(Iterator var11 = this.exclusionLocations.values().iterator(); var11.hasNext(); this.range[idx++] = (long)lit1.getBytesCount() + n) {
                    lit1 = (PdfLiteral)var11.next();
                    n = lit1.getPosition();
                    this.range[idx++] = n;
                }

                Arrays.sort(this.range, 1, this.range.length - 1);

                for(int k = 3; k < this.range.length - 2; k += 2) {
                    long[] var10000 = this.range;
                    var10000[k] -= this.range[k - 1];
                }

                if (this.tempFile == null) {
                    this.bout = this.temporaryOS.toByteArray();
                    this.range[this.range.length - 1] = (long)this.bout.length - this.range[this.range.length - 2];
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    PdfOutputStream os = new PdfOutputStream(bos);
                    os.write(91);

                    for(int k = 0; k < this.range.length; ++k) {
                        ((PdfOutputStream)os.writeLong(this.range[k])).write(32);
                    }

                    os.write(93);
                    System.arraycopy(bos.toByteArray(), 0, this.bout, (int)byteRangePosition, bos.size());
                } else {
                    try {
                        this.raf = FileUtil.getRandomAccessFile(this.tempFile);
                        long len = this.raf.length();
                        this.range[this.range.length - 1] = len - this.range[this.range.length - 2];
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        PdfOutputStream os = new PdfOutputStream(bos);
                        os.write(91);

                        for(int k = 0; k < this.range.length; ++k) {
                            ((PdfOutputStream)os.writeLong(this.range[k])).write(32);
                        }

                        os.write(93);
                        this.raf.seek(byteRangePosition);
                        this.raf.write(bos.toByteArray(), 0, bos.size());
                    } catch (IOException var18) {
                        try {
                            this.raf.close();
                        } catch (Exception var17) {
                        }

                        try {
                            this.tempFile.delete();
                        } catch (Exception var16) {
                        }

                        throw var18;
                    }
                }

            }
        }
    }

    protected InputStream getRangeStream() throws IOException {
        RandomAccessSourceFactory fac = new RandomAccessSourceFactory();
        return new RASInputStream(fac.createRanged(this.getUnderlyingSource(), this.range));
    }

    protected void close(PdfDictionary update) throws IOException {
        try {
            if (!this.preClosed) {
                throw new PdfException("Document must be preClosed.");
            }

            ByteArrayOutputStream bous = new ByteArrayOutputStream();
            PdfOutputStream os = new PdfOutputStream(bous);
            Iterator var4 = update.keySet().iterator();

            while(var4.hasNext()) {
                PdfName key = (PdfName)var4.next();
                PdfObject obj = update.get(key);
                PdfLiteral lit = (PdfLiteral)this.exclusionLocations.get(key);
                if (lit == null) {
                    throw new IllegalArgumentException("The key didn't reserve space in preclose");
                }

                bous.reset();
                os.write(obj);
                if (bous.size() > lit.getBytesCount()) {
                    throw new IllegalArgumentException("The key is too big");
                }

                if (this.tempFile == null) {
                    System.arraycopy(bous.toByteArray(), 0, this.bout, (int)lit.getPosition(), bous.size());
                } else {
                    this.raf.seek(lit.getPosition());
                    this.raf.write(bous.toByteArray(), 0, bous.size());
                }
            }

            if (update.size() != this.exclusionLocations.size()) {
                throw new IllegalArgumentException("The update dictionary has less keys than required");
            }

            if (this.tempFile == null) {
                this.originalOS.write(this.bout, 0, this.bout.length);
            } else if (this.originalOS != null) {
                this.raf.seek(0L);
                long length = this.raf.length();

                int r;
                for(byte[] buf = new byte[8192]; length > 0L; length -= (long)r) {
                    r = this.raf.read(buf, 0, (int)Math.min((long)buf.length, length));
                    if (r < 0) {
                        throw new EOFException("unexpected eof");
                    }

                    this.originalOS.write(buf, 0, r);
                }
            }
        } finally {
            if (this.tempFile != null) {
                this.raf.close();
                if (this.originalOS != null) {
                    this.tempFile.delete();
                }
            }

            if (this.originalOS != null) {
                try {
                    this.originalOS.close();
                } catch (Exception var13) {
                }
            }

        }

    }

    protected IRandomAccessSource getUnderlyingSource() throws IOException {
        RandomAccessSourceFactory fac = new RandomAccessSourceFactory();
        return this.raf == null ? fac.createSource(this.bout) : fac.createSource(this.raf);
    }

    protected void addDocMDP(PdfSignature crypto) {
        PdfDictionary reference = new PdfDictionary();
        PdfDictionary transformParams = new PdfDictionary();
        transformParams.put(PdfName.P, new PdfNumber(this.certificationLevel));
        transformParams.put(PdfName.V, new PdfName("1.2"));
        transformParams.put(PdfName.Type, PdfName.TransformParams);
        reference.put(PdfName.TransformMethod, PdfName.DocMDP);
        reference.put(PdfName.Type, PdfName.SigRef);
        reference.put(PdfName.TransformParams, transformParams);
        this.setDigestParamToSigRefIfNeeded(reference);
        reference.put(PdfName.Data, this.document.getTrailer().get(PdfName.Root));
        PdfArray types = new PdfArray();
        types.add(reference);
        crypto.put(PdfName.Reference, types);
    }

    protected void addFieldMDP(PdfSignature crypto, PdfSigFieldLock fieldLock) {
        PdfDictionary reference = new PdfDictionary();
        PdfDictionary transformParams = new PdfDictionary();
        transformParams.putAll((PdfDictionary)fieldLock.getPdfObject());
        transformParams.put(PdfName.Type, PdfName.TransformParams);
        transformParams.put(PdfName.V, new PdfName("1.2"));
        reference.put(PdfName.TransformMethod, PdfName.FieldMDP);
        reference.put(PdfName.Type, PdfName.SigRef);
        reference.put(PdfName.TransformParams, transformParams);
        this.setDigestParamToSigRefIfNeeded(reference);
        reference.put(PdfName.Data, this.document.getTrailer().get(PdfName.Root));
        PdfArray types = ((PdfDictionary)crypto.getPdfObject()).getAsArray(PdfName.Reference);
        if (types == null) {
            types = new PdfArray();
            crypto.put(PdfName.Reference, types);
        }

        types.add(reference);
    }

    protected boolean documentContainsCertificationOrApprovalSignatures() {
        boolean containsCertificationOrApprovalSignature = false;
        PdfDictionary urSignature = null;
        PdfDictionary catalogPerms = ((PdfDictionary)this.document.getCatalog().getPdfObject()).getAsDictionary(PdfName.Perms);
        if (catalogPerms != null) {
            urSignature = catalogPerms.getAsDictionary(PdfName.UR3);
        }

        PdfAcroForm acroForm = PdfAcroForm.getAcroForm(this.document, false);
        if (acroForm != null) {
            Iterator var5 = acroForm.getFormFields().entrySet().iterator();

            while(var5.hasNext()) {
                Entry<String, PdfFormField> entry = (Entry)var5.next();
                PdfDictionary fieldDict = (PdfDictionary)((PdfFormField)entry.getValue()).getPdfObject();
                if (PdfName.Sig.equals(fieldDict.get(PdfName.FT))) {
                    PdfDictionary sigDict = fieldDict.getAsDictionary(PdfName.V);
                    if (sigDict != null) {
                        PdfSignature pdfSignature = new PdfSignature(sigDict);
                        if (pdfSignature.getContents() != null && pdfSignature.getByteRange() != null && !pdfSignature.getType().equals(PdfName.DocTimeStamp) && sigDict != urSignature) {
                            containsCertificationOrApprovalSignature = true;
                            break;
                        }
                    }
                }
            }
        }

        return containsCertificationOrApprovalSignature;
    }

    protected Rectangle getWidgetRectangle(PdfWidgetAnnotation widget) {
        return widget.getRectangle().toRectangle();
    }

    protected int getWidgetPageNumber(PdfWidgetAnnotation widget) {
        int pageNumber = 0;
        PdfDictionary pageDict = ((PdfDictionary)widget.getPdfObject()).getAsDictionary(PdfName.P);
        if (pageDict != null) {
            pageNumber = this.document.getPageNumber(pageDict);
        } else {
            for(int i = 1; i <= this.document.getNumberOfPages(); ++i) {
                PdfPage page = this.document.getPage(i);
                if (!page.isFlushed() && page.containsAnnotation(widget)) {
                    pageNumber = i;
                    break;
                }
            }
        }

        return pageNumber;
    }

    private void setDigestParamToSigRefIfNeeded(PdfDictionary reference) {
        if (this.document.getPdfVersion().compareTo(PdfVersion.PDF_1_6) < 0) {
            reference.put(PdfName.DigestValue, new PdfString("aa"));
            PdfArray loc = new PdfArray();
            loc.add(new PdfNumber(0));
            loc.add(new PdfNumber(0));
            reference.put(PdfName.DigestLocation, loc);
            reference.put(PdfName.DigestMethod, PdfName.MD5);
        } else if (this.isDocumentPdf2()) {
            if (this.digestMethod != null) {
                reference.put(PdfName.DigestMethod, this.digestMethod);
            } else {
                Logger logger = LoggerFactory.getLogger(PdfSigner.class);
                logger.error("Unknown digest method. Valid values are MD5, SHA1 SHA256, SHA384, SHA512 and RIPEMD160.");
            }
        }

    }

    private PdfName getHashAlgorithmNameInCompatibleForPdfForm(String hashAlgorithm) {
        PdfName pdfCompatibleName = null;
        String hashAlgOid = DigestAlgorithms.getAllowedDigest(hashAlgorithm);
        if (hashAlgOid != null) {
            String hashAlgorithmNameInCompatibleForPdfForm = DigestAlgorithms.getDigest(hashAlgOid);
            if (hashAlgorithmNameInCompatibleForPdfForm != null) {
                pdfCompatibleName = new PdfName(hashAlgorithmNameInCompatibleForPdfForm);
            }
        }

        return pdfCompatibleName;
    }

    private boolean isDocumentPdf2() {
        return this.document.getPdfVersion().compareTo(PdfVersion.PDF_2_0) >= 0;
    }

    private static StampingProperties initStampingProperties(boolean append) {
        StampingProperties properties = new StampingProperties();
        if (append) {
            properties.useAppendMode();
        }

        return properties;
    }

    public interface ISignatureEvent {
        void getSignatureDictionary(PdfSignature var1);
    }

    public static enum CryptoStandard {
        CMS,
        CADES;

        private CryptoStandard() {
        }
    }
}
