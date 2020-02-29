package com.lb.zip_file_provider_sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //NOTE: this is a lot of work for the UI thread, but it's just a sample...
        val installedPackages = packageManager.getInstalledPackages(0)
        val filesToCompress = ArrayList<String>()
        val maxFiles = 3
        var maxTotalSize = 10 * 1024L * 1024L
        for (installedPackage in installedPackages) {
            val filePath = installedPackage.applicationInfo.publicSourceDir
            val file = File(filePath)
            val fileSize = file.length()
            if (maxTotalSize - fileSize >= 0) {
                maxTotalSize-= fileSize
                filesToCompress.add(filePath)
                if (filesToCompress.size >= maxFiles)
                    break
            }
        }
        val uri = ZipFilesProvider.prepareFilesToShareAsZippedFile(filesToCompress, "someZipFile.zip")
        val intent = Intent(Intent.ACTION_SEND).setType(ZipFilesProvider.ZIP_FILE_MIME_TYPE).putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(intent, ""))
    }
}
