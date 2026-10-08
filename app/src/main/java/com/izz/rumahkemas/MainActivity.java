package com.izz.rumahkemas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import androidx.webkit.WebViewAssetLoader;
import androidx.webkit.WebViewAssetLoader.AssetsPathHandler;
import androidx.webkit.WebViewClientCompat;
import android.net.Uri;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.widget.Toast;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends Activity {
  private static final int BACKUP_REQUEST = 104;
  private WebView webView;
  private String pendingBackup;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setStatusBarColor(0xff173d34);
    getWindow().setNavigationBarColor(0xfff5f6f0);
    webView = new WebView(this);
    webView.setBackgroundColor(0xfff5f6f0);
    webView.getSettings().setJavaScriptEnabled(true);
    webView.getSettings().setDomStorageEnabled(true);
    webView.getSettings().setAllowFileAccess(false);
    webView.getSettings().setAllowContentAccess(false);
    webView.addJavascriptInterface(new BackupBridge(), "AndroidBackup");
    final WebViewAssetLoader loader = new WebViewAssetLoader.Builder()
        .addPathHandler("/assets/", new AssetsPathHandler(this)).build();
    webView.setWebViewClient(new WebViewClientCompat() {
      @Override public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return loader.shouldInterceptRequest(request.getUrl());
      }
      @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return !"appassets.androidplatform.net".equals(request.getUrl().getHost());
      }
    });
    webView.setWebChromeClient(new WebChromeClient());
    setContentView(webView);
    webView.loadUrl("https://appassets.androidplatform.net/assets/index.html");
  }

  public class BackupBridge {
    @JavascriptInterface public void save(String data) {
      runOnUiThread(() -> {
        pendingBackup = data;
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        intent.putExtra(Intent.EXTRA_TITLE, "backup-rumah-kemas.json");
        startActivityForResult(intent, BACKUP_REQUEST);
      });
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == BACKUP_REQUEST && resultCode == RESULT_OK && data != null && pendingBackup != null) {
      Uri uri = data.getData();
      try (OutputStream out = getContentResolver().openOutputStream(uri)) {
        if(out == null) throw new Exception("Output unavailable");
        out.write(pendingBackup.getBytes(StandardCharsets.UTF_8));
        Toast.makeText(this, "Backup berjaya disimpan", Toast.LENGTH_SHORT).show();
      } catch(Exception e) { Toast.makeText(this, "Gagal menyimpan backup", Toast.LENGTH_LONG).show(); }
    }
    pendingBackup = null;
  }

  @Override public void onBackPressed() {
    if (webView.canGoBack()) webView.goBack(); else super.onBackPressed();
  }
  @Override protected void onDestroy() { webView.destroy(); super.onDestroy(); }
}
