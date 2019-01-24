package com.dlvn.mcustomerportal.activity.prototype;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.utils.myLog;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.List;

public class PdfViewActivity extends BaseActivity implements OnPageChangeListener, OnLoadCompleteListener {

    private static final String TAG = "PdfViewActivity";
    public static final String INT_FILEPATH_PDF = "FILEPATH_PDF";

    LinearLayout lloBack;

    PDFView pdfView;
    int pageNumber = 0;
    String pdfFileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lloBack = findViewById(R.id.lloBack);
        lloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pdfView = findViewById(R.id.pdfView);

        if (getIntent().getExtras() != null)
            if (getIntent().getExtras().containsKey(INT_FILEPATH_PDF))
                pdfFileName = getIntent().getStringExtra(INT_FILEPATH_PDF);

        File file = new File(pdfFileName);
        if (file.exists())
            displayFromFilePath(file);

    }

    private void displayFromFilePath(File file) {
        myLog.e(TAG, "file PDF Open = " + file.getAbsolutePath());
        pdfView.fromFile(file)
                .defaultPage(pageNumber)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
    }


    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {
            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

}
